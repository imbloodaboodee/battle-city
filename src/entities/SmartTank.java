package entities;

import constants.GameConstants;
import physics.CollisionHandling;
import render.GameScreen;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;

public class SmartTank extends JLabel {
    // Image variables
    private ImageIcon cannonImage;
    private ImageIcon baseImage;
    private Tank tank;
    private Bullet defaultBullet;
    private double cannonAngle = 0;
    private boolean canFire = true;

    // Timers
    private Timer movementTimer;
    private Timer bulletCreationTimer;
    private Timer bulletTimerCountdown;
    private Timer gameLoopTimer;

    // Random movement
    private int movementDirection;

    public SmartTank(Tank tank, BulletType bulletType) {
        this.tank = tank;
        this.defaultBullet = new Bullet(bulletType);
        this.setSize(800, 800);

        // Initialize tank images
        baseImage = new ImageIcon("./src/assets/image/tank.png");
        cannonImage = new ImageIcon("./src/assets/image/cannon.png");

        // Initialize the hitbox
        tank.setHitbox(new Rectangle(tank.getX(), tank.getY(), baseImage.getIconWidth(), baseImage.getIconHeight()));

        // Timer for autonomous movement
        movementTimer = new Timer(GameConstants.DELAY, e -> moveTank());
        movementTimer.start();

        // Timer for creating bullets, starts when the enemy decides to shoot
        bulletCreationTimer = new Timer(GameConstants.DELAY, e -> createBullet());
        bulletCreationTimer.setRepeats(true);
        bulletCreationTimer.start();

        // Timer for updating entities (tank position, bullets, etc.)
        gameLoopTimer = new Timer(GameConstants.DELAY, e -> {
            updateCannonAngle();
            updateBullets();
            repaint();
        });
        gameLoopTimer.start();
    }

