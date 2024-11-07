package manager;

import entities.BulletType;
import entities.DumbTank;
import entities.SmartTank;
import entities.Tank;
import render.DumbTankRender;
import render.GameScreen;
import render.SmartTankRender;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TankSpawner {
    private ArrayList<Tank> enemyTanks;
    private int stage;

    public TankSpawner(ArrayList<Tank> enemyTanks, int stage) {
        this.enemyTanks = enemyTanks;
        this.stage = stage;
    }

    public void spawnEnemyTanks() {
//        for (int i = 0; i < Math.max(2 * stage - 5, 2); i++) {
//            spawnBasicTank();
//        }
//        for (int i = 0; i < Math.max(2 * stage - 5, 2); i++) {
//            spawnFastTank();
//        }
//        for (int i = 0; i < stage / 2; i++) {
//            spawnPowerTank();
//        }
        for (int i = 0; i < stage / 3; i++) {
            spawnArmorTank();
        }
    }

    private void spawnBasicTank() {
        DumbTank basicTank = new DumbTank(416, 16, 3, 1, BulletType.NORMAL, new ImageIcon("./src/assets/image/tank_basic.png"));
        enemyTanks.add(basicTank);
    }

    private void spawnFastTank() {
        DumbTank fastTank = new DumbTank(416, 16, 3, 5, BulletType.NORMAL, new ImageIcon("./src/assets/image/tank_fast.png"));
        enemyTanks.add(fastTank);
    }

    private void spawnPowerTank() {
        SmartTank powerTank = new SmartTank(416, 16, 3, 1, BulletType.NORMAL, new ImageIcon("./src/assets/image/tank_power_base.png"), new ImageIcon("./src/assets/image/tank_power_cannon.png"));
        enemyTanks.add(powerTank);
    }

    private void spawnArmorTank() {
        SmartTank armorTank = new SmartTank(400, 20, 3, 1, BulletType.NORMAL, new ImageIcon("./src/assets/image/tank_armor_base.png"), new ImageIcon("./src/assets/image/tank_armor_cannon.png"));
        enemyTanks.add(armorTank);

    }

    public void setStage(int stage) {
        this.stage = stage;
    }
}
