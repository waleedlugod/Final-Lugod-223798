import java.awt.*;
import javax.swing.*;
import java.util.*;

public class GameCanvas extends JComponent {
    private final int ID;
    private ArrayList<DrawingObject> objectsToDraw = new ArrayList<DrawingObject>();
    private Bullet[][] bullets = new Bullet[2][Bullet.MAX_BULLETS];
    private Player selfPlayer;
    private Player otherPlayer;

    public GameCanvas(int ID) {
        this.ID = ID;

        setPreferredSize(new Dimension(800, 600));

        addPlayers();
        addBullets();
    }

    public Vector2 getSelfPlayerPosition() {
        return selfPlayer.getPosition();
    }

    public void setOtherPlayerPosition(Vector2 position) {
        otherPlayer.setPostion(position);
    }

    public Vector2[] getSelfBulletsPositions() {
        Vector2[] selfBulletsPositions = new Vector2[Bullet.MAX_BULLETS];
        for (int i = 0; i < bullets[0].length; i++) {
            selfBulletsPositions[i] = bullets[0][i].getPosition();
        }
        return selfBulletsPositions;
    }

    public void setOtherBulletsPositions(Vector2[] otherBulletsPositions) {
        for (int i = 0; i < Bullet.MAX_BULLETS; i++) {
            bullets[1][i].setPosition(otherBulletsPositions[i]);
        }
    }

    public Vector2[] getSelfBulletsVelocities() {
        Vector2[] selfBulletsVelocities = new Vector2[Bullet.MAX_BULLETS];
        for (int i = 0; i < bullets[0].length; i++) {
            selfBulletsVelocities[i] = bullets[0][i].getVelocity();
        }
        return selfBulletsVelocities;
    }

    public void setSelfBulletsVelocities(Vector2[] otherBulletsVelocities) {
        for (int i = 0; i < Bullet.MAX_BULLETS; i++) {
            bullets[1][i].setVelocity(otherBulletsVelocities[i]);
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

    private void addPlayers() {
        selfPlayer = new Player(100, 100, true, new Color(ID == 0 ? 0xff0000 : 0x0000ff));
        otherPlayer = new Player(200, 100, false, new Color(ID == 1 ? 0xff0000 : 0x0000ff));
        objectsToDraw.add(selfPlayer);
        objectsToDraw.add(otherPlayer);
    }

    private void addBullets() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < bullets[i].length; j++) {
                bullets[i][j] = new Bullet(i == 0 ? selfPlayer : otherPlayer, j, new Color(0x00ff00));
                objectsToDraw.add(bullets[i][j]);
            }
        }
    }
}
