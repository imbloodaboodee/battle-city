package render;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private JPanel gamePanel;
    private GameGuideScreen gameGuideScreen;
    private GameMenuScreen gameMenuScreen;

    public GameFrame() {
        initComponents();
        setLocationRelativeTo(null);
        switchToMenuScreen(); // Start with the guide screen

    }

    private void initComponents() {

        gamePanel = new JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Battle City");
        setName("mainFrame"); // NOI18N
        setPreferredSize(new java.awt.Dimension(512, 470));

        gamePanel.setMinimumSize(new java.awt.Dimension(500, 500));
        gamePanel.setSize(new java.awt.Dimension(528, 448));
        gamePanel.setLayout(new CardLayout()); // Use CardLayout here

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(gamePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(gamePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }
    private void switchToMenuScreen() {
        gameMenuScreen = new GameMenuScreen(this::switchToGuideScreen); // Callback to switch to Guide screen
        gamePanel.add(gameMenuScreen, "MenuScreen");
        ((CardLayout) gamePanel.getLayout()).show(gamePanel, "MenuScreen");
        gameMenuScreen.requestFocusInWindow(); // Ensure the menu screen gets focus for key events
    }

    private void switchToGuideScreen() {
        gameGuideScreen = new GameGuideScreen(this::switchToGameScreen); // Transition callback
        gamePanel.add(gameGuideScreen, "GuideScreen");
        ((CardLayout) gamePanel.getLayout()).show(gamePanel, "GuideScreen");
        gameGuideScreen.requestFocusInWindow(); // Ensure key listener works
    }

    private void switchToGameScreen() {
        gamePanel.add(GameScreen.getInstance(), "GameScreen");
        ((CardLayout) gamePanel.getLayout()).show(gamePanel, "GameScreen");
        GameScreen.getInstance().requestFocusInWindow();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GameFrame().setVisible(true);
            }
        });
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }

}
