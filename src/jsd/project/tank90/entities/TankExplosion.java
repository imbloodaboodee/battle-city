package jsd.project.tank90.entities;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class TankExplosion extends Animation {

    public TankExplosion(int x, int y, int frameDelay, double ratioResize, boolean loop) {
        super(x, y, loadExplosionFrames(), frameDelay, ratioResize, loop);
    }

    private static BufferedImage[] loadExplosionFrames() {
        BufferedImage[] frames = new BufferedImage[5];
        try {
            frames[0] = ImageIO.read(new File("./src/jsd/project/tank90/assets/image/tank_explosion_1.png"));
            frames[1] = ImageIO.read(new File("./src/jsd/project/tank90/assets/image/tank_explosion_2.png"));
            frames[2] = ImageIO.read(new File("./src/jsd/project/tank90/assets/image/tank_explosion_3.png"));
            frames[3] = ImageIO.read(new File("./src/jsd/project/tank90/assets/image/tank_explosion_4.png"));
            frames[4] = ImageIO.read(new File("./src/jsd/project/tank90/assets/image/tank_explosion_5.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return frames;
    }
}
