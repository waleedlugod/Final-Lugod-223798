import java.awt.*;
import javax.swing.*;
import java.util.*;

public class GameCanvas extends JComponent {
    // TODO: Change size
    public static final int WIDTH = 800;
    public static final int HEIGHT = 300;
    public Bullet[][] bullets = new Bullet[2][Bullet.MAX_BULLETS];
    public Player[] players = new Player[2];
    public final int CLIENT_ID;
    public final int CANVAS_ID;
    private ArrayList<DrawingObject> objectsToDraw = new ArrayList<DrawingObject>();

    public GameCanvas(int CLIENT_ID, int CANVAS_ID) {
        this.CLIENT_ID = CLIENT_ID;
        this.CANVAS_ID = CANVAS_ID;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        addBackground();
        addPlayers();
        addBullets();

        objectsToDraw.add(new PlayerStats(players[CANVAS_ID]));
    }

    private void addBackground() {
        Background bg = new Background();
        bg.loadAssets(this);
        objectsToDraw.add(bg);
    }

    private void addPlayers() {
        players[0] = new Player(new Vector2(CLIENT_ID == 0
                ? 100
                : WIDTH - 100, (HEIGHT / 2) - Player.SIZE.y / 2),
                true);
        players[1] = new Player(new Vector2(CLIENT_ID == 0
                ? WIDTH - 100
                : 100, (HEIGHT / 2) - Player.SIZE.y / 2),
                false);
        players[0].loadAssets(this);
        players[1].loadAssets(this);
        objectsToDraw.add(players[0]);
        objectsToDraw.add(players[1]);
    }

    private void addBullets() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < bullets[i].length; j++) {
                bullets[i][j] = new Bullet(
                        i == 0 ? players[0] : players[1],
                        i == 0 ? players[1] : players[0],
                        j);
                bullets[i][j].loadAssets(this);
                objectsToDraw.add(bullets[i][j]);
            }
        }
    }

    public void copy(GameCanvas gameCanvas) {
        for (int i = 0; i < 2; i++) {
            Vector2 playerPosition = gameCanvas.players[i].getPosition();
            players[i].setPostion(playerPosition.x, playerPosition.y);
            players[i].health = gameCanvas.players[i].health;
            for (int j = 0; j < Bullet.MAX_BULLETS; j++) {
                Vector2 bulletPosition = gameCanvas.bullets[i][j].getPosition();
                Vector2 bulletVelocity = gameCanvas.bullets[i][j].getVelocity();
                bullets[i][j].setPosition(bulletPosition.x, bulletPosition.y);
                bullets[i][j].setVelocity(bulletVelocity.x, bulletVelocity.y);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        for (DrawingObject object : objectsToDraw) {
            object.draw(g2d);
        }
    }
}
