package jsd.project.tank90.entities;

import jsd.project.tank90.manager.BulletManager;
import jsd.project.tank90.physics.CollisionHandling;
import jsd.project.tank90.render.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public class Tank {
    private boolean isMovingUp = false;
    private boolean isMovingDown = false;
    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;
    private double tankAngle = 0;
    private Rectangle hitbox;

    private int x;
    private int y;
    private int health;
    private int speed;
    private CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<>();
    private boolean isFrozen = false;
    public boolean shield = false;
    private long shieldEndTime = 0;
    private Timer freezeTimer;
    private int movementDirection = 1;
    private int moveCounter = 0;
    private final int MAX_STEPS = 100;
    private Bullet defaultBullet;
    private ImageIcon cannonImage;
    private ImageIcon baseImage;

    private Timer bulletTimerCountdown;
    private double cannonAngle = 0;
    private boolean canFire = true;
    private BulletManager bulletManager;
    private TankType tankType;
    private double rotationSpeed;

    public Tank() {
        x = 176;
        y = 400;
        health = 1;
        speed = 1;
        bulletManager = new BulletManager(getBullets());

    }

    public Tank(int x, int y, int health, int speed) {
        this.x = x;
        this.speed = speed;
        this.y = y;
        this.health = health;
        bulletManager = new BulletManager(getBullets());
    }

    public double getTankAngle() {
        return tankAngle;
    }

    public void setTankAngle(double tankAngle) {
        this.tankAngle = tankAngle;
    }

    public void updateTankPosition() {
        int oldX = x;
        int oldY = y;


        if (isMovingLeft && isMovingRight) {
        } else if (isMovingLeft) {
            x -= speed;
            tankAngle = Math.toRadians(-90);
        } else if (isMovingRight) {
            x += speed;
            tankAngle = Math.toRadians(90);
        }

        updateHitbox();
        boolean xCollision = CollisionHandling.checkMovingCollisions(this, GameScreen.blocks);
        if (xCollision) {
            x = oldX;
        }

        if (isMovingUp && isMovingDown) {
        } else if (isMovingUp) {
            y -= speed;
            tankAngle = Math.toRadians(0);
        } else if (isMovingDown) {
            y += speed;
            tankAngle = Math.toRadians(180);
        }

        updateHitbox();
        boolean yCollision = CollisionHandling.checkMovingCollisions(this, GameScreen.blocks);
        if (yCollision) {
            y = oldY;
        }

        if (isMovingUp && isMovingRight) {
            tankAngle = Math.toRadians(45);
        } else if (isMovingUp && isMovingLeft) {
            tankAngle = Math.toRadians(-45);
        } else if (isMovingDown && isMovingRight) {
            tankAngle = Math.toRadians(135);
        } else if (isMovingDown && isMovingLeft) {
            tankAngle = Math.toRadians(-135);
        }

        updateHitbox();
    }

    public void freeze(int duration) {
        isFrozen = true;
        System.out.println("SmartTank is now frozen for " + duration + " milliseconds.");

        if (freezeTimer != null && freezeTimer.isRunning()) {
            freezeTimer.stop();
        }

        freezeTimer = new Timer(duration, e -> {
            isFrozen = false;
            System.out.println("SmartTank is no longer frozen.");
        });
        freezeTimer.setRepeats(false);
        freezeTimer.start();
    }

    public void activateShield(long duration) {
        this.shield = true;
        this.shieldEndTime = System.currentTimeMillis() + duration;
    }

    public void checkShieldStatus() {
        if (shield && System.currentTimeMillis() > shieldEndTime) {
            shield = false;
        }
    }

    public void bumpMove() {
        if (isFrozen()) {
            System.out.println("SmartTank is frozen, skipping movement.");
            return; // Stop movement if frozen
        }

        int originalX = getX();
        int originalY = getY();

        switch (movementDirection) {
            case 0:
                setY(getY() - getSpeed());
                setTankAngle(Math.toRadians(0));

                break;
            case 1:
                setY(getY() + getSpeed());
                setTankAngle(Math.toRadians(180));
                break;
            case 2:

                setX(getX() - getSpeed());
                setTankAngle(Math.toRadians(-90));
                break;
            case 3:
                setX(getX() + getSpeed());
                setTankAngle(Math.toRadians(90));
                break;
        }

        updateHitbox();

        if (CollisionHandling.checkMovingCollisions(this, GameScreen.blocks) || moveCounter >= MAX_STEPS) {
            setX(originalX);
            setY(originalY);

            moveCounter = 0;
            int newDirection = findValidDirection();
            if (newDirection != -1) {
                movementDirection = newDirection;
            }
        } else {
            moveCounter++;
        }
    }

    private int findValidDirection() {
        ArrayList<Integer> directions = new ArrayList<>(Arrays.asList(0, 1, 1, 2, 3));
        Collections.shuffle(directions);

        int originalX = getX();
        int originalY = getY();

        for (int direction : directions) {
            int testX = originalX;
            int testY = originalY;

            switch (direction) {
                case 0:
                    testY -= getSpeed();
                    break;
                case 1:
                    testY += getSpeed();
                    break;
                case 2:
                    testX -= getSpeed();
                    break;
                case 3:
                    testX += getSpeed();
                    break;
            }

            setX(testX);
            setY(testY);
            updateHitbox();

            if (!CollisionHandling.checkMovingCollisions(this, GameScreen.blocks)) {
                setX(originalX);
                setY(originalY);
                return direction;
            }
        }

        setX(originalX);
        setY(originalY);
        return -1;
    }

    public void shoot(ImageIcon baseImage) {
        if (isFrozen()) {
            System.out.println("SmartTank is frozen, skipping movement.");
            return;
        }

        if (canFire) {
            int cannonTipX = (int) (getX() + baseImage.getIconWidth() / 2 + Math.cos(getTankAngle() - Math.PI / 2) * baseImage.getIconHeight() / 2);
            int cannonTipY = (int) (getY() + baseImage.getIconHeight() / 2 + Math.sin(getTankAngle() - Math.PI / 2) * baseImage.getIconHeight() / 2);

            Bullet bullet = new Bullet(cannonTipX - getDefaultBullet().getBulletImage().getIconWidth() / 2, cannonTipY - getDefaultBullet().getBulletImage().getIconHeight() / 2, defaultBullet.getBulletType(), getTankAngle() - Math.PI / 2);
            getBullets().add(bullet);

            canFire = false;
            bulletTimerCountdown = new Timer(defaultBullet.getCooldown() + 100, e -> {
                canFire = true;
                bulletTimerCountdown.stop();
            });
            bulletTimerCountdown.start();
        }
    }

    public void downHealth(int amount) {
        if (!shield) {
            this.health -= amount;
        }
    }

    public void upHealth(int amount) {
        this.health += amount;
    }

    void updateHitbox() {
        hitbox.setLocation(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isMovingUp() {
        return isMovingUp;
    }

    public void setMovingUp(boolean movingUp) {
        this.isMovingUp = movingUp;
    }

    public boolean isMovingDown() {
        return isMovingDown;
    }

    public void setMovingDown(boolean movingDown) {
        this.isMovingDown = movingDown;
    }

    public boolean isMovingLeft() {
        return isMovingLeft;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.isMovingLeft = movingLeft;
    }


    public boolean isMovingRight() {
        return isMovingRight;
    }

    public void setMovingRight(boolean movingRight) {
        this.isMovingRight = movingRight;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public CopyOnWriteArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(CopyOnWriteArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public boolean isShield() {
        return shield;
    }

    public Bullet getDefaultBullet() {
        return defaultBullet;
    }

    public void setDefaultBullet(Bullet defaultBullet) {
        this.defaultBullet = defaultBullet;
    }

    public ImageIcon getCannonImage() {
        return cannonImage;
    }

    public void setCannonImage(ImageIcon cannonImage) {
        this.cannonImage = cannonImage;
    }

    public ImageIcon getBaseImage() {
        return baseImage;
    }

    public void setBaseImage(ImageIcon baseImage) {
        this.baseImage = baseImage;
    }

    public double getCannonAngle() {
        return cannonAngle;
    }

    public void setCannonAngle(double cannonAngle) {
        this.cannonAngle = cannonAngle;
    }

    public boolean isCanFire() {
        return canFire;
    }

    public void setCanFire(boolean canFire) {
        this.canFire = canFire;
    }

    public BulletManager getBulletManager() {
        return bulletManager;
    }

    public TankType getTankType() {
        return tankType;
    }

    public void setTankType(TankType tankType) {
        this.tankType = tankType;
    }

    public double getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(double rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }
}
