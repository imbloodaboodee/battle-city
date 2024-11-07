package entities.PowerUps;

import SpriteClasses.Brick;
import SpriteClasses.Steel;
import environment.BlockType;
import SpriteClasses.Block;
import java.util.ArrayList;

public class ShovelPowerUp extends PowerUp {

    private static final int SHOVEL_DURATION = 5000; // 10 seconds for ShovelPowerUp

    public ShovelPowerUp(int x, int y) {
        super(x, y);
        loadImage("src/assets/image/powerup_shovel.png");
        getImageDimensions();
        setType(11); // Set power-up type ID
        imagePath = "src/assets/image/powerup_shovel.png";
    }

    public void activate(ArrayList<Block> blocks) {
        // Step 1: Find all bricks around the base (using predefined coordinates)
        ArrayList<Block> affectedBlocks = new ArrayList<>();

        for (Block block : blocks) {
            // Kiểm tra điều kiện cho 3 phạm vi tọa độ đã nêu
            if ((block.getX() >= 192 && block.getX() <= 256 && block.getY() == 384) ||  // Điều kiện 1
                    (block.getX() >= 192 && block.getX() <= 208 && block.getY() >= 400 && block.getY() <= 416) ||  // Điều kiện 2
                    (block.getX() >= 240 && block.getX() <= 256 && block.getY() >= 400 && block.getY() <= 416)) {  // Điều kiện 3
                affectedBlocks.add(block);
            }
        }

        // Step 2: Replace brick blocks with steel blocks
        for (int i = 0; i < affectedBlocks.size(); i++) {
            Block block = affectedBlocks.get(i);
            // Create a new Steel block at the same position as the old block
            Block steelBlock = new Steel(block.getX(), block.getY());
            blocks.remove(block); // Remove the old brick block
            blocks.add(steelBlock); // Add the new steel block
            System.out.println("Brick block changed to steel.");
        }

        // Step 3: Set a timer to revert steel blocks back to brick after SHOVEL_DURATION
        new Thread(() -> {
            try {
                // Wait for the duration of the shovel power-up (10 seconds)
                Thread.sleep(SHOVEL_DURATION);

                // After 10 seconds, replace steel blocks with brick blocks
                for (int i = 0; i < affectedBlocks.size(); i++) {
                    Block block = affectedBlocks.get(i);
                    // Create a new Brick block at the same position as the steel block
                    Block brickBlock = new Brick(block.getX(), block.getY());
                    brickBlock.setType(BlockType.BRICK.getValue());
                    blocks.remove(block); // Remove the steel block
                    blocks.add(brickBlock); // Add the new brick block
                    System.out.println("Steel block reverted back to brick.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }



}
