package entities;

import constants.GameConstants;
import manager.BulletManager;
import physics.SoundUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PlayerTank extends Tank implements KeyListener {
    // Image variables
    private ImageIcon cannonImage;
    private ImageIcon baseImage;
    private ImageIcon aimImage;

    // Timers
    private Timer bulletCreationTimer;
    private Timer bulletTimerCountdown;
    private Timer gameLoopTimer;

    // Attributes
    private Bullet defaultBullet;
    private double cannonAngle = 0;
    private boolean canFire = true;
    public static int lives = GameConstants.INITIAL_LIVES; // Số mạng của tank
    private double targetCannonAngle = 0; // The target angle to rotate to
    private final double ROTATION_SPEED = GameConstants.PLAYER_ROTATION_SPEED; // Speed at which the cannon rotates

    private BulletManager bulletManager;

    public PlayerTank(BulletType bulletType) {
        super();  // Call Tank's constructor
        this.defaultBullet = new Bullet(bulletType);
        initializeCommonResources();
    }

    private void initializeCommonResources() {
        bulletManager = new BulletManager(getBullets());

        // Initialize tank images
        baseImage = new ImageIcon(GameConstants.BASE_IMAGE);
        cannonImage = new ImageIcon(GameConstants.CANNON_IMAGE);
        aimImage = new ImageIcon(GameConstants.AIM_IMAGE);

        // Initialize the hitbox (using inherited x and y coordinates)
        setHitbox(new Rectangle(getX(), getY(), baseImage.getIconWidth(), baseImage.getIconHeight()));

        // Timer for creating bullets, starts when firing
        bulletCreationTimer = new Timer(GameConstants.DELAY, e -> shoot());
        bulletCreationTimer.setRepeats(true);

        // Timer for updating entities that need constant updating
        gameLoopTimer = new Timer(GameConstants.DELAY, e -> {
            bulletManager.updateBullets();
        });
        gameLoopTimer.start();
    }

    public void mousePressed(MouseEvent e) {
        System.out.println("start");
        bulletCreationTimer.start(); // Start firing bullets
    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("stop");
        bulletCreationTimer.stop(); // Stop firing bullets
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_W -> setMovingUp(true);
            case KeyEvent.VK_S -> setMovingDown(true);
            case KeyEvent.VK_A -> setMovingLeft(true);
            case KeyEvent.VK_D -> setMovingRight(true);
        }
        updateTankPosition();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_W -> setMovingUp(false);
            case KeyEvent.VK_S -> setMovingDown(false);
            case KeyEvent.VK_A -> setMovingLeft(false);
            case KeyEvent.VK_D -> setMovingRight(false);
        }
    }


    // Create (fire) a bullet
    private void shoot() {
        if (canFire) {
            // Calculate the starting point of the bullet (at the cannon tip)
            int cannonTipX = (int) (getX() + baseImage.getIconWidth() / 2 + Math.cos(cannonAngle - Math.PI / 2) * cannonImage.getIconHeight() / 2);
            int cannonTipY = (int) (getY() + baseImage.getIconHeight() / 2 + Math.sin(cannonAngle - Math.PI / 2) * cannonImage.getIconHeight() / 2);

            // Create the bullet with the current cannon angle
            Bullet bullet = new Bullet(cannonTipX- GameConstants.BULLET_SIZE / 2, cannonTipY- GameConstants.BULLET_SIZE / 2, defaultBullet.getBulletType(), cannonAngle - Math.PI / 2);
            getBullets().add(bullet);

            // Mechanism for handling firing cooldown
            canFire = false;
            bulletTimerCountdown = new Timer(defaultBullet.getCooldown(), e -> {
                canFire = true;
                bulletTimerCountdown.stop();
            });
            bulletTimerCountdown.start();
            SoundUtility.fireSound();
        }
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

    public static int getLives() {
        return lives;
    }

    public void resetPosition() {
        setX(176);
        setY(400);
    }
}
