import java.awt.*;
import javax.swing.*;
import java.util.*;

public class GameCanvas extends JComponent {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public Bullet[][] bullets = new Bullet[2][Bullet.MAX_BULLETS];
    public Player[] players = new Player[2];
    private final int ID;
    private ArrayList<DrawingObject> objectsToDraw = new ArrayList<DrawingObject>();

    public GameCanvas(int ID) {
        this.ID = ID;

        setPreferredSize(new Dimension(800, 600));

        addPlayers();
        addBullets();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        movePlayer();

        for (DrawingObject object : objectsToDraw) {
            object.draw(g2d);
        }
    }

    private void addPlayers() {
        players[0] = new Player(ID == 0 ? 0 : 100, 0, new Color(ID == 0 ? 0xff0000 : 0x0000ff), 0);
        players[1] = new Player(ID == 0 ? 100 : 0, 0, new Color(ID == 0 ? 0x0000ff : 0xff0000), 1);
        objectsToDraw.add(players[0]);
        objectsToDraw.add(players[1]);
    }

    private void addBullets() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < bullets[i].length; j++) {
                bullets[i][j] = new Bullet(i == 0 ? players[0] : players[1], j, new Color(0x00ff00));
                objectsToDraw.add(bullets[i][j]);
            }
        }
    }

    public void movePlayer() {
        Vector2 playerPosition = players[0].getPosition();
        Vector2 newPosition = new Vector2(playerPosition.x, playerPosition.y);
        for (String pressedKey : GameFrame.pressedKeys) {
            switch (pressedKey) {
                case "A":
                    newPosition.x -= Player.SPEED;
                    break;
                case "D":
                    newPosition.x += Player.SPEED;
                    break;
                case "W":
                    newPosition.y -= Player.SPEED;
                    break;
                case "S":
                    newPosition.y += Player.SPEED;
                    break;
            }
        }
        if (!Collision.isCollidingWithWall(newPosition, Player.SIZE) &&
                !Collision.isColliding(newPosition, Player.SIZE, players[1].getPosition(), Player.SIZE)) {
            players[0].setPostion(newPosition.x, newPosition.y);
        }
    }
}
