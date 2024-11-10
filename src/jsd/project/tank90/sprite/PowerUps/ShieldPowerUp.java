package jsd.project.tank90.sprite.PowerUps;

public class ShieldPowerUp extends PowerUp {
    public ShieldPowerUp(int x, int y) {
        super(x, y);
        loadImage("src/jsd/project/tank90/assets/image/powerup_helmet.png");
        getImageDimensions();
        setType(12);
        imagePath = "src/jsd/project/tank90/assets/image/powerup_helmet.png";
    }
}
