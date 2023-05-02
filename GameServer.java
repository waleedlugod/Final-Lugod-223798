import java.io.*;
import java.net.*;

public class GameServer {
    private Vector2[] playerPositions = { new Vector2(), new Vector2() };
    private Vector2[][] bulletPositions = new Vector2[2][Bullet.MAX_BULLETS];
    private Vector2[][] bulletVelocities = new Vector2[2][Bullet.MAX_BULLETS];
    private ServerSocket serverSocket;
    private Socket[] clientSockets = new Socket[2];

    public GameServer() {
        System.out.println("-----SERVER-----");
        initializeBulletArrays();
        createServer();
        acceptConnections();
    }

    private void createServer() {
        System.out.println("Creating server...");
        try {
            serverSocket = new ServerSocket(51251);
            System.out.println("Server successfully created.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeBulletArrays() {
        for (int i = 0; i < playerPositions.length; i++) {
            for (int j = 0; j < Bullet.MAX_BULLETS; j++) {
                bulletPositions[i][j] = new Vector2();
                bulletVelocities[i][j] = new Vector2();
            }
        }
    }

    private void acceptConnections() {
        try {
            System.out.println("Waiting for connections...");

            int playerCount = 0;
            while (playerCount < 2) {
                clientSockets[playerCount] = serverSocket.accept();

                DataInputStream inputStream = new DataInputStream(clientSockets[playerCount].getInputStream());
                DataOutputStream outputStream = new DataOutputStream(clientSockets[playerCount].getOutputStream());

                int ID = playerCount;
                outputStream.writeInt(ID);

                setupReadWriteThreads(inputStream, outputStream, ID);

                playerCount++;
                System.out.printf("Player %d has connected.\n", playerCount);
            }
            System.out.println("No longer accepting connections.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupReadWriteThreads(DataInputStream inputStream, DataOutputStream outputStream, int ID) {
        ReadFromClient readFromClient = new ReadFromClient(inputStream, ID);
        Thread readThread = new Thread(readFromClient);
        readThread.start();

        WriteToClient writeToClient = new WriteToClient(outputStream, ID);
        Thread writeThread = new Thread(writeToClient);
        writeThread.start();
    }

    private class ReadFromClient implements Runnable {
        private final int ID;
        private DataInputStream inputStream;

        public ReadFromClient(DataInputStream inputStream, int ID) {
            this.inputStream = inputStream;
            this.ID = ID;
        }

        @Override
        public void run() {
            while (true) {
                readOtherPlayerPosition();
                readOtherBulletsPositions();
                readOtherBulletsVelocities();
            }
        }

        private void readOtherPlayerPosition() {
            try {
                playerPositions[ID].x = inputStream.readInt();
                playerPositions[ID].y = inputStream.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void readOtherBulletsPositions() {
            try {
                for (int i = 0; i < Bullet.MAX_BULLETS; i++) {
                    bulletPositions[ID][i].x = inputStream.readInt();
                    bulletPositions[ID][i].y = inputStream.readInt();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void readOtherBulletsVelocities() {
            try {
                for (int i = 0; i < Bullet.MAX_BULLETS; i++) {
                    bulletVelocities[ID][i].x = inputStream.readInt();
                    bulletVelocities[ID][i].y = inputStream.readInt();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class WriteToClient implements Runnable {
        private final int ID;
        private DataOutputStream outputStream;

        public WriteToClient(DataOutputStream outputStream, int ID) {
            this.outputStream = outputStream;
            this.ID = ID;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    writeOtherPlayerPosition();
                    writeOtherBulletsPositions();
                    writeOtherBulletsVelocities();
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void writeOtherPlayerPosition() {
            try {
                int otherClientID = ID == 0 ? 1 : 0;
                outputStream.writeInt(playerPositions[otherClientID].x);
                outputStream.writeInt(playerPositions[otherClientID].y);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void writeOtherBulletsPositions() {
            try {
                int otherClientID = ID == 0 ? 1 : 0;
                for (int i = 0; i < Bullet.MAX_BULLETS; i++) {
                    outputStream.writeInt(bulletPositions[otherClientID][i].x);
                    outputStream.writeInt(bulletPositions[otherClientID][i].y);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void writeOtherBulletsVelocities() {
            try {
                int otherClientID = ID == 0 ? 1 : 0;
                for (int i = 0; i < Bullet.MAX_BULLETS; i++) {
                    outputStream.writeInt(bulletVelocities[otherClientID][i].x);
                    outputStream.writeInt(bulletVelocities[otherClientID][i].y);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new GameServer();
    }
}