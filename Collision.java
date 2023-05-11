public class Collision {
    public static boolean isCollidingWithWall(Vector2 pos, Vector2 size) {
        return (pos.x < 0 + 32 ||
                pos.y < 0 + 32 ||
                pos.x + size.x > GameCanvas.WIDTH - 32 ||
                pos.y + size.y > GameCanvas.HEIGHT - 32);
    }

    public static boolean isColliding(Vector2 pos1, Vector2 size1, Vector2 pos2, Vector2 size2) {
        return (pos1.y + size1.y >= pos2.y &&
                pos1.y <= pos2.y + size2.y &&
                pos1.x + size1.x >= pos2.x &&
                pos1.x <= pos2.x + size2.x);
    }
}
