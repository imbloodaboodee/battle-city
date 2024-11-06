package manager;

import constants.GameConstants;
import entities.Bullet;
import render.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static physics.CollisionHandling.checkCollisionBulletsBlocks;

public class BulletManager {
    private ArrayList<Bullet> bullets;

    public BulletManager(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }

    public void updateBullets() {
        if (bullets.size() > 0) {
            Iterator<Bullet> bulletIterator = bullets.iterator();

            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                bullet.updatePosition();

                if (bullet.getX() < 0 || bullet.getX() > GameConstants.FRAME_WIDTH || bullet.getY() < 0 || bullet.getY() > GameConstants.FRAME_HEIGHT) {
                    bulletIterator.remove();
                }
            }
            checkCollisionBulletsBlocks(bullets, GameScreen.blocks);
        }
    }
}
