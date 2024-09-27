package physics;

import SpriteClasses.Block;
import entities.Tank;

import java.awt.*;
import java.util.ArrayList;

public class CollisionHandling {
    public static boolean checkMovingCollisions(Tank tank, ArrayList<Block> blocks) {
        Rectangle tankHitbox = tank.getHitbox();
        ArrayList<Block> blocksCopy = new ArrayList<>(blocks);

        // Check collision with blocks
        for (Block block : blocksCopy) {
            if (tankHitbox.intersects(block.getHitbox())) {
                System.out.println("Collision detected with block!");
                // Handle block collision logic here
                return true;
            }
        }
        return false;


    }

}
