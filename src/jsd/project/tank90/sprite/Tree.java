package jsd.project.tank90.sprite;

public class Tree extends Block {
    public Tree(int x, int y) {
        super(x, y);
        loadImage("src/jsd/project/tank90/assets/image/trees.png");
        getImageDimensions();
        setType(5);
        setHealth(1);
    }

}
