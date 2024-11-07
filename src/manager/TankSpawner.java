package manager;

import entities.*;
import render.GameScreen;

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

    private int maxBasicTanks;
    private int maxFastTanks;
    private int maxPowerTanks;
    private int maxArmorTanks;

    private Timer spawnTimer;
    private static final int SPAWN_DELAY_MS = 3000; // 3 seconds

    public TankSpawner(ArrayList<Tank> enemyTanks, int stage) {
        this.enemyTanks = enemyTanks;
        setStage(stage);
    }

    public void startSpawning() {
        if (spawnTimer != null) {
            spawnTimer.stop();
        }

        GameScreen.isSpawning = true;
        resetSpawner();
        updateTankLimits();
        spawnTimer = new Timer(10000, e -> spawnCycle());
        spawnCycle();
        spawnTimer.start();
    }

    private void spawnCycle() {
        if (basicTankCount < maxBasicTanks) {
            final Point spawnpoint = findSpawnPoint();  // Declare spawnpoint as final
            scheduleTankSpawn(() -> spawnBasicTank(spawnpoint), SPAWN_DELAY_MS, spawnpoint);
            basicTankCount++;
        }
        if (fastTankCount < maxFastTanks) {
            final Point spawnpoint = findSpawnPoint();  // Declare spawnpoint as final
            scheduleTankSpawn(() -> spawnFastTank(spawnpoint), SPAWN_DELAY_MS, spawnpoint);
            fastTankCount++;
        }
        if (powerTankCount < maxPowerTanks) {
            final Point spawnpoint = findSpawnPoint();  // Declare spawnpoint as final
            scheduleTankSpawn(() -> spawnPowerTank(spawnpoint), SPAWN_DELAY_MS, spawnpoint);
            powerTankCount++;
        }
        if (armorTankCount < maxArmorTanks) {
            final Point spawnpoint = findSpawnPoint();  // Declare spawnpoint as final
            scheduleTankSpawn(() -> spawnArmorTank(spawnpoint), SPAWN_DELAY_MS, spawnpoint);
            armorTankCount++;
        }

        if (basicTankCount >= maxBasicTanks &&
                fastTankCount >= maxFastTanks &&
                powerTankCount >= maxPowerTanks &&
                armorTankCount >= maxArmorTanks) {
            GameScreen.isSpawning = false;
            spawnTimer.stop();
        }
    }

    private void resetSpawner() {
        basicTankCount = 0;
        fastTankCount = 0;
        powerTankCount = 0;
        armorTankCount = 0;
        setStage(GameScreen.stage);
    }

    private void updateTankLimits() {
        this.maxBasicTanks = Math.max(2 * stage - 5, 2);
        this.maxFastTanks = Math.max(2 * stage - 5, 2);
        this.maxPowerTanks = stage / 2;
        this.maxArmorTanks = stage / 3;
    }

    private void scheduleTankSpawn(Runnable spawnMethod, int delay, Point spawnPoint) {
        GameScreen.animations.add(new SpawnAnimation(spawnPoint.x, spawnPoint.y, 100, 1, true));

        // Delay the tank spawn by `delay` milliseconds after the animation starts
        new Timer(delay, e -> {
            spawnMethod.run();
            ((Timer) e.getSource()).stop();  // Stop the timer after it fires once
        }).start();
    }

    private void spawnBasicTank(Point spawnPoint) {
        DumbTank basicTank = new DumbTank(spawnPoint.x, spawnPoint.y, 3, 1, BulletType.NORMAL, new ImageIcon("./src/assets/image/tank_basic.png"));
        enemyTanks.add(basicTank);
    }

    private void spawnFastTank(Point spawnPoint) {
        DumbTank fastTank = new DumbTank(spawnPoint.x, spawnPoint.y, 3, 3, BulletType.NORMAL, new ImageIcon("./src/assets/image/tank_fast.png"));
        enemyTanks.add(fastTank);
    }

    private void spawnPowerTank(Point spawnPoint) {
        SmartTank powerTank = new SmartTank(spawnPoint.x, spawnPoint.y, 3, 1, BulletType.NORMAL, new ImageIcon("./src/assets/image/tank_power_base.png"), new ImageIcon("./src/assets/image/tank_power_cannon.png"));
        enemyTanks.add(powerTank);
    }

    private void spawnArmorTank(Point spawnPoint) {
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
        updateTankLimits();
    }
}
