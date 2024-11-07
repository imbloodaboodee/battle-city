package manager;

import entities.BulletType;
import entities.DumbTank;
import entities.SmartTank;
import entities.Tank;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class TankSpawner {
    private ArrayList<Tank> enemyTanks;
    private int stage;
    private int basicTankCount;
    private int fastTankCount;
    private int powerTankCount;
    private int armorTankCount;

    private final int maxBasicTanks;
    private final int maxFastTanks;
    private final int maxPowerTanks;
    private final int maxArmorTanks;

    private Timer spawnTimer;

    public TankSpawner(ArrayList<Tank> enemyTanks, int stage) {
        this.enemyTanks = enemyTanks;
        this.stage = stage;

        // Set tank limits based on stage
        this.maxBasicTanks = Math.max(2 * stage - 5, 2);
        this.maxFastTanks = Math.max(2 * stage - 5, 2);
        this.maxPowerTanks = stage / 2;
        this.maxArmorTanks = stage / 3;

        basicTankCount = 0;
        fastTankCount = 0;
        powerTankCount = 0;
        armorTankCount = 0;
    }

    public void startSpawning() {
        // Set up a timer to spawn tanks in cycles, one of each type per second
        spawnCycle();
        spawnTimer = new Timer(10000, e -> spawnCycle());
        spawnTimer.start();
    }

    private void spawnCycle() {
        // Spawn one basic tank if limit not reached
        if (basicTankCount < maxBasicTanks) {
            spawnBasicTank();
            basicTankCount++;
        }

        // Spawn one fast tank if limit not reached
        if (fastTankCount < maxFastTanks) {
            spawnFastTank();
            fastTankCount++;
        }

        // Spawn one power tank if limit not reached
        if (powerTankCount < maxPowerTanks) {
            spawnPowerTank();
            powerTankCount++;
        }

        // Spawn one armor tank if limit not reached
        if (armorTankCount < maxArmorTanks) {
            spawnArmorTank();
            armorTankCount++;
        }

        // Stop the timer if all tanks of each type have been spawned
        if (basicTankCount >= maxBasicTanks &&
                fastTankCount >= maxFastTanks &&
                powerTankCount >= maxPowerTanks &&
                armorTankCount >= maxArmorTanks) {
            spawnTimer.stop();
        }
    }

    private void spawnBasicTank() {
        Point spawnPoint = findSpawnPoint();
        DumbTank basicTank = new DumbTank(spawnPoint.x, spawnPoint.y, 3, 1, BulletType.NORMAL, new ImageIcon("./src/assets/image/tank_basic.png"));
        enemyTanks.add(basicTank);
    }

    private void spawnFastTank() {
        Point spawnPoint = findSpawnPoint();
        DumbTank fastTank = new DumbTank(spawnPoint.x, spawnPoint.y, 3, 3, BulletType.NORMAL, new ImageIcon("./src/assets/image/tank_fast.png"));
        enemyTanks.add(fastTank);
    }

    private void spawnPowerTank() {
        Point spawnPoint = findSpawnPoint();
        SmartTank powerTank = new SmartTank(spawnPoint.x, spawnPoint.y, 3, 1, BulletType.NORMAL, new ImageIcon("./src/assets/image/tank_power_base.png"), new ImageIcon("./src/assets/image/tank_power_cannon.png"));
        enemyTanks.add(powerTank);
    }

    private void spawnArmorTank() {
        Point spawnPoint = findSpawnPoint();
        SmartTank armorTank = new SmartTank(spawnPoint.x, spawnPoint.y, 3, 1, BulletType.NORMAL, new ImageIcon("./src/assets/image/tank_armor_base.png"), new ImageIcon("./src/assets/image/tank_armor_cannon.png"));
        enemyTanks.add(armorTank);
    }

    private Point findSpawnPoint() {
        Random rnd = new Random();
        Point[] arr = new Point[]{new Point(416, 16), new Point(32, 16), new Point(224, 16)};
        return arr[rnd.nextInt(arr.length)];
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
}
