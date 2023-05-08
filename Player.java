import java.awt.*;
import java.awt.geom.*;

public class Player implements DrawingObject {
    public final int ID;
    private final int SPEED = 1;
    private Vector2 position = new Vector2();
    private Vector2 size = new Vector2(25, 50);
    private Color color;
    private final Rectangle2D.Double player;

    public Player(int x, int y, Color color, int ID) {
        this.color = color;
        this.ID = ID;
        // TODO: Change sprite
        player = new Rectangle.Double(0, 0, size.x, size.y);
    }

    @Override
    public void draw(Graphics2D g2d) {
        animate();
        AffineTransform reset = g2d.getTransform();
        g2d.translate(position.x, position.y);
        g2d.setColor(color);
        g2d.draw(player);
        g2d.fill(player);
        g2d.setTransform(reset);
    }

    public Vector2 getPosition() {
        return new Vector2(position.x, position.y);
    }

    public void setPostion(int x, int y) {
        position.x = x;
        position.y = y;
    }

    public Vector2 getSize() {
        return size;
    }

    private void animate() {
        if (ID == 0) {
            for (String pressedKey : GameFrame.pressedKeys) {
                switch (pressedKey) {
                    case "A":
                        position.x -= SPEED;
                        break;
                    case "D":
                        position.x += SPEED;
                        break;
                    case "W":
                        position.y -= SPEED;
                        break;
                    case "S":
                        position.y += SPEED;
                        break;
                }
            }
        }
    }
}
