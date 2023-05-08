import java.awt.*;
import java.awt.geom.*;

public class PlayerStats {
    private final Player[] players;

    public PlayerStats(Player[] players) {
        this.players = players;
    }

    public void draw(Graphics2D g2d) {
        AffineTransform reset = g2d.getTransform();
        g2d.setColor(new Color(0x000000));
        g2d.drawString("Your health: " + players[0].health, 10, 10);
        g2d.drawString("Enemy health: " + players[1].health, 10, 20);
        g2d.setTransform(reset);
    }
}
