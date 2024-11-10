package jsd.project.tank90.SpriteClasses.PowerUps;

public class TankPowerUp extends PowerUp {
    public TankPowerUp(int x, int y) {
        super(x, y);
        loadImage("src/jsd/project/tank90/assets/image/powerup_tank.png");
        getImageDimensions();
        setType(7);
        imagePath = "src/jsd/project/tank90/assets/image/powerup_tank.png";
    }

}
