package physics;

import SpriteClasses.Block;
import entities.Bullet;
import entities.Tank;
import environment.BlockType;  // Import BlockType

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class CollisionHandling {

    public static boolean checkMovingCollisions(Tank tank, ArrayList<Block> blocks) {
        Rectangle tankHitbox = tank.getHitbox();
        ArrayList<Block> blocksCopy = new ArrayList<>(blocks);

        for (Block block : blocksCopy) {
            if (tankHitbox.intersects(block.getHitbox()) && block.getType() != 5) {
                System.out.println("Collision detected with block!");
                return true;
            }
        }
        return false;
    }

    public static void checkCollisionBulletsBlocks(ArrayList<Bullet> bullets, ArrayList<Block> blocks) {
        for (int x = 0; x < bullets.size(); x++) {
            Bullet b = bullets.get(x);
            Rectangle2D.Double bulletHitbox = b.getHitbox();

            for (int i = 0; i < blocks.size(); i++) {
                Block block = blocks.get(i);
                Rectangle blockHitbox = block.getHitbox();
                BlockType blockType = BlockType.getTypeFromInt(block.getType()); // Convert int to BlockType

                if (bulletHitbox.intersects(blockHitbox)) {
                    System.out.println("Collision detected with block of type: " + blockType);
                    CollisionBulletsBlocksHelper(bullets, blocks, x, i, blockType);
                    break;  // Exit loop after handling collision
                }
            }
        }
    }

    private static void CollisionBulletsBlocksHelper(ArrayList<Bullet> bullets, ArrayList<Block> blocks, int bulletIndex, int blockIndex, BlockType blockType) {
        switch (blockType) {
            case RIVER:
            case TREE:
                // Bullet goes through tree; do nothing.
                System.out.println("Bullet passed through tree.");
                break;

            case BRICK:
                // Remove both the bullet and the brick block
                bullets.remove(bulletIndex);
                blocks.remove(blockIndex);
                System.out.println("Bullet and brick block removed after collision.");
                break;

            case EDGE:
            case STEEL:
                // Only remove the bullet; steel block remains
                bullets.remove(bulletIndex);
                System.out.println("Bullet removed after hitting steel block.");
                break;

            default:
                System.out.println("Unknown block type.");
        }
    }
}
