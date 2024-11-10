package jsd.project.tank90.render;

import jsd.project.tank90.entities.BulletType;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class AssignStatsScreen extends JPanel {
    private int remainingPoints = 5; // Points available for assigning
    private int speed = 0;
    private int health = 0;
    private int rotationSpeed = 0;
    private BulletType selectedBulletType = null; // Initially no bullet type selected

    private JLabel speedLabel;
    private JLabel healthLabel;
    private JLabel rotationLabel;

    private JButton selectedCard; // Track the selected bullet card
    private JButton confirmButton; // Confirm button to be shown only after conditions are met

    public AssignStatsScreen(Runnable onConfirm) {
        setLayout(null); // Use absolute layout for fixed positioning
        setBackground(Color.BLACK); // Set background to black
        setPreferredSize(new Dimension(512, 500)); // Set fixed size for the panel

        // Title label
        JLabel titleLabel = new JLabel("Create Your Tank!");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Large bold font
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 10, 512, 30); // Position at the top, centered
        add(titleLabel);

        // Tank image positioned next to stat rows
        ImageIcon originalIcon = new ImageIcon("./src/jsd/project/tank90/assets/image/playerTank_up.png"); // Placeholder image path
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Scale image up to 150x150
        JLabel tankImage = new JLabel(new ImageIcon(scaledImage));
        tankImage.setBounds(0, 50, 150, 150); // Adjusted position and size to make it larger
        add(tankImage);

        // Remaining points label positioned below the tank image
        JLabel remainingPointsLabel = new JLabel("Points Left: " + remainingPoints);
        remainingPointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        remainingPointsLabel.setForeground(Color.WHITE); // Set text color to white
        remainingPointsLabel.setBounds(0, 210, 150, 30); // Adjusted position below the larger tank image
        add(remainingPointsLabel);

        // Stat assignment section with adjusted positions
        speedLabel = createStatRow("Speed", 90, e -> increaseStat("speed", remainingPointsLabel), e -> decreaseStat("speed", remainingPointsLabel));
        healthLabel = createStatRow("Health", 130, e -> increaseStat("health", remainingPointsLabel), e -> decreaseStat("health", remainingPointsLabel));
        rotationLabel = createStatRow("Rotation", 170, e -> increaseStat("rotationSpeed", remainingPointsLabel), e -> decreaseStat("rotationSpeed", remainingPointsLabel));
        add(speedLabel);
        add(healthLabel);
        add(rotationLabel);

        // Bullet selection cards
        JPanel bulletPanel = new JPanel(null);
        bulletPanel.setBackground(Color.BLACK);
        bulletPanel.setBounds(20, 250, 472, 240); // Adjusted to fit below stats
        add(bulletPanel);

        // Define card width, height, and spacing
        int cardWidth = 100;
        int cardHeight = 130;
        int cardSpacing = 20;

        // Calculate the total width needed for three cards and the spacing between them
        int totalWidth = (3 * cardWidth) + (2 * cardSpacing);
        int startX = (bulletPanel.getWidth() - totalWidth) / 2; // Calculate starting x position for centering

        // Add bullet cards with adjusted spacing and centered position
        for (int i = 0; i < 3; i++) {
            BulletType bulletType;
            String imagePath = "./src/jsd/project/tank90/assets/image/bullet.png";
            String description;

            switch (i) {
                case 0 -> {
                    bulletType = BulletType.NORMAL;
                    description = "Standard bullet with balanced damage.";
                }
                case 1 -> {
                    bulletType = BulletType.RAPID;
                    description = "Rapid-fire bullet with lower damage.";
                }
                case 2 -> {
                    bulletType = BulletType.EXPLOSIVE;
                    description = "Explosive bullet with splash damage.";
                }
                default -> throw new IllegalStateException("Unexpected value: " + i);
            }

            int xPosition = startX + i * (cardWidth + cardSpacing); // Set x position for each card
            JButton bulletCard = createBulletCardButton(bulletType, imagePath, description, xPosition, cardWidth, cardHeight);
            bulletPanel.add(bulletCard);
        }

        // Confirm button to pass selected values (initially hidden)
        confirmButton = new JButton("Confirm");
        confirmButton.setForeground(Color.WHITE); // White text
        confirmButton.setBackground(Color.BLACK); // Black background
        confirmButton.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // White border
        confirmButton.setBounds(200, 380, 120, 30);
        confirmButton.addActionListener(e -> onConfirm.run());
        confirmButton.setVisible(false); // Hide the button initially
        add(confirmButton);
    }


    private JLabel createStatRow(String statName, int yPosition, ActionListener addListener, ActionListener removeListener) {
        JLabel statLabel = new JLabel(statName + ": 0");
        statLabel.setForeground(Color.WHITE);
        statLabel.setBounds(140, yPosition, 80, 30); // Position shifted right to align with tank image

        JButton addButton = new JButton("Assign");
        addButton.setBackground(Color.BLACK); // Black background
        addButton.setForeground(Color.WHITE); // White text
        addButton.setBorder(BorderFactory.createLineBorder(Color.GREEN)); // White border
        addButton.setBounds(300, yPosition, 80, 30); // Increased `x` position to space out the button from the label
        addButton.setFont(new Font("Arial", Font.PLAIN, 10)); // Smaller font for buttons
        addButton.addActionListener(addListener);
        add(addButton);

        JButton removeButton = new JButton("Remove");
        removeButton.setBackground(Color.BLACK);
        removeButton.setForeground(Color.WHITE);
        removeButton.setBorder(BorderFactory.createLineBorder(Color.RED));
        removeButton.setBounds(390, yPosition, 80, 30); // Position to the right of the assign button
        removeButton.setFont(new Font("Arial", Font.PLAIN, 10));
        removeButton.addActionListener(removeListener);
        add(removeButton);

        return statLabel;
    }

    private JButton createBulletCardButton(BulletType bulletType, String imagePath, String description, int xPosition, int cardWidth, int cardHeight) {
        JButton bulletCard = new JButton();
        bulletCard.setLayout(null);
        bulletCard.setBackground(Color.BLACK);
        bulletCard.setBounds(xPosition, 0, cardWidth, cardHeight); // Set width dynamically for smaller card size
        bulletCard.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // Default white border around the card

        // Add bullet image
        JLabel bulletImage = new JLabel(new ImageIcon(imagePath));
        bulletImage.setBounds((cardWidth - 40) / 2, 10, 40, 40); // Smaller image to fit within the card
        bulletCard.add(bulletImage);

        // Add bullet description with smaller font
        JLabel bulletDescription = new JLabel("<html><center>" + bulletType + "<br>" + description + "</center></html>", SwingConstants.CENTER);
        bulletDescription.setForeground(Color.WHITE);
        bulletDescription.setFont(new Font("Arial", Font.PLAIN, 10)); // Smaller font for description
        bulletDescription.setBounds(0, 55, cardWidth, 70); // Center within the card
        bulletCard.add(bulletDescription);

        // Set the action for selecting the bullet type and highlighting the card
        bulletCard.addActionListener(e -> selectBulletType(bulletType, bulletCard));

        return bulletCard;
    }

    private void increaseStat(String statType, JLabel remainingPointsLabel) {
        if (remainingPoints > 0) {
            switch (statType) {
                case "speed" -> {
                    speed++;
                    speedLabel.setText("Speed: " + speed); // Update speed label
                }
                case "health" -> {
                    health++;
                    healthLabel.setText("Health: " + health); // Update health label
                }
                case "rotationSpeed" -> {
                    rotationSpeed++;
                    rotationLabel.setText("Rotation: " + rotationSpeed); // Update rotation speed label
                }
            }
            remainingPoints--;
            updateRemainingPointsLabel(remainingPointsLabel);
            checkConditionsForConfirmButton(); // Check conditions after each stat assignment
        }
    }

    private void decreaseStat(String statType, JLabel remainingPointsLabel) {
        switch (statType) {
            case "speed" -> {
                if (speed > 0) {
                    speed--;
                    speedLabel.setText("Speed: " + speed); // Update speed label
                    remainingPoints++;
                }
            }
            case "health" -> {
                if (health > 0) {
                    health--;
                    healthLabel.setText("Health: " + health); // Update health label
                    remainingPoints++;
                }
            }
            case "rotationSpeed" -> {
                if (rotationSpeed > 0) {
                    rotationSpeed--;
                    rotationLabel.setText("Rotation: " + rotationSpeed); // Update rotation speed label
                    remainingPoints++;
                }
            }
        }
        updateRemainingPointsLabel(remainingPointsLabel);
        checkConditionsForConfirmButton(); // Check conditions after each stat adjustment
    }

    private void updateRemainingPointsLabel(JLabel remainingPointsLabel) {
        remainingPointsLabel.setText("Poins Left: " + remainingPoints);
    }

    private void selectBulletType(BulletType bulletType, JButton bulletCard) {
        if (selectedCard != null) {
            selectedCard.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        }

        selectedBulletType = bulletType; // Set selectedBulletType to BulletType instead of String
        bulletCard.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2)); // Cyan border for selected card
        selectedCard = bulletCard; // Update selected card reference

        checkConditionsForConfirmButton(); // Check conditions after bullet selection
    }

    private void checkConditionsForConfirmButton() {
        if (remainingPoints == 0 && selectedBulletType != null) {
            confirmButton.setVisible(true);
        } else {
            confirmButton.setVisible(false);
        }
        setComponentZOrder(confirmButton, 0); // Bring the confirm button to the front
        revalidate();
        repaint();
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
