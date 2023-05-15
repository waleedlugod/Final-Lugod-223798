
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

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Handles the client side connection to the server. Handles the reading and
 * writing of data to the server.
 */
public class GameClient {
    private int CLIENT_ID = -1;
    private Socket clientSocket;
    private final GameFrame FRAME;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    /**
     * Connects to server, and setups the threads for reading and writing.
     */
    public GameClient() {
        System.out.println("-----CLIENT-----");
        connectToServer();

        FRAME = new GameFrame(CLIENT_ID);
        FRAME.setupGUI();

        setupReadWriteThreads(inputStream, outputStream);
    }

    /**
     * Connects to the server, and determines the client id.
     */
    private void connectToServer() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter ip: ");
            String ip = scanner.nextLine();
            scanner.close();
            System.out.println("Connecting to server...");

            clientSocket = new Socket(ip, 51251);

            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new DataOutputStream(clientSocket.getOutputStream());

            CLIENT_ID = inputStream.readInt();

            System.out.println("Successfully connected to server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts all read and write threads to the server.
     * 
     * @param inputStream
     * @param outputStream
     */
    private void setupReadWriteThreads(DataInputStream inputStream, DataOutputStream outputStream) {
        ReadFromServer readFromServer = new ReadFromServer(inputStream);
        Thread readThread = new Thread(readFromServer);
        readThread.start();

        WriteToServer writeToServer = new WriteToServer(outputStream);
        Thread writeThread = new Thread(writeToServer);
        writeThread.start();
    }

    /**
     * Handles reading all the data from the server.
     */
    private class ReadFromServer implements Runnable {
        private DataInputStream inputStream;

        /**
         * Instantiates fields
         */
        public ReadFromServer(DataInputStream inputStream) {
            this.inputStream = inputStream;
        }

        /**
         * Reads and sets the position of the opponent and which direction they are
         * facing.
         */
        private void readOtherPlayerData() {
            try {
                FRAME.selfCanvas.players[1].setPostion(inputStream.readInt(), inputStream.readInt());
                FRAME.enemyCanvas.players[1].isFacingLeft = inputStream.readBoolean();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Reads and sets the position and velocity of the bullets that belong to the
         * opponent.
         */
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

        /**
         * Reads and sets the health of the self Player.
         */
        private void readSelfHealth() {
            try {
                FRAME.selfCanvas.players[0].health = inputStream.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Reads the data of the opponent player and bullets, and self health.
         */
        @Override
        public void run() {
            while (true) {
                readOtherPlayerData();
                readOtherBulletsData();
                readSelfHealth();
                FRAME.enemyCanvas.copy(FRAME.selfCanvas);
            }
        }
    }

    /**
     * Handles writing all the player and bullet data to the client.
     */
    private class WriteToServer implements Runnable {
        private DataOutputStream outputStream;

        /**
         * Instantiates fields.
         */
        public WriteToServer(DataOutputStream outputStream) {
            this.outputStream = outputStream;
        }

        /**
         * Writes the position and direction of the self player.
         */
        private void writeSelfPlayerData() {
            try {
                outputStream.writeInt(FRAME.selfCanvas.players[0].getPosition().x);
                outputStream.writeInt(FRAME.selfCanvas.players[0].getPosition().y);
                outputStream.writeBoolean(FRAME.selfCanvas.players[0].isFacingLeft);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Writes the positions and velocities of the bullets that belong to the self.
         */
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

        /**
         * Write the health of the opponent.
         */
        private void writeOtherHealth() {
            try {
                outputStream.writeInt(FRAME.selfCanvas.players[1].health);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Writes the data of the self player and the bullets that belong to the self.
         * Also writes the health of the opponent.
         */
        @Override
        public void run() {
            while (true) {
                try {
                    writeSelfPlayerData();
                    writeSelfBulletsData();
                    writeOtherHealth();
                    outputStream.flush();
                    Thread.sleep(10);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
