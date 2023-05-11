import java.net.*;
import java.io.*;

public class GameClient {
    private int CLIENT_ID = -1;
    private Socket clientSocket;
    private final GameFrame FRAME;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public GameClient() {
        System.out.println("-----CLIENT-----");
        connectToServer();

        FRAME = new GameFrame(CLIENT_ID);
        FRAME.setupGUI();

        setupReadWriteThreads(inputStream, outputStream);
    }

    private void connectToServer() {
        try {
            System.out.println("Connecting to server...");

            clientSocket = new Socket("localhost", 51251);

            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new DataOutputStream(clientSocket.getOutputStream());

            CLIENT_ID = inputStream.readInt();

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

        private void readOtherPlayerPosition() {
            try {
                int x = inputStream.readInt();
                int y = inputStream.readInt();
                FRAME.selfCanvas.players[1].setPostion(x, y);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void readOtherBulletsData() {
            try {
                for (int i = 0; i < Bullet.MAX_BULLETS; i++) {
                    FRAME.selfCanvas.bullets[1][i].setPosition(inputStream.readInt(), inputStream.readInt());
                    FRAME.selfCanvas.bullets[1][i].setVelocity(inputStream.readInt(), inputStream.readInt());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void readOtherStats() {
            try {
                FRAME.selfCanvas.players[1].points = inputStream.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                readOtherPlayerPosition();
                readOtherBulletsData();
                readOtherStats();
                FRAME.enemyCanvas.copy(FRAME.selfCanvas);
            }
        }
    }

    private class WriteToServer implements Runnable {
        private DataOutputStream outputStream;

        public WriteToServer(DataOutputStream outputStream) {
            this.outputStream = outputStream;
        }

        private void writeSelfPlayerPosition() {
            try {
                outputStream.writeInt(FRAME.selfCanvas.players[0].getPosition().x);
                outputStream.writeInt(FRAME.selfCanvas.players[0].getPosition().y);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void writeSelfBulletsData() {
            try {
                for (int i = 0; i < FRAME.selfCanvas.bullets[1].length; i++) {
                    outputStream.writeInt(FRAME.selfCanvas.bullets[0][i].getPosition().x);
                    outputStream.writeInt(FRAME.selfCanvas.bullets[0][i].getPosition().y);
                    outputStream.writeInt(FRAME.selfCanvas.bullets[0][i].getVelocity().x);
                    outputStream.writeInt(FRAME.selfCanvas.bullets[0][i].getVelocity().y);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void writeSelfStats() {
            try {
                outputStream.writeInt(FRAME.selfCanvas.players[0].points);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    writeSelfPlayerPosition();
                    writeSelfBulletsData();
                    writeSelfStats();

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
    }
}
