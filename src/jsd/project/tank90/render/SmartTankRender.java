
package jsd.project.tank90.render;

import jsd.project.tank90.constants.GameConstants;
import jsd.project.tank90.entities.Bullet;
import jsd.project.tank90.entities.PlayerTank;
import jsd.project.tank90.entities.SmartTank;
import jsd.project.tank90.physics.CollisionHandling;

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
        baseImage = smartTank.getBaseImage();
        cannonImage = smartTank.getCannonImage();

        this.setSize(800, 800);

    }

    public void updateCannonAngle() {
        PlayerTank playerTank = GameScreen.getInstance().getPlayerTankRender().getPlayerTank();

        int playerCenterX = playerTank.getX() + playerTank.getHitbox().width / 2;
        int playerCenterY = playerTank.getY() + playerTank.getHitbox().height / 2;
        int cannonCenterX = smartTank.getX() + baseImage.getIconWidth() / 2;
        int cannonCenterY = smartTank.getY() + baseImage.getIconHeight() / 2;

        double distanceToPlayer = Math.sqrt(
                Math.pow(playerCenterX - cannonCenterX, 2) + Math.pow(playerCenterY - cannonCenterY, 2)
        );

        double distanceToBase = Math.sqrt(
                Math.pow(GameConstants.BASE_COORDINATE_X - cannonCenterX, 2) + Math.pow(GameConstants.BASE_COORDINATE_Y - cannonCenterY, 2)
        );

        int targetX, targetY;
        if (distanceToPlayer < distanceToBase && !CollisionHandling.isPlayerTankInTree()) {
            targetX = playerCenterX;
            targetY = playerCenterY;
        } else {
            targetX = (int) GameConstants.BASE_COORDINATE_X;
            targetY = (int) GameConstants.BASE_COORDINATE_Y;
        }

        double targetAngle = Math.atan2(targetY - cannonCenterY, targetX - cannonCenterX) + (Math.PI / 2);

        double angleDifference = targetAngle - smartTank.getCannonAngle();
        angleDifference = (angleDifference + Math.PI) % (2 * Math.PI) - Math.PI;

        if (Math.abs(angleDifference) > smartTank.getRotationSpeed()) {
            if (angleDifference > 0) {
                smartTank.setCannonAngle(smartTank.getCannonAngle() + smartTank.getRotationSpeed());
            } else {
                smartTank.setCannonAngle(smartTank.getCannonAngle() - smartTank.getRotationSpeed());
            }
        } else {
            smartTank.setCannonAngle(targetAngle);
        }
        smartTank.setCannonAngle((smartTank.getCannonAngle() + Math.PI) % (2 * Math.PI) - Math.PI);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        updateCannonAngle();
        int tankCenterX = smartTank.getX() + baseImage.getIconWidth() / 2;
        int tankCenterY = smartTank.getY() + baseImage.getIconHeight() / 2;
        AffineTransform atTank = AffineTransform.getRotateInstance(smartTank.getTankAngle(), tankCenterX, tankCenterY);
        atTank.translate(smartTank.getX(), smartTank.getY());

        g2d.drawImage(baseImage.getImage(), atTank, this);

        int cannonX = tankCenterX - cannonImage.getIconWidth() / 2;
        int cannonY = tankCenterY - cannonImage.getIconHeight() / 2;
        AffineTransform atCannon = AffineTransform.getRotateInstance(smartTank.getCannonAngle(), cannonX + cannonImage.getIconWidth() / 2, cannonY + cannonImage.getIconHeight() / 2);
        atCannon.translate(cannonX, cannonY);

        g2d.drawImage(cannonImage.getImage(), atCannon, this);

        g2d.setColor(Color.WHITE);

        for (Bullet bullet : smartTank.getBullets()) {
            ImageIcon bulletImage = bullet.getBulletImage();
            int bulletX = (int) bullet.getX();
            int bulletY = (int) bullet.getY();

            double bulletAngle = bullet.getAngle()+Math.PI/2;

            AffineTransform atBullet = AffineTransform.getRotateInstance(bulletAngle, bulletX + bulletImage.getImage().getWidth(null) / 2, bulletY + bulletImage.getImage().getHeight(null) / 2);
            atBullet.translate(bulletX, bulletY);

            g2d.drawImage(bulletImage.getImage(), atBullet, this);
        }

        g2d.setColor(Color.RED);
        g2d.draw(smartTank.getHitbox());

        g2d.dispose();
    }

    public SmartTank getSmartTank() {
        return smartTank;
    }
}
