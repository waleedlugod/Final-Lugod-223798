
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
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

/**
 * Handles drawing the stats of the player. It draws a certain amount of heart
 * depending on the health of the player.
 */
public class PlayerStats implements DrawingObject {
    private Player player;
    private BufferedImage fullHeart;
    private BufferedImage emptyHeart;

    /**
     * Initializes fields and loads assets.
     * 
     * @params player The player which the data will be taken from.
     */
    public PlayerStats(Player player) {
        this.player = player;
        loadAssets();
    }

    /**
     * Draws the amount of full hearts as much as the curret player health,
     * otherwise draw empty hearts.
     */
    public void draw(Graphics2D g2d) {
        AffineTransform reset = g2d.getTransform();
        g2d.setColor(new Color(0x000000));
        g2d.translate(5, 5);
        g2d.scale(1.8, 1.8);
        for (int i = 0; i < Player.MAX_HEALTH; i++) {
            g2d.drawImage(i < player.health ? fullHeart : emptyHeart, null, (fullHeart.getWidth() + 1) * i, 0);
        }
        g2d.setTransform(reset);
    }

    /**
     * Loads the full and empty hearts.
     */
    private void loadAssets() {
        try {
            fullHeart = ImageIO.read(new File("assets/health/ui_heart_full.png"));
            emptyHeart = ImageIO.read(new File("assets/health/ui_heart_empty.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
