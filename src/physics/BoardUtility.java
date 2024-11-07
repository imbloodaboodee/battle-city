package physics;

import entities.PowerUps.BombPowerUp;
import entities.PowerUps.ClockPowerUp;
import entities.PowerUps.PowerUp;
import entities.PowerUps.ShieldPowerUp;
import entities.SmartTank;
import entities.Tank;


import java.util.ArrayList;
import java.util.Random;

public class BoardUtility {

    private static final int POWER_UP_SPAWN_INTERVAL = 10000; // 10 seconds
    private static ArrayList<PowerUp> powerUps = new ArrayList<>();
    private static Random random = new Random();

    public static void spawnRandomPowerUp() {
//        int x = random.nextInt(GameConstants.FRAME_WIDTH);
//        int y = random.nextInt(GameConstants.FRAME_HEIGHT);
//
//        PowerUp powerUp = random.nextBoolean() ? new BombPowerUp(x, y) : new ClockPowerUp(x, y);
//        powerUps.add(powerUp);
        int bombX = 100;
        int bombY = 200;
        int clockX = 300;
        int clockY = 400;
        int shieldX = 100;
        int shieldY = 100;

        powerUps.add(new BombPowerUp(bombX, bombY));
        powerUps.add(new ClockPowerUp(clockX, clockY));
        powerUps.add(new ShieldPowerUp(shieldX, shieldY));
    }

    // Call checkTankPowerUpCollision from CollisionHandling
    public static void checkTankPowerUpCollision(Tank playerTank, ArrayList<Tank> enemyTanks) {
        CollisionHandling.checkTankPowerUpCollision(playerTank, powerUps, enemyTanks);
    }

    public static void activateBombPowerUp(ArrayList<Tank> enemyTanks) {
        enemyTanks.clear(); // Xoá tất cả enemy tanks khỏi danh sách
        System.out.println("Bomb PowerUp activated: All enemy tanks destroyed.");
    }

    public static void activateClockPowerUp(ArrayList<Tank> enemyTanks) {
        System.out.println("Clock PowerUp activated: All enemy tanks frozen for 3 seconds.");
        for (Tank enemy : enemyTanks) {
            enemy.freeze(10000); // Freeze each enemy tank for 10 seconds
        }
    }

    public static void activateShieldPowerUp(Tank playerTank) {
        playerTank.activateShield(10000); // Bảo vệ trong 10 giây
    }

    public static ArrayList<PowerUp> getPowerUps() {
        return powerUps;
    }
}
