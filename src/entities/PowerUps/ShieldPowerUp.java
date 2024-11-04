package entities.PowerUps;

public class ShieldPowerUp extends PowerUp {
    public ShieldPowerUp(int x, int y) {
        super(x, y);
        loadImage("src/assets/image/powerup_helmet.png");
        getImageDimensions();
        setType(12);
        imagePath = "src/assets/image/powerup_helmet.png";
    }
}
