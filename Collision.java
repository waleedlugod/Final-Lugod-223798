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

/**
 * Handles the calculation for all collisions. Calculates if an object is
 * colliding with a wall or another object.
 */
public class Collision {
    /**
     * Determines if the object is colliding with the edge of the screen.
     * 
     * @param pos  Position of the object.
     * @param size Size of the object.
     * @return If the object is colliding with a wall.
     */
    public static boolean isCollidingWithWall(Vector2 pos, Vector2 size) {
        return (pos.x < 0 + 32 ||
                pos.y < 0 + 32 ||
                pos.x + size.x > GameCanvas.WIDTH - 32 ||
                pos.y + size.y > GameCanvas.HEIGHT - 32);
    }

    /**
     * Determines if the object is colliding with another object.
     * 
     * @param pos1  Position of the first object
     * @param size1 Size of the first object.
     * @param pos2  Position of the second object.
     * @param size2 Size of the second object.
     * @return If the objects are colliding.
     */
    public static boolean isColliding(Vector2 pos1, Vector2 size1, Vector2 pos2, Vector2 size2) {
        return (pos1.y + size1.y >= pos2.y &&
                pos1.y <= pos2.y + size2.y &&
                pos1.x + size1.x >= pos2.x &&
                pos1.x <= pos2.x + size2.x);
    }
}
