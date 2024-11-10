
package jsd.project.tank90.render;

import constants.GameConstants;
import entities.Bullet;
import entities.PlayerTank;
import entities.SmartTank;
import physics.CollisionHandling;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class SmartTankRender extends JLabel {
    private SmartTank smartTank;
    private ImageIcon cannonImage;
    private ImageIcon baseImage;
    private Component c;

    public SmartTankRender(SmartTank smartTank, Component c) {
        this.smartTank = smartTank;
        this.c = c;
        // Load images
        baseImage = smartTank.getBaseImage();
        cannonImage = smartTank.getCannonImage();

        this.setSize(800, 800);

    }

    public void updateCannonAngle() {
        PlayerTank playerTank = GameScreen.getInstance().getPlayerTankRender().getPlayerTank();

        // Calculate the center coordinates for player, base, and cannon
        int playerCenterX = playerTank.getX() + playerTank.getHitbox().width / 2;
        int playerCenterY = playerTank.getY() + playerTank.getHitbox().height / 2;
        int cannonCenterX = smartTank.getX() + baseImage.getIconWidth() / 2;
        int cannonCenterY = smartTank.getY() + baseImage.getIconHeight() / 2;

        // Calculate distance to player
        double distanceToPlayer = Math.sqrt(
                Math.pow(playerCenterX - cannonCenterX, 2) + Math.pow(playerCenterY - cannonCenterY, 2)
        );

        // Calculate distance to base
        double distanceToBase = Math.sqrt(
                Math.pow(GameConstants.BASE_COORDINATE_X - cannonCenterX, 2) + Math.pow(GameConstants.BASE_COORDINATE_Y - cannonCenterY, 2)
        );

        // Determine the target coordinates based on the closer distance
        int targetX, targetY;
        if (distanceToPlayer < distanceToBase && !CollisionHandling.isPlayerTankInTree()) {
            // Target player if closer
            targetX = playerCenterX;
            targetY = playerCenterY;
        } else {
            // Target base if closer
            targetX = (int) GameConstants.BASE_COORDINATE_X;
            targetY = (int) GameConstants.BASE_COORDINATE_Y;
        }

        // Calculate the target angle to the chosen target
        double targetAngle = Math.atan2(targetY - cannonCenterY, targetX - cannonCenterX) + (Math.PI / 2);

        // Normalize angle difference to be between -PI and PI
        double angleDifference = targetAngle - smartTank.getCannonAngle();
        angleDifference = (angleDifference + Math.PI) % (2 * Math.PI) - Math.PI;

        // Rotate the cannon towards the target angle gradually
        if (Math.abs(angleDifference) > GameConstants.ENEMY_TANK_ROTATION_SPEED) {
            if (angleDifference > 0) {
                smartTank.setCannonAngle(smartTank.getCannonAngle() + GameConstants.ENEMY_TANK_ROTATION_SPEED);
            } else {
                smartTank.setCannonAngle(smartTank.getCannonAngle() - GameConstants.ENEMY_TANK_ROTATION_SPEED);
            }
        } else {
            smartTank.setCannonAngle(targetAngle);
        }

        // Normalize cannon angle to be between -PI and PI
        smartTank.setCannonAngle((smartTank.getCannonAngle() + Math.PI) % (2 * Math.PI) - Math.PI);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        updateCannonAngle();
        // Rotate the tank base image according to the tankAngle
        int tankCenterX = smartTank.getX() + baseImage.getIconWidth() / 2;
        int tankCenterY = smartTank.getY() + baseImage.getIconHeight() / 2;
        AffineTransform atTank = AffineTransform.getRotateInstance(smartTank.getTankAngle(), tankCenterX, tankCenterY);
        atTank.translate(smartTank.getX(), smartTank.getY());

        // Draw the rotated tank base
        g2d.drawImage(baseImage.getImage(), atTank, this);

        // Rotate the cannon
        int cannonX = tankCenterX - cannonImage.getIconWidth() / 2;
        int cannonY = tankCenterY - cannonImage.getIconHeight() / 2;
        AffineTransform atCannon = AffineTransform.getRotateInstance(smartTank.getCannonAngle(), cannonX + cannonImage.getIconWidth() / 2, cannonY + cannonImage.getIconHeight() / 2);
        atCannon.translate(cannonX, cannonY);

        // Draw the rotated cannon
        g2d.drawImage(cannonImage.getImage(), atCannon, this);

        // Set the color to white for the bullets
        g2d.setColor(Color.WHITE);

        // Draw bullets
        for (Bullet bullet : smartTank.getBullets()) {
            ImageIcon bulletImage = bullet.getBulletImage();
            int bulletX = (int) bullet.getX();
            int bulletY = (int) bullet.getY();

            // Calculate bullet rotation angle based on its direction
            double bulletAngle = bullet.getAngle()+Math.PI/2;

            // Center bullet at its current position and apply rotation
            AffineTransform atBullet = AffineTransform.getRotateInstance(bulletAngle, bulletX + bulletImage.getImage().getWidth(null) / 2, bulletY + bulletImage.getImage().getHeight(null) / 2);
            atBullet.translate(bulletX, bulletY);

            // Draw the rotated bullet image
            g2d.drawImage(bulletImage.getImage(), atBullet, this);
        }

        // Draw the hitbox for debugging
        g2d.setColor(Color.RED);
        g2d.draw(smartTank.getHitbox());

        g2d.dispose();
    }

    public SmartTank getSmartTank() {
        return smartTank;
    }
}
