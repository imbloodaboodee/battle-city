package jsd.project.tank90.entities;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Bullet {
    private double x, y;
    private double angle; // Direction in radians
    BulletType bulletType;
    private int damage;
    private int speed;
    private int cooldown;
    private Rectangle2D.Double hitbox;
    private ImageIcon bulletImage;

    public Bullet(double x, double y, BulletType bulletType, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.bulletType = bulletType;
        bulletAttributeSetter(bulletType);
        this.setHitbox(new Rectangle2D.Double(x, y, getBulletImage().getIconHeight(), getBulletImage().getIconWidth()));
    }

    public Bullet(BulletType bulletType) {
        this.bulletType = bulletType;
        bulletAttributeSetter(bulletType);
    }

    private void bulletAttributeSetter(BulletType bulletType) {
        switch (bulletType) {
            case STANDARD:
                this.damage = 3;
                this.cooldown = 1000;
                this.speed = 10;
                setBulletImage(resizeImageIcon(new ImageIcon("./src/jsd/project/tank90/assets/image/bullet.png"), 1.2));
                break;
            case STANDARD_TIER_2:
                this.damage = 1;
                this.cooldown = 500;
                this.speed = 10;
                setBulletImage(new ImageIcon("./src/jsd/project/tank90/assets/image/bullet.png"));
                break;
            case STANDARD_TIER_3, STANDARD_TIER_4:
                this.damage = 2;
                this.cooldown = 500;
                this.speed = 15;
                setBulletImage(new ImageIcon("./src/jsd/project/tank90/assets/image/bullet.png"));
                break;
            case RAPID:
                this.damage = 1;
                this.cooldown = 1;
                this.speed = 10;
                setBulletImage(resizeImageIcon(new ImageIcon("./src/jsd/project/tank90/assets/image/bullet_rapid.png"), 1.2));
                break;
            case RAPID_TIER_2:
                this.damage = 2;
                this.cooldown=1;
                this.speed=10;
                setBulletImage(resizeImageIcon(new ImageIcon("./src/jsd/project/tank90/assets/image/bullet_rapid.png"), 1.2));
                break;
            case RAPID_TIER_3:
                this.damage = 2;
                this.cooldown=1;
                this.speed=10;
                setBulletImage(resizeImageIcon(new ImageIcon("./src/jsd/project/tank90/assets/image/bullet_rapid.png"), 1.2));
                break;
            case RAPID_TIER_4:
                this.damage = 2;
                this.cooldown=1;
                this.speed=10;
                setBulletImage(resizeImageIcon(new ImageIcon("./src/jsd/project/tank90/assets/image/bullet_rapid.png"), 1.2));
                break;

            case EXPLOSIVE:
                this.damage = 5;
                this.cooldown = 1000;
                this.speed = 2;
                setBulletImage(resizeImageIcon(new ImageIcon("./src/jsd/project/tank90/assets/image/bullet_explosive.png"), 1.2));
                break;
            case EXPLOSIVE_TIER_2:
                this.damage = 5;
                this.cooldown = 1000;
                this.speed = 2;
                setBulletImage(resizeImageIcon(new ImageIcon("./src/jsd/project/tank90/assets/image/bullet_explosive.png"), 1.2));
                break;
            case EXPLOSIVE_TIER_3:
                this.damage = 5;
                this.cooldown = 1000;
                this.speed = 2;
                setBulletImage(resizeImageIcon(new ImageIcon("./src/jsd/project/tank90/assets/image/bullet_explosive.png"), 1.2));
                break;
            case EXPLOSIVE_TIER_4:
                this.damage = 5;
                this.cooldown = 1000;
                this.speed = 2;
                setBulletImage(resizeImageIcon(new ImageIcon("./src/jsd/project/tank90/assets/image/bullet_explosive.png"), 1.2));
                break;

        }
    }

    public void updatePosition() {
        x += speed * Math.cos(angle);
        y += speed * Math.sin(angle);
        hitbox.setRect(x, y, hitbox.getWidth(), hitbox.getHeight());
    }
    private ImageIcon resizeImageIcon(ImageIcon icon, double ratio) {
        int originalWidth = icon.getIconWidth();
        int originalHeight = icon.getIconHeight();
        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);
        Image resizedImage = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    public void setBulletType(BulletType bulletType) {
        this.bulletType = bulletType;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getDamage() {
        return damage;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public Rectangle2D.Double getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle2D.Double hitbox) {
        this.hitbox = hitbox;
    }

    public ImageIcon getBulletImage() {
        return bulletImage;
    }

    public void setBulletImage(ImageIcon bulletImage) {
        this.bulletImage = bulletImage;
    }
}
