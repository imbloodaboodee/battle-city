package jsd.project.tank90.entities;

import jsd.project.tank90.constants.GameConstants;
import jsd.project.tank90.physics.SoundUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PlayerTank extends Tank implements KeyListener {
    private ImageIcon aimImage;

    private Timer bulletCreationTimer;
    private Timer bulletTimerCountdown;

    private int assignedHealth;
    public static int lives = GameConstants.INITIAL_LIVES;
    private double targetCannonAngle = 0;

    public PlayerTank(int assignedHealth, int assignedSpeed, int assignedRotationSpeed, BulletType assignedBulletType) {
        super();
        setHealth(getHealth() + assignedHealth * 100);
        setAssignedHealth(assignedHealth);
        setSpeed(getSpeed() + assignedSpeed);
        setRotationSpeed(GameConstants.PLAYER_ROTATION_SPEED);
        setRotationSpeed(getRotationSpeed() + (assignedRotationSpeed / 200));
        setDefaultBullet(new Bullet(assignedBulletType));
        setBaseImage(new ImageIcon(GameConstants.BASE_IMAGE));
        setCannonImage(new ImageIcon(GameConstants.CANNON_IMAGE));
        aimImage = new ImageIcon(GameConstants.AIM_IMAGE);

        setHitbox(new Rectangle(getX(), getY(), getBaseImage().getIconWidth(), getBaseImage().getIconHeight()));

        bulletCreationTimer = new Timer(GameConstants.DELAY, e -> shoot());
        bulletCreationTimer.setRepeats(true);
    }

    public void mousePressed(MouseEvent e) {
        bulletCreationTimer.start();
    }

    public void mouseReleased(MouseEvent e) {
        bulletCreationTimer.stop();
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


    public void shoot() {
        if (isFrozen()) {
            return;
        }
        if (isCanFire()) {
            int cannonTipX = (int) (getX() + getBaseImage().getIconWidth() / 2 + Math.cos(getCannonAngle() - Math.PI / 2) * getCannonImage().getIconHeight() / 2);
            int cannonTipY = (int) (getY() + getBaseImage().getIconHeight() / 2 + Math.sin(getCannonAngle() - Math.PI / 2) * getCannonImage().getIconHeight() / 2);

            Bullet firstBullet = new Bullet(
                    cannonTipX - getDefaultBullet().getBulletImage().getIconWidth() / 2,
                    cannonTipY - getDefaultBullet().getBulletImage().getIconHeight() / 2,
                    getDefaultBullet().getBulletType(),
                    getCannonAngle() - Math.PI / 2
            );
            getBullets().add(firstBullet);

            if (getDefaultBullet().getBulletType() == BulletType.STANDARD_TIER_3 ||
                    getDefaultBullet().getBulletType() == BulletType.STANDARD_TIER_4 ||
                    getDefaultBullet().getBulletType() == BulletType.EXPLOSIVE_TIER_4) {
                Timer dualBulletTimer = new Timer(100, e -> { // 100 ms delay
                    Bullet secondBullet = new Bullet(
                            cannonTipX - getDefaultBullet().getBulletImage().getIconWidth() / 2,
                            cannonTipY - getDefaultBullet().getBulletImage().getIconHeight() / 2,
                            getDefaultBullet().getBulletType(),
                            getCannonAngle() - Math.PI / 2
                    );
                    getBullets().add(secondBullet);
                });
                dualBulletTimer.setRepeats(false);
                dualBulletTimer.start();
            }

            setCanFire(false);
            bulletTimerCountdown = new Timer(getDefaultBullet().getCooldown(), e -> {
                setCanFire(true);
                bulletTimerCountdown.stop();
            });
            bulletTimerCountdown.start();
            SoundUtility.fireSound();
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

    public int getAssignedHealth() {
        return assignedHealth;
    }

    public void setAssignedHealth(int assignedHealth) {
        this.assignedHealth = assignedHealth;
    }
}
