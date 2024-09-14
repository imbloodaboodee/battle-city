package entities;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Tank implements KeyListener {
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private double tankAngle = 0; // Default angle when the tank is stationary

    private int x;
    private int y;
    private int health;
    private int speed;  // Increase speed for visible movement
    private ArrayList<Bullet> bullets = new ArrayList<>();

    public Tank(int x, int y, int health, int speed) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.speed = speed;  // Initialize speed

        // Initialize the bullet timer for automatic firing

    }

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
        // Track the movement direction and update the tank's angle
        if (upPressed && rightPressed) {
            tankAngle = Math.toRadians(45); // Top-right
            x += speed;
            y -= speed;
            System.out.println("Top right");
        } else if (upPressed && leftPressed) {
            tankAngle = Math.toRadians(-45); // Top-left
            x -= speed;
            y -= speed;
            System.out.println("Top left");

        } else if (downPressed && rightPressed) {
            tankAngle = Math.toRadians(135); // Bottom-right
            x += speed;
            y += speed;
            System.out.println("Bottom right");

        } else if (downPressed && leftPressed) {
            tankAngle = Math.toRadians(-135); // Bottom-left
            x -= speed;
            y += speed;
            System.out.println("Top left");
        } else if (upPressed) {
            tankAngle = Math.toRadians(0); // Moving up
            y -= speed;
            System.out.println("Up");
        } else if (downPressed) {
            tankAngle = Math.toRadians(180); // Moving down
            y += speed;
            System.out.println("Down");
        } else if (leftPressed) {
            tankAngle = Math.toRadians(-90); // Moving left
            x -= speed;
            System.out.println("Left");
        } else if (rightPressed) {
            tankAngle = Math.toRadians(90); // Moving right
            x += speed;
            System.out.println("Right");
        }

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

}
