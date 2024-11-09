package render;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class GameGuideRender extends JPanel {

    private BufferedImage mainBackgroundImage;
    private BufferedImage battleCityText;

    public GameGuideRender(Runnable onEnterPress) {
        setLayout(new BorderLayout());

        // Load images for background and title using BufferedImage and ImageIO
        try {
            mainBackgroundImage = ImageIO.read(new File("src/assets/UI/old-stone-brick-wall-textures.jpg"));
            battleCityText = ImageIO.read(new File("src/assets/UI/khung2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Main panel with background image, resized to fit the GameFrame size
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(mainBackgroundImage, 0, 0, 512, 470, this); // Resize background to 512x470
            }
        };

        // Black overlay panel for menu, resized to fit within the frame
        JPanel overlayPanel = new JPanel(new BorderLayout());
        overlayPanel.setBackground(Color.black);
        overlayPanel.setOpaque(true);
        overlayPanel.setPreferredSize(new Dimension(460, 350));

        // Panel for battle city text image, resized to fit within the overlay panel
        JPanel battleCity = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(battleCityText, 0, 0, 460, 130, this); // Resize title to 460x130
            }
        };
        battleCity.setPreferredSize(new Dimension(460, 130));
        overlayPanel.add(battleCity, BorderLayout.NORTH);

        // Labels panel with options, adjusted font size and layout for smaller height
        JPanel labelPanel = new JPanel(new GridLayout(0, 1));
        labelPanel.setOpaque(false);
        Font airstrikeFont = new Font("Airstrike", Font.PLAIN, 18); // Adjust font size for smaller frame

        JLabel label1 = new JLabel("1 PLAYER", SwingConstants.CENTER);
        label1.setFont(airstrikeFont);
        label1.setForeground(Color.WHITE);

        // Add a mouse listener to label1 for click event
        label1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onEnterPress.run(); // Trigger transition to the game screen
            }
        });

        JLabel label2 = new JLabel("2 PLAYERS", SwingConstants.CENTER);
        label2.setFont(airstrikeFont);
        label2.setForeground(Color.WHITE);

        JLabel label3 = new JLabel("CONSTRUCTION", SwingConstants.CENTER);
        label3.setFont(airstrikeFont);
        label3.setForeground(Color.WHITE);

        JLabel label4 = new JLabel("ALL RIGHTS RESERVED", SwingConstants.CENTER);
        label4.setFont(airstrikeFont);
        label4.setForeground(Color.WHITE);

        // Add labels to panel
        labelPanel.add(label1);
        labelPanel.add(label2);
        labelPanel.add(label3);
        labelPanel.add(label4);

        overlayPanel.add(labelPanel, BorderLayout.SOUTH);
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(overlayPanel, new GridBagConstraints());

        // Add main panel with overlay to the center of GameGuideRender
        add(mainPanel, BorderLayout.CENTER);

        // Key listener for Enter key to start the game
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    onEnterPress.run();
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Battle City");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(512, 470);
        frame.setLocationRelativeTo(null);

        GameGuideRender guideRender = new GameGuideRender(() -> {
            System.out.println("Game Started!");
            // Logic to switch to the game screen
        });

        frame.add(guideRender);
        frame.setVisible(true);
    }
}
