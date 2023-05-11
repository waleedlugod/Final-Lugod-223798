import java.io.*;
import java.net.*;
import java.awt.*;
import java.util.*;

// TODO: Fix reset posiion
public class GameServer {
    private Player[] players = new Player[2];
    private Bullet[][] bullets = new Bullet[2][Bullet.MAX_BULLETS];
    private ServerSocket serverSocket;
    private Socket[] clientSockets = new Socket[2];
    private ArrayList<Thread> threads = new ArrayList<>();

    public GameServer() {
        System.out.println("-----SERVER-----");
        createServer();
        acceptConnections();

        startThreads();
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

    private void acceptConnections() {
        try {
            System.out.println("Waiting for connections...");

            int playerCount = 0;
            while (playerCount < 2) {
                clientSockets[playerCount] = serverSocket.accept();

                DataInputStream inputStream = new DataInputStream(clientSockets[playerCount].getInputStream());
                DataOutputStream outputStream = new DataOutputStream(clientSockets[playerCount].getOutputStream());

                int ID = playerCount;
                players[playerCount] = new Player(new Vector2(), false);
                for (int i = 0; i < Bullet.MAX_BULLETS; i++) {
                    bullets[playerCount][i] = new Bullet(players[playerCount], null, i);
                }
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
        threads.add(readThread);

        WriteToClient writeToClient = new WriteToClient(outputStream, ID);
        Thread writeThread = new Thread(writeToClient);
        threads.add(writeThread);
    }

    private void startThreads() {
        for (Thread thread : threads) {
            thread.start();
        }
    }

    private class ReadFromClient implements Runnable {
        private final int ID;
        private final int OTHER_ID;
        private DataInputStream inputStream;

        public ReadFromClient(DataInputStream inputStream, int ID) {
            this.inputStream = inputStream;
            this.ID = ID;
            OTHER_ID = ID == 0 ? 1 : 0;
        }

        private void readSelfPlayerPosition() {
            try {
                int x = inputStream.readInt();
                int y = inputStream.readInt();
                players[ID].setPostion(x, y);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void readSelfBulletsData() {
            try {
                for (int i = 0; i < Bullet.MAX_BULLETS; i++) {
                    bullets[ID][i].setPosition(inputStream.readInt(), inputStream.readInt());
                    bullets[ID][i].setVelocity(inputStream.readInt(), inputStream.readInt());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void readOtherHealth() {
            try {
                players[OTHER_ID].health = inputStream.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                readSelfPlayerPosition();
                readSelfBulletsData();
                readOtherHealth();
            }
        }
    }

    private class WriteToClient implements Runnable {
        private final int ID;
        private final int OTHER_ID;
        private DataOutputStream outputStream;

        public WriteToClient(DataOutputStream outputStream, int ID) {
            this.outputStream = outputStream;
            this.ID = ID;
            OTHER_ID = ID == 0 ? 1 : 0;
        }

        private void writeOtherPlayerPosition() {
            try {
                Vector2 otherPlayerPosition = players[OTHER_ID].getPosition();
                outputStream.writeInt(otherPlayerPosition.x);
                outputStream.writeInt(otherPlayerPosition.y);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void writeOtherBulletsData() {
            try {
                for (int i = 0; i < Bullet.MAX_BULLETS; i++) {
                    Vector2 otherBulletPosition = bullets[OTHER_ID][i].getPosition();
                    Vector2 otherBulletVelocity = bullets[OTHER_ID][i].getVelocity();
                    outputStream.writeInt(otherBulletPosition.x);
                    outputStream.writeInt(otherBulletPosition.y);
                    outputStream.writeInt(otherBulletVelocity.x);
                    outputStream.writeInt(otherBulletVelocity.y);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void writeSelfHealth() {
            try {
                outputStream.writeInt(players[ID].health);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    writeOtherPlayerPosition();
                    writeOtherBulletsData();
                    writeSelfHealth();

                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new GameServer();
    }
}