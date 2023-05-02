import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GameFrame extends JFrame {
    public static final ArrayList<String> pressedKeys = new ArrayList<>();
    private GameCanvas gameCanvas;
    private int ID;

    public GameFrame(int ID) {
        this.ID = ID;

        JPanel keyBindingsPane = (JPanel) getContentPane();
        keyBindingsPane.setFocusable(true);
        addKeyListeners(keyBindingsPane);
        gameCanvas = new GameCanvas(ID);

        Timer timer = new Timer(0, new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                gameCanvas.repaint();
            }
        });
        timer.start();
    }

    public void setupGUI() {
        add(gameCanvas);
        setTitle("Player " + (int) (ID + 1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }

    public GameCanvas getGameCanvas() {
        return gameCanvas;
    }

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
