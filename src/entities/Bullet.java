package entities;

import constants.GameConstants;

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

    public Bullet(double x, double y, BulletType bulletType, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.bulletType = bulletType;
        bulletAttributeSetter(bulletType);
        this.setHitbox(new Rectangle2D.Double(x, y, GameConstants.BULLET_SIZE, GameConstants.BULLET_SIZE));

    }

    public Bullet(BulletType bulletType) {
        this.bulletType = bulletType;
        bulletAttributeSetter(bulletType);

    }

    private void bulletAttributeSetter(BulletType bulletType) {
        switch (bulletType) {
            case NORMAL:
                this.damage = 3;
                this.cooldown = 500;  //1  100
                this.speed = 10;    //10 1
                break;
            case EXPLOSIVE:
                this.damage = 5;
                this.cooldown = 1000;
                this.speed = 2;
                this.isExplosive = true;
                break;
            case RICOCHET:
                this.damage = 2;
                this.cooldown = 350;
                this.speed = 3;
                this.ricochetLeft = 2;
                break;
            case RAPID:
                this.damage = 1;
                this.cooldown = 100;
                this.speed = 5;
                break;
        }
    }

    public void updatePosition() {
        x += speed * Math.cos(angle);
        y += speed * Math.sin(angle);
        hitbox.setRect(x, y, hitbox.getWidth(), hitbox.getHeight());
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

}