    // Simulate autonomous movement by changing directions randomly
    private void moveTank() {
        int originalX = tank.getX();
        int originalY = tank.getY();

        // Try moving in the current direction
        switch (movementDirection) {
            case 0: // Move up
                if (isWithinBounds(tank.getX(), tank.getY() - tank.getSpeed())) {
                    tank.setY(tank.getY() - tank.getSpeed());
                    tank.setTankAngle(Math.toRadians(0));
                }
                break;
            case 1: // Move down
                if (isWithinBounds(tank.getX(), tank.getY() + tank.getSpeed())) {
                    tank.setY(tank.getY() + tank.getSpeed());
                    tank.setTankAngle(Math.toRadians(180));

                }
                break;
            case 2: // Move left
                if (isWithinBounds(tank.getX() - tank.getSpeed(), tank.getY())) {
                    tank.setX(tank.getX() - tank.getSpeed());
                    tank.setTankAngle(Math.toRadians(-90));

                }
                break;
            case 3: // Move right
                if (isWithinBounds(tank.getX() + tank.getSpeed(), tank.getY())) {
                    tank.setX(tank.getX() + tank.getSpeed());
                    tank.setTankAngle(Math.toRadians(90));
                }
                break;
        }

        // Update the hitbox after moving
        tank.updateHitbox();

        // Check if the movement results in a collision
        if (CollisionHandling.checkMovingCollisions(tank, GameScreen.blocks) || !isWithinBounds(tank.getX(), tank.getY())) {
            // Revert to original position if collision or boundary violation occurs
            tank.setX(originalX);
            tank.setY(originalY);

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

        return (x >= 0 && x + tankWidth <= getWidth()) && (y >= 0 && y + tankHeight <= getHeight());
    }

    // Helper method to find a valid direction
    private int findValidDirection() {
        // Array of directions with an extra 'down' (1) to increase downward likelihood
        ArrayList<Integer> directions = new ArrayList<>(Arrays.asList(0, 1, 1, 2, 3));
        Collections.shuffle(directions); // Shuffle to randomize direction order

        int originalX = tank.getX();
        int originalY = tank.getY();

        for (int direction : directions) {
            int testX = originalX;
            int testY = originalY;

            // Test the movement based on the shuffled direction
            switch (direction) {
                case 0: // Test up
                    testY -= tank.getSpeed();
                    break;
                case 1: // Test down
                    testY += tank.getSpeed();
                    break;
                case 2: // Test left
                    testX -= tank.getSpeed();
                    break;
                case 3: // Test right
                    testX += tank.getSpeed();
                    break;
            }

            // Temporarily set the test position and update hitbox
            tank.setX(testX);
            tank.setY(testY);
            tank.updateHitbox();

            // Check if this direction is free of collisions and within bounds
            if (!CollisionHandling.checkMovingCollisions(tank, GameScreen.blocks) && isWithinBounds(testX, testY)) {
                // Restore the tank's position if a valid direction is found
                tank.setX(originalX);
                tank.setY(originalY);
                return direction; // Found a valid direction
            }
        }

        // Restore the original position if no valid direction is found
        tank.setX(originalX);
        tank.setY(originalY);
        return -1; // Return -1 if no valid direction is found
    }



    // Update cannon angle (can be random or towards player if you want to implement targeting)
    private void updateCannonAngle() {
        // Calculate the angle toward the player tank
        int playerCenterX = GameScreen.pt.getTank().getX() + GameScreen.pt.getTank().getHitbox().width / 2;
        int playerCenterY = GameScreen.pt.getTank().getY() + GameScreen.pt.getTank().getHitbox().height / 2;
        int cannonCenterX = tank.getX() + baseImage.getIconWidth() / 2;
        int cannonCenterY = tank.getY() + baseImage.getIconHeight() / 2;

        // Calculate the target angle to the player
        double targetAngle = Math.atan2(playerCenterY - cannonCenterY, playerCenterX - cannonCenterX) + (Math.PI / 2);

        // Normalize angle difference to be between -PI and PI
        double angleDifference = targetAngle - cannonAngle;
        angleDifference = (angleDifference + Math.PI) % (2 * Math.PI) - Math.PI;

        // Rotate the cannon towards the target angle gradually
        if (Math.abs(angleDifference) > GameConstants.ROTATION_SPEED) {
            if (angleDifference > 0) {
                cannonAngle += GameConstants.ROTATION_SPEED;
            } else {
                cannonAngle -= GameConstants.ROTATION_SPEED;
            }
        } else {
            cannonAngle = targetAngle;
        }

        // Keep the cannon angle in the range of -PI to PI
        cannonAngle = (cannonAngle + Math.PI) % (2 * Math.PI) - Math.PI;
    }
    // Create (fire) a bullet, similar to player tank, but with its own logic
    private void createBullet() {
        if (canFire) {
            // Random firing logic or based on some condition
            int cannonTipX = (int) (tank.getX() + baseImage.getIconWidth() / 2 + Math.cos(cannonAngle - Math.PI / 2) * cannonImage.getIconHeight() / 2);
            int cannonTipY = (int) (tank.getY() + baseImage.getIconHeight() / 2 + Math.sin(cannonAngle - Math.PI / 2) * cannonImage.getIconHeight() / 2);

            Bullet bullet = new Bullet(cannonTipX, cannonTipY, defaultBullet.getBulletType(), cannonAngle - Math.PI / 2);
            tank.getBullets().add(bullet);

            canFire = false;
            bulletTimerCountdown = new Timer(defaultBullet.getCooldown()+100, e -> {
                canFire = true;
                bulletTimerCountdown.stop();
            });
            bulletTimerCountdown.start();
        }
    }

    // Update bullets, same as player tank
    private void updateBullets() {
        Iterator<Bullet> bulletIterator = tank.getBullets().iterator();

        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.updatePosition();

            if (bullet.getX() < 0 || bullet.getX() > getWidth() || bullet.getY() < 0 || bullet.getY() > getHeight()) {
                bulletIterator.remove(); // Safely remove the bullet
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Rotate the tank base image according to the tankAngle
        int tankCenterX = tank.getX() + baseImage.getIconWidth() / 2;
        int tankCenterY = tank.getY() + baseImage.getIconHeight() / 2;
        AffineTransform atTank = AffineTransform.getRotateInstance(tank.getTankAngle(), tankCenterX, tankCenterY);
        atTank.translate(tank.getX(), tank.getY());

        // Draw the rotated tank base
        g2d.drawImage(baseImage.getImage(), atTank, this);

        // Rotate the cannon
        int cannonX = tankCenterX - cannonImage.getIconWidth() / 2;
        int cannonY = tankCenterY - cannonImage.getIconHeight() / 2;
        AffineTransform atCannon = AffineTransform.getRotateInstance(cannonAngle, cannonX + cannonImage.getIconWidth() / 2, cannonY + cannonImage.getIconHeight() / 2);
        atCannon.translate(cannonX, cannonY);

        // Draw the rotated cannon
        g2d.drawImage(cannonImage.getImage(), atCannon, this);

        // Set the color to white for the bullets
        g2d.setColor(Color.WHITE);

        // Draw bullets
        for (Bullet bullet : tank.getBullets()) {
            g2d.setColor(Color.WHITE);
            g2d.fillOval((int) bullet.getX(), (int) bullet.getY(), GameConstants.BULLET_SIZE, GameConstants.BULLET_SIZE);
            g2d.setColor(Color.RED);
            g2d.draw(bullet.getHitbox());
        }

        // Draw the hitbox for debugging
        g2d.setColor(Color.RED);
        g2d.draw(tank.getHitbox());

        g2d.dispose();
    }
}
