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
    private boolean isExplosive = false;
    private int ricochetLeft = 0;
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
            case NORMAL:
                this.damage = 3;
                this.cooldown = 1000;  //1  100
                this.speed = 10;    //10 1
                setBulletImage(resizeImageIcon(new ImageIcon("./src/jsd/project/tank90/assets/image/bullet.png"), 1.2));
                break;
            case EXPLOSIVE:
                this.damage = 5;
                this.cooldown = 1000;
                this.speed = 2;
                setBulletImage(resizeImageIcon(new ImageIcon("./src/jsd/project/tank90/assets/image/bullet.png"), 1.2));
                break;
            case RAPID:
                this.damage = 1;
                this.cooldown = 1;
                this.speed = 10;
                setBulletImage(resizeImageIcon(new ImageIcon("./src/jsd/project/tank90/assets/image/bullet.png"), 1.2));
                break;
        }
    }

    public void updatePosition() {
        x += speed * Math.cos(angle);
        y += speed * Math.sin(angle);
        hitbox.setRect(x, y, hitbox.getWidth(), hitbox.getHeight());
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

    public boolean isExplosive() {
        return isExplosive;
    }

    public void setExplosive(boolean explosive) {
        isExplosive = explosive;
    }

    public int getRicochetLeft() {
        return ricochetLeft;
    }

    public void setRicochetLeft(int ricochetLeft) {
        this.ricochetLeft = ricochetLeft;
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
    public boolean isOutOfBounds(int width, int height) {
        return x < 0 || x > width || y < 0 || y > height;
    }

    public ImageIcon getBulletImage() {
        return bulletImage;
    }

    public void setBulletImage(ImageIcon bulletImage) {
        this.bulletImage = bulletImage;
    }
}
