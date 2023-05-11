import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;

public class PlayerStats implements DrawingObject {
    private Player player;
    private BufferedImage fullHeart;
    private BufferedImage emptyHeart;

    public PlayerStats(Player player) {
        this.player = player;
        loadAssets();
    }

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

    private void loadAssets() {
        try {
            fullHeart = ImageIO.read(new File("assets/health/ui_heart_full.png"));
            emptyHeart = ImageIO.read(new File("assets/health/ui_heart_empty.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
