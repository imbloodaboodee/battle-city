package physics;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * A class that loads the lives image.
 */
public class ImageUtility {
    // Instance variable for the lives image
    private final Image lives;
    private static ImageUtility instance;

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

    /**
     * Get lives image
     *
     * @return lives
     */
    public Image getLives() {
        return lives;
    }
}
