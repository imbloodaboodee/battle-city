package entities.PowerUps;

public class ShovelPowerUp extends PowerUp {
    public ShovelPowerUp(int x, int y) {
        super(x, y);
        loadImage("src/assets/image/powerup_shovel.png");
        getImageDimensions();
        setType(11);
        imagePath = "src/assets/image/powerup_shovel.png";
    }
}
