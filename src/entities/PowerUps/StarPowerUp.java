package entities.PowerUps;

public class StarPowerUp extends PowerUp {
    public StarPowerUp(int x, int y) {
        super(x, y);
        loadImage("src/assets/image/powerup_star.png");
        getImageDimensions();
        setType(8);
        imagePath = "src/assets/image/powerup_star.png";
    }
}
