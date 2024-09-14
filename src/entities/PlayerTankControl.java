package entities;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PlayerTankControl extends JPanel {
    private JLabel label;

    private BufferedImage cannonImage;

    private Timer movementTimer;
    private Timer cannonRotateTimer;
    private Timer bulletUpdateTimer;
    private Timer bulletCreationTimer;
    private Timer bulletTimerCountdown;

    private double cannonAngle = 0;
    private boolean canFire = true;

    private Tank tank;
    private Bullet defaultBullet;


    public PlayerTankControl(Tank tank, BulletType bulletType) {
        this.tank = tank;
        this.defaultBullet = new Bullet(bulletType);

        ImageIcon base = new ImageIcon("./src/image/base.png");

        label = new JLabel(base);
        label.setBounds(tank.getX(), tank.getY(), base.getIconWidth(), base.getIconHeight());

        try {
            cannonImage = ImageIO.read(new File("./src/image/cannon.png"));
        } catch (IOException e) {
            e.printStackTrace();
            cannonImage = new BufferedImage(30, 10, BufferedImage.TYPE_INT_ARGB);
        }


        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                tank.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                tank.keyReleased(e);
            }
        });
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("start");
                bulletCreationTimer.start(); // Start firing bullets

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("stop");
                bulletCreationTimer.stop(); // Stop firing bullets

            }

        });
        bulletCreationTimer = new Timer(0, e -> createBullet());
        bulletCreationTimer.setRepeats(true);

        bulletCreationTimer = new Timer(0, e -> createBullet());
        bulletCreationTimer.setRepeats(true);

        bulletUpdateTimer = new Timer(0, e -> updateBullets());
        bulletUpdateTimer.start();

        movementTimer = new Timer(10, e -> updateTankAndLabel());
        movementTimer.start();

        cannonRotateTimer = new Timer(10, e -> updateCannonAngleFromMouseInfo());
        cannonRotateTimer.start();

        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    private void updateTankAndLabel() {
        tank.updateTankPosition();
        ImageIcon ii = (ImageIcon) label.getIcon();
        label.setBounds(tank.getX(), tank.getY(), ii.getIconWidth(), ii.getIconHeight());
        repaint();
    }

    private void updateCannonAngleFromMouseInfo() {
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mousePosition, this);
        updateCannonAngle(mousePosition.x, mousePosition.y);
    }

    private void updateCannonAngle(int mouseX, int mouseY) {
        int cannonCenterX = label.getX() + label.getWidth() / 2;
        int cannonCenterY = label.getY() + label.getHeight() / 2;
        cannonAngle = Math.atan2(mouseY - cannonCenterY, mouseX - cannonCenterX) + (Math.PI / 2);
        repaint();
    }

    private void createBullet() {
        if (canFire) {
            // Get the current mouse position in the panel's coordinate system
            Point mousePosition = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(mousePosition, this);

            // Calculate the starting point of the bullet (at the cannon tip)
            int cannonTipX = (int) (label.getX() + label.getWidth() / 2 + Math.cos(cannonAngle - Math.PI / 2) * cannonImage.getHeight() / 2);
            int cannonTipY = (int) (label.getY() + label.getHeight() / 2 + Math.sin(cannonAngle - Math.PI / 2) * cannonImage.getHeight() / 2);

            // Calculate the center of the cannon (or tank)
            int cannonCenterX = label.getX() + label.getWidth() / 2;
            int cannonCenterY = label.getY() + label.getHeight() / 2;

            // Calculate the angle from the cannon center to the mouse position
            double angle = Math.atan2(mousePosition.y - cannonCenterY, mousePosition.x - cannonCenterX);

            // Create the bullet with the calculated angle, starting from the cannon's center
            Bullet bullet = new Bullet(cannonTipX, cannonTipY, defaultBullet.getBulletType(), angle); // Adjust angle for correct firing direction
            tank.getBullets().add(bullet);

            canFire = false;

            // Start the cooldown timer
            fireCooldownTimer(defaultBullet.getCooldown());
        }
    }

    private void fireCooldownTimer(int cooldown) {
        bulletTimerCountdown = new Timer(cooldown, e -> {
            canFire = true;
            bulletTimerCountdown.stop();
        });
        bulletTimerCountdown.start();
    }


    private void updateBullets() {
        for (int i = 0; i < tank.getBullets().size(); i++) {
            tank.getBullets().get(i).updatePosition();
            if (tank.getBullets().get(i).getX() < 0 || tank.getBullets().get(i).getX() > getWidth() || tank.getBullets().get(i).getY() < 0 || tank.getBullets().get(i).getY() > getHeight()) {
                tank.getBullets().remove(i); // Remove bullet if it goes out of bounds
                i--; // Decrement to compensate for the shift in index
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        int baseX = label.getX();
        int baseY = label.getY();
        ImageIcon ii = (ImageIcon) label.getIcon();
        // Rotate the tank base image according to the tankAngle
        int tankCenterX = label.getX() + label.getWidth() / 2;
        int tankCenterY = label.getY() + label.getHeight() / 2;
        AffineTransform atTank = AffineTransform.getRotateInstance(tank.getTankAngle(), tankCenterX, tankCenterY);
        atTank.translate(baseX, baseY);

        // Draw the rotated tank base
        g2d.drawImage(ii.getImage(), atTank, this);

        int cannonX = label.getX() + label.getWidth() / 2 - cannonImage.getWidth() / 2;
        int cannonY = label.getY() + label.getHeight() / 2 - cannonImage.getHeight() / 2;
        AffineTransform atCannon = AffineTransform.getRotateInstance(cannonAngle, cannonX + cannonImage.getWidth() / 2, cannonY + cannonImage.getHeight() / 2);
        atCannon.translate(cannonX, cannonY);

        g2d.drawImage(cannonImage, atCannon, this);


        for (Bullet bullet : tank.getBullets()) {
            g2d.fillOval((int) bullet.getX(), (int) bullet.getY(), 5, 5); // Drawing a simple circle for the bullet
        }
        ;
        g2d.dispose();

    }


}
