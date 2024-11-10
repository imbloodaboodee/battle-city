
package jsd.project.tank90.render;

import jsd.project.tank90.entities.Bullet;
import jsd.project.tank90.entities.PlayerTank;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class PlayerTankRender extends JLabel {
    private PlayerTank playerTank;
    private ImageIcon cannonImage;
    private ImageIcon baseImage;
    private ImageIcon aimImage;
    private Component c;

    public PlayerTankRender(PlayerTank playerTank, Component c) {
        this.playerTank = playerTank;
        this.c = c;
        baseImage = playerTank.getBaseImage();
        cannonImage = playerTank.getCannonImage();
        aimImage = playerTank.getAimImage();

        this.setSize(800, 800);

    }

    private void updateCannonAngle() {
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mousePosition, c);
        int cannonCenterX = playerTank.getX() + baseImage.getIconWidth() / 2;
        int cannonCenterY = playerTank.getY() + baseImage.getIconHeight() / 2;
        playerTank.setTargetCannonAngle(Math.atan2(mousePosition.y - cannonCenterY, mousePosition.x - cannonCenterX) + (Math.PI / 2));

        double angleDifference = playerTank.getTargetCannonAngle() - playerTank.getCannonAngle();

        angleDifference = (angleDifference + Math.PI) % (2 * Math.PI) - Math.PI;

        if (Math.abs(angleDifference) > playerTank.getRotationSpeed()) {
            if (angleDifference > 0) {
                playerTank.setCannonAngle(playerTank.getCannonAngle() + playerTank.getRotationSpeed());
            } else {
                playerTank.setCannonAngle(playerTank.getCannonAngle() - playerTank.getRotationSpeed());
            }

            playerTank.setCannonAngle((playerTank.getCannonAngle() + Math.PI) % (2 * Math.PI) - Math.PI);
        } else {
            playerTank.setCannonAngle(playerTank.getTargetCannonAngle());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        updateCannonAngle();
        int tankCenterX = playerTank.getX() + baseImage.getIconWidth() / 2;
        int tankCenterY = playerTank.getY() + baseImage.getIconHeight() / 2;

        AffineTransform atTank = AffineTransform.getRotateInstance(playerTank.getTankAngle(), tankCenterX, tankCenterY);
        atTank.translate(playerTank.getX(), playerTank.getY());
        g2d.drawImage(baseImage.getImage(), atTank, this);

        int cannonX = tankCenterX - cannonImage.getIconWidth() / 2;
        int cannonY = tankCenterY - cannonImage.getIconHeight() / 2;
        AffineTransform atCannon = AffineTransform.getRotateInstance(playerTank.getCannonAngle(), cannonX + cannonImage.getIconWidth() / 2, cannonY + cannonImage.getIconHeight() / 2);
        atCannon.translate(cannonX, cannonY);
        g2d.drawImage(cannonImage.getImage(), atCannon, this);

        AffineTransform atAim = AffineTransform.getRotateInstance(playerTank.getCannonAngle(), tankCenterX, tankCenterY);
        atAim.translate(tankCenterX - aimImage.getIconWidth() / 2, tankCenterY - aimImage.getIconHeight() - cannonImage.getIconHeight() / 2);

        g2d.drawImage(aimImage.getImage(), atAim, this);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2d.setColor(Color.red);
        for (Bullet bullet : playerTank.getBullets()) {
            ImageIcon bulletImage = bullet.getBulletImage();
            int bulletX = (int) bullet.getX();
            int bulletY = (int) bullet.getY();

            double bulletAngle = bullet.getAngle() + Math.PI / 2;

            AffineTransform atBullet = AffineTransform.getRotateInstance(bulletAngle, bulletX + bulletImage.getImage().getWidth(null) / 2, bulletY + bulletImage.getImage().getHeight(null) / 2);
            atBullet.translate(bulletX, bulletY);

            g2d.drawImage(bulletImage.getImage(), atBullet, this);
        }
        g2d.dispose();
    }
    public PlayerTank getPlayerTank() {
        return playerTank;
    }
}
