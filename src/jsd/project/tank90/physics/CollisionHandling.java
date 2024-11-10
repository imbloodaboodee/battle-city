package jsd.project.tank90.physics;

import jsd.project.tank90.sprite.Block;
import jsd.project.tank90.constants.GameConstants;
import jsd.project.tank90.entities.*;
import jsd.project.tank90.sprite.PowerUps.*;
import jsd.project.tank90.environment.BlockType;
import jsd.project.tank90.manager.TankSpawner;
import jsd.project.tank90.render.GameScreen;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollisionHandling {

    private static int[] enemyTankNum = {0, 0, 0, 0};

    public static void checkCollisionBulletsTank(CopyOnWriteArrayList<Bullet> bullets, Tank playerTank) {
        Rectangle playerHitbox = playerTank.getHitbox();

        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bullet.getHitbox().intersects(playerHitbox)) {
                bullets.remove(i);
                playerTank.downHealth(bullet.getDamage());
                GameScreen.getInstance().checkHealth(GameScreen.getInstance().ptRenderer.getPlayerTank());
                break;
            }
        }
    }


    public static void checkCollisionBulletsTankAI(CopyOnWriteArrayList<Bullet> bullets, CopyOnWriteArrayList<Tank> enemyTanks) {
        for (int i = 0; i < enemyTanks.size(); i++) {
            Tank enemy = enemyTanks.get(i);
            Rectangle enemyHitbox = enemy.getHitbox();
            for (int j = 0; j < bullets.size(); j++) {
                Bullet bullet = bullets.get(j);
                if (bullet.getHitbox().intersects(enemyHitbox)) {
                    bullets.remove(j);
                    enemy.downHealth(bullet.getDamage());
                    SoundUtility.BulletHitTank();
                    if (enemy.getHealth() <= 0) {
                        incrementNum(enemy);
                        TankSpawner.onEnemyTankDestroyed();
                        GameScreen.animations.add(new TankExplosion(enemyTanks.get(i).getX(), enemyTanks.get(i).getY(), 50, 1, false));
                        BoardUtility.spawnRandomPowerUp(enemyTanks.get(i).getX(), enemyTanks.get(i).getY(), GameConstants.POWER_UP_SPAWN_CHANCE);
                        enemyTanks.remove(i);
                        SoundUtility.explosion1();
                    }
                    break;
                }
            }
        }
    }


    public static boolean checkMovingCollisions(Tank tank, CopyOnWriteArrayList<Block> blocks) {
        Rectangle tankHitbox = tank.getHitbox();
        ArrayList<Block> blocksClone = new ArrayList<>(blocks);
        for (Block block : blocksClone) {
            if (tankHitbox.intersects(block.getHitbox()) && block.getType() != BlockType.TREE.getValue()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPlayerTankInTree() {
        Rectangle tankHitbox = GameScreen.getInstance().ptRenderer.getPlayerTank().getHitbox();
        ArrayList<Block> blocksClone = new ArrayList<>(GameScreen.blocks);
        for (Block block : blocksClone) {
            if (tankHitbox.intersects(block.getHitbox()) && block.getType() == BlockType.TREE.getValue())
                return true;
        }
        return false;
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
                    if (b.getBulletType() == BulletType.STANDARD_TIER_4||
                            b.getBulletType() == BulletType.EXPLOSIVE||
                            b.getBulletType() == BulletType.EXPLOSIVE_TIER_2||
                            b.getBulletType() == BulletType.EXPLOSIVE_TIER_3||
                            b.getBulletType() == BulletType.EXPLOSIVE_TIER_4) {
                        if (blockType == BlockType.EDGE) {
                            bulletShouldBeRemoved = true;
                            break;
                        } else if (blockType != BlockType.RIVER&& blockType!=BlockType.TREE) {
                            GameScreen.animations.add(new BlockExplosion(block.getX(), block.getY(), 100, 0.5, false));
                            blocksToRemove.add(block);
                            bulletShouldBeRemoved = true;
                            SoundUtility.BulletHitBrick();
                        }
                    } else {
                        switch (blockType) {
                            case BRICK:
                                GameScreen.animations.add(new BlockExplosion(block.getX(), block.getY(), 100, 0.5, false));
                                blocksToRemove.add(block);
                                bulletShouldBeRemoved = true;
                                SoundUtility.BulletHitBrick();
                                break;
                            case STEEL, EDGE:
                                bulletShouldBeRemoved = true;
                                break;
                            case RIVER:
                            case TREE:
                            default:
                                break;
                        }
                    }

                    if (bulletShouldBeRemoved) {
                        break;
                    }
                }
            }
            if (bulletShouldBeRemoved) {
                bulletsToRemove.add(b);
            }
        }

        bullets.removeAll(bulletsToRemove);
        blocks.removeAll(blocksToRemove);
    }



    public static boolean isBulletTouchingBase(CopyOnWriteArrayList<Bullet> bullets, CopyOnWriteArrayList<Block> blocks) {
        for (Bullet bullet : bullets) {
            Rectangle2D.Double bulletHitbox = bullet.getHitbox();

            for (Block block : blocks) {
                if (block.getType() == BlockType.BASE.getValue()) {
                    Rectangle baseHitbox = block.getHitbox();

                    if (bulletHitbox.intersects(baseHitbox)) {
                        GameScreen.animations.add(new BlockExplosion(block.getX(), block.getY(), 100, 0.5, false));
                        SoundUtility.explosion2();
                        return true;
                    }
                }
            }
        }
        return false;
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
                    BoardUtility.activateShovelPowerUp(blocks);
                } else if (powerUp instanceof TankPowerUp) {
                    BoardUtility.activateTankPowerUp();
                } else if (powerUp instanceof StarPowerUp) {
                    BoardUtility.activateStarPowerUp();
                }
                powerUp.setVisible(false);
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
                playerTank.downHealth(enemy.getHealth());
                enemy.downHealth(playerTank.getHealth());
                GameScreen.getInstance().checkHealth(GameScreen.getInstance().ptRenderer.getPlayerTank());

                if (enemy.getHealth() <= 0) {
                    TankSpawner.onEnemyTankDestroyed();
                    incrementNum(enemy);
                    GameScreen.animations.add(new TankExplosion(enemyTanks.get(i).getX(), enemyTanks.get(i).getY(), 100, 1, false));
                    BoardUtility.spawnRandomPowerUp(enemyTanks.get(i).getX(), enemyTanks.get(i).getY(), GameConstants.POWER_UP_SPAWN_CHANCE);
                    enemyTanks.remove(i);
                }
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
