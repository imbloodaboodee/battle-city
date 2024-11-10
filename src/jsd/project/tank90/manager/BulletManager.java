package jsd.project.tank90.manager;

import jsd.project.tank90.constants.GameConstants;
import jsd.project.tank90.entities.Bullet;
import jsd.project.tank90.render.GameScreen;

import java.util.concurrent.CopyOnWriteArrayList;

import static jsd.project.tank90.physics.CollisionHandling.checkCollisionBulletsBlocks;

public class BulletManager {
    private CopyOnWriteArrayList<Bullet> bullets;

    public BulletManager(CopyOnWriteArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }

    public void updateBullets() {
        if (bullets.size() > 0) {
            // Update each bullet's position and remove it if it's out of bounds
            bullets.removeIf(bullet -> {
                bullet.updatePosition();
                return bullet.getX() < 0 || bullet.getX() > GameConstants.FRAME_WIDTH ||
                        bullet.getY() < 0 || bullet.getY() > GameConstants.FRAME_HEIGHT;
            });

            // Check for collisions after updating positions
            checkCollisionBulletsBlocks(bullets, GameScreen.blocks);
        }
    }

    public void clearBullets(){
        bullets.clear();
    }
}
