package entities;

import constants.GameConstants;
import manager.BulletManager;
import physics.CollisionHandling;
import render.GameScreen;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;

public class SmartTank extends Tank {
    private BulletManager bulletManager;

    // Image variables
    private ImageIcon cannonImage;
    private ImageIcon baseImage;
    private Bullet defaultBullet;
    private double cannonAngle = 0;
    private boolean canFire = true;

    // Timers
    private Timer movementTimer;
    private Timer bulletCreationTimer;
    private Timer bulletTimerCountdown;
    private Timer gameLoopTimer;

    // Random movement
    private int movementDirection = 1;
    private int moveCounter = 0; // Counter to track steps in the current direction
    private final int MAX_STEPS = 100; // Maximum steps before direction change


    public SmartTank(BulletType bulletType) {
        super();
        this.defaultBullet = new Bullet(bulletType);

        // Initialize tank images
        baseImage = new ImageIcon("./src/assets/image/tank.png");
        cannonImage = new ImageIcon("./src/assets/image/cannon.png");

        initializeCommonResources();

    }

    public SmartTank(int x, int y, int health, int speed, BulletType bulletType, ImageIcon baseImage, ImageIcon cannonImage) {
        super(x, y, health, speed);
        this.baseImage = resizeImageIcon(baseImage, 0.9);
        this.cannonImage = resizeImageIcon(cannonImage, 0.9); // or separate sizes for the cannon
        this.defaultBullet = new Bullet(bulletType);
        initializeCommonResources();

    }

    private void initializeCommonResources() {
        bulletManager = new BulletManager(getBullets());

        setHitbox(new Rectangle(getX(), getY(), baseImage.getIconWidth(), baseImage.getIconHeight()));

        // Timer for autonomous movement
        movementTimer = new Timer(GameConstants.DELAY, e -> bumpMove());
        movementTimer.start();
        // Timer for creating bullets, starts when the enemy decides to shoot
        bulletCreationTimer = new Timer(GameConstants.DELAY, e -> shoot());
        bulletCreationTimer.setRepeats(true);
//        bulletCreationTimer.start();

        // Timer for updating entities (tank position, bullets, etc.)
        gameLoopTimer = new Timer(GameConstants.DELAY, e -> {
            bulletManager.updateBullets();
        });
        gameLoopTimer.start();
    }

    private ImageIcon resizeImageIcon(ImageIcon icon, double ratio) {
        // Get the original width and height of the image
        int originalWidth = icon.getIconWidth();
        int originalHeight = icon.getIconHeight();

        // Calculate the new width and height based on the ratio
        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);

        // Scale the image to the new dimensions
        Image resizedImage = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        // Return a new ImageIcon from the scaled image
        return new ImageIcon(resizedImage);
    }

    // Simulate autonomous movement by changing directions randomly
    private void bumpMove() {
        if (isFrozen()) {
            System.out.println("SmartTank is frozen, skipping movement.");
            return; // Stop movement if frozen
        }

        int originalX = getX();
        int originalY = getY();

        // Check if the current direction is blocked
        if (willCollideInDirection(movementDirection)) {
            // Choose a new direction if the current one is blocked
            movementDirection = findValidDirection();
        }

        // Move in the current direction and increment move counter
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

        updateHitbox(); // Update the hitbox after moving

        // Check for collisions after moving
        if (CollisionHandling.checkMovingCollisions(this, GameScreen.blocks) || !isWithinBounds(getX(), getY()) || moveCounter >= MAX_STEPS) {
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

    // Helper method to check if moving in the given direction would result in a collision
    private boolean willCollideInDirection(int direction) {
        int testX = getX();
        int testY = getY();

        // Determine the test position based on the direction
        switch (direction) {
            case 0: // Up
                testY -= getSpeed();
                break;
            case 1: // Down
                testY += getSpeed();
                break;
            case 2: // Left
                testX -= getSpeed();
                break;
            case 3: // Right
                testX += getSpeed();
                break;
        }

        // Check if this position collides with any blocks
        setX(testX);
        setY(testY);
        updateHitbox();

        boolean willCollide = CollisionHandling.checkMovingCollisions(this, GameScreen.blocks) || !isWithinBounds(testX, testY);

        // Restore original position
        setX(getX());
        setY(getY());
        updateHitbox();

        return willCollide;
    }

    // Helper method to check if the tank is within bounds
    private boolean isWithinBounds(int x, int y) {
        int tankWidth = baseImage.getIconWidth();
        int tankHeight = baseImage.getIconHeight();

        return (x >= 0 && x + tankWidth <= GameConstants.FRAME_WIDTH) && (y >= 0 && y + tankHeight <= GameConstants.FRAME_HEIGHT);
    }

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
            if (!CollisionHandling.checkMovingCollisions(this, GameScreen.blocks) && isWithinBounds(testX, testY)) {
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

    // Create (fire) a bullet, similar to player tank, but with its own logic
    private void shoot() {
        if (isFrozen()) {
            System.out.println("SmartTank is frozen, skipping bullet creation.");
            return; // Dừng bắn nếu bị đóng băng
        }
        if (canFire) {
            // Random firing logic or based on some condition
            int cannonTipX = (int) (getX() + baseImage.getIconWidth() / 2 + Math.cos(cannonAngle - Math.PI / 2) * cannonImage.getIconHeight() / 2);
            int cannonTipY = (int) (getY() + baseImage.getIconHeight() / 2 + Math.sin(cannonAngle - Math.PI / 2) * cannonImage.getIconHeight() / 2);

            Bullet bullet = new Bullet(cannonTipX, cannonTipY, defaultBullet.getBulletType(), cannonAngle - Math.PI / 2);
            getBullets().add(bullet);

            canFire = false;
            bulletTimerCountdown = new Timer(defaultBullet.getCooldown() + 100, e -> {
                canFire = true;
                bulletTimerCountdown.stop();
            });
            bulletTimerCountdown.start();
        }
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

    public int getMovementDirection() {
        return movementDirection;
    }

    public void setMovementDirection(int movementDirection) {
        this.movementDirection = movementDirection;
    }
}
