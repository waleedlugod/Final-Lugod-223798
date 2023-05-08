import java.net.*;
import java.io.*;

public class GameClient {
    private int ID = -1;
    private Socket clientSocket;
    private final GameCanvas gameCanvas;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public GameClient() {
        System.out.println("-----CLIENT-----");
        connectToServer();

        GameFrame gameFrame = new GameFrame(ID);
        gameFrame.setupGUI();
        gameCanvas = gameFrame.getGameCanvas();

        setupReadWriteThreads(inputStream, outputStream);
    }

    private void connectToServer() {
        try {
            System.out.println("Connecting to server...");

            clientSocket = new Socket("localhost", 51251);

            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new DataOutputStream(clientSocket.getOutputStream());

            ID = inputStream.readInt();

            System.out.println("Successfully connected to server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupReadWriteThreads(DataInputStream inputStream, DataOutputStream outputStream) {
        ReadFromServer readFromServer = new ReadFromServer(inputStream);
        Thread readThread = new Thread(readFromServer);
        readThread.start();

        WriteToServer writeToServer = new WriteToServer(outputStream);
        Thread writeThread = new Thread(writeToServer);
        writeThread.start();
    }

    private class ReadFromServer implements Runnable {
        private DataInputStream inputStream;

        public ReadFromServer(DataInputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            while (true) {
                readOtherPlayerPosition();
                readOtherBulletsPositions();
            }
        }

        private void readOtherPlayerPosition() {
            try {
                int x = inputStream.readInt();
                int y = inputStream.readInt();
                gameCanvas.players[1].setPostion(x, y);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void readOtherBulletsPositions() {
            try {
                for (int i = 0; i < Bullet.MAX_BULLETS; i++) {
                    int x = inputStream.readInt();
                    int y = inputStream.readInt();
                    gameCanvas.bullets[1][i].setPosition(x, y);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class WriteToServer implements Runnable {
        private DataOutputStream outputStream;

        public WriteToServer(DataOutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    writeSelfPlayerPosition();
                    writeSelfBulletsPositions();

                    outputStream.flush();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void writeSelfPlayerPosition() {
            try {
                outputStream.writeInt(gameCanvas.players[0].getPosition().x);
                outputStream.writeInt(gameCanvas.players[0].getPosition().y);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void writeSelfBulletsPositions() {
            try {
                for (int i = 0; i < gameCanvas.bullets[1].length; i++) {
                    outputStream.writeInt(gameCanvas.bullets[0][i].getPosition().x);
                    outputStream.writeInt(gameCanvas.bullets[0][i].getPosition().y);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
