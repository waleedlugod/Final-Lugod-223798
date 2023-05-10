import javax.swing.*;

import java.awt.GridLayout;
import java.awt.event.*;
import java.util.ArrayList;

public class GameFrame extends JFrame {
    public static final ArrayList<String> pressedKeys = new ArrayList<>();
    public static int animationFrame = 0;
    public GameCanvas selfCanvas;
    public GameCanvas enemyCanvas;
    private final int CLIENT_ID;

    public GameFrame(int CLIENT_ID) {
        this.CLIENT_ID = CLIENT_ID;

        JPanel keyBindingsPane = (JPanel) getContentPane();
        keyBindingsPane.setFocusable(true);
        addKeyListeners(keyBindingsPane);
        selfCanvas = new GameCanvas(CLIENT_ID, 0);
        enemyCanvas = new GameCanvas(CLIENT_ID, 1);
    }

    public void setupGUI() {
        Timer timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                selfCanvas.repaint();
                enemyCanvas.repaint();
            }
        });
        timer.start();
        Timer animationTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                animationFrame = (animationFrame + 1) % 4;
            }
        });
        animationTimer.start();
        setLayout(new GridLayout(2, 1));
        add(selfCanvas);
        add(enemyCanvas);
        setTitle(String.format("Player %d", CLIENT_ID + 1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }

    public void setSelfCanvas(GameCanvas gameCanvas) {
        this.selfCanvas = gameCanvas;
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
