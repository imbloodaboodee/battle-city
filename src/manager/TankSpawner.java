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

    public TankSpawner(ArrayList<Tank> enemyTanks,int stage) {
        this.enemyTanks = enemyTanks;
        this.stage = stage;
    }

    public void spawnEnemyTanks() {
        for (int i = 0; i < stage; i++) {
            spawnBasicTank();
        }
    }

    private void spawnBasicTank() {
        DumbTank basicTank = new DumbTank(400, 20, 3, 1, BulletType.NORMAL, new ImageIcon("./src/assets/image/tank_basic.png"));
        enemyTanks.add(basicTank);
    }

    private void spawnFastTank() {
        DumbTank fastTank = new DumbTank(416, 16, 3, 20, BulletType.NORMAL, new ImageIcon("./src/assets/image/tank_fast.png"));
        enemyTanks.add(fastTank);
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
}
