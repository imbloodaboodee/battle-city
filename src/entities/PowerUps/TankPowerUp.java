package entities.PowerUps;

public class TankPowerUp extends PowerUp {
    public TankPowerUp(int x, int y) {
        super(x, y);
        loadImage("src/assets/image/powerup_tank.png");
        getImageDimensions();
        setType(7);
        imagePath = "src/assets/image/powerup_tank.png";
    }

}
