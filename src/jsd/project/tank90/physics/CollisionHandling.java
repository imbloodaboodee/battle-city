package jsd.project.tank90.physics;

import jsd.project.tank90.SpriteClasses.Block;
import jsd.project.tank90.constants.GameConstants;
import jsd.project.tank90.entities.*;
import jsd.project.tank90.SpriteClasses.PowerUps.*;
import jsd.project.tank90.environment.BlockType;
import jsd.project.tank90.manager.TankSpawner;
import jsd.project.tank90.render.GameScreen;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollisionHandling {

    private static int[] enemyTankNum = {0, 0, 0, 0};

    public static void resetScore() {
        enemyTankNum = new int[]{0, 0, 0, 0};
    }

    public static void checkCollisionBulletsTank(CopyOnWriteArrayList<Bullet> bullets, Tank playerTank) {
        Rectangle playerHitbox = playerTank.getHitbox();

        // Duyệt qua từng viên đạn trong danh sách bullets
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            // Kiểm tra xem đạn có va chạm với xe tăng người chơi không
            if (bullet.getHitbox().intersects(playerHitbox)) {
                // Nếu có va chạm, xóa viên đạn
                bullets.remove(i);
                // Giảm máu cho xe tăng người chơi
                playerTank.downHealth(1);
                System.out.println("Bullet hit the player tank! Player health: " + playerTank.getHealth());

                GameScreen.getInstance().checkHealth(GameScreen.getInstance().ptRenderer.getPlayerTank());

                // Đảm bảo thoát khỏi vòng lặp đạn sau khi xử lý va chạm
                break;
            }
        }
    }


    // Check collision between bullets and enemy tanks (Tank)
    public static void checkCollisionBulletsTankAI(CopyOnWriteArrayList<Bullet> bullets, CopyOnWriteArrayList<Tank> enemyTanks) {
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
                    enemy.downHealth(1);
                    System.out.println("Bullet hit an enemy tank! Enemy health: " + enemy.getHealth());
                    SoundUtility.BulletHitTank();
                    // Nếu xe tăng đối phương bị tiêu diệt, xóa xe tăng đó khỏi danh sách
                    if (enemy.getHealth() <= 0) {
                        incrementNum(enemy);
                        TankSpawner.onEnemyTankDestroyed();
                        GameScreen.animations.add(new TankExplosion(enemyTanks.get(i).getX(), enemyTanks.get(i).getY(), 50, 1, false));
                        BoardUtility.spawnRandomPowerUp(enemyTanks.get(i).getX(), enemyTanks.get(i).getY(), GameConstants.POWER_UP_SPAWN_CHANCE);
                        enemyTanks.remove(i); // Loại bỏ enemy khỏi danh sách
                        SoundUtility.explosion1();
                    }
                    // Đảm bảo thoát khỏi vòng lặp đạn sau khi xử lý va chạm
                    break;
                }
            }
        }
    }


    // Check collision between moving tanks and blocks
    public static boolean checkMovingCollisions(Tank tank, CopyOnWriteArrayList<Block> blocks) {
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

    public static boolean isPlayerTankInTree() {
        Rectangle tankHitbox = GameScreen.getInstance().ptRenderer.getPlayerTank().getHitbox();
        ArrayList<Block> blocksClone = new ArrayList<>(GameScreen.blocks);
        // Loop through the list by index instead of using an iterator
        for (Block block : blocksClone) {
            if (tankHitbox.intersects(block.getHitbox()) && block.getType() == BlockType.TREE.getValue())
                return true; // Collision detected
        }
        return false; // No collision detected
    }


    public static void checkCollisionBulletsBlocks(CopyOnWriteArrayList<Bullet> bullets, CopyOnWriteArrayList<Block> blocks) {
        CopyOnWriteArrayList<Bullet> bulletsToRemove = new CopyOnWriteArrayList<>();
        ArrayList<Block> blocksToRemove = new ArrayList<>();

        for (Bullet b : bullets) {
            Rectangle2D.Double bulletHitbox = b.getHitbox();
            boolean bulletShouldBeRemoved = false;

            for (Block block : blocks) {
                Rectangle blockHitbox = block.getHitbox();
                BlockType blockType = BlockType.getTypeFromInt(block.getType());

                if (bulletHitbox.intersects(blockHitbox)) {
                    System.out.println("Collision detected with block of type: " + blockType);

                    if (b.getBulletType() == BulletType.STANDARD_TIER_4) {
                        // TIER_3 bullets can destroy any block except EDGE and RIVER
                        if (blockType == BlockType.EDGE) {
                            bulletShouldBeRemoved = true; // Remove the bullet when it hits EDGE
                            break;
                        } else if (blockType != BlockType.RIVER) {
                            GameScreen.animations.add(new BlockExplosion(block.getX(), block.getY(), 100, 0.5, false));
                            blocksToRemove.add(block); // Mark block for removal
                            bulletShouldBeRemoved = true;
                            SoundUtility.BulletHitBrick(); // Use appropriate sound effect
                        }
                    } else {
                        // Handle other bullet types as before
                        switch (blockType) {
                            case BRICK:
                                GameScreen.animations.add(new BlockExplosion(block.getX(), block.getY(), 100, 0.5, false));
                                blocksToRemove.add(block); // Mark brick block for removal
                                bulletShouldBeRemoved = true; // Bullet should be removed after colliding with bricks
                                SoundUtility.BulletHitBrick();
                                break;
                            case STEEL:
                                bulletShouldBeRemoved = true; // Bullet is removed when hitting steel
                                break;
                            case EDGE:
                                bulletShouldBeRemoved = true; // Bullet is removed when hitting edge
                                break;
                            case RIVER:
                            case TREE:
                                // Bullet goes through river or tree; do nothing
                                break;
                            default:
                                System.out.println("Unknown block type.");
                                break;
                        }
                    }

                    // Stop further checks for this bullet if it hits a block that stops it
                    if (bulletShouldBeRemoved) {
                        break;
                    }
                }
            }

            // Only add the bullet to be removed if it should actually be removed
            if (bulletShouldBeRemoved) {
                bulletsToRemove.add(b);
            }
        }

        // Remove all marked bullets and blocks after looping
        bullets.removeAll(bulletsToRemove);
        blocks.removeAll(blocksToRemove);
    }



    public static boolean isBulletTouchingBase(CopyOnWriteArrayList<Bullet> bullets, CopyOnWriteArrayList<Block> blocks) {
        for (Bullet bullet : bullets) {
            Rectangle2D.Double bulletHitbox = bullet.getHitbox();

            for (Block block : blocks) {
                // Check if the block is of type BASE
                if (block.getType() == BlockType.BASE.getValue()) {
                    Rectangle baseHitbox = block.getHitbox();

                    // Check if the bullet intersects with the base block
                    if (bulletHitbox.intersects(baseHitbox)) {
                        GameScreen.animations.add(new BlockExplosion(block.getX(), block.getY(), 100, 0.5, false));
                        SoundUtility.explosion2();
                        return true; // Collision detected with base
                    }
                }
            }
        }
        return false; // No collision with base detected
    }


    public static void checkTankPowerUpCollision(Tank playerTank, ArrayList<PowerUp> powerUps, CopyOnWriteArrayList<Tank> enemyTanks, CopyOnWriteArrayList<Block> blocks) {
        for (PowerUp powerUp : powerUps) {
            if (playerTank.getHitbox().intersects(powerUp.getHitbox()) && powerUp.isVisible()) {
                SoundUtility.powerupPick();
                if (powerUp instanceof BombPowerUp) {
                    BoardUtility.activateBombPowerUp(enemyTanks);
                } else if (powerUp instanceof ShieldPowerUp) {
                    BoardUtility.activateShieldPowerUp(playerTank);
                } else if (powerUp instanceof ClockPowerUp) {
                    BoardUtility.activateClockPowerUp(enemyTanks);
                } else if (powerUp instanceof ShovelPowerUp) {
                    BoardUtility.activateShovelPowerUp(blocks); // Truyền blocks vào đây
                } else if (powerUp instanceof TankPowerUp) {
                    BoardUtility.activateTankPowerUp();
                } else if (powerUp instanceof StarPowerUp) {
                    BoardUtility.activateStarPowerUp();
                }
                powerUp.setVisible(false); // Ẩn PowerUp sau khi được nhặt
                break;
            }
        }
    }

    public static void checkCollisionTankTankAI(Tank playerTank, CopyOnWriteArrayList<Tank> enemyTanks) {
        Rectangle playerHitbox = playerTank.getHitbox();

        for (int i = 0; i < enemyTanks.size(); i++) {
            Tank enemy = enemyTanks.get(i);
            Rectangle enemyHitbox = enemy.getHitbox();

            if (playerHitbox.intersects(enemyHitbox)) {
                System.out.println("Collision detected between PlayerTank and an enemy tank!");

                // Reduce health for both tanks on collision, or you can decide on specific logic
                playerTank.downHealth(1);
                enemy.downHealth(1);

                System.out.println("Player health: " + playerTank.getHealth());
                System.out.println("Enemy health: " + enemy.getHealth());

                // Handle player tank destruction if health is zero or below
                GameScreen.getInstance().checkHealth(GameScreen.getInstance().ptRenderer.getPlayerTank());

                // Handle enemy tank destruction if health is zero or below
                if (enemy.getHealth() <= 0) {
                    TankSpawner.onEnemyTankDestroyed();
                    incrementNum(enemy);
                    GameScreen.animations.add(new TankExplosion(enemyTanks.get(i).getX(), enemyTanks.get(i).getY(), 100, 1, false));
                    BoardUtility.spawnRandomPowerUp(enemyTanks.get(i).getX(), enemyTanks.get(i).getY(), GameConstants.POWER_UP_SPAWN_CHANCE);
                    enemyTanks.remove(i); // Loại bỏ enemy khỏi danh sách
                }

                // Exit after handling collision with one enemy tank to avoid ConcurrentModificationException
                break;
            }
        }
    }

    public static void incrementNum(Tank enemyTank) {
        TankType tankType = enemyTank.getTankType();
        switch (tankType) {
            case BASIC:
                enemyTankNum[0] += 1;
                break;
            case FAST:
                enemyTankNum[1] += 1;
                break;
            case POWER:
                enemyTankNum[2] += 1;
                break;
            case ARMOR:
                enemyTankNum[3] += 1;
                break;
            default:
                break;
        }
    }

    public static int[] getEnemyTankNum() {
        return enemyTankNum;
    }
}
