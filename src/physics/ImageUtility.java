package physics;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * A class that loads the lives image.
 */
public class ImageUtility {
    // Instance variable for the lives image
    private final Image lives, enemyIcon;
    private static ImageUtility instance;
    private final Image background, tank;

    /**
     * Get the instance of the ImageUtility
     *
     * @return instance
     */
    public static ImageUtility getInstance() {
        if (instance == null) {
            instance = new ImageUtility();
        }
        return instance;
    }

    /**
     * Private constructor to initialize the lives image
     */
    private ImageUtility() {
        lives = loadImage("src/assets/image/lives.png");
        enemyIcon = loadImage("src/assets/image/enemy.png");
        background = loadImage("src/assets/image/battle_city.png");
        tank = loadImage("src/assets/image/playerTank_right.png");

    }

    /**
     * Load image given the image address
     *
     * @param imageAddress image address to the required image
     * @return image
     */
    private Image loadImage(String imageAddress) {
        ImageIcon icon = new ImageIcon(imageAddress);
        return icon.getImage();
    }

    public Image getLives() {
        return lives;
    }

    public Image getEnemyIcon() {
        return enemyIcon;
    }

    public Image getBackground() {
        return background;
    }

    public Image getTank() {
        return tank;
    }

}
