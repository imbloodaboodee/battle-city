package jsd.project.tank90.physics;

import jsd.project.tank90.sprite.Block;
import jsd.project.tank90.constants.GameConstants;
import jsd.project.tank90.entities.*;
import jsd.project.tank90.sprite.PowerUps.*;
import jsd.project.tank90.manager.TankSpawner;
import jsd.project.tank90.render.GameScreen;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class BoardUtility {

    private static ArrayList<PowerUp> powerUps = new ArrayList<>();
    private static Random random = new Random();
    public static Timer powerUpExpireTimer = new Timer(7000, e -> {clearPowerUps();});
    private static int starLevel = 1;

    public static void spawnRandomPowerUp(int x, int y, int percentageChance) {
        powerUpExpireTimer.stop();
        if (returnTrueAtPercentage(percentageChance)) {
            int randomIndex = random.nextInt(6);
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
            powerUpExpireTimer.start();
            SoundUtility.powerupAppear();
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
            CollisionHandling.incrementNum(enemyTank);
            tanksToRemove.add(enemyTank);
            destroyedCount++;
        }

        enemyTanks.removeAll(tanksToRemove);
        for (int i = 0; i < destroyedCount; i++) {
            TankSpawner.onEnemyTankDestroyed();
        }

        return destroyedCount;

    }

    public static void activateClockPowerUp(CopyOnWriteArrayList<Tank> enemyTanks) {
        for (Tank enemy : enemyTanks) {
            enemy.freeze(10000);
        }
    }

    public static void activateShieldPowerUp(Tank playerTank) {
        playerTank.activateShield(10000);
    }

    public static void activateShovelPowerUp(CopyOnWriteArrayList<Block> blocks) {
        ShovelPowerUp shovelPowerUp = new ShovelPowerUp(0, 0);
        shovelPowerUp.activate(blocks);
    }

    public static void activateTankPowerUp() {
        PlayerTank.lives++;
    }

    public static void increaseStarLevel() {
        if (starLevel < 4) {
            starLevel++;
        }
    }

    public static void resetPowerLevel() {
        starLevel = 1;
    }

    public static void activateStarPowerUp() {
        increaseStarLevel();
        BulletType currentBulletType = GameScreen.getInstance().ptRenderer.getPlayerTank().getDefaultBullet().getBulletType();
        BulletType upgradedBulletType = currentBulletType;

        switch (currentBulletType) {
            case STANDARD:
                upgradedBulletType = switch (starLevel) {
                    case 1 -> BulletType.STANDARD;
                    case 2 -> BulletType.STANDARD_TIER_2;
                    case 3 -> BulletType.STANDARD_TIER_3;
                    case 4 -> BulletType.STANDARD_TIER_4;
                    default -> BulletType.STANDARD;
                };
                break;
            case EXPLOSIVE:
                upgradedBulletType = switch (starLevel) {
                    case 1 -> BulletType.EXPLOSIVE;
                    case 2 -> BulletType.EXPLOSIVE_TIER_2;
                    case 3 -> BulletType.EXPLOSIVE_TIER_3;
                    case 4 -> BulletType.EXPLOSIVE_TIER_4;
                    default -> BulletType.EXPLOSIVE;
                };
                break;
            case RAPID:
                upgradedBulletType = switch (starLevel) {
                    case 1 -> BulletType.RAPID;
                    case 2 -> BulletType.RAPID_TIER_2;
                    case 3 -> BulletType.RAPID_TIER_3;
                    case 4 -> BulletType.RAPID_TIER_4;
                    default -> BulletType.RAPID;
                };
                break;
            default:
                break;
        }

        GameScreen.getInstance().ptRenderer.getPlayerTank().setDefaultBullet(new Bullet(upgradedBulletType));
    }

    public static int getStarLevel() {
        return starLevel;
    }

    public static ArrayList<PowerUp> getPowerUps() {
        return powerUps;
    }
}
