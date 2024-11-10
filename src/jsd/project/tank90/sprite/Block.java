package jsd.project.tank90.sprite;

public class Block extends Sprite {
    public int health = 1;
    private int type;

    public Block(int x, int y) {
        super(x, y);

    }

    public Block(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void lowerHealth() {
        health -= 1;
    }

    public int currentHealth() {
        return health;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void updateAnimation() {

    }

}
