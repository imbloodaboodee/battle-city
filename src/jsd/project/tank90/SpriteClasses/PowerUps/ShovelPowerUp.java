package jsd.project.tank90.SpriteClasses.PowerUps;

import SpriteClasses.Brick;
import SpriteClasses.Steel;
import SpriteClasses.Block;

import java.util.TimerTask;
import java.util.Timer;

import java.util.concurrent.CopyOnWriteArrayList;

public class ShovelPowerUp extends PowerUp {

    private static final int SHOVEL_DURATION = 5000; // 10 seconds for ShovelPowerUp

    public ShovelPowerUp(int x, int y) {
        super(x, y);
        loadImage("src/jsd/project/tank90/assets/image/powerup_shovel.png");
        getImageDimensions();
        setType(11); // Set power-up type ID
        imagePath = "src/jsd/project/tank90/assets/image/powerup_shovel.png";
    }

    public void activate(CopyOnWriteArrayList<Block> blocks) {
        // Remove all brick blocks around the base
        removeBlocks(blocks);

        // Replace with steel blocks
        addSteelBlocks(blocks);

        // Set a timer to replace steel blocks with brick blocks after SHOVEL_DURATION
        Timer shovelTimer = new Timer();
        shovelTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                removeBlocks(blocks);
                addBrickBlocks(blocks);
            }
        }, SHOVEL_DURATION);
    }

    // Helper method to remove blocks around specific coordinates
    private void removeBlocks(CopyOnWriteArrayList<Block> blocks) {
        blocks.removeIf(block ->
                (block.getX() >= 192 && block.getX() <= 256 && block.getY() == 384) ||
                        (block.getX() >= 192 && block.getX() <= 208 && block.getY() >= 400 && block.getY() <= 416) ||
                        (block.getX() >= 240 && block.getX() <= 256 && block.getY() >= 400 && block.getY() <= 416)
        );
    }

    // Helper method to add steel blocks at specific coordinates
    private void addSteelBlocks(CopyOnWriteArrayList<Block> blocks) {
        for (int x = 208; x <= 256; x += 16) {
            blocks.add(new Steel(x, 384));
        }
        for (int y = 400; y <= 416; y += 16) {
            blocks.add(new Steel(208, y));
            blocks.add(new Steel(256, y));
        }
    }

    // Helper method to add brick blocks at specific coordinates
    private void addBrickBlocks(CopyOnWriteArrayList<Block> blocks) {
        for (int x = 208; x <= 256; x += 16) {
            blocks.add(new Brick(x, 384));
        }
        for (int y = 400; y <= 416; y += 16) {
            blocks.add(new Brick(208, y));
            blocks.add(new Brick(256, y));
        }
    }




}
