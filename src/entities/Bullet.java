package entities;

public class Bullet {
    private final int damage;
    private double x, y;
    private int speed;
    private int cooldown;
    private double angle; // Direction in radians

    public Bullet(double x, double y, int damage, int speed, double angle) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.speed = speed;
        this.angle = angle;
    }

    public void updatePosition() {
        x += speed * Math.cos(angle);
        y += speed * Math.sin(angle);
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
}
