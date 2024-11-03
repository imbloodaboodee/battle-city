
package render;

import entities.Bullet;
import entities.PlayerTank;

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
        // Load images
        baseImage = playerTank.getBaseImage();
        cannonImage = playerTank.getCannonImage();
        aimImage = playerTank.getAimImage();

        this.setSize(800, 800);

    }

    private void updateCannonAngle() {
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mousePosition, c);

        // Calculate the center of the tank
        int cannonCenterX = playerTank.getX() + baseImage.getIconWidth() / 2;
        int cannonCenterY = playerTank.getY() + baseImage.getIconHeight() / 2;
        playerTank.setTargetCannonAngle(Math.atan2(mousePosition.y - cannonCenterY, mousePosition.x - cannonCenterX) + (Math.PI / 2));
        // Calculate the angle from tank center to mouse position, for consistent cannon and aim rotation

        // Calculate the difference between the current angle and the target angle
        double angleDifference = playerTank.getTargetCannonAngle() - playerTank.getCannonAngle();

        // Normalize the angle difference to be within the range of -PI to PI
        angleDifference = (angleDifference + Math.PI) % (2 * Math.PI) - Math.PI;

        // If the angle difference is greater than the rotation speed, rotate towards the target angle
        if (Math.abs(angleDifference) > playerTank.getROTATION_SPEED()) {
            if (angleDifference > 0) {
                playerTank.setCannonAngle(playerTank.getCannonAngle() + playerTank.getROTATION_SPEED());
            } else {
                playerTank.setCannonAngle(playerTank.getCannonAngle() - playerTank.getROTATION_SPEED());
            }

            // Keep the angle within the range of -PI to PI for consistency
            playerTank.setCannonAngle((playerTank.getCannonAngle() + Math.PI) % (2 * Math.PI) - Math.PI);
        } else {
            // If the difference is smaller than the rotation speed, set the angle directly
            playerTank.setCannonAngle(playerTank.getTargetCannonAngle());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        updateCannonAngle();
        // Draw tank base and cannon with rotation
        int tankCenterX = playerTank.getX() + baseImage.getIconWidth() / 2;
        int tankCenterY = playerTank.getY() + baseImage.getIconHeight() / 2;

        AffineTransform atTank = AffineTransform.getRotateInstance(playerTank.getTankAngle(), tankCenterX, tankCenterY);
        atTank.translate(playerTank.getX(), playerTank.getY());
        g2d.drawImage(baseImage.getImage(), atTank, this);

        // Rotate and draw the cannon according to the mouse position
        int cannonX = tankCenterX - cannonImage.getIconWidth() / 2;
        int cannonY = tankCenterY - cannonImage.getIconHeight() / 2;
        AffineTransform atCannon = AffineTransform.getRotateInstance(playerTank.getCannonAngle(), cannonX + cannonImage.getIconWidth() / 2, cannonY + cannonImage.getIconHeight() / 2);
        atCannon.translate(cannonX, cannonY);
        g2d.drawImage(cannonImage.getImage(), atCannon, this);

        // Rotate and draw the aim according to the mouse position
        AffineTransform atAim = AffineTransform.getRotateInstance(playerTank.getCannonAngle(), tankCenterX, tankCenterY);
        atAim.translate(tankCenterX - aimImage.getIconWidth() / 2, tankCenterY - aimImage.getIconHeight() - cannonImage.getIconHeight() / 2);

        // Draw the rotated aim image
        g2d.drawImage(aimImage.getImage(), atAim, this);

        // Reset opacity
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        // Draw bullets
        g2d.setColor(Color.WHITE);
        for (Bullet bullet : playerTank.getBullets()) {
            g2d.fillOval((int) bullet.getX(), (int) bullet.getY(), 5, 5);
        }

        g2d.dispose();
    }

    public PlayerTank getPlayerTank() {
        return playerTank;
    }
}
