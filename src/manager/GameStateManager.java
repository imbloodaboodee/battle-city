package manager;

import render.GameScreen;
import physics.BoardUtility;

import javax.swing.*;

public class GameStateManager {
    private static boolean levelTransitioning = false;  // New flag
    public static void checkTankDestroyed() {
        if (GameScreen.enemyTanks.size() == 0 && !GameScreen.isSpawning && !levelTransitioning) {
            levelTransitioning = true; // Set flag to prevent re-triggering
                nextLevel();
        }
    }

    private static void nextLevel() {
        // Increment level
        GameScreen.setStage(GameScreen.getStage() + 1);
        System.out.println("Advancing to level: " + GameScreen.getStage());

        // Clear enemy tanks, blocks, and power-ups for fresh level setup
        GameScreen.getInstance().getTankSpawner().startSpawning();
        GameScreen.blocks.clear();
        BoardUtility.clearPowerUps();
        // Reload the map for the new stage
        GameScreen.getInstance().initBlocks();

        // Reset player tank position
        GameScreen.getInstance().getPlayerTankRender().getPlayerTank().resetPosition();

        // Add a small delay to reset the flag after spawning starts
        new Timer(500, e -> levelTransitioning = false).start();  // Adjust delay as needed
    }
}
