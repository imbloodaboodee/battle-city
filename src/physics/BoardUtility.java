package physics;

import SpriteClasses.Block;
import constants.GameConstants;
import entities.PlayerTank;
import entities.PowerUps.*;
import entities.Tank;
import entities.TankExplosion;
import manager.TankSpawner;
import render.GameScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class BoardUtility {

    private static final int POWER_UP_SPAWN_INTERVAL = 10000; // 10 seconds
    private static ArrayList<PowerUp> powerUps = new ArrayList<>();
    private static Random random = new Random();
    private static final int SHOVEL_POWER_UP_DURATION = 10000; // 10 seconds

    public static void spawnRandomPowerUp(int x, int y, int percentageChance) {
        if (returnTrueAtPercentage(percentageChance)) {
            int randomIndex = random.nextInt(4); // Randomly pick a number between 0 and 3
            if (powerUps.size() > 0)
                powerUps.clear();
            switch (randomIndex) {
                case 0 -> powerUps.add(new BombPowerUp(x, y));
                case 1 -> powerUps.add(new ClockPowerUp(x, y));
                case 2 -> powerUps.add(new ShieldPowerUp(x, y));
                case 3 -> powerUps.add(new ShovelPowerUp(x, y));
                case 4 -> powerUps.add(new TankPowerUp(x, y));
                case 5 -> powerUps.add(new StarPowerUp(x, y));
            }
        }
    }

    public static void clearPowerUps() {
        powerUps.clear();
    }

    private static boolean returnTrueAtPercentage(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        return random.nextInt(100) < percentage;
    }

    public static void checkTankPowerUpCollision(Tank playerTank, CopyOnWriteArrayList<Tank> enemyTanks, CopyOnWriteArrayList<Block> blocks) {
        CollisionHandling.checkTankPowerUpCollision(playerTank, powerUps, enemyTanks, blocks);
    }


    public static int activateBombPowerUp(CopyOnWriteArrayList<Tank> enemyTanks) {
        int destroyedCount = 0;
        List<Tank> tanksToRemove = new ArrayList<>();

        for (Tank enemyTank : enemyTanks) {
            GameScreen.animations.add(new TankExplosion(enemyTank.getX(), enemyTank.getY(), 100, 1, false));
            BoardUtility.spawnRandomPowerUp(enemyTank.getX(), enemyTank.getY(), GameConstants.POWER_UP_SPAWN_CHANCE);

            tanksToRemove.add(enemyTank);  // Thêm enemyTank vào danh sách tạm thời
            destroyedCount++;  // Tăng số lượng enemy bị tiêu diệt
        }

        // Sau khi vòng lặp kết thúc, xóa tất cả enemyTanks từ danh sách tạm
        enemyTanks.removeAll(tanksToRemove);
        for (int i = 0; i < destroyedCount; i++) {
            TankSpawner.onEnemyTankDestroyed();
        }

        return destroyedCount;  // Trả về số lượng enemy bị tiêu diệt

    }

    public static void activateClockPowerUp(CopyOnWriteArrayList<Tank> enemyTanks) {
        for (Tank enemy : enemyTanks) {
            enemy.freeze(10000); // Freeze each enemy tank for 10 seconds
        }
    }

    public static void activateShieldPowerUp(Tank playerTank) {
        playerTank.activateShield(10000); // Bảo vệ trong 10 giây
    }

    public static void activateShovelPowerUp(CopyOnWriteArrayList<Block> blocks) {
        // Tạo instance của ShovelPowerUp
        ShovelPowerUp shovelPowerUp = new ShovelPowerUp(0, 0); // Tọa độ có thể không cần thiết nếu chỉ sử dụng logic kích hoạt
        // Kích hoạt powerup
        shovelPowerUp.activate(blocks); // Truyền danh sách các block vào để powerup kích hoạt
    }

    public static void activateTankPowerUp() {
        PlayerTank.lives++;
    }

    public static void activateStarPowerUp() {
        return;
    }


    public static ArrayList<PowerUp> getPowerUps() {
        return powerUps;
    }
}
