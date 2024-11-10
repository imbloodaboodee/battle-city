package jsd.project.tank90.SpriteClasses;

public class Steel extends Block {
    public Steel(int x, int y) {
        super(x, y);
        loadImage("src/jsd/project/tank90/assets/image/wall_steel.png");
        getImageDimensions();
        setHealth(1);
        setType(2);
    }
}
