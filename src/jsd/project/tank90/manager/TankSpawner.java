package jsd.project.tank90.manager;

import jsd.project.tank90.entities.*;
import jsd.project.tank90.render.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class TankSpawner {
    private CopyOnWriteArrayList<Tank> enemyTanks;
    private int stage;
    private int basicTankCount;
    private int fastTankCount;
    private int powerTankCount;
    private int armorTankCount;

    private int maxBasicTanks;
    private int maxFastTanks;
    private int maxPowerTanks;
    private int maxArmorTanks;
    private static int totalEnemyTanks;

    private Timer spawnTimer;

    public TankSpawner(CopyOnWriteArrayList<Tank> enemyTanks, int stage) {
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
            spawnBasicTank();
            basicTankCount++;
        }
        if (fastTankCount < maxFastTanks) {
            spawnFastTank();
            fastTankCount++;
        }
        if (powerTankCount < maxPowerTanks) {
            spawnPowerTank();
            powerTankCount++;
        }
        if (armorTankCount < maxArmorTanks) {
            spawnArmorTank();
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

    private void resetSpawner(){
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
        this.totalEnemyTanks = maxBasicTanks + maxFastTanks + maxPowerTanks + maxArmorTanks;
    }

    private void spawnBasicTank() {
        Point spawnPoint = findSpawnPoint();
        DumbTank basicTank = new DumbTank(spawnPoint.x, spawnPoint.y, 3, 1, BulletType.STANDARD, new ImageIcon("./src/jsd/project/tank90/assets/image/tank_basic.png"));
        GameScreen.animations.add(new SpawnAnimation(spawnPoint.x,spawnPoint.y, 200, 1,false));
        basicTank.setTankType(TankType.BASIC);
        enemyTanks.add(basicTank);
    }

    private void spawnFastTank() {
        Point spawnPoint = findSpawnPoint();
        DumbTank fastTank = new DumbTank(spawnPoint.x, spawnPoint.y, 3, 3, BulletType.STANDARD, new ImageIcon("./src/jsd/project/tank90/assets/image/tank_fast.png"));
        GameScreen.animations.add(new SpawnAnimation(spawnPoint.x,spawnPoint.y, 200, 1,false));
        fastTank.setTankType(TankType.FAST);
        enemyTanks.add(fastTank);
    }

    private void spawnPowerTank() {
        Point spawnPoint = findSpawnPoint();
        SmartTank powerTank = new SmartTank(spawnPoint.x, spawnPoint.y, 3, 1, BulletType.STANDARD, new ImageIcon("./src/jsd/project/tank90/assets/image/tank_power_base.png"), new ImageIcon("./src/jsd/project/tank90/assets/image/tank_power_cannon.png"));
        GameScreen.animations.add(new SpawnAnimation(spawnPoint.x,spawnPoint.y, 200, 1,false));
        powerTank.setTankType(TankType.POWER);
        enemyTanks.add(powerTank);
    }

    private void spawnArmorTank() {
        Point spawnPoint = findSpawnPoint();
        SmartTank armorTank = new SmartTank(spawnPoint.x, spawnPoint.y, 3, 1, BulletType.STANDARD, new ImageIcon("./src/jsd/project/tank90/assets/image/tank_armor_base.png"), new ImageIcon("./src/jsd/project/tank90/assets/image/tank_armor_cannon.png"));
        GameScreen.animations.add(new SpawnAnimation(spawnPoint.x,spawnPoint.y, 200, 1,false));
        armorTank.setTankType(TankType.ARMOR);
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

    public static int getTotalEnemyTanks() {
        return totalEnemyTanks;
    }

    public static void onEnemyTankDestroyed() {
        totalEnemyTanks--;
        if (totalEnemyTanks < 0) {
            totalEnemyTanks = 0;
        }
        GameScreen.getInstance().repaint();
    }
}

