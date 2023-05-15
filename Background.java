
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
 * Draws the background where the players are in. The background is a simple
 * png. Assets taken from https://scut.itch.io/7drl-tileset-2018.
 */
public class Background implements DrawingObject {
    private BufferedImage map;

    /**
     * Draws the background.
     */
    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform reset = g2d.getTransform();
        g2d.setColor(new Color(0x000000));
        g2d.scale((double) GameCanvas.WIDTH / (double) map.getWidth(),
                (double) GameCanvas.HEIGHT / (double) map.getHeight());
        g2d.drawImage(map, null, 0, 0);
        g2d.setTransform(reset);
    }

    /**
     * Loads the background image.
     */
    public void loadAssets() {
        try {
            map = ImageIO.read(new File("assets/Background/map.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
