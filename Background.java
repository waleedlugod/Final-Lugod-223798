import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Background implements DrawingObject {
    private BufferedImage map;

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform reset = g2d.getTransform();
        g2d.setColor(new Color(0x000000));
        g2d.scale((double) GameCanvas.WIDTH / (double) map.getWidth(),
                (double) GameCanvas.HEIGHT / (double) map.getHeight());
        g2d.drawImage(map, null, 0, 0);
        g2d.setTransform(reset);
    }

    public void loadAssets(GameCanvas canvas) {
        try {
            map = ImageIO.read(new File("assets/Background/map.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
