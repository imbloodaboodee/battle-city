package entities;

import SpriteClasses.Block;
import constants.GameConstants;
import environment.BlockType;
import render.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.Iterator;

import static physics.CollisionHandling.checkCollisionBulletsBlocks;

public class PlayerTank {
    //Images variables
    private ImageIcon cannonImage;
    private ImageIcon baseImage;
    private ImageIcon aimImage;

    //Timers
    private Timer bulletCreationTimer;
    private Timer bulletTimerCountdown;
    private Timer gameLoopTimer;

    //Attributes
    private Tank tank;
    private Bullet defaultBullet;
    private double cannonAngle = 0;
    private boolean canFire = true;

    private double targetCannonAngle = 0; // The target angle to rotate to
    private final double ROTATION_SPEED = 1; // Speed at which the cannon rotates

    public PlayerTank(Tank tank, BulletType bulletType) {
        this.tank = tank;
        this.defaultBullet = new Bullet(bulletType);


        //Initialize tank images
        baseImage = new ImageIcon("./src/assets/image/tank.png");
        cannonImage = new ImageIcon("./src/assets/image/cannon.png");
        aimImage = new ImageIcon("./src/assets/image/aim.png");

        // Initialize the hitbox (assuming the tank has a known width and height)
        tank.setHitbox(new Rectangle(tank.getX(), tank.getY(), baseImage.getIconWidth(), baseImage.getIconHeight()));

        //Mouse listeners




        //Timer for creating bullets, starts when firing
        bulletCreationTimer = new Timer(GameConstants.DELAY, e -> createBullet());
        bulletCreationTimer.setRepeats(true);

        //Timer for updating entities that need constant updating
        gameLoopTimer = new Timer(GameConstants.DELAY, e -> {
            tank.updateTankPosition();
            updateBullets();
        });
        gameLoopTimer.start();

        //Set and request focus on the panel


    }
    public void mousePressed(MouseEvent e) {
        System.out.println("start");
        bulletCreationTimer.start(); // Start firing bullets

    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("stop");
        bulletCreationTimer.stop(); // Stop firing bullets

    }

    public void keyPressed(KeyEvent e) {
        tank.keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        tank.keyReleased(e);
    }

    private void updateTankPosition() {
        int oldX = tank.getX();
        int oldY = tank.getY();
        tank.updateTankPosition();

    }

    //Update cannon according to mouse position
    //Create (fire) a bullet
    private void createBullet() {
        if (canFire) {
            // Calculate the starting point of the bullet (at the cannon tip)
            int cannonTipX = (int) (tank.getX() + baseImage.getIconWidth() / 2 + Math.cos(cannonAngle - Math.PI / 2) * cannonImage.getIconHeight() / 2);
            int cannonTipY = (int) (tank.getY() + baseImage.getIconHeight() / 2 + Math.sin(cannonAngle - Math.PI / 2) * cannonImage.getIconHeight() / 2);

            // Create the bullet with the current cannon angle
            Bullet bullet = new Bullet(cannonTipX, cannonTipY, defaultBullet.getBulletType(), cannonAngle - Math.PI / 2);
            tank.getBullets().add(bullet);

            // Mechanism for handling firing cooldown
            canFire = false;
            bulletTimerCountdown = new Timer(defaultBullet.getCooldown(), e -> {
                canFire = true;
                bulletTimerCountdown.stop();
            });
            bulletTimerCountdown.start();
        }
    }

    //Update the bullet's position, remove the bullet once it goes out of bounds
    private void updateBullets() {
        // Use an iterator to safely remove bullets while iterating
        Iterator<Bullet> bulletIterator = tank.getBullets().iterator();

        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.updatePosition();

            // Check if the bullet goes out of bounds
            if (bullet.getX() < 0 || bullet.getX() > GameConstants.FRAME_WIDTH || bullet.getY() < 0 || bullet.getY() > GameConstants.FRAME_HEIGHT) {
                bulletIterator.remove(); // Safely remove the bullet
            }
        }
        checkCollisionBulletsBlocks(tank.getBullets(), GameScreen.blocks);
    }


    public Tank getTank() {
        return tank;
    }

    public double getCannonAngle() {
        return cannonAngle;
    }

    public void setCannonAngle(double cannonAngle) {
        this.cannonAngle = cannonAngle;
    }

    public double getTargetCannonAngle() {
        return targetCannonAngle;
    }

    public void setTargetCannonAngle(double targetCannonAngle) {
        this.targetCannonAngle = targetCannonAngle;
    }

    public double getROTATION_SPEED() {
        return ROTATION_SPEED;
    }

    public ImageIcon getCannonImage() {
        return cannonImage;
    }

    public ImageIcon getBaseImage() {
        return baseImage;
    }

    public ImageIcon getAimImage() {
        return aimImage;
    }
}
