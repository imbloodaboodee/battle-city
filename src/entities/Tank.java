package entities;

import physics.CollisionHandling;
import render.GameScreen;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Tank implements KeyListener {
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

    public Tank() {
        x = 100;
        y = 100;
        health = 3;
        speed = 1;
    }

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
        health = 3;
        speed = 1;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_W:
                isMovingUp = true;
                break;
            case KeyEvent.VK_S:
                isMovingDown = true;
                break;
            case KeyEvent.VK_A:
                isMovingLeft = true;
                break;
            case KeyEvent.VK_D:
                isMovingRight = true;
                break;
        }
        updateTankPosition();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_W:
                isMovingUp = false;
                break;
            case KeyEvent.VK_S:
                isMovingDown = false;
                break;
            case KeyEvent.VK_A:
                isMovingLeft = false;
                break;
            case KeyEvent.VK_D:
                isMovingRight = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
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
public void moveUp(){
        isMovingUp = true;
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
}
