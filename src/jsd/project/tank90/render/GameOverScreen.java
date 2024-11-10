package jsd.project.tank90.render;

import jsd.project.tank90.physics.CollisionHandling;
import jsd.project.tank90.physics.ImageUtility;
import jsd.project.tank90.physics.SoundUtility;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameOverScreen extends JPanel {

    private int stage, totalTankNum;
    private int totalScore = 0;
    private final int SHIFT = 80;
    private final ImageUtility imageInstance = ImageUtility.getInstance();
    private int[] tankScoreList = {0, 0, 0, 0};
    private int[] tankNumList = {0, 0, 0, 0};

    public static Font loadFont() {
        Font font = null;
        try {
            font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT,
                    new File("src/jsd/project/tank90/assets/font/prstart.ttf"));
            font = font.deriveFont(java.awt.Font.PLAIN, 15);
            GraphicsEnvironment ge
                    = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);

        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return font;
    }

    public GameOverScreen(Runnable onEnterPress) {
        SoundUtility.statistics();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    onEnterPress.run();
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();

        setBackground(Color.BLACK);
        setForeground(Color.BLACK);
        setLayout(null);
    }

    @Override
    public void paintComponent(Graphics g) {
        loadScore();
        stage = GameScreen.getStage();
        super.paintComponent(g);
        Font font = loadFont();
        ArrayList<Image> tankList = new ArrayList<>(
                Arrays.asList(imageInstance.getTankBasic(),
                        imageInstance.getTankFast(),
                        imageInstance.getTankPower(),
                        imageInstance.getTankArmor()));

        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("GAME OVER", 100 + SHIFT, 60);

        g.setColor(Color.RED);
        g.drawString("1-PLAYER", 37 + SHIFT, 95);

        g.setColor(Color.orange);
        g.drawString("SCORE: " + totalScore, 121 + SHIFT, 130);

        for (int i = 0; i < 4; i++) {
            g.drawImage(tankList.get(i), 226 + SHIFT, 160 + (i * 45), this);
            g.drawImage(imageInstance.getArrow(), 206 + SHIFT, 168 + (i * 45), this);
        }
        for (int i = 0; i < 4; i++) {
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(tankScoreList[i]), 55 + SHIFT, 180 + (i * 45));
            g.drawString("PTS", 115 + SHIFT, 180 + (i * 45));
        }

        for (int i = 0; i < 4; i++) {
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(tankNumList[i]), 180 + SHIFT, 180 + (i * 45));
        }

        g.drawLine(170, 330, 307, 330);

        g.drawString("TOTAL", 85 + SHIFT, 355);
        g.drawString(String.valueOf(totalTankNum), 180 + SHIFT, 355);

        g.setColor(Color.ORANGE);
        g.drawString("PRESS ENTER TO RESTART", getWidth() / 2 - 170, getHeight() * 4 / 5);


    }
    public void loadScore() {
        for (int i = 0; i < 4; i++) {
            int[] enemyTankNum = CollisionHandling.getEnemyTankNum();
            tankNumList[i] = enemyTankNum[i];
        }
        for (int i = 0; i < 4; i++) {
            tankScoreList[i] = tankNumList[i] * 100 * (i + 1);
        }
        totalScore = 0;
        for (Integer score : tankScoreList) {
            totalScore += score;
        }
        totalTankNum = 0;
        for (Integer num : tankNumList) {
            totalTankNum += num;
        }
    }

}