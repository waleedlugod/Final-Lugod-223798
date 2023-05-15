
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

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Handles the frame where the game is played. Also handles what keys the player
 * pressed.
 */
public class GameFrame extends JFrame {
    public static final ArrayList<String> pressedKeys = new ArrayList<>();
    public GameCanvas selfCanvas;
    public GameCanvas enemyCanvas;
    private final int CLIENT_ID;

    /**
     * Instantiates fields.
     * 
     * @param CLIENT_ID
     */
    public GameFrame(int CLIENT_ID) {
        this.CLIENT_ID = CLIENT_ID;

        JPanel keyBindingsPane = (JPanel) getContentPane();
        keyBindingsPane.setFocusable(true);
        addKeyListeners(keyBindingsPane);
        selfCanvas = new GameCanvas(CLIENT_ID, 0);
        enemyCanvas = new GameCanvas(CLIENT_ID, 1);
    }

    /**
     * Creates a timer to constantly repaint the canvases and switches the canvas
     * when the game ends. Also adds canvases to the screen.
     */
    public void setupGUI() {
        Timer timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                selfCanvas.repaint();
                enemyCanvas.repaint();
                if (selfCanvas.players[0].health <= 0)
                    setEnd(CLIENT_ID == 0 ? 1 : 0);
                else if (selfCanvas.players[1].health <= 0)
                    setEnd(CLIENT_ID);
            }
        });
        timer.start();
        setLayout(new GridLayout(2, 1));
        add(selfCanvas);
        add(enemyCanvas);
        setTitle(String.format("Player %d", CLIENT_ID + 1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }

    /**
     * Sets the frame to only include the ending screen.
     * 
     * @param winner
     */
    public void setEnd(int winner) {
        remove(selfCanvas);
        remove(enemyCanvas);
        setLayout(new BorderLayout());
        EndCanvas end = new EndCanvas(winner);
        add(end);
        revalidate();
    }

    /**
     * Setups the key listeners for controls. Updates an arraylist containing what
     * keys are being pressed.
     * 
     * @param contentPane
     */
    private void addKeyListeners(JPanel contentPane) {
        contentPane.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent ke) {
                String keyText = KeyEvent.getKeyText(ke.getKeyCode());
                if (!pressedKeys.contains(keyText))
                    pressedKeys.add(keyText);
            }

            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                pressedKeys.remove(KeyEvent.getKeyText(ke.getKeyCode()));
            }
        });
    }

}
