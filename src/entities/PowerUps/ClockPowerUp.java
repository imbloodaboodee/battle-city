package entities.PowerUps;

public class ClockPowerUp extends PowerUp {
    public ClockPowerUp(int x, int y) {
        super(x, y);
        loadImage("src/assets/image/powerup_timer.png");
        getImageDimensions();
        setType(10);
        imagePath = "src/assets/image/powerup_timer.png";
    }

}
