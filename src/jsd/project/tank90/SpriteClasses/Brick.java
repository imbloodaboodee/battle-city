package jsd.project.tank90.SpriteClasses;

public class Brick extends Block {
    public Brick(int x, int y) {
        super(x, y);
        loadImage("src/jsd/project/tank90/assets/image/wall_brick.png");
        getImageDimensions();
        setType(1);
        setHealth(1);
    }

}
