
package render;

import constants.GameConstants;
import entities.Bullet;
import entities.DumbTank;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class DumbTankRender extends JComponent {
    private DumbTank dumbTank;
    private ImageIcon baseImage;
    private Component c;

    public DumbTankRender(DumbTank dumbTank, Component c) {
        this.dumbTank = dumbTank;
        this.c = c;

        // Load images
        baseImage = dumbTank.getBaseImage();

        this.setSize(800, 800);
        System.out.println("bbbbb");

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        // Rotate the tank base image according to the tankAngle
        int tankCenterX = dumbTank.getX() + baseImage.getIconWidth() / 2;
        int tankCenterY = dumbTank.getY() + baseImage.getIconHeight() / 2;
        AffineTransform atTank = AffineTransform.getRotateInstance(dumbTank.getTankAngle(), tankCenterX, tankCenterY);
        atTank.translate(dumbTank.getX(), dumbTank.getY());

        // Draw the rotated tank base
        g2d.drawImage(baseImage.getImage(), atTank, this);

        // Set the color to white for the bullets
        g2d.setColor(Color.WHITE);

        // Draw bullets
        for (Bullet bullet : dumbTank.getBullets()) {
            g2d.setColor(Color.WHITE);
            g2d.fillOval((int) bullet.getX(), (int) bullet.getY(), GameConstants.BULLET_SIZE, GameConstants.BULLET_SIZE);
            g2d.setColor(Color.RED);
            g2d.draw(bullet.getHitbox());
        }

        // Draw the hitbox for debugging
        g2d.setColor(Color.RED);
        g2d.draw(dumbTank.getHitbox());

        g2d.dispose();
    }

}
