import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;

public class Player implements DrawingObject {

    public static final int MAX_HEALTH = 5;
    public static final int SPEED = 1;
    public static final Vector2 SIZE = new Vector2(24, 34);
    public final boolean IS_SELF;

    public int points = 0;

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

    public Player(Vector2 RESET_POSITION, boolean IS_SELF) {
        this.IS_SELF = IS_SELF;
        if (!Player.animationTimer.isRunning())
            animationTimer.start();
    }

    @Override
    public void draw(Graphics2D g2d) {
        animate();
        AffineTransform reset = g2d.getTransform();
        g2d.translate(position.x, position.y);
        g2d.scale(2, 2);
        g2d.drawImage(sprites[Player.animationFrame], null, spriteOffset.x, spriteOffset.y);
        g2d.setTransform(reset);
    }

    public Vector2 getPosition() {
        return new Vector2(position.x, position.y);
    }

    public void setPostion(int x, int y) {
        position.x = x;
        position.y = y;
    }

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

    private void animate() {
        if (IS_SELF) {
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
