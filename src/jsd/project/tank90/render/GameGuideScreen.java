package jsd.project.tank90.render;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class GameGuideScreen extends JPanel {

    public GameGuideScreen(Runnable onEnterPress) {
        setLayout(new GridLayout(2, 1, 0, 0)); // 2 rows, 1 column with spacing
        setBackground(Color.BLACK); // Set background color to black

        // Set padding around the edges of the main panel
        setBorder(BorderFactory.createEmptyBorder(20, 50, 80, 20));

        // Load custom font
        Font customFont = loadFont();

        // Create the first row
        JPanel row1 = new JPanel(new BorderLayout());
        row1.setBackground(Color.BLACK); // Set row background to black
        JLabel imageLabel1 = new JLabel(resizeImageIcon(new ImageIcon("./src/jsd/project/tank90/assets/image/move.png"), 0.3));

        // Wrap text in HTML for line-breaking and add padding for spacing
        JLabel textLabel1 = new JLabel("<html><body style='width:230px'>Press W, A, S, D to move</body></html>", SwingConstants.LEFT);
        textLabel1.setForeground(Color.WHITE); // Set text color to white
        textLabel1.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // Add padding to the left of the text
        if (customFont != null) textLabel1.setFont(customFont); // Set custom font if loaded
        row1.add(imageLabel1, BorderLayout.WEST);
        row1.add(textLabel1, BorderLayout.CENTER);

        // Create the second row
        JPanel row2 = new JPanel(new BorderLayout());
        row2.setBackground(Color.BLACK); // Set row background to black
        JLabel imageLabel2 = new JLabel(resizeImageIcon(new ImageIcon("./src/jsd/project/tank90/assets/image/fire.png"), 0.3));

        // Wrap text in HTML for line-breaking and add padding for spacing
        JLabel textLabel2 = new JLabel("<html><body style='width:230px'>Click to fire, hold for continuous firing</body></html>", SwingConstants.LEFT);
        textLabel2.setForeground(Color.WHITE); // Set text color to white
        textLabel2.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0)); // Add padding to the left of the text
        if (customFont != null) textLabel2.setFont(customFont); // Set custom font if loaded
        row2.add(imageLabel2, BorderLayout.WEST);
        row2.add(textLabel2, BorderLayout.CENTER);

        // Add rows to the main panel
        add(row1);
        add(row2);

        // Add key listener for Enter key to switch to game screen
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    onEnterPress.run(); // Trigger the transition to the game screen
                }
            }
        });

        setFocusable(true); // To ensure the panel can receive key events
        requestFocusInWindow();
    }

    private ImageIcon resizeImageIcon(ImageIcon icon, double ratio) {
        int originalWidth = icon.getIconWidth();
        int originalHeight = icon.getIconHeight();
        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);
        Image resizedImage = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    public static Font loadFont() {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("./src/jsd/project/tank90/assets/font/prstart.ttf")).deriveFont(Font.PLAIN, 15);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font;
        } catch (FontFormatException | IOException ex) {
            ex.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, 15);
        }
    }
}
