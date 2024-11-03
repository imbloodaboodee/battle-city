
package render;

import constants.GameConstants;
import entities.Bullet;
import entities.DumbTank;
import entities.PlayerTank;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class DumbTankRender extends JLabel {
    private DumbTank smartTank;
    private ImageIcon cannonImage;
    private ImageIcon baseImage;
    private Component c;

    public DumbTankRender(DumbTank dumbTank, Component c) {
        this.smartTank = dumbTank;
        this.c = c;
        // Load images
        baseImage = dumbTank.getBaseImage();
        cannonImage = dumbTank.getCannonImage();

        this.setSize(800, 800);

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
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
