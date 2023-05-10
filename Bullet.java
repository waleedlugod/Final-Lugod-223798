import java.awt.*;
import java.awt.geom.*;
import java.time.*;

public class Bullet implements DrawingObject {
    public static final int MAX_BULLETS = 4;
    public static final int SPEED = 2;
    public static final Vector2 SIZE = new Vector2(15, 15);
    private static int shootableIdx = 0;
    private static long shootBufferPrevTime = 0;
    private final int BULLET_IDX;
    private Vector2 position = new Vector2(-100, -100);
    private Vector2 velocity = new Vector2();
    private long directionBufferPrevTime = 0;
    private Ellipse2D.Double bullet;
    private Player owner;
    private Player otherPlayer;
    private Color color;
    private Clock clock;

    public Bullet(Player owner, Player otherPlayer, int BULLET_IDX, Color COLOR) {
        // TODO: Change sprite
        bullet = new Ellipse2D.Double(0, 0, SIZE.x, SIZE.y);
        this.BULLET_IDX = BULLET_IDX;
        this.owner = owner;
        this.otherPlayer = otherPlayer;
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

    public void setVelocity(int x, int y) {
        velocity.x = x;
        velocity.y = y;
    }

    public Vector2 getVelocity() {
        return new Vector2(velocity.x, velocity.y);
    }

    private void animate() {
        if (owner.IS_SELF && shootableIdx == BULLET_IDX && clock.millis() - shootBufferPrevTime > 200) {
            shoot();
        }
        position.x += velocity.x;
        position.y += velocity.y;
        if (Collision.isCollidingWithWall(position, SIZE)) {
            reset();
        } else if (Collision.isColliding(position, SIZE, otherPlayer.getPosition(), Player.SIZE)) {
            reset();
            owner.points++;
        }
    }

    public void reset() {
        velocity = new Vector2();
        position = new Vector2(-100, -100);
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
        position.x += Player.SIZE.x / 2 - SIZE.x / 2;
        position.y += Player.SIZE.y / 2 - SIZE.y / 2;
    }
}
