
/**
 * @author Waleed Lugod (223798)
 * @version May 15, 2023
 */
/**
 * I have not discussed the Java language code in my program
 * with anyone other than my instructor or the teaching assistants
 * assigned to this course.
 * I have not used Java language code obtained from another student,
 * or any other unauthorized source, either modified or unmodified.
 * If any Java language code or documentation used in my program
 * was obtained from another source, such as a textbook or website,
 * that has been clearly noted with a proper citation in the comments
 * of my program.
 */

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Handles the server side connection to both the clients. Also handles the
 * reading and writing of data for the server.
 */
public class GameServer {
    private Player[] players = new Player[2];
    private Bullet[][] bullets = new Bullet[2][Bullet.MAX_BULLETS];
    private ServerSocket serverSocket;
    private Socket[] clientSockets = new Socket[2];
    private ArrayList<Thread> threads = new ArrayList<>();

    /**
     * Creates the server and accept connections. Start the read and write threads
     * when all clients connect.
     */
    public GameServer() {
        System.out.println("-----SERVER-----");
        createServer();
        acceptConnections();

        startThreads();
    }

    /**
     * Creates the server.
     */
    private void createServer() {
        System.out.println("Creating server...");
        try {
            serverSocket = new ServerSocket(51251);
            System.out.println("Server successfully created.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Accepts the socket. Creates the input and output streams for the client.
     * Sends the client id to the socket.
     */
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

    /**
     * Creates the read and write threads and adds them to an arraylist.
     * 
     * @param inputStream
     * @param outputStream
     * @param ID
     */
    private void setupReadWriteThreads(DataInputStream inputStream, DataOutputStream outputStream, int ID) {
        ReadFromClient readFromClient = new ReadFromClient(inputStream, ID);
        Thread readThread = new Thread(readFromClient);
        threads.add(readThread);

        WriteToClient writeToClient = new WriteToClient(outputStream, ID);
        Thread writeThread = new Thread(writeToClient);
        threads.add(writeThread);
    }

    /**
     * Starts all the read and write threads.
     */
    private void startThreads() {
        for (Thread thread : threads) {
            thread.start();
        }
    }

    /**
     * Closes a socket. Exits the program when both sockets are closed.
     * 
     * @param ID
     */
    private void closeSocket(int ID) {
        try {
            clientSockets[ID].close();
            System.out.println("Player " + (ID + 1) + " has disconnected.");
            if (clientSockets[0].isClosed() && clientSockets[1].isClosed()) {
                System.out.println("Shutting down server.");
                System.exit(1);
            }
        } catch (IOException eClose) {
            eClose.printStackTrace();
        }
    }

    /**
     * Handles reading the data from the client. Reads the player data and bullets
     * data.
     */
    private class ReadFromClient implements Runnable {
        private final int ID;
        private final int OTHER_ID;
        private DataInputStream inputStream;

        /**
         * 
         * @param inputStream
         * @param ID
         */
        public ReadFromClient(DataInputStream inputStream, int ID) {
            this.inputStream = inputStream;
            this.ID = ID;
            OTHER_ID = ID == 0 ? 1 : 0;
        }

        private void readSelfPlayerData() throws IOException {
            players[ID].setPostion(inputStream.readInt(), inputStream.readInt());
            players[ID].isFacingLeft = inputStream.readBoolean();
        }

        private void readSelfBulletsData() throws IOException {
            for (int i = 0; i < Bullet.MAX_BULLETS; i++) {
                bullets[ID][i].setPosition(inputStream.readInt(), inputStream.readInt());
                bullets[ID][i].setVelocity(inputStream.readInt(), inputStream.readInt());
            }
        }

        private void readOtherHealth() throws IOException {
            players[OTHER_ID].health = inputStream.readInt();
        }

        @Override
        public void run() {
            while (!clientSockets[ID].isClosed()) {
                try {
                    readSelfPlayerData();
                    readSelfBulletsData();
                    readOtherHealth();
                } catch (IOException e) {
                    closeSocket(ID);
                }
            }
        }
    }

    /**
     * Handles writing of data to the client. Writes the player data and bullets
     * data.
     */
    private class WriteToClient implements Runnable {
        private final int ID;
        private final int OTHER_ID;
        private DataOutputStream outputStream;

        /**
         * Initalizes fields.
         */
        public WriteToClient(DataOutputStream outputStream, int ID) {
            this.outputStream = outputStream;
            this.ID = ID;
            OTHER_ID = ID == 0 ? 1 : 0;
        }

        /**
         * Writes the the position and direction of the opponent of the client.
         * 
         * @throws IOException
         */
        private void writeOtherPlayerData() throws IOException {
            Vector2 otherPlayerPosition = players[OTHER_ID].getPosition();
            outputStream.writeInt(otherPlayerPosition.x);
            outputStream.writeInt(otherPlayerPosition.y);
            outputStream.writeBoolean(players[OTHER_ID].isFacingLeft);
        }

        /**
         * Writes the positions and velocities of the bullets of the opponent of the
         * client.
         * 
         * @throws IOException
         */
        private void writeOtherBulletsData() throws IOException {
            for (int i = 0; i < Bullet.MAX_BULLETS; i++) {
                Vector2 otherBulletPosition = bullets[OTHER_ID][i].getPosition();
                Vector2 otherBulletVelocity = bullets[OTHER_ID][i].getVelocity();
                outputStream.writeInt(otherBulletPosition.x);
                outputStream.writeInt(otherBulletPosition.y);
                outputStream.writeInt(otherBulletVelocity.x);
                outputStream.writeInt(otherBulletVelocity.y);
            }
        }

        /**
         * Writes the updated health of the client.
         * 
         * @throws IOException
         */
        private void writeSelfHealth() throws IOException {
            outputStream.writeInt(players[ID].health);
        }

        /**
         * Writes the player data and bullet data
         */
        @Override
        public void run() {
            while (!clientSockets[ID].isClosed()) {
                try {
                    writeOtherPlayerData();
                    writeOtherBulletsData();
                    writeSelfHealth();
                    outputStream.flush();

                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    closeSocket(ID);
                }
            }
        }
    }

    public static void main(String[] args) {
        new GameServer();
    }
}