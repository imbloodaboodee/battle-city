package physics;

import SpriteClasses.Base;
import SpriteClasses.Block;
import entities.PowerUps.*;
import entities.Tank;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class BoardUtility {

    private static final int POWER_UP_SPAWN_INTERVAL = 10000; // 10 seconds
    private static ArrayList<PowerUp> powerUps = new ArrayList<>();
    private static Random random = new Random();
    private static final int SHOVEL_POWER_UP_DURATION = 10000; // 10 seconds

    public static void spawnRandomPowerUp() {
        int bombX = 100;
        int bombY = 200;
        int clockX = 300;
        int clockY = 400;
        int shieldX = 100;
        int shieldY = 100;
        int shovelX = 220;
        int shovelY = 200;

        powerUps.add(new BombPowerUp(bombX, bombY));
        powerUps.add(new ClockPowerUp(clockX, clockY));
        powerUps.add(new ShieldPowerUp(shieldX, shieldY));
        powerUps.add(new ShovelPowerUp(shovelX, shovelY));
    }

    public static void checkTankPowerUpCollision(Tank playerTank, ArrayList<Tank> enemyTanks, ArrayList<Block> blocks) {
        CollisionHandling.checkTankPowerUpCollision(playerTank, powerUps, enemyTanks, blocks);
    }


    public static void activateBombPowerUp(ArrayList<Tank> enemyTanks) {
        enemyTanks.clear();
        System.out.println("Bomb PowerUp activated: All enemy tanks destroyed.");
    }

    public static void activateClockPowerUp(ArrayList<Tank> enemyTanks) {
        System.out.println("Clock PowerUp activated: All enemy tanks frozen for 10 seconds.");
        for (Tank enemy : enemyTanks) {
            enemy.freeze(10000); // Freeze each enemy tank for 10 seconds
        }
    }

    public static void activateShieldPowerUp(Tank playerTank) {
        playerTank.activateShield(10000); // Bảo vệ trong 10 giây
    }

    public static void activateShovelPowerUp(ArrayList<Block> blocks) {
        // Tạo instance của ShovelPowerUp
        ShovelPowerUp shovelPowerUp = new ShovelPowerUp(0, 0); // Tọa độ có thể không cần thiết nếu chỉ sử dụng logic kích hoạt
        // Kích hoạt powerup
        shovelPowerUp.activate(blocks); // Truyền danh sách các block vào để powerup kích hoạt
    }


    public static ArrayList<PowerUp> getPowerUps() {
        return powerUps;
    }
}
