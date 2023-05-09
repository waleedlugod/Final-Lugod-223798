import java.awt.*;
import javax.swing.*;
import java.util.*;

public class GameCanvas extends JComponent {
    // TODO: Change size
    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;
    public Bullet[][] bullets = new Bullet[2][Bullet.MAX_BULLETS];
    public Player[] players = new Player[2];
    private final int CLIENT_ID;
    private final int CANVAS_ID;
    private ArrayList<DrawingObject> objectsToDraw = new ArrayList<DrawingObject>();
    private PlayerStats playerStats;

    public GameCanvas(int CLIENT_ID, int CANVAS_ID) {
        this.CLIENT_ID = CLIENT_ID;
        this.CANVAS_ID = CANVAS_ID;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        addPlayers();
        addBullets();

        if (CANVAS_ID == 0) {
            playerStats = new PlayerStats(players);
            objectsToDraw.add(playerStats);
        }

        objectsToDraw.add(new Border());
    }

    private void addPlayers() {
        players[0] = new Player(new Color(CLIENT_ID == 0 ? 0xff0000 : 0x0000ff, CANVAS_ID != 0), 0);
        players[1] = new Player(new Color(CLIENT_ID == 0 ? 0x0000ff : 0xff0000, CANVAS_ID != 1), 1);
        objectsToDraw.add(players[0]);
        objectsToDraw.add(players[1]);
    }

    private void addBullets() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < bullets[i].length; j++) {
                bullets[i][j] = new Bullet(
                        i == 0 ? players[0] : players[1],
                        i == 0 ? players[1] : players[0],
                        j,
                        new Color(0x00ff00));
                objectsToDraw.add(bullets[i][j]);
            }
        }
    }

    public void copy(GameCanvas gameCanvas) {
        for (int i = 0; i < 2; i++) {
            Vector2 playerPosition = gameCanvas.players[i].getPosition();
            players[i].setPostion(playerPosition.x, playerPosition.y);
            players[i].health = gameCanvas.players[i].health;
            players[i].wins = gameCanvas.players[i].wins;
            players[i].losses = gameCanvas.players[i].losses;
            for (int j = 0; j < Bullet.MAX_BULLETS; j++) {
                Vector2 bulletPosition = gameCanvas.bullets[i][j].getPosition();
                Vector2 bulletVelocity = gameCanvas.bullets[i][j].getVelocity();
                bullets[i][j].setPosition(bulletPosition.x, bulletPosition.y);
                bullets[i][j].setVelocity(bulletVelocity.x, bulletVelocity.y);
            }
        }
    }

    private void reset() {
        for (int i = 0; i < 2; i++) {
            players[i].reset();
            for (int j = 0; j < Bullet.MAX_BULLETS; j++) {
                bullets[i][j].reset();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        if (players[0].health <= 0 || players[1].health <= 0) {
            if (players[0].health <= 0) {
                players[0].losses++;
                players[1].wins++;
            } else if (players[1].health <= 0) {
                players[0].wins++;
                players[1].losses++;
            }
            reset();
        }

        for (DrawingObject object : objectsToDraw) {
            object.draw(g2d);
        }
    }
}
