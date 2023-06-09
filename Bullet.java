
/**
 * @author Waleed Lugod (223798)
 * @version May 15, 2023
 */
/**
 * I have not discussed the Java language code in my program
 * with anyone other than my instructor or the teaching assistants
 * assigned to this course.
 * I have not used Java language code obtained from another student,
 * or any other unauthorized source, either modified or unmodified.
 * If any Java language code or documentation used in my program
 * was obtained from another source, such as a textbook or website,
 * that has been clearly noted with a proper citation in the comments
 * of my program.
 */

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.event.*;

import java.time.*;
import java.io.*;

import javax.swing.*;
import javax.imageio.*;

/**
 * Creates a bullet that the players are able to shoot. Animates the bullet
 * with an internal timer. Handles which bullet the player is able to shoot.
 * Assets taken from https://bdragon1727.itch.io/fire-pixel-bullet-16x16.
 */
public class Bullet implements DrawingObject {
    public static final int MAX_BULLETS = 4;
    public static final int SPEED = 3;
    public static final Vector2 SIZE = new Vector2(16, 16);

    private static int SHOOTABLE_IDX = 0;
    private static long SHOOT_BUFFER = 0;
    private final int BULLET_IDX;

    private static int animationFrame = 0;
    private static final Timer animationTimer = new Timer(200, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            animationFrame = (animationFrame + 1) % 4;
        }
    });;

    private BufferedImage[] sprites = new BufferedImage[4];
    private Vector2 spriteOffset = new Vector2();
    private double[] spriteScale = { 1, 1 };

    private Vector2 position = new Vector2(-100, -100);
    private Vector2 velocity = new Vector2();

    private long directionBufferPrevTime = 0;
    private Clock clock;

    private Player owner;
    private Player otherPlayer;

    /**
     * Initializes fields.
     * 
     * @param owner       Which Player the Bullet belongs to.
     * @param otherPlayer Which Player the Bullet does not belong to.
     * @param BULLET_IDX  The order of which the Bullet will be shot. If the index
     *                    is 0, it will be shot first.
     */
    public Bullet(Player owner, Player otherPlayer, int BULLET_IDX) {
        this.BULLET_IDX = BULLET_IDX;
        this.owner = owner;
        this.otherPlayer = otherPlayer;
        clock = Clock.systemUTC();
        if (!Bullet.animationTimer.isRunning())
            animationTimer.start();
    }

    /**
     * Draws the sprite at the appropriate transformation.
     */
    @Override
    public void draw(Graphics2D g2d) {
        animate();
        AffineTransform reset = g2d.getTransform();
        g2d.translate(position.x, position.y);
        g2d.rotate(getShootAngle(), SIZE.x / 2, SIZE.y / 2);
        g2d.scale(spriteScale[0], spriteScale[1]);
        g2d.drawImage(sprites[Bullet.animationFrame], null, spriteOffset.x, spriteOffset.y);
        g2d.setTransform(reset);
    }

    /**
     * Sets the position.
     * 
     * @param x horizontal position
     * @param y vertical position
     */
    public void setPosition(int x, int y) {
        position.x = x;
        position.y = y;
    }

    /**
     * @return The current position.
     */
    public Vector2 getPosition() {
        return new Vector2(position.x, position.y);
    }

    /**
     * Sets the current velocity.
     * 
     * @param x horizontal velocity
     * @param y vertical velocity
     */
    public void setVelocity(int x, int y) {
        velocity.x = x;
        velocity.y = y;
    }

    /**
     * @return The current velocity.
     */
    public Vector2 getVelocity() {
        return new Vector2(velocity.x, velocity.y);
    }

    /**
     * Loads the images of sprite. Determines which image to load for the instance
     * of the Bullet.
     * 
     * @param canvas The canvas where the bullet is in.
     */
    public void loadAssets(GameCanvas canvas) {
        try {
            if ((canvas.CLIENT_ID == 0 && canvas.CANVAS_ID == 1 && owner.IS_SELF)
                    || (canvas.CLIENT_ID == 1 && canvas.CANVAS_ID == 0 && !owner.IS_SELF)) {
                for (int i = 0; i < sprites.length; i++) {
                    sprites[i] = ImageIO.read(new File(String.format("assets/player1/bullet/energyBall%d.png", i)));
                }
                spriteOffset = new Vector2(-3, -3);
                spriteScale = new double[] { 1.5, 1.5 };
            } else if ((canvas.CLIENT_ID == 0 && canvas.CANVAS_ID == 0 && !owner.IS_SELF)
                    || (canvas.CLIENT_ID == 1 && canvas.CANVAS_ID == 1 && owner.IS_SELF)) {
                for (int i = 0; i < sprites.length; i++) {
                    sprites[i] = ImageIO.read(new File(String.format("assets/player2/bullet/magic%d.png", i)));
                }
                spriteOffset = new Vector2(-8, -8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets the velocity, and sets the position to out of the screen.
     */
    public void reset() {
        velocity = new Vector2();
        position = new Vector2(-100, -100);
    }

    /**
     * Updates the position of the bullet. If it collides with something then resets
     * the velocity and position. Has a buffer of 200 milliseconds between each
     * Bullet shot.
     */
    private void animate() {
        if (owner.IS_SELF && SHOOTABLE_IDX == BULLET_IDX && clock.millis() - SHOOT_BUFFER > 200) {
            shoot();
        }
        position.x += velocity.x;
        position.y += velocity.y;
        if (Collision.isCollidingWithWall(position, SIZE)) {
            reset();
        } else if (Collision.isColliding(position, SIZE, otherPlayer.getPosition(), Player.SIZE)) {
            reset();
            otherPlayer.health--;
        }
    }

    /**
     * Determines the velocity of the Bullet depending on the keyboard input. Has a
     * 50 millisecond buffer to allot for if the user wants to do a combination of
     * inputs before determining the final velocity.
     */
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
                // Velocity has been determined.
                SHOOTABLE_IDX = (SHOOTABLE_IDX + 1) % MAX_BULLETS;
                SHOOT_BUFFER = clock.millis();
            }
        }
    }

    /**
     * Sets the position to the middle of the owner Player.
     */
    private void centerOnPlayer() {
        position = owner.getPosition();
        position.x += Player.SIZE.x / 2 - SIZE.x / 2;
        position.y += Player.SIZE.y / 2 - SIZE.y / 2;
    }

    /**
     * Determines the how to rotate the sprite depending on its velocity.
     * 
     * @return The rotation of the sprite.
     */
    private double getShootAngle() {
        double xAngle = velocity.x / SPEED;
        double yAngle = velocity.y / SPEED;
        double angle;
        if (xAngle == 0) {
            angle = Math.asin(yAngle);
        } else if (yAngle == 0) {
            angle = Math.acos(xAngle);
        } else {
            angle = Math.atan(yAngle / xAngle);
            if (xAngle == -1)
                angle += Math.PI;
        }
        return angle;
    }
}
