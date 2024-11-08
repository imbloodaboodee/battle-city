package entities;

import constants.GameConstants;
import manager.BulletManager;
import physics.CollisionHandling;
import render.GameScreen;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;

public class DumbTank extends Tank {



    public DumbTank(int x, int y, int health, int speed, BulletType bulletType, ImageIcon baseImage) {
        super(x, y, health, speed);
        setDefaultBullet(new Bullet(bulletType));
        setBaseImage(resizeImageIcon(baseImage, 0.9));


        initializeCommonResources();
    }

    private void initializeCommonResources() {

        // Initialize the hitbox
        setHitbox(new Rectangle(getX(), getY(), getBaseImage().getIconWidth(), getBaseImage().getIconHeight()));

        // Timer for updating entities (tank position, bullets, etc.)
    }

    private ImageIcon resizeImageIcon(ImageIcon icon, double ratio) {
        // Get the original width and height of the image
        int originalWidth = icon.getIconWidth();
        int originalHeight = icon.getIconHeight();

        // Calculate the new width and height based on the ratio
        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);

        // Scale the image to the new dimensions
        Image resizedImage = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        // Return a new ImageIcon from the scaled image
        return new ImageIcon(resizedImage);
    }

    // Create (fire) a bullet, similar to player tank, but with its own logic


}
