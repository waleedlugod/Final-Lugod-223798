
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

import java.awt.*;

/**
 * Interface for objects to be drawn on the screen. Uses Graphics2D.
 */
public interface DrawingObject {
    /**
     * Draws the object.
     * 
     * @param g2d
     */
    public void draw(Graphics2D g2d);
}
