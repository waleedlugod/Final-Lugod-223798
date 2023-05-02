import java.awt.*;
import java.awt.geom.*;

public class Player implements DrawingObject {
    private final int SPEED = 1;
    public final boolean IS_SELF;
    private Vector2 position = new Vector2();
    private Vector2 size = new Vector2(25, 50);
    private Color color;
    private final Rectangle2D.Double player;

    public Player(int x, int y, boolean IS_SELF, Color color) {
        this.IS_SELF = IS_SELF;
        this.color = color;
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

    public void setPostion(Vector2 position) {
        this.position = position;
    }

    public Vector2 getSize() {
        return size;
    }

    private void animate() {
        if (IS_SELF) {
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
