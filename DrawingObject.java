import java.awt.*;

public interface DrawingObject {
    public void draw(Graphics2D g2d);

    public Vector2 getPosition();

    public Vector2 getSize();
}
