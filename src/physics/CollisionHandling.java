package physics;

import SpriteClasses.Block;
import entities.Bullet;
import entities.Tank;
import entities.PowerUps.BombPowerUp;
import entities.PowerUps.ClockPowerUp;
import entities.PowerUps.PowerUp;
import entities.Tank;
import environment.BlockType;
import render.GameScreen;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

public class CollisionHandling {

    // Check collision between bullets and the player tank
    public static void checkCollisionBulletsTank(ArrayList<Bullet> bullets, Tank playerTank) {
        Rectangle playerHitbox = playerTank.getHitbox();

        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            if (bullet.getHitbox().intersects(playerHitbox)) {
                bulletIterator.remove(); // Remove the bullet upon collision
                playerTank.setHealth(playerTank.getHealth() - 1); // Reduce player health
                System.out.println("Bullet hit the player tank! Player health: " + playerTank.getHealth());
                // Optionally, handle player death if health reaches zero
                if (playerTank.getHealth() <= 0) {
                    System.out.println("Player tank destroyed!");
                }
                break;
            }
        }
    }

    // Check collision between bullets and enemy tanks (Tank)
    public static void checkCollisionBulletsTankAI(ArrayList<Bullet> bullets, ArrayList<Tank> enemyTanks) {
        Iterator<Tank> enemyIterator = enemyTanks.iterator();

        while (enemyIterator.hasNext()) {
            Tank enemy = enemyIterator.next();
            Rectangle enemyHitbox = enemy.getHitbox();

            Iterator<Bullet> bulletIterator = bullets.iterator();

            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                System.out.println("b");

                if (bullet.getHitbox().intersects(enemyHitbox)) {
                    System.out.println("a");
                    bulletIterator.remove(); // Remove the bullet upon collision
                    enemy.setHealth(enemy.getHealth() - 1); // Reduce enemy health
                    System.out.println("Bullet hit an enemy tank! Enemy health: " + enemy.getHealth());

                    // If enemy health reaches zero, remove the enemy tank
                    if (enemy.getHealth() <= 0) {
                        enemyIterator.remove(); // Safely remove the enemy tank from the iterator
                        System.out.println("Enemy tank destroyed!");
                    }
                    break; // Exit bullet loop to avoid further modifications on this enemy tank
                }
            }
        }
    }

    // Check collision between moving tanks and blocks
    public static boolean checkMovingCollisions(Tank tank, ArrayList<Block> blocks) {
        Rectangle tankHitbox = tank.getHitbox();

        // Loop through the list by index instead of using an iterator
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);

            // Check if the tank collides with any block except trees
            if (tankHitbox.intersects(block.getHitbox()) && block.getType() != BlockType.TREE.getValue()) {
                System.out.println("Collision detected with block!");
                return true; // Collision detected
            }
        }
        return false; // No collision detected
    }


    // Check collision between bullets and blocks
    public static void checkCollisionBulletsBlocks(ArrayList<Bullet> bullets, ArrayList<Block> blocks) {
        for (int x = 0; x < bullets.size(); x++) {
            Bullet b = bullets.get(x);
            Rectangle2D.Double bulletHitbox = b.getHitbox();

            for (int i = 0; i < blocks.size(); i++) {
                Block block = blocks.get(i);
                Rectangle blockHitbox = block.getHitbox();
                BlockType blockType = BlockType.getTypeFromInt(block.getType()); // Convert int to BlockType

                if (bulletHitbox.intersects(blockHitbox)) {
                    System.out.println("Collision detected with block of type: " + blockType);
                    CollisionBulletsBlocksHelper(bullets, blocks, x, i, blockType);
                    break;  // Exit loop after handling collision
                }
            }
        }
    }

    private static void CollisionBulletsBlocksHelper(ArrayList<Bullet> bullets, ArrayList<Block> blocks, int bulletIndex, int blockIndex, BlockType blockType) {
        switch (blockType) {
            case RIVER:
            case TREE:
                // Bullet goes through tree; do nothing.
                System.out.println("Bullet passed through tree.");
                break;

            case BRICK:
                // Remove both the bullet and the brick block
                bullets.remove(bulletIndex);
                blocks.remove(blockIndex);
                System.out.println("Bullet and brick block removed after collision.");

                break;
            case EDGE:
            case STEEL:
                // Only remove the bullet; steel block remains
                bullets.remove(bulletIndex);
                System.out.println("Bullet removed after hitting steel block.");
                break;

            default:
                System.out.println("Unknown block type.");
        }
    }



    // Check collision between Tank and PowerUps
    public static void checkTankPowerUpCollision(Tank playerTank, ArrayList<PowerUp> powerUps, ArrayList<Tank> enemyTanks) {
        Iterator<PowerUp> powerUpIterator = powerUps.iterator();

        while (powerUpIterator.hasNext()) {
            PowerUp powerUp = powerUpIterator.next();

            if (playerTank.getHitbox().intersects(powerUp.getHitbox())) {
                if (powerUp instanceof BombPowerUp) {
                    BoardUtility.activateBombPowerUp(enemyTanks);
                } else if (powerUp instanceof ClockPowerUp) {
                    BoardUtility.activateClockPowerUp(enemyTanks);
                }
                powerUpIterator.remove(); // Remove PowerUp after collection
                break;
            }
        }
    }

    // Check collision between PlayerTank and enemy tanks (Tank)
    public static void checkCollisionTankTankAI(Tank playerTank, ArrayList<Tank> enemyTanks) {
        Rectangle playerHitbox = playerTank.getHitbox();

        for (Tank enemy : enemyTanks) {
            Rectangle enemyHitbox = enemy.getHitbox();

            if (playerHitbox.intersects(enemyHitbox)) {
                System.out.println("Collision detected between PlayerTank and an enemy tank!");

                // Reduce health for both tanks on collision, or you can decide on specific logic
                playerTank.setHealth(playerTank.getHealth() - 1);
                enemy.setHealth(enemy.getHealth() - 1);

                System.out.println("Player health: " + playerTank.getHealth());
                System.out.println("Enemy health: " + enemy.getHealth());

                // Handle player tank destruction if health is zero or below
                if (playerTank.getHealth() <= 0) {
                    System.out.println("Player tank destroyed!");
                    // Optionally, handle player tank removal or game over logic
                }

                // Handle enemy tank destruction if health is zero or below
                if (enemy.getHealth() <= 0) {
                    System.out.println("Enemy tank destroyed!");
//                    enemy.setVisible(false);
//                    enemyTanks.remove(enemy); // Remove the enemy tank from the game
                }

                // Exit after handling collision with one enemy tank to avoid ConcurrentModificationException
                break;
            }
        }
    }
}
