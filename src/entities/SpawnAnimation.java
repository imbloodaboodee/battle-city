package entities;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class SpawnAnimation extends Animation {

    public SpawnAnimation(int x, int y, int frameDelay, double ratioResize, boolean loop) {
        super(x, y, loadExplosionFrames(), frameDelay, ratioResize, loop);
    }

    private static BufferedImage[] loadExplosionFrames() {
        BufferedImage[] frames = new BufferedImage[2];
        try {
            frames[0] = ImageIO.read(new File("./src/assets/image/spawn_1.png"));
            frames[1] = ImageIO.read(new File("./src/assets/image/spawn_2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return frames;
    }
}