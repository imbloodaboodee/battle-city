package entities.PowerUps;

public class BombPowerUp extends PowerUp {
    public BombPowerUp(int x, int y) {
        super(x, y);
        loadImage("src/assets/image/powerup_grenade.png");
        getImageDimensions();
        setType(9);
        imagePath = "src/assets/image/powerup_grenade.png";
    }

}
