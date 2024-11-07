package physics;

import SpriteClasses.Block;
import entities.BlockExplosion;
import entities.Bullet;
import entities.PowerUps.ShieldPowerUp;
import entities.Tank;
import entities.PowerUps.BombPowerUp;
import entities.PowerUps.ClockPowerUp;
import entities.PowerUps.PowerUp;
import entities.TankExplosion;
import environment.BlockType;
import render.GameScreen;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class CollisionHandling {

    public static void checkCollisionBulletsTank(ArrayList<Bullet> bullets, Tank playerTank) {
        Rectangle playerHitbox = playerTank.getHitbox();

        // Duyệt qua từng viên đạn trong danh sách bullets
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            // Kiểm tra xem đạn có va chạm với xe tăng người chơi không
            if (bullet.getHitbox().intersects(playerHitbox)) {
                // Nếu có va chạm, xóa viên đạn
                bullets.remove(i);
                // Giảm máu cho xe tăng người chơi
                playerTank.downHealth(3);
                System.out.println("Bullet hit the player tank! Player health: " + playerTank.getHealth());

                // Nếu máu của người chơi <= 0, xử lý việc tiêu diệt người chơi (game over)
                if (playerTank.getHealth() <= 0) {
                    System.out.println("Player tank destroyed!");
                }

                // Đảm bảo thoát khỏi vòng lặp đạn sau khi xử lý va chạm
                break;
            }
        }
    }


    // Check collision between bullets and enemy tanks (Tank)
    public static void checkCollisionBulletsTankAI(ArrayList<Bullet> bullets, ArrayList<Tank> enemyTanks) {
        for (int i = 0; i < enemyTanks.size(); i++) {
            Tank enemy = enemyTanks.get(i);
            Rectangle enemyHitbox = enemy.getHitbox();

            // Duyệt qua từng viên đạn trong danh sách bullets
            for (int j = 0; j < bullets.size(); j++) {
                Bullet bullet = bullets.get(j);
                // Kiểm tra xem đạn có va chạm với xe tăng đối phương không
                if (bullet.getHitbox().intersects(enemyHitbox)) {
                    // Nếu có va chạm, xóa viên đạn
                    bullets.remove(j);
                    // Giảm máu cho xe tăng đối phương
                    enemy.downHealth(3);
                    System.out.println("Bullet hit an enemy tank! Enemy health: " + enemy.getHealth());

                    // Nếu xe tăng đối phương bị tiêu diệt, xóa xe tăng đó khỏi danh sách
                    if (enemy.getHealth() <= 0) {
                        GameScreen.animations.add(new TankExplosion(enemyTanks.get(i).getX(), enemyTanks.get(i).getY(), 100, 1, false));
                        enemyTanks.remove(i); // Loại bỏ enemy khỏi danh sách
                        GameScreen.tankDestroyed+=1;
                    }
                    // Đảm bảo thoát khỏi vòng lặp đạn sau khi xử lý va chạm
                    break;
                }
            }
        }
    }


    // Check collision between moving tanks and blocks
    public static boolean checkMovingCollisions(Tank tank, ArrayList<Block> blocks) {
        Rectangle tankHitbox = tank.getHitbox();
        ArrayList<Block> blocksClone = new ArrayList<>(blocks);
        // Loop through the list by index instead of using an iterator
        for (Block block : blocksClone) {
            // Check if the tank collides with any block except trees
            if (tankHitbox.intersects(block.getHitbox()) && block.getType() != BlockType.TREE.getValue()) {
//                System.out.println("Collision detected with block!");
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
                GameScreen.animations.add(new BlockExplosion(blocks.get(blockIndex).getX(), blocks.get(blockIndex).getY(), 100, 0.5, false));
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



    public static void checkTankPowerUpCollision(Tank playerTank, ArrayList<PowerUp> powerUps, ArrayList<Tank> enemyTanks) {
        for (PowerUp powerUp : powerUps) {
            if (playerTank.getHitbox().intersects(powerUp.getHitbox()) && powerUp.isVisible()) {
                if (powerUp instanceof BombPowerUp) {
                    BoardUtility.activateBombPowerUp(enemyTanks);
                } else if (powerUp instanceof ShieldPowerUp) {
                    BoardUtility.activateShieldPowerUp(playerTank);
                } else if (powerUp instanceof ClockPowerUp) {
                    BoardUtility.activateClockPowerUp(enemyTanks);
                }
                powerUp.setVisible(false); // Ẩn PowerUp sau khi được nhặt
                break;
            }
        }
    }


//    public static void checkCollisionTankTankAI(Tank playerTank, ArrayList<Tank> enemyTanks) {
//        Rectangle playerHitbox = playerTank.getHitbox();
//
//        for (Tank enemy : enemyTanks) {
//            Rectangle enemyHitbox = enemy.getHitbox();
//
//            if (playerHitbox.intersects(enemyHitbox)) {
//                System.out.println("Collision detected between PlayerTank and an enemy tank!");
//
//                // Reduce health for both tanks on collision, or you can decide on specific logic
//                playerTank.setHealth(playerTank.getHealth() - 1);
//                enemy.setHealth(enemy.getHealth() - 1);
//
//                System.out.println("Player health: " + playerTank.getHealth());
//                System.out.println("Enemy health: " + enemy.getHealth());
//
//                // Handle player tank destruction if health is zero or below
//                if (playerTank.getHealth() <= 0) {
//                    System.out.println("Player tank destroyed!");
//                    // Optionally, handle player tank removal or game over logic
//                }
//
//                // Handle enemy tank destruction if health is zero or below
//                if (enemy.getHealth() <= 0) {
//                    System.out.println("Enemy tank destroyed!");
////                    enemy.setVisible(false);
////                    enemyTanks.remove(enemy); // Remove the enemy tank from the game
//                }
//
//                // Exit after handling collision with one enemy tank to avoid ConcurrentModificationException
//                break;
//            }
//        }
//    }


}
