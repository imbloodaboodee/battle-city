package jsd.project.tank90.render;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private JPanel gamePanel;

    private GameMenuScreen gameMenuScreen;
    private GameGuideScreen gameGuideScreen;
    private AssignStatsScreen assignStatsScreen;
    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;

    public GameFrame() {
        initComponents();
        setLocationRelativeTo(null);
        switchToMenuScreen();
    }

    private void initComponents() {
        gamePanel = new JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Battle City");
        setName("mainFrame");
        setPreferredSize(new java.awt.Dimension(512, 470));

        gamePanel.setMinimumSize(new java.awt.Dimension(500, 500));
        gamePanel.setSize(new java.awt.Dimension(528, 448));
        gamePanel.setLayout(new CardLayout());

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
        gameMenuScreen = new GameMenuScreen(this::switchToGuideScreen);
        gamePanel.add(gameMenuScreen, "MenuScreen");
        ((CardLayout) gamePanel.getLayout()).show(gamePanel, "MenuScreen");
        gameMenuScreen.requestFocusInWindow();
    }

    private void switchToGuideScreen() {
        gameGuideScreen = new GameGuideScreen(this::switchToAssignStatsScreen);
        gamePanel.add(gameGuideScreen, "GuideScreen");
        ((CardLayout) gamePanel.getLayout()).show(gamePanel, "GuideScreen");
        gameGuideScreen.requestFocusInWindow();
    }

    private void switchToAssignStatsScreen() {
        assignStatsScreen = new AssignStatsScreen(this::switchToGameScreen);
        gamePanel.add(assignStatsScreen, "AssignStatsScreen");
        ((CardLayout) gamePanel.getLayout()).show(gamePanel, "AssignStatsScreen");
        assignStatsScreen.requestFocusInWindow();
    }


    private void switchToGameScreen() {
        gameScreen = GameScreen.getInstance();
        gameScreen.setOnGameOver(this::switchToGameOverScreen);
        gameScreen.setPlayerTankRender(assignStatsScreen.getHealth(), assignStatsScreen.getSpeed(), assignStatsScreen.getRotationSpeed(), assignStatsScreen.getSelectedBulletType());
        gamePanel.add(gameScreen, "GameScreen");
        ((CardLayout) gamePanel.getLayout()).show(gamePanel, "GameScreen");
        gameScreen.requestFocusInWindow();
    }

    private void switchToGameOverScreen() {
        gameOverScreen = new GameOverScreen(this::resetGame);
        gamePanel.add(gameOverScreen, "GameOverScreen");
        ((CardLayout) gamePanel.getLayout()).show(gamePanel, "GameOverScreen");
        gameOverScreen.requestFocusInWindow();
    }

    private void resetGame() {
        gamePanel.removeAll();

        gameMenuScreen = null;
        assignStatsScreen = null;
        gameGuideScreen = null;
        gameScreen = null;
        gameOverScreen = null;

        GameScreen.resetInstance();

        switchToMenuScreen();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new GameFrame().setVisible(true));
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }
}
