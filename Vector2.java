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
 * Stores two pieces of data as a single variable. Used for position, velocity,
 * size, etc. An alternative to arrays of length two for readability.
 */
public class Vector2 {
    public int x = 0;
    public int y = 0;

    /**
     * Initializes to default values
     */
    public Vector2() {
    }

    /**
     * Initializes to values given.
     */
    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
