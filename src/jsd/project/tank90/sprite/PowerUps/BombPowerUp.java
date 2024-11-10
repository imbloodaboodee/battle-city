package jsd.project.tank90.sprite.PowerUps;

public class BombPowerUp extends PowerUp {
    public BombPowerUp(int x, int y) {
        super(x, y);
        loadImage("src/jsd/project/tank90/assets/image/powerup_grenade.png");
        getImageDimensions();
        setType(9);
        imagePath = "src/jsd/project/tank90/assets/image/powerup_grenade.png";
    }

}
