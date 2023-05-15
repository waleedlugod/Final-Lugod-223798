
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

import java.awt.*;
import javax.swing.*;
import java.util.*;

/**
 * The canvas to draw the screen of each player. Draws the Background, Players,
 * and Bullets.
 */
public class GameCanvas extends JComponent {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 300;
    public Bullet[][] bullets = new Bullet[2][Bullet.MAX_BULLETS];
    public Player[] players = new Player[2];
    public final int CLIENT_ID;
    public final int CANVAS_ID;
    private ArrayList<DrawingObject> objectsToDraw = new ArrayList<DrawingObject>();

    /**
     * Initializes fields, add background, players, and bullets to draw.
     * 
     * @param CLIENT_ID Which client the instance belongs to.
     * @param CANVAS_ID Which canvas the instance is. If it is 0, the canvas is at
     *                  the top. If it is 1, the canvas is at the bottom.
     */
    public GameCanvas(int CLIENT_ID, int CANVAS_ID) {
        this.CLIENT_ID = CLIENT_ID;
        this.CANVAS_ID = CANVAS_ID;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        addBackground();
        addPlayers();
        addBullets();

        objectsToDraw.add(new PlayerStats(players[CANVAS_ID]));
    }

    /**
     * Draws all the objects inside the canvas.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        for (DrawingObject object : objectsToDraw) {
            object.draw(g2d);
        }
    }

    /**
     * Adds the background.
     */
    private void addBackground() {
        Background bg = new Background();
        bg.loadAssets();
        objectsToDraw.add(bg);
    }

    /**
     * Instantiates both the players to draw.
     */
    private void addPlayers() {
        players[0] = new Player(new Vector2(CLIENT_ID == 0
                ? 100
                : WIDTH - 100, (HEIGHT / 2) - Player.SIZE.y / 2),
                true);
        players[1] = new Player(new Vector2(CLIENT_ID == 0
                ? WIDTH - 100
                : 100, (HEIGHT / 2) - Player.SIZE.y / 2),
                false);
        players[0].loadAssets(this);
        players[1].loadAssets(this);
        objectsToDraw.add(players[0]);
        objectsToDraw.add(players[1]);
    }

    /**
     * Instantiates all the bullets to draw
     */
    private void addBullets() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < bullets[i].length; j++) {
                bullets[i][j] = new Bullet(
                        i == 0 ? players[0] : players[1],
                        i == 0 ? players[1] : players[0],
                        j);
                bullets[i][j].loadAssets(this);
                objectsToDraw.add(bullets[i][j]);
            }
        }
    }

    /**
     * Copies the data of Players and Bullets from another GameCanvas.
     * 
     * @param gameCanvas The other canvas
     */
    public void copy(GameCanvas gameCanvas) {
        for (int i = 0; i < 2; i++) {
            Vector2 playerPosition = gameCanvas.players[i].getPosition();
            players[i].setPostion(playerPosition.x, playerPosition.y);
            players[i].health = gameCanvas.players[i].health;
            for (int j = 0; j < Bullet.MAX_BULLETS; j++) {
                Vector2 bulletPosition = gameCanvas.bullets[i][j].getPosition();
                Vector2 bulletVelocity = gameCanvas.bullets[i][j].getVelocity();
                bullets[i][j].setPosition(bulletPosition.x, bulletPosition.y);
                bullets[i][j].setVelocity(bulletVelocity.x, bulletVelocity.y);
            }
        }
    }
}
