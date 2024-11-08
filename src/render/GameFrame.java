package render;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private JPanel gamePanel;
    private GameScreen gameScreen;
    private GameGuideRender gameGuideRender;

    public GameFrame() {
        initComponents();
        setLocationRelativeTo(null);
        switchToGuideScreen(); // Start with the guide screen

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

    private void switchToGuideScreen() {
        gameGuideRender = new GameGuideRender(this::switchToGameScreen); // Transition callback
        gamePanel.add(gameGuideRender, "GuideScreen");
        ((CardLayout) gamePanel.getLayout()).show(gamePanel, "GuideScreen");
        gameGuideRender.requestFocusInWindow(); // Ensure key listener works
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
