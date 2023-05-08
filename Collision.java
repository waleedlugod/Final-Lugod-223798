public class Collision {
    public static boolean isCollidingWithWall(Vector2 pos, Vector2 size) {
        if (pos.x < 0 || pos.y < 0 || pos.x + size.x > GameCanvas.WIDTH || pos.y + size.y > GameCanvas.HEIGHT) {
            return true;
        }
        return false;
    }
}
