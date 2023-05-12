import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

public class EndCanvas extends JComponent {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private BufferedImage bg;
    private Font font;
    private int winner;

    public EndCanvas(int winner) {
        this.winner = winner;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        loadAssets();
    }

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

    // assets: https://paperhatlizard.itch.io/cryos-mini-gui
    // https://not-jam.itch.io/not-jam-slab-serif-11
    // https://stackoverflow.com/questions/5652344/how-can-i-use-a-custom-font-in-java
    private void loadAssets() {
        try {
            bg = ImageIO.read(new File("assets/background/end.png"));
            // TODO: fix font
            font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/NotJamSlabSerif11.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
    }

    // http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/Centertext.htm
    public void drawCenteredString(String s, int w, int h, Graphics2D g2d) {
        g2d.setFont(new Font(font.getFontName(), Font.PLAIN, 15));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (w - fm.stringWidth(s)) / 2;
        int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
        g2d.drawString(s, x, y);
    }
}
