package jsd.project.tank90.render;

import physics.ImageUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class GameMenuScreen extends JPanel {

    private final Runnable onEnterPress;
    private int yPos = 100;
    private Image background, tank, tree;
    private final ImageUtility imageInstance = ImageUtility.getInstance();

    /**
     * Constructor for the menu
     *
     * @param onEnterPress Runnable to trigger when Enter is pressed
     */
    public GameMenuScreen(Runnable onEnterPress) {
        this.onEnterPress = onEnterPress;
        setBackground(Color.BLACK);
        setLayout(null);
        loadMenuImages();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    onEnterPress.run(); // Switch to the game screen
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }

    private void loadMenuImages() {
        background = imageInstance.getBackground();
        tank = imageInstance.getTank();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Font font = loadFont();
        g.setFont(font);
        g.setColor(Color.WHITE);

        // Draw the background image
        g.drawImage(background, getWidth() / 2 - background.getWidth(null) / 2, yPos, this);

        // Draw the "1 PLAYER" text
        g.drawString("1 PLAYER", getWidth() / 2 - 56, yPos + background.getHeight(null) + 50);

        // Draw additional components if animation has completed
        drawMenuComponents(g);
    }

    private void drawMenuComponents(Graphics g) {
        g.drawImage(tank, getWidth() / 2 - 90, yPos + background.getHeight(null) + 25, this);
        g.drawString("PRESS ENTER", getWidth() / 2 - 80, getHeight() * 4 / 5 + 25);
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
