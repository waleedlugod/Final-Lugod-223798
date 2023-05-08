import java.awt.*;
import java.awt.geom.*;
import java.time.*;

public class Bullet implements DrawingObject {
    public static final int MAX_BULLETS = 4;
    private static int shootableIdx = 0;
    private static long shootBufferPrevTime = 0;
    private final int SPEED = 1;
    private final int BULLET_IDX;
    private Vector2 position = new Vector2();
    private Vector2 velocity = new Vector2();
    private Vector2 size = new Vector2(10, 10);
    private long directionBufferPrevTime = 0;
    private Ellipse2D.Double bullet;
    private Player owner;
    private Color color;
    private Clock clock;

    public Bullet(Player owner, int BULLET_IDX, Color COLOR) {
        // TODO: Change sprite
        bullet = new Ellipse2D.Double(0, 0, size.x, size.y);
        this.BULLET_IDX = BULLET_IDX;
        this.owner = owner;
        this.color = COLOR;
        clock = Clock.systemUTC();
    }

    public void draw(Graphics2D g2d) {
        animate();
        AffineTransform reset = g2d.getTransform();
        g2d.translate(position.x, position.y);
        g2d.setColor(color);
        g2d.draw(bullet);
        g2d.fill(bullet);
        g2d.setTransform(reset);
    }

    public void setPosition(int x, int y) {
        position.x = x;
        position.y = y;
    }

    public Vector2 getPosition() {
        return new Vector2(position.x, position.y);
    }

    public Vector2 getSize() {
        return new Vector2(size.x, size.y);
    }

    public void setVelocity(int x, int y) {
        velocity.x = x;
        velocity.y = y;
    }

    public Vector2 getVelocity() {
        return new Vector2(velocity.x, velocity.y);
    }

    private void animate() {
        if (owner.ID == 0 &&
                shootableIdx == BULLET_IDX &&
                clock.millis() - shootBufferPrevTime > 500) {
            shoot();
        }
        position.x += velocity.x;
        position.y += velocity.y;
    }

    private void shoot() {
        for (String pressedKey : GameFrame.pressedKeys) {
            if (velocity.x == 0 && velocity.y == 0) {
                directionBufferPrevTime = clock.millis();
            }
            if (clock.millis() - directionBufferPrevTime < 50) {
                switch (pressedKey) {
                    case "Left":
                        velocity.x = -SPEED;
                        centerOnPlayer();
                        break;
                    case "Right":
                        velocity.x = SPEED;
                        centerOnPlayer();
                        break;
                    case "Up":
                        velocity.y = -SPEED;
                        centerOnPlayer();
                        break;
                    case "Down":
                        velocity.y = SPEED;
                        centerOnPlayer();
                        break;
                }
            } else {
                // Finished shooting
                shootableIdx = (shootableIdx + 1) % MAX_BULLETS;
                shootBufferPrevTime = clock.millis();
            }
        }
    }

    private void centerOnPlayer() {
        position = owner.getPosition();
        position.x += owner.getSize().x / 2 - size.x;
        position.y += owner.getSize().y / 2 - size.y;
    }
}
