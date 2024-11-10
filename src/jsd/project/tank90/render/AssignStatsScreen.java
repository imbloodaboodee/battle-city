package jsd.project.tank90.render;

import jsd.project.tank90.entities.BulletType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AssignStatsScreen extends JPanel {
    private int remainingPoints = 5;
    private int speed = 0;
    private int health = 0;
    private int rotationSpeed = 0;
    private BulletType selectedBulletType = null;

    private JLabel speedLabel;
    private JLabel healthLabel;
    private JLabel rotationLabel;

    private JButton selectedCard;
    private JButton confirmButton;

    public AssignStatsScreen(Runnable onConfirm) {
        setLayout(null);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(512, 500));

        JLabel titleLabel = new JLabel("Create Your Tank!");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 10, 512, 30);
        add(titleLabel);

        ImageIcon originalIcon = new ImageIcon("./src/jsd/project/tank90/assets/image/playerTank_up.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel tankImage = new JLabel(new ImageIcon(scaledImage));
        tankImage.setBounds(0, 50, 150, 150);
        add(tankImage);

        JLabel remainingPointsLabel = new JLabel("Points Left: " + remainingPoints);
        remainingPointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        remainingPointsLabel.setForeground(Color.WHITE);
        remainingPointsLabel.setBounds(0, 210, 150, 30);
        add(remainingPointsLabel);

        speedLabel = createStatRow("Speed", 90, e -> increaseStat("speed", remainingPointsLabel), e -> decreaseStat("speed", remainingPointsLabel));
        healthLabel = createStatRow("Health", 130, e -> increaseStat("health", remainingPointsLabel), e -> decreaseStat("health", remainingPointsLabel));
        rotationLabel = createStatRow("Rotation", 170, e -> increaseStat("rotationSpeed", remainingPointsLabel), e -> decreaseStat("rotationSpeed", remainingPointsLabel));
        add(speedLabel);
        add(healthLabel);
        add(rotationLabel);

        JPanel bulletPanel = new JPanel(null);
        bulletPanel.setBackground(Color.BLACK);
        bulletPanel.setBounds(20, 250, 472, 240);
        add(bulletPanel);

        int cardWidth = 100;
        int cardHeight = 130;
        int cardSpacing = 20;

        int totalWidth = (3 * cardWidth) + (2 * cardSpacing);
        int startX = (bulletPanel.getWidth() - totalWidth) / 2;

        for (int i = 0; i < 3; i++) {
            BulletType bulletType;
            String imagePath = "./src/jsd/project/tank90/assets/image/bullet.png";
            String description;

            switch (i) {
                case 0 -> {
                    bulletType = BulletType.STANDARD;
                    description = "Standard bullet.";
                }
                case 1 -> {
                    bulletType = BulletType.RAPID;
                    description = "High fire rate, low damage.";
                }
                case 2 -> {
                    bulletType = BulletType.EXPLOSIVE;
                    description = "High damage, low fire rate.";
                }
                default -> throw new IllegalStateException("Unexpected value: " + i);
            }

            int xPosition = startX + i * (cardWidth + cardSpacing);
            JButton bulletCard = createBulletCardButton(bulletType, imagePath, description, xPosition, cardWidth, cardHeight);
            bulletPanel.add(bulletCard);
        }

        confirmButton = new JButton("Confirm");
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBackground(Color.BLACK);
        confirmButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        confirmButton.setBounds(200, 390, 120, 30);
        confirmButton.addActionListener(e -> onConfirm.run());
        confirmButton.setVisible(false); //
        add(confirmButton);
    }


    private JLabel createStatRow(String statName, int yPosition, ActionListener addListener, ActionListener removeListener) {
        JLabel statLabel = new JLabel(statName + ": 0");
        statLabel.setForeground(Color.WHITE);
        statLabel.setBounds(140, yPosition, 80, 30);
        JButton addButton = new JButton("Assign");
        addButton.setBackground(Color.BLACK);
        addButton.setForeground(Color.WHITE);
        addButton.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        addButton.setBounds(300, yPosition, 80, 30);
        addButton.setFont(new Font("Arial", Font.PLAIN, 10));
        addButton.addActionListener(addListener);
        add(addButton);

        JButton removeButton = new JButton("Remove");
        removeButton.setBackground(Color.BLACK);
        removeButton.setForeground(Color.WHITE);
        removeButton.setBorder(BorderFactory.createLineBorder(Color.RED));
        removeButton.setBounds(390, yPosition, 80, 30);
        removeButton.setFont(new Font("Arial", Font.PLAIN, 10));
        removeButton.addActionListener(removeListener);
        add(removeButton);

        return statLabel;
    }

    private JButton createBulletCardButton(BulletType bulletType, String defaultImagePath, String description, int xPosition, int cardWidth, int cardHeight) {
        String imagePath;

        switch (bulletType) {
            case EXPLOSIVE -> imagePath = "./src/jsd/project/tank90/assets/image/bullet_explosive.png";
            case RAPID -> imagePath = "./src/jsd/project/tank90/assets/image/bullet_rapid.png";
            default -> imagePath = defaultImagePath;
        }

        JButton bulletCard = new JButton();
        bulletCard.setLayout(null);
        bulletCard.setBackground(Color.BLACK);
        bulletCard.setBounds(xPosition, 0, cardWidth, cardHeight);
        bulletCard.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        JLabel bulletImage = new JLabel(resizeImageIcon(new ImageIcon(imagePath),4));
        bulletImage.setBounds((cardWidth - 60) / 2, 20, 60, 60);
        bulletCard.add(bulletImage);

        JLabel bulletDescription = new JLabel(
                "<html><center><b style='color:red;'>" + bulletType + "</b><br><span style='font-size:6px;'>" + description + "</span></center></html>",
                SwingConstants.CENTER
        );
        bulletDescription.setForeground(Color.WHITE);
        bulletDescription.setFont(new Font("Arial", Font.PLAIN, 10));
        bulletDescription.setBounds(0, 75, cardWidth-3, 70);
        bulletCard.add(bulletDescription);

        bulletCard.addActionListener(e -> selectBulletType(bulletType, bulletCard));

        return bulletCard;
    }

    private void increaseStat(String statType, JLabel remainingPointsLabel) {
        if (remainingPoints > 0) {
            switch (statType) {
                case "speed" -> {
                    speed++;
                    speedLabel.setText("Speed: " + speed);
                }
                case "health" -> {
                    health++;
                    healthLabel.setText("Health: " + health);
                }
                case "rotationSpeed" -> {
                    rotationSpeed++;
                    rotationLabel.setText("Rotation: " + rotationSpeed);
                }
            }
            remainingPoints--;
            updateRemainingPointsLabel(remainingPointsLabel);
            checkConditionsForConfirmButton();
        }
    }

    private void decreaseStat(String statType, JLabel remainingPointsLabel) {
        switch (statType) {
            case "speed" -> {
                if (speed > 0) {
                    speed--;
                    speedLabel.setText("Speed: " + speed);
                    remainingPoints++;
                }
            }
            case "health" -> {
                if (health > 0) {
                    health--;
                    healthLabel.setText("Health: " + health);
                    remainingPoints++;
                }
            }
            case "rotationSpeed" -> {
                if (rotationSpeed > 0) {
                    rotationSpeed--;
                    rotationLabel.setText("Rotation: " + rotationSpeed);
                    remainingPoints++;
                }
            }
        }
        updateRemainingPointsLabel(remainingPointsLabel);
        checkConditionsForConfirmButton();
    }

    private void updateRemainingPointsLabel(JLabel remainingPointsLabel) {
        remainingPointsLabel.setText("Poins Left: " + remainingPoints);
    }

    private void selectBulletType(BulletType bulletType, JButton bulletCard) {
        if (selectedCard != null) {
            selectedCard.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        }

        selectedBulletType = bulletType;
        bulletCard.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        selectedCard = bulletCard;

        checkConditionsForConfirmButton();
    }

    private void checkConditionsForConfirmButton() {
        if (remainingPoints == 0 && selectedBulletType != null) {
            confirmButton.setVisible(true);
        } else {
            confirmButton.setVisible(false);
        }
        setComponentZOrder(confirmButton, 0);
        revalidate();
        repaint();
    }
    private ImageIcon resizeImageIcon(ImageIcon icon, double ratio) {
        int originalWidth = icon.getIconWidth();
        int originalHeight = icon.getIconHeight();
        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);
        Image resizedImage = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    public int getSpeed() {
        return speed;
    }

    public int getHealth() {
        return health;
    }

    public int getRotationSpeed() {
        return rotationSpeed;
    }

    public BulletType getSelectedBulletType() {
        return selectedBulletType;
    }
}
