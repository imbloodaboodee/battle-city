package jsd.project.tank90.entities;

import jsd.project.tank90.constants.GameConstants;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;

public class SmartTank extends Tank {
    private Timer bulletTimerCountdown;

    public SmartTank(int x, int y, int health, int speed, BulletType bulletType, ImageIcon baseImage, ImageIcon cannonImage) {
        super(x, y, health, speed);
        setRotationSpeed(GameConstants.ENEMY_TANK_ROTATION_SPEED);
        setBaseImage(resizeImageIcon(baseImage, 0.9));
        setCannonImage(resizeImageIcon(cannonImage, 0.9));
        setDefaultBullet(new Bullet(bulletType));

        setHitbox(new Rectangle(getX(), getY(), getBaseImage().getIconWidth(), getBaseImage().getIconHeight()));

    }

    private ImageIcon resizeImageIcon(ImageIcon icon, double ratio) {
        int originalWidth = icon.getIconWidth();
        int originalHeight = icon.getIconHeight();

        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);

        Image resizedImage = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        return new ImageIcon(resizedImage);
    }

    @Override
    public void shoot(ImageIcon baseImage) {
        if (isFrozen()) {
            System.out.println("SmartTank is frozen, skipping bullet creation.");
            return;
        }
        if (isCanFire()) {
            int cannonTipX = (int) (getX() + baseImage.getIconWidth() / 2 + Math.cos(getCannonAngle() - Math.PI / 2) * getCannonImage().getIconHeight() / 2);
            int cannonTipY = (int) (getY() + baseImage.getIconHeight() / 2 + Math.sin(getCannonAngle() - Math.PI / 2) * getCannonImage().getIconHeight() / 2);

            Bullet bullet = new Bullet(cannonTipX- getDefaultBullet().getBulletImage().getIconWidth()/2, cannonTipY- getDefaultBullet().getBulletImage().getIconHeight()/2, getDefaultBullet().getBulletType(), getCannonAngle() - Math.PI / 2);
            getBullets().add(bullet);

            setCanFire(false);
            bulletTimerCountdown = new Timer(getDefaultBullet().getCooldown() + 100, e -> {
                setCanFire(true);
                bulletTimerCountdown.stop();
            });
            bulletTimerCountdown.start();
        }
    }

}
