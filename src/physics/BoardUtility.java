package physics;

import SpriteClasses.Base;
import SpriteClasses.Block;
import constants.GameConstants;
import entities.PowerUps.*;
import entities.Tank;
import entities.TankExplosion;
import render.GameScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
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


    public static void activateBombPowerUp(CopyOnWriteArrayList<Tank> enemyTanks) {
        for (Tank enemyTank : enemyTanks) {
            GameScreen.animations.add(new TankExplosion(enemyTank.getX(), enemyTank.getY(), 100, 1, false));
            BoardUtility.spawnRandomPowerUp(enemyTank.getX(), enemyTank.getY(), GameConstants.POWER_UP_SPAWN_CHANCE);
            enemyTanks.remove(enemyTank); // Loại bỏ enemy khỏi danh sách
        }
        System.out.println("Bomb PowerUp activated: All enemy tanks destroyed.");
    }

    public static void activateClockPowerUp(CopyOnWriteArrayList<Tank> enemyTanks) {
        System.out.println("Clock PowerUp activated: All enemy tanks frozen for 10 seconds.");
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


    public static ArrayList<PowerUp> getPowerUps() {
        return powerUps;
    }
}
