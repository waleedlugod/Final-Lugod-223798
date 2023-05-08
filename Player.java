import java.awt.*;
import java.awt.geom.*;

public class Player implements DrawingObject {
    public static final int SPEED = 1;
    public static final Vector2 SIZE = new Vector2(25, 50);
    public final int ID;
    public int health = 5;
    private Vector2 position = new Vector2();
    private Color color;
    private final Rectangle2D.Double player;

    public Player(int x, int y, Color color, int ID) {
        this.color = color;
        this.ID = ID;
        // TODO: Change sprite
        player = new Rectangle.Double(0, 0, SIZE.x, SIZE.y);
        position.x = x;
        position.y = y;
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (health < 0) {
            color = new Color(0xff00ff);
        }
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

    public void hit() {
        health--;
        System.out.println(health);
    }

    private void animate() {
        if (ID == 0) {
            Vector2 newPosition = new Vector2(position.x, position.y);
            for (String pressedKey : GameFrame.pressedKeys) {
                switch (pressedKey) {
                    case "A":
                        newPosition.x -= SPEED;
                        break;
                    case "D":
                        newPosition.x += SPEED;
                        break;
                    case "W":
                        newPosition.y -= SPEED;
                        break;
                    case "S":
                        newPosition.y += SPEED;
                        break;
                }
            }
            if (!Collision.isCollidingWithWall(newPosition, SIZE)) {
                position = newPosition;
            }
        }
    }
}
