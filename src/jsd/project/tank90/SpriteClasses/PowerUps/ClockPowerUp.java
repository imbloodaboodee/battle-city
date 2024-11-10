package jsd.project.tank90.SpriteClasses.PowerUps;

public class ClockPowerUp extends PowerUp {
    public ClockPowerUp(int x, int y) {
        super(x, y);
        loadImage("src/jsd/project/tank90/assets/image/powerup_timer.png");
        getImageDimensions();
        setType(10);
        imagePath = "src/jsd/project/tank90/assets/image/powerup_timer.png";
    }

}
