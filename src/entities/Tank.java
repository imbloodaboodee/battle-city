package entities;

import physics.CollisionHandling;
import render.GameScreen;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Tank implements KeyListener {
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
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

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_W:
                upPressed = true;
                break;
            case KeyEvent.VK_S:
                downPressed = true;
                break;
            case KeyEvent.VK_A:
                leftPressed = true;
                break;
            case KeyEvent.VK_D:
                rightPressed = true;
                break;
        }
        updateTankPosition();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_W:
                upPressed = false;
                break;
            case KeyEvent.VK_S:
                downPressed = false;
                break;
            case KeyEvent.VK_A:
                leftPressed = false;
                break;
            case KeyEvent.VK_D:
                rightPressed = false;
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
        if (leftPressed && rightPressed) {
            // Do nothing if both left and right are pressed
        } else if (leftPressed) {
            x -= speed;
            tankAngle = Math.toRadians(-90); // Moving left
        } else if (rightPressed) {
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
        if (upPressed && downPressed) {
            // Do nothing if both up and down are pressed
        } else if (upPressed) {
            y -= speed;
            tankAngle = Math.toRadians(0); // Moving up
        } else if (downPressed) {
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
        if (upPressed && rightPressed) {
            tankAngle = Math.toRadians(45); // Moving top-right
        } else if (upPressed && leftPressed) {
            tankAngle = Math.toRadians(-45); // Moving top-left
        } else if (downPressed && rightPressed) {
            tankAngle = Math.toRadians(135); // Moving bottom-right
        } else if (downPressed && leftPressed) {
            tankAngle = Math.toRadians(-135); // Moving bottom-left
        }

        // Final hitbox update
        updateHitbox();
    }






    private void updateHitbox() {
        hitbox.setLocation(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public void setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }


    public boolean isRightPressed() {
        return rightPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
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
