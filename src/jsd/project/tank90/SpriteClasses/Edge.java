package jsd.project.tank90.SpriteClasses;

public class Edge extends Block {
    public Edge(int x, int y) {
        super(x, y);
        loadImage("src/jsd/project/tank90/assets/image/edge.png");
        getImageDimensions();
        setType(6);
        setHealth(1);
    }
}
