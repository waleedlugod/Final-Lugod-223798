
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
import java.io.*;
import javax.swing.*;
import javax.imageio.*;

/**
 * Handles the Player logic. Handles the sprite animation and controls of the
 * player.
 */
public class Player implements DrawingObject {

    public static final int MAX_HEALTH = 5;
    public static final int SPEED = 1;
    public static final Vector2 SIZE = new Vector2(24, 34);
    public final boolean IS_SELF;

    public int health = MAX_HEALTH;
    public boolean isFacingLeft = false;

    private static int animationFrame = 0;
    private static final Timer animationTimer = new Timer(100, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            animationFrame = (animationFrame + 1) % 4;
        }
    });;
    private BufferedImage[] sprites = new BufferedImage[4];

    private Vector2 position = new Vector2();
    private Vector2 spriteOffset = new Vector2();

    /**
     * Initializes fields and start the animatinon timer for the sprite.
     * 
     * @param RESET_POSITION
     * @param IS_SELF
     */
    public Player(Vector2 RESET_POSITION, boolean IS_SELF) {
        position = new Vector2(RESET_POSITION.x, RESET_POSITION.y);
        this.IS_SELF = IS_SELF;
        if (!Player.animationTimer.isRunning())
            animationTimer.start();
    }

    /**
     * Draws the sprite on the screen.
     */
    @Override
    public void draw(Graphics2D g2d) {
        animate();
        AffineTransform reset = g2d.getTransform();
        g2d.translate(position.x, position.y);
        g2d.scale(2, 2);
        if (isFacingLeft)
            flip(g2d);
        g2d.drawImage(sprites[Player.animationFrame], null, spriteOffset.x, spriteOffset.y);
        g2d.setTransform(reset);
    }

    /**
     * @return Current position of the player.
     */
    public Vector2 getPosition() {
        return new Vector2(position.x, position.y);
    }

    /**
     * Sets the position of the player
     * 
     * @param x horizontal position
     * @param y vertical position
     */
    public void setPostion(int x, int y) {
        position.x = x;
        position.y = y;
    }

    /**
     * Determines which sprite to load for the player and loads it. Assets taken
     * from https://0x72.itch.io/dungeontileset-ii.
     * 
     * @param canvas The canvas which the player is in.
     */
    public void loadAssets(GameCanvas canvas) {
        try {
            if ((canvas.CLIENT_ID == 0 && canvas.CANVAS_ID == 0 && IS_SELF)
                    || (canvas.CLIENT_ID == 1 && canvas.CANVAS_ID == 1 && !IS_SELF)) {

                for (int i = 0; i < sprites.length; i++) {
                    sprites[i] = ImageIO.read(new File(
                            String.format("assets/player1/idle/wizzard_m_idle_anim_f%d.png", i)));
                }
                spriteOffset = new Vector2(-3, -11);
            } else if ((canvas.CLIENT_ID == 0 && canvas.CANVAS_ID == 1 && !IS_SELF)
                    || (canvas.CLIENT_ID == 1 && canvas.CANVAS_ID == 0 && IS_SELF)) {

                for (int i = 0; i < sprites.length; i++) {
                    sprites[i] = ImageIO.read(new File(
                            String.format("assets/player2/idle/necromancer_anim_f%d.png", i)));
                }
                spriteOffset = new Vector2(-2, -6);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Flips the sprite
     * 
     * @param g2d
     */
    private void flip(Graphics2D g2d) {
        g2d.scale(-1, 1);
        g2d.translate(-SIZE.x / 2, 0);
    }

    /**
     * Updates the position of the player depending on which keys are pressed.
     */
    private void animate() {
        if (IS_SELF) {
            Vector2 newPosition = new Vector2(position.x, position.y);
            for (String pressedKey : GameFrame.pressedKeys) {
                switch (pressedKey) {
                    case "A":
                        newPosition.x -= SPEED;
                        isFacingLeft = true;
                        break;
                    case "D":
                        newPosition.x += SPEED;
                        isFacingLeft = false;
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
