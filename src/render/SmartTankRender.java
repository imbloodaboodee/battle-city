
package render;

import constants.GameConstants;
import entities.Bullet;
import entities.PlayerTank;
import entities.SmartTank;

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

    private void updateCannonAngle() {
        PlayerTank playerTank = GameScreen.getInstance().getPlayerTankRender().getPlayerTank();
        // Calculate the angle toward the player tank
        int playerCenterX = playerTank.getX() + playerTank.getHitbox().width / 2;
        int playerCenterY = playerTank.getY() + playerTank.getHitbox().height / 2;
        int cannonCenterX = smartTank.getX() + baseImage.getIconWidth() / 2;
        int cannonCenterY = smartTank.getY() + baseImage.getIconHeight() / 2;

        // Calculate the target angle to the player
        double targetAngle = Math.atan2(playerCenterY - cannonCenterY, playerCenterX - cannonCenterX) + (Math.PI / 2);

        // Normalize angle difference to be between -PI and PI
        double angleDifference = targetAngle - smartTank.getCannonAngle();
        angleDifference = (angleDifference + Math.PI) % (2 * Math.PI) - Math.PI;

        // Rotate the cannon towards the target angle gradually
        if (Math.abs(angleDifference) > GameConstants.ENEMY_TANK_ROTATION_SPEED) {
            if (angleDifference > 0) {
                smartTank.setCannonAngle(smartTank.getCannonAngle()+GameConstants.ENEMY_TANK_ROTATION_SPEED);
            } else {
                smartTank.setCannonAngle(smartTank.getCannonAngle()-GameConstants.ENEMY_TANK_ROTATION_SPEED);

            }
        } else {
            smartTank.setCannonAngle(targetAngle);
        }
        smartTank.setCannonAngle((smartTank.getCannonAngle()+ Math.PI) % (2 * Math.PI) - Math.PI);
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
            g2d.setColor(Color.WHITE);
            g2d.fillOval((int) bullet.getX(), (int) bullet.getY(), GameConstants.BULLET_SIZE, GameConstants.BULLET_SIZE);
            g2d.setColor(Color.RED);
            g2d.draw(bullet.getHitbox());
        }

        // Draw the hitbox for debugging
        g2d.setColor(Color.RED);
        g2d.draw(smartTank.getHitbox());

        g2d.dispose();
    }

}
