package jsd.project.tank90.SpriteClasses.PowerUps;

import SpriteClasses.Sprite;
import constants.GameConstants;

public class PowerUp extends Sprite {
    long loadTime;
    private int type;
    boolean flip = false;
    String imagePath;

    public PowerUp(int x, int y) {
        super(x, y, GameConstants.BLOCK_SIZE*2, GameConstants.BLOCK_SIZE*2);
        this.imagePath = imagePath;
        loadImage(imagePath);
        getImageDimensions();
        loadTime = System.currentTimeMillis();
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public long getLoadTime() {
        return loadTime;
    }

    public void updateAnimation() {
        long timeDifference = (System.currentTimeMillis() - loadTime);
        if (timeDifference > 5000) {
            if (timeDifference % 10 == 0 && !flip) {
                loadImage("");
                getImageDimensions();
                flip = true;
            } else if (timeDifference % 10 == 0 && flip) {
                loadImage(imagePath);
                getImageDimensions();
                flip = false;
            }
        }
    }

}
