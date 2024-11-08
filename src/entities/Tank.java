package entities;

import constants.GameConstants;
import manager.BulletManager;
import physics.CollisionHandling;
import render.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public class Tank {
    private boolean isMovingUp = false;
    private boolean isMovingDown = false;
    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;
    private double tankAngle = 0; // Default angle when the tank is stationary
    private Rectangle hitbox;

    private int x;
    private int y;
    private int health;
    private int speed;  // Increase speed for visible movement
    private CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<>();
    private boolean isFrozen = false;
    public boolean shield = false;
    private long shieldEndTime = 0;
    private Timer freezeTimer;
    private int movementDirection = 1;
    private int moveCounter = 0; // Counter to track steps in the current direction
    private final int MAX_STEPS = 100; // Maximum steps before direction change
    private Bullet defaultBullet;
    private ImageIcon cannonImage;
    private ImageIcon baseImage;

    private Timer bulletTimerCountdown;
    private double cannonAngle = 0;
    private boolean canFire = true;
    private BulletManager bulletManager;

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


        // Move horizontally first and update angle if necessary
        if (isMovingLeft && isMovingRight) {
            // Do nothing if both left and right are pressed
        } else if (isMovingLeft) {
            x -= speed;
            tankAngle = Math.toRadians(-90); // Moving left
        } else if (isMovingRight) {
            x += speed;
            tankAngle = Math.toRadians(90); // Moving right
        }

        // Handle x-axis movement and collision
        updateHitbox(); // Update hitbox for the new x position
        boolean xCollision = CollisionHandling.checkMovingCollisions(this, GameScreen.blocks);
        if (xCollision) {
            x = oldX; // Revert x position if a collision occurs
        }

        // Now move vertically and update angle if necessary
        if (isMovingUp && isMovingDown) {
            // Do nothing if both up and down are pressed
        } else if (isMovingUp) {
            y -= speed;
            tankAngle = Math.toRadians(0); // Moving up
        } else if (isMovingDown) {
            y += speed;
            tankAngle = Math.toRadians(180); // Moving down
        }

        // Handle y-axis movement and collision
        updateHitbox(); // Update hitbox for the new y position
        boolean yCollision = CollisionHandling.checkMovingCollisions(this, GameScreen.blocks);
        if (yCollision) {
            y = oldY; // Revert y position if a collision occurs
        }

        // Handle diagonal movement and update angle
        if (isMovingUp && isMovingRight) {
            tankAngle = Math.toRadians(45); // Moving top-right
        } else if (isMovingUp && isMovingLeft) {
            tankAngle = Math.toRadians(-45); // Moving top-left
        } else if (isMovingDown && isMovingRight) {
            tankAngle = Math.toRadians(135); // Moving bottom-right
        } else if (isMovingDown && isMovingLeft) {
            tankAngle = Math.toRadians(-135); // Moving bottom-left
        }

        // Final hitbox update
        updateHitbox();
    }

    public void freeze(int duration) {
        isFrozen = true; // Đặt trạng thái đóng băng
        System.out.println("SmartTank is now frozen for " + duration + " milliseconds.");

        if (freezeTimer != null && freezeTimer.isRunning()) {
            freezeTimer.stop();  // Dừng timer hiện có để tránh xung đột
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

        // Try moving in the current direction
        switch (movementDirection) {
            case 0: // Move up
                    setY(getY() - getSpeed());
                    setTankAngle(Math.toRadians(0));

                break;
            case 1: // Move down
                    setY(getY() + getSpeed());
                    setTankAngle(Math.toRadians(180));
                break;
            case 2: // Move left

                    setX(getX() - getSpeed());
                    setTankAngle(Math.toRadians(-90));
                break;
            case 3: // Move right
                    setX(getX() + getSpeed());
                    setTankAngle(Math.toRadians(90));
                break;
        }

        // Update the hitbox after moving
        updateHitbox();

        // Check if the movement results in a collision
        if (CollisionHandling.checkMovingCollisions(this, GameScreen.blocks) || moveCounter >= MAX_STEPS) {
            // Revert to original position if collision or boundary violation occurs
            setX(originalX);
            setY(originalY);

            // Reset the counter and find a new random direction
            moveCounter = 0;
            int newDirection = findValidDirection();
            if (newDirection != -1) {
                movementDirection = newDirection;
            }
        } else {
            moveCounter++; // Increment counter if no collision occurs and maximum steps are not reached
        }
    }

    // Helper method to check if the tank is within bounds

    // Helper method to find a valid direction
    private int findValidDirection() {
        // Array of directions with an extra 'down' (1) to increase downward likelihood
        ArrayList<Integer> directions = new ArrayList<>(Arrays.asList(0, 1, 1, 2, 3));
        Collections.shuffle(directions); // Shuffle to randomize direction order

        int originalX = getX();
        int originalY = getY();

        for (int direction : directions) {
            int testX = originalX;
            int testY = originalY;

            // Test the movement based on the shuffled direction
            switch (direction) {
                case 0: // Test up
                    testY -= getSpeed();
                    break;
                case 1: // Test down
                    testY += getSpeed();
                    break;
                case 2: // Test left
                    testX -= getSpeed();
                    break;
                case 3: // Test right
                    testX += getSpeed();
                    break;
            }

            // Temporarily set the test position and update hitbox
            setX(testX);
            setY(testY);
            updateHitbox();

            // Check if this direction is free of collisions and within bounds
            if (!CollisionHandling.checkMovingCollisions(this, GameScreen.blocks)) {
                // Restore the tank's position if a valid direction is found
                setX(originalX);
                setY(originalY);
                return direction; // Found a valid direction
            }
        }

        // Restore the original position if no valid direction is found
        setX(originalX);
        setY(originalY);
        return -1; // Return -1 if no valid direction is found
    }
    public void shoot(ImageIcon baseImage) {
        if (isFrozen()) {
            System.out.println("SmartTank is frozen, skipping movement.");
            return; // Stop movement if frozen
        }

        if (canFire) {
            // Random firing logic or based on some condition
            int cannonTipX = (int) (getX() + baseImage.getIconWidth() / 2 + Math.cos(getTankAngle() - Math.PI / 2) * baseImage.getIconHeight() / 2);
            int cannonTipY = (int) (getY() + baseImage.getIconHeight() / 2 + Math.sin(getTankAngle() - Math.PI / 2) * baseImage.getIconHeight() / 2);

            Bullet bullet = new Bullet(cannonTipX- GameConstants.BULLET_SIZE / 2, cannonTipY- GameConstants.BULLET_SIZE / 2, defaultBullet.getBulletType(), getTankAngle() - Math.PI / 2);
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
}
