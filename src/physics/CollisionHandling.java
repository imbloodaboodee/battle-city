package physics;

import SpriteClasses.Block;
import entities.Bullet;
import entities.Tank;
import entities.PowerUps.BombPowerUp;
import entities.PowerUps.ClockPowerUp;
import entities.PowerUps.PowerUp;
import entities.SmartTank;
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

    // Check collision between bullets and enemy tanks (SmartTank)
    public static void checkCollisionBulletsTankAI(ArrayList<Bullet> bullets, ArrayList<SmartTank> enemyTanks) {
        for (SmartTank enemy : enemyTanks) {
            Rectangle enemyHitbox = enemy.getTank().getHitbox();

            Iterator<Bullet> bulletIterator = bullets.iterator();
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();

                if (bullet.getHitbox().intersects(enemyHitbox)) {
                    bulletIterator.remove(); // Remove the bullet upon collision
                    enemy.getTank().setHealth(enemy.getTank().getHealth() - 1); // Reduce enemy health
                    System.out.println("Bullet hit an enemy tank! Enemy health: " + enemy.getTank().getHealth());

                    // If enemy health reaches zero, remove the enemy tank
                    if (enemy.getTank().getHealth() <= 0) {
                        enemy.setVisible(false);
                        enemyTanks.remove(enemy);
                        System.out.println("Enemy tank destroyed!");
                    }
                    break;
                }
            }
        }
    }

    // Check collision between moving tanks and blocks
    public static boolean checkMovingCollisions(Tank tank, ArrayList<Block> blocks) {
        Rectangle tankHitbox = tank.getHitbox();

        for (Block block : blocks) {
            if (tankHitbox.intersects(block.getHitbox()) && block.getType() != BlockType.TREE.getValue()) {
                System.out.println("Collision detected with block!");
                return true;
            }
        }
        return false;
    }

    // Check collision between bullets and blocks
    public static void checkCollisionBulletsBlocks(ArrayList<Bullet> bullets, ArrayList<Block> blocks) {
        Iterator<Bullet> bulletIterator = bullets.iterator();

        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Rectangle2D.Double bulletHitbox = bullet.getHitbox();

            for (int i = 0; i < blocks.size(); i++) {
                Block block = blocks.get(i);
                Rectangle blockHitbox = block.getHitbox();
                BlockType blockType = BlockType.getTypeFromInt(block.getType());

                if (bulletHitbox.intersects(blockHitbox)) {
                    System.out.println("Collision detected with block of type: " + blockType);
                    handleBulletBlockCollision(bulletIterator, blocks, i, blockType);
                    break; // Exit loop after handling collision
                }
            }
        }
    }

    // Helper method for handling bullet and block collision
    private static void handleBulletBlockCollision(Iterator<Bullet> bulletIterator, ArrayList<Block> blocks, int blockIndex, BlockType blockType) {
        switch (blockType) {
            case RIVER:
            case TREE:
                // Bullet goes through tree or river; do nothing
                System.out.println("Bullet passed through tree or river.");
                break;

            case BRICK:
                // Remove both the bullet and the brick block
                bulletIterator.remove();
                blocks.remove(blockIndex);
                System.out.println("Bullet and brick block removed after collision.");
                break;

            case EDGE:
            case STEEL:
                // Only remove the bullet; steel and edge blocks remain
                bulletIterator.remove();
                System.out.println("Bullet removed after hitting steel or edge block.");
                break;

            default:
                System.out.println("Unknown block type.");
        }
    }

    // Check collision between Tank and PowerUps
    public static void checkTankPowerUpCollision(Tank playerTank, ArrayList<PowerUp> powerUps, ArrayList<SmartTank> enemyTanks) {
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

    // Check collision between PlayerTank and enemy tanks (SmartTank)
    public static void checkCollisionTankTankAI(Tank playerTank, ArrayList<SmartTank> enemyTanks) {
        Rectangle playerHitbox = playerTank.getHitbox();

        for (SmartTank enemy : enemyTanks) {
            Rectangle enemyHitbox = enemy.getTank().getHitbox();

            if (playerHitbox.intersects(enemyHitbox)) {
                System.out.println("Collision detected between PlayerTank and an enemy tank!");

                // Reduce health for both tanks on collision, or you can decide on specific logic
                playerTank.setHealth(playerTank.getHealth() - 1);
                enemy.getTank().setHealth(enemy.getTank().getHealth() - 1);

                System.out.println("Player health: " + playerTank.getHealth());
                System.out.println("Enemy health: " + enemy.getTank().getHealth());

                // Handle player tank destruction if health is zero or below
                if (playerTank.getHealth() <= 0) {
                    System.out.println("Player tank destroyed!");
                    // Optionally, handle player tank removal or game over logic
                }

                // Handle enemy tank destruction if health is zero or below
                if (enemy.getTank().getHealth() <= 0) {
                    System.out.println("Enemy tank destroyed!");
                    enemy.setVisible(false);
                    enemyTanks.remove(enemy); // Remove the enemy tank from the game
                }

                // Exit after handling collision with one enemy tank to avoid ConcurrentModificationException
                break;
            }
        }
    }
}
