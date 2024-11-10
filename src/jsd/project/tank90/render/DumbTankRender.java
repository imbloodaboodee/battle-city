
package jsd.project.tank90.render;

import jsd.project.tank90.entities.Bullet;
import jsd.project.tank90.entities.DumbTank;

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
        baseImage = dumbTank.getBaseImage();
        this.setSize(800, 800);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        int tankCenterX = dumbTank.getX() + baseImage.getIconWidth() / 2;
        int tankCenterY = dumbTank.getY() + baseImage.getIconHeight() / 2;
        AffineTransform atTank = AffineTransform.getRotateInstance(dumbTank.getTankAngle(), tankCenterX, tankCenterY);
        atTank.translate(dumbTank.getX(), dumbTank.getY());

        g2d.drawImage(baseImage.getImage(), atTank, this);
        g2d.setColor(Color.WHITE);

        for (Bullet bullet : dumbTank.getBullets()) {
            ImageIcon bulletImage = bullet.getBulletImage();
            int bulletX = (int) bullet.getX();
            int bulletY = (int) bullet.getY();

            double bulletAngle = bullet.getAngle()+Math.PI/2;

            AffineTransform atBullet = AffineTransform.getRotateInstance(bulletAngle, bulletX + bulletImage.getImage().getWidth(null) / 2, bulletY + bulletImage.getImage().getHeight(null) / 2);
            atBullet.translate(bulletX, bulletY);

            g2d.drawImage(bulletImage.getImage(), atBullet, this);
        }

        g2d.dispose();
    }

}
