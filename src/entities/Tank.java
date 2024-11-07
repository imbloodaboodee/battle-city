package entities;

import physics.CollisionHandling;
import render.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private boolean isFrozen = false;
    public boolean shield = false;
    private long shieldEndTime = 0;
    private Timer freezeTimer;

    public Tank() {
        x = 416;
        y = 16;
        health = 3;
        speed = 1;
    }

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
        health = 3;
        speed = 1;
    }

    public Tank(int x, int y, int health, int speed) {
        this.x = x;
        this.speed = speed;
        this.y = y;
        this.health = health;
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

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public boolean isFrozen() {
        return isFrozen;
    }
}
