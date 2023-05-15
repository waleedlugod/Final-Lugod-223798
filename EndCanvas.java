
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
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

/**
 * The canvas to draw once the game ends. Draws which a text saying which Player
 * has won.
 */
public class EndCanvas extends JComponent {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private BufferedImage bg;
    private Font font;
    private int winner;

    /**
     * Initializes fields and loads assets.
     * 
     * @param winner Who won the game.
     */
    public EndCanvas(int winner) {
        this.winner = winner;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        loadAssets();
    }

    /**
     * Paints the canvas and the text.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        AffineTransform reset = g2d.getTransform();
        g2d.scale((double) WIDTH / (double) bg.getWidth(), (double) HEIGHT / (double) bg.getHeight());
        g2d.drawImage(bg, null, 0, 0);
        drawCenteredString("Player " + (winner + 1) + " wins!", bg.getWidth(), bg.getHeight(), g2d);
        g2d.setTransform(reset);
    }

    /**
     * Loads the assets used in the canvas. Assets from
     * https://not-jam.itch.io/not-jam-slab-serif-11
     * https://paperhatlizard.itch.io/cryos-mini-gui
     */
    private void loadAssets() {
        try {
            bg = ImageIO.read(new File("assets/background/end.png"));
            font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/NotJamSlabSerif11.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Centers a string on screen.
     * 
     * @param s The text.
     * @param w Width of the canvas.
     * @param h Height of the canvas.
     */
    public void drawCenteredString(String s, int w, int h, Graphics2D g2d) {
        g2d.setFont(new Font(font.getFontName(), Font.PLAIN, 15));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (w - fm.stringWidth(s)) / 2;
        int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
        g2d.drawString(s, x, y);
    }
}
