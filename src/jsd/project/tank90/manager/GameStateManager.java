package jsd.project.tank90.manager;

import jsd.project.tank90.render.GameScreen;
import jsd.project.tank90.physics.BoardUtility;

import javax.swing.*;

public class GameStateManager {
    private static boolean levelTransitioning = false;  // New flag
    public static void checkTankDestroyed() {
        if (GameScreen.enemyTanks.size() == 0 && !GameScreen.isSpawning && !levelTransitioning) {
            if (GameScreen.getStage() < 20) {
                levelTransitioning = true;
                nextLevel();
            } else {
                GameScreen.getInstance().completeGame();
            }
        }
    }

    private static void nextLevel() {
        GameScreen.setStage(GameScreen.getStage() + 1);
        System.out.println("Advancing to level: " + GameScreen.getStage());

        GameScreen.getInstance().getTankSpawner().startSpawning();
        GameScreen.blocks.clear();
        BoardUtility.clearPowerUps();
        GameScreen.getInstance().initBlocks();

        GameScreen.getInstance().getPlayerTankRender().getPlayerTank().resetPosition();

        new Timer(500, e -> levelTransitioning = false).start();
    }
}
