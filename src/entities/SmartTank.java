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
    private boolean isFrozen = false;
    private Timer freezeTimer;

    // Timers
    private Timer movementTimer;
    private Timer bulletCreationTimer;
    private Timer bulletTimerCountdown;
    private Timer gameLoopTimer;

    // Random movement
    private int movementDirection;


    public SmartTank(BulletType bulletType) {
        super();
        this.defaultBullet = new Bullet(bulletType);

        bulletManager = new BulletManager(getBullets());
        // Initialize tank images
        baseImage = new ImageIcon("./src/assets/image/tank.png");
        cannonImage = new ImageIcon("./src/assets/image/cannon.png");

        // Initialize the hitboxssss
        setHitbox(new Rectangle(getX(), getY(), baseImage.getIconWidth(), baseImage.getIconHeight()));

        // Timer for autonomous movement
        movementTimer = new Timer(GameConstants.DELAY, e -> bumpMove());
        movementTimer.start();
        // Timer for creating bullets, starts when the enemy decides to shoot
        bulletCreationTimer = new Timer(GameConstants.DELAY, e -> shoot());
        bulletCreationTimer.setRepeats(true);
        bulletCreationTimer.start();

        // Timer for updating entities (tank position, bullets, etc.)
        gameLoopTimer = new Timer(GameConstants.DELAY, e -> {
            bulletManager.updateBullets();
        });
        gameLoopTimer.start();

    }



    // Simulate autonomous movement by changing directions randomly
    private void bumpMove() {
        if (isFrozen) {
            System.out.println("SmartTank is frozen, skipping movement.");
            return; // Dừng di chuyển nếu bị đóng băng
        }
        int originalX = getX();
        int originalY = getY();

        // Try moving in the current direction
        switch (movementDirection) {
            case 0: // Move up
                if (isWithinBounds(getX(), getY() - getSpeed())) {
                    setY(getY() - getSpeed());
                    setTankAngle(Math.toRadians(0));
                }
                break;
            case 1: // Move down
                if (isWithinBounds(getX(), getY() + getSpeed())) {
                    setY(getY() + getSpeed());
                    setTankAngle(Math.toRadians(180));

                }
                break;
            case 2: // Move left
                if (isWithinBounds(getX() - getSpeed(), getY())) {

                    setX(getX() - getSpeed());
                    setTankAngle(Math.toRadians(-90));

                }
                break;
            case 3: // Move right
                if (isWithinBounds(getX() + getSpeed(), getY())) {
                    setX(getX() + getSpeed());
                    setTankAngle(Math.toRadians(90));
                }
                break;
        }

        // Update the hitbox after moving
        updateHitbox();

        // Check if the movement results in a collision
        if (CollisionHandling.checkMovingCollisions(this, GameScreen.blocks) || !isWithinBounds(getX(), getY())) {
            // Revert to original position if collision or boundary violation occurs
            setX(originalX);
            setY(originalY);

            // Find a valid direction that does not result in a collision or boundary violation
            int newDirection = findValidDirection();
            if (newDirection != -1) {
                movementDirection = newDirection; // Change to the valid direction
            }
        }
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
        if (isFrozen) {
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
