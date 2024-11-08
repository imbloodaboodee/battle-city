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
    private Timer bulletTimerCountdown;

    public SmartTank(int x, int y, int health, int speed, BulletType bulletType, ImageIcon baseImage, ImageIcon cannonImage) {
        super(x, y, health, speed);
        setBaseImage(resizeImageIcon(baseImage, 0.9));
        setCannonImage(resizeImageIcon(cannonImage, 0.9));
        setDefaultBullet(new Bullet(bulletType));

        initializeCommonResources();

    }

    private void initializeCommonResources() {
        setHitbox(new Rectangle(getX(), getY(), getBaseImage().getIconWidth(), getBaseImage().getIconHeight()));
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

    @Override
    public void shoot(ImageIcon baseImage) {
        if (isFrozen()) {
            System.out.println("SmartTank is frozen, skipping bullet creation.");
            return; // Dừng bắn nếu bị đóng băng
        }
        if (isCanFire()) {
            // Random firing logic or based on some condition
            int cannonTipX = (int) (getX() + baseImage.getIconWidth() / 2 + Math.cos(getCannonAngle() - Math.PI / 2) * getCannonImage().getIconHeight() / 2);
            int cannonTipY = (int) (getY() + baseImage.getIconHeight() / 2 + Math.sin(getCannonAngle() - Math.PI / 2) * getCannonImage().getIconHeight() / 2);

            Bullet bullet = new Bullet(cannonTipX- GameConstants.BULLET_SIZE / 2, cannonTipY- GameConstants.BULLET_SIZE / 2, getDefaultBullet().getBulletType(), getCannonAngle() - Math.PI / 2);
            getBullets().add(bullet);

            setCanFire(false);
            bulletTimerCountdown = new Timer(getDefaultBullet().getCooldown() + 100, e -> {
                setCanFire(true);
                bulletTimerCountdown.stop();
            });
            bulletTimerCountdown.start();
        }
    }


    // Simulate autonomous movement by changing directions randomly
    // Create (fire) a bullet, similar to player tank, but with its own logic

}
