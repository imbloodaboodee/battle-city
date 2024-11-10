package jsd.project.tank90.entities;

import javax.swing.*;
import java.awt.*;

public class DumbTank extends Tank {

    public DumbTank(int x, int y, int health, int speed, BulletType bulletType, ImageIcon baseImage) {
        super(x, y, health, speed);
        setDefaultBullet(new Bullet(bulletType));
        setBaseImage(resizeImageIcon(baseImage, 0.9));
        setHitbox(new Rectangle(getX(), getY(), getBaseImage().getIconWidth(), getBaseImage().getIconHeight()));
    }


    private ImageIcon resizeImageIcon(ImageIcon icon, double ratio) {
        int originalWidth = icon.getIconWidth();
        int originalHeight = icon.getIconHeight();

        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);

        Image resizedImage = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        return new ImageIcon(resizedImage);
    }

}
