package jsd.project.tank90.physics;

import java.awt.Image;
import javax.swing.ImageIcon;

public class ImageUtility {
    private final Image lives, flagIcon, enemyIcon, starIcon;
    private final Image arrow, tankBasic, tankFast, tankPower, tankArmor;
    private static ImageUtility instance;
    private final Image background, tank;
    public static ImageUtility getInstance() {
        if (instance == null) {
            instance = new ImageUtility();
        }
        return instance;
    }

    private ImageUtility() {
        lives = loadImage("src/jsd/project/tank90/assets/image/lives.png");
        enemyIcon = loadImage("src/jsd/project/tank90/assets/image/enemy.png");
        background = loadImage("src/jsd/project/tank90/assets/image/battle_city.png");
        tank = loadImage("src/jsd/project/tank90/assets/image/playerTank_right.png");
        starIcon = loadImage("src/jsd/project/tank90/assets/image/powerup_star.png");
        tankBasic = loadImage("src/jsd/project/tank90/assets/image/tank_basic.png");
        tankFast = loadImage("src/jsd/project/tank90/assets/image/tank_fast.png");
        tankPower = loadImage("src/jsd/project/tank90/assets/image/tank_power.png");
        tankArmor = loadImage("src/jsd/project/tank90/assets/image/tank_armor.png");
        arrow = loadImage("src/jsd/project/tank90/assets/image/arrow.png");
        flagIcon = loadImage("src/jsd/project/tank90/assets/image/flag.png");
    }

    private Image loadImage(String imageAddress) {
        ImageIcon icon = new ImageIcon(imageAddress);
        return icon.getImage();
    }

    public Image getLives() {
        return lives;
    }

    public Image getEnemyIcon() {
        return enemyIcon;
    }

    public Image getBackground() {
        return background;
    }

    public Image getTank() {
        return tank;
    }


    public Image getTankBasic() {
        return tankBasic;
    }

    public Image getTankFast() {
        return tankFast;
    }


    public Image getTankPower() {
        return tankPower;
    }

    public Image getTankArmor() {
        return tankArmor;
    }

    public Image getArrow() {
        return arrow;
    }

    public Image getFlagIcon() {
        return flagIcon;
    }

    public Image getStarIcon() {
        return starIcon;
    }

}
