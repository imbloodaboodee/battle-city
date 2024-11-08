package render;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameGuideRender extends JPanel {

    public GameGuideRender(Runnable onEnterPress) {
        setLayout(new BorderLayout());
        JLabel guideLabel = new JLabel("<html><h1>Welcome to Battle City!</h1><p>Press Enter to Start</p></html>", SwingConstants.CENTER);
        add(guideLabel, BorderLayout.CENTER);

        // Add key listener for Enter key to switch to game screen
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    onEnterPress.run(); // Trigger the transition to the game screen
                }
            }
        });

        setFocusable(true); // To ensure the panel can receive key events
        requestFocusInWindow();
    }
}
