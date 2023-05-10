import java.awt.*;
import java.awt.geom.*;

public class PlayerStats implements DrawingObject {
    private final Player[] players;

    public PlayerStats(Player[] players) {
        this.players = players;
    }

    public void draw(Graphics2D g2d) {
        AffineTransform reset = g2d.getTransform();
        g2d.setColor(new Color(0x000000));
        g2d.drawString("Your points: " + players[0].points, 0, 10);
        g2d.drawString("Enemy points: " + players[1].points, 0, 20);
        g2d.setTransform(reset);
    }
}
