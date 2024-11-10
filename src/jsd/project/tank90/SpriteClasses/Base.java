package jsd.project.tank90.SpriteClasses;

import jsd.project.tank90.constants.GameConstants;

public class Base extends Block {
    public boolean gameOver = false;

    public Base(int x, int y) {
        super(x, y, GameConstants.BLOCK_SIZE*2, GameConstants.BLOCK_SIZE*2);
        loadImage("src/jsd/project/tank90/assets/image/base.png");
        getImageDimensions();
        setHealth(1);
        setType(3);

    }
}
