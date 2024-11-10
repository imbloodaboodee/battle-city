package jsd.project.tank90.SpriteClasses.PowerUps;

public class StarPowerUp extends PowerUp {
    public StarPowerUp(int x, int y) {
        super(x, y);
        loadImage("src/jsd/project/tank90/assets/image/powerup_star.png");
        getImageDimensions();
        setType(8);
        imagePath = "src/jsd/project/tank90/assets/image/powerup_star.png";
    }
}
