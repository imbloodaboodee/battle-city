package jsd.project.tank90.render;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class GameGuideScreen extends JPanel {

    public GameGuideScreen(Runnable onEnterPress) {
        setLayout(new GridLayout(2, 1, 0, 0));
        setBackground(Color.BLACK);

        setBorder(BorderFactory.createEmptyBorder(20, 50, 80, 20));

        Font customFont = loadFont();

        JPanel row1 = new JPanel(new BorderLayout());
        row1.setBackground(Color.BLACK);
        JLabel imageLabel1 = new JLabel(resizeImageIcon(new ImageIcon("./src/jsd/project/tank90/assets/image/move.png"), 0.3));

        JLabel textLabel1 = new JLabel("<html><body style='width:230px'>Press W, A, S, D to move</body></html>", SwingConstants.LEFT);
        textLabel1.setForeground(Color.WHITE);
        textLabel1.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        if (customFont != null) textLabel1.setFont(customFont);
        row1.add(imageLabel1, BorderLayout.WEST);
        row1.add(textLabel1, BorderLayout.CENTER);

        JPanel row2 = new JPanel(new BorderLayout());
        row2.setBackground(Color.BLACK);
        JLabel imageLabel2 = new JLabel(resizeImageIcon(new ImageIcon("./src/jsd/project/tank90/assets/image/fire.png"), 0.3));

        JLabel textLabel2 = new JLabel("<html><body style='width:230px'>Click to fire, hold for continuous firing</body></html>", SwingConstants.LEFT);
        textLabel2.setForeground(Color.WHITE); // Set text color to white
        textLabel2.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0)); // Add padding to the left of the text
        if (customFont != null) textLabel2.setFont(customFont); // Set custom font if loaded
        row2.add(imageLabel2, BorderLayout.WEST);
        row2.add(textLabel2, BorderLayout.CENTER);

        add(row1);
        add(row2);

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
