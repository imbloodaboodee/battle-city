package entities;

import constants.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.Iterator;

public class PlayerTank extends JLabel {
    //Images variables
    private ImageIcon cannonImage;
    private ImageIcon baseImage;
    private ImageIcon aimImage;

    //Timers
    private Timer bulletCreationTimer;
    private Timer bulletTimerCountdown;
    private Timer gameLoopTimer;

    //Attributes
    private Tank tank;
    private Bullet defaultBullet;
    private double cannonAngle = 0;
    private boolean canFire = true;

    private double targetCannonAngle = 0; // The target angle to rotate to
    private final double ROTATION_SPEED = 0.02; // Speed at which the cannon rotates

    public PlayerTank(Tank tank, BulletType bulletType) {
        this.tank = tank;
        this.defaultBullet = new Bullet(bulletType);
        this.setSize(800, 800);


        //Initialize tank images
        baseImage = new ImageIcon("./src/assets/image/tank.png");
        cannonImage = new ImageIcon("./src/assets/image/cannon.png");
        aimImage = new ImageIcon("./src/assets/image/aim.png");

        // Initialize the hitbox (assuming the tank has a known width and height)
        tank.setHitbox(new Rectangle(tank.getX(), tank.getY(), baseImage.getIconWidth(), baseImage.getIconHeight()));

        //Mouse listeners
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

        //Timer for creating bullets, starts when firing
        bulletCreationTimer = new Timer(GameConstants.DELAY, e -> createBullet());
        bulletCreationTimer.setRepeats(true);

        //Timer for updating entities that need constant updating
        gameLoopTimer = new Timer(GameConstants.DELAY, e -> {
            tank.updateTankPosition();
            updateBullets();
            updateCannonAngleFromMousePosition();

            repaint();
        });
        gameLoopTimer.start();

        //Set and request focus on the panel
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setSize(GameConstants.FRAME_WIDTH, GameConstants.FRAME_HEIGHT);


    }

    public void keyPressed(KeyEvent e) {
        tank.keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        tank.keyReleased(e);
    }

    private void updateTankPosition() {
        int oldX = tank.getX();
        int oldY = tank.getY();
        tank.updateTankPosition();

    }

    //Update cannon according to mouse position
    private void updateCannonAngleFromMousePosition() {
        // Get the current mouse position in the panel's coordinate system
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mousePosition, this);

        // Calculate the target angle from the cannon center to the mouse position
        int cannonCenterX = tank.getX() + baseImage.getIconWidth() / 2;
        int cannonCenterY = tank.getY() + baseImage.getIconHeight() / 2;
        targetCannonAngle = Math.atan2(mousePosition.y - cannonCenterY, mousePosition.x - cannonCenterX) + (Math.PI / 2);

        // Gradually adjust the cannon angle towards the target angle
        if (Math.abs(targetCannonAngle - cannonAngle) > ROTATION_SPEED) {
            // Check if we need to rotate clockwise or counter-clockwise
            if (targetCannonAngle > cannonAngle) {
                cannonAngle += ROTATION_SPEED; // Rotate clockwise
            } else {
                cannonAngle -= ROTATION_SPEED; // Rotate counter-clockwise
            }

            // Keep the angle within the range of -PI to PI for consistency
            cannonAngle = (cannonAngle + Math.PI * 2) % (Math.PI * 2);
        }
    }

    //Create (fire) a bullet
    private void createBullet() {
        if (canFire) {
            // Calculate the starting point of the bullet (at the cannon tip)
            int cannonTipX = (int) (tank.getX() + baseImage.getIconWidth() / 2 + Math.cos(cannonAngle - Math.PI / 2) * cannonImage.getIconHeight() / 2);
            int cannonTipY = (int) (tank.getY() + baseImage.getIconHeight() / 2 + Math.sin(cannonAngle - Math.PI / 2) * cannonImage.getIconHeight() / 2);

            // Create the bullet with the current cannon angle
            Bullet bullet = new Bullet(cannonTipX, cannonTipY, defaultBullet.getBulletType(), cannonAngle - Math.PI / 2);
            tank.getBullets().add(bullet);

            // Mechanism for handling firing cooldown
            canFire = false;
            bulletTimerCountdown = new Timer(defaultBullet.getCooldown(), e -> {
                canFire = true;
                bulletTimerCountdown.stop();
            });
            bulletTimerCountdown.start();
        }
    }

    //Update the bullet's position, remove the bullet once it goes out of bounds
    private void updateBullets() {
        // Use an iterator to safely remove bullets while iterating
        Iterator<Bullet> bulletIterator = tank.getBullets().iterator();

        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.updatePosition();

            // Check if the bullet goes out of bounds
            if (bullet.getX() < 0 || bullet.getX() > getWidth() || bullet.getY() < 0 || bullet.getY() > getHeight()) {
                bulletIterator.remove(); // Safely remove the bullet
            }
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Rotate the tank base image according to the tankAngle
        int tankCenterX = tank.getX() + baseImage.getIconWidth() / 2;
        int tankCenterY = tank.getY() + baseImage.getIconHeight() / 2;
        AffineTransform atTank = AffineTransform.getRotateInstance(tank.getTankAngle(), tankCenterX, tankCenterY);
        atTank.translate(tank.getX(), tank.getY());

        // Draw the rotated tank base
        g2d.drawImage(baseImage.getImage(), atTank, this);

        // Rotate the cannon according to the mouse position
        int cannonX = tankCenterX - cannonImage.getIconWidth() / 2;
        int cannonY = tankCenterY - cannonImage.getIconHeight() / 2;
        AffineTransform atCannon = AffineTransform.getRotateInstance(cannonAngle, cannonX + cannonImage.getIconWidth() / 2, cannonY + cannonImage.getIconHeight() / 2);
        atCannon.translate(cannonX, cannonY);

        // Draw the rotated cannon
        g2d.drawImage(cannonImage.getImage(), atCannon, this);

        //Adjust opacity for the aim
        float alpha = 0.5f;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        // Rotate the aim according to the mouse position
        AffineTransform atAim = AffineTransform.getRotateInstance(cannonAngle, tankCenterX, tankCenterY);
        atAim.translate(tankCenterX - aimImage.getIconWidth() / 2, tankCenterY - aimImage.getIconHeight() - cannonImage.getIconHeight() / 2);

        // Draw the rotated aim image
        g2d.drawImage(aimImage.getImage(), atAim, this);

        // Reset transparency to default
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        // Set the color to white for the bullets
        g2d.setColor(Color.WHITE);

        // Draw bullets
        for (Bullet bullet : tank.getBullets()) {
            g2d.setColor(Color.WHITE);
            g2d.fillOval((int) bullet.getX(), (int) bullet.getY(), GameConstants.BULLET_SIZE, GameConstants.BULLET_SIZE); // Drawing a simple circle for the bullet
            g2d.setColor(Color.RED);

            g2d.draw(bullet.getHitbox());
        }
        // **Draw the hitbox for debugging**
        g2d.setColor(Color.RED);  // Set hitbox color (e.g., red for visibility)
        g2d.draw(tank.getHitbox());  // Draw the hitbox around the tank

        g2d.dispose();
    }

    public Tank getTank() {
        return tank;
    }
}
