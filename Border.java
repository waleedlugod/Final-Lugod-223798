import java.awt.*;
import java.awt.geom.*;

public class Border implements DrawingObject {
    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform reset = g2d.getTransform();
        g2d.setColor(new Color(0x000000));
        g2d.draw(new Rectangle2D.Double(0, 0, GameCanvas.WIDTH, GameCanvas.HEIGHT));
        g2d.setTransform(reset);
    }
}
