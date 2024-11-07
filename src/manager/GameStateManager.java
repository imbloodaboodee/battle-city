package manager;

import render.GameScreen;
import physics.BoardUtility;


public class GameStateManager {
    private static boolean levelTransitioning = false;  // New flag

    public static void checkTankDestroyed() {
        if (GameScreen.enemyTanks.size() == 0 && !GameScreen.isSpawning && !levelTransitioning) {
            nextLevel();
        }
    }

    private static void nextLevel() {
        // Set flag to indicate level is transitioning
        levelTransitioning = true;

        // Increment level
        GameScreen.setStage(GameScreen.getStage() + 1);
        System.out.println("Advancing to level: " + GameScreen.getStage());

        // Clear enemy tanks, blocks, and power-ups for fresh level setup
        GameScreen.getInstance().getTankSpawner().startSpawning();
        GameScreen.blocks.clear();

        // Reload the map for the new stage
        GameScreen.getInstance().initBlocks();

        // Reset player tank position
        GameScreen.getInstance().getPlayerTankRender().getPlayerTank().resetPosition();

        // Add a small delay to reset the flag after spawning starts
        new javax.swing.Timer(500, e -> levelTransitioning = false).start();  // Adjust delay as needed
    }
}
