import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GameFrame extends JFrame {
    public static final ArrayList<String> pressedKeys = new ArrayList<>();
    private GameCanvas gameCanvas;
    private final int CLIENT_ID;
    private final int FRAME_ID;

    public GameFrame(int CLIENT_ID, int FRAME_ID) {
        this.CLIENT_ID = CLIENT_ID;
        this.FRAME_ID = FRAME_ID;

        JPanel keyBindingsPane = (JPanel) getContentPane();
        keyBindingsPane.setFocusable(true);
        addKeyListeners(keyBindingsPane);
        gameCanvas = new GameCanvas(CLIENT_ID, FRAME_ID);
    }

    public void setupGUI() {
        Timer timer = new Timer(0, new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                gameCanvas.repaint();
            }
        });
        timer.start();
        add(gameCanvas);
        setTitle("Player " + (int) (CLIENT_ID + 1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }

    public GameCanvas getGameCanvas() {
        return gameCanvas;
    }

    public void setGameCanvas(GameCanvas gameCanvas) {
        this.gameCanvas = gameCanvas;
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
