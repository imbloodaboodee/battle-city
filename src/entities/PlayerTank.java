package entities;

import constants.GameConstants;
import manager.BulletManager;
import physics.SoundUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PlayerTank extends Tank implements KeyListener {
    // Image variables
    private ImageIcon aimImage;

    // Timers
    private Timer bulletCreationTimer;
    private Timer bulletTimerCountdown;

    // Attributes
    private static int lives = GameConstants.INITIAL_LIVES; // Số mạng của tank
    private double targetCannonAngle = 0; // The target angle to rotate to


    public PlayerTank(BulletType bulletType) {
        super();  // Call Tank's constructor
        setDefaultBullet(new Bullet(bulletType));
        // Initialize tank images
        setBaseImage(new ImageIcon(GameConstants.BASE_IMAGE));
        setCannonImage(new ImageIcon(GameConstants.CANNON_IMAGE));
        aimImage = new ImageIcon(GameConstants.AIM_IMAGE);


        // Initialize the hitbox (using inherited x and y coordinates)
        setHitbox(new Rectangle(getX(), getY(), getBaseImage().getIconWidth(), getBaseImage().getIconHeight()));

        // Timer for creating bullets, starts when firing
        bulletCreationTimer = new Timer(GameConstants.DELAY, e -> shoot());
        bulletCreationTimer.setRepeats(true);

        // Timer for updating entities that need constant updating
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
    public void shoot() {
        if (isFrozen()) {
            System.out.println("SmartTank is frozen, skipping bullet creation.");
            return; // Dừng bắn nếu bị đóng băng
        }
        if (isCanFire()) {
            // Random firing logic or based on some condition
            int cannonTipX = (int) (getX() + getBaseImage().getIconWidth() / 2 + Math.cos(getCannonAngle() - Math.PI / 2) * getCannonImage().getIconHeight() / 2);
            int cannonTipY = (int) (getY() + getBaseImage().getIconHeight() / 2 + Math.sin(getCannonAngle() - Math.PI / 2) * getCannonImage().getIconHeight() / 2);

            Bullet bullet = new Bullet(cannonTipX - GameConstants.BULLET_SIZE / 2, cannonTipY - GameConstants.BULLET_SIZE / 2, getDefaultBullet().getBulletType(), getCannonAngle() - Math.PI / 2);
            getBullets().add(bullet);

            setCanFire(false);
            bulletTimerCountdown = new Timer(getDefaultBullet().getCooldown(), e -> {
                setCanFire(true);
                bulletTimerCountdown.stop();
            });
            bulletTimerCountdown.start();
            SoundUtility.fireSound();
        }
    }

    // Check if health is zero and manage lives
    private void checkHealth() {
        if (getHealth() <= 0) {
            lives--;
            if (lives > 0) {
                resetPosition();
                setHealth(GameConstants.PLAYER_MAX_HEALTH); // Reset health
            } else {
                System.out.println("Game Over");
            }
        }
    }

    public double getTargetCannonAngle() {
        return targetCannonAngle;
    }

    public void setTargetCannonAngle(double targetCannonAngle) {
        this.targetCannonAngle = targetCannonAngle;
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
