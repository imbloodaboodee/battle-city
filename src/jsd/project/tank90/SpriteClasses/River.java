/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2016
 *
 * Name: Tongyu Yang, Peter Unrein, Hung Giang
 * Date: Apr 18, 2016
 * Time: 6:32:12 PM
 *
 * Project: csci205FinalProject
 * Package: SpriteClasses
 * File: River
 * Description: River class
 *
 * ****************************************
 */
package jsd.project.tank90.SpriteClasses;

public class River extends Block {

    long lastImage = 0;
    boolean lastLoad = false;

    @Override
    public void updateAnimation() {
        if ((System.currentTimeMillis() - lastImage) > 500) {

            if (lastLoad) {
                loadImage("src/jsd/project/tank90/assets/image/water_1.png");
                lastImage = System.currentTimeMillis();
                lastLoad = false;
            } else {
                loadImage("src/jsd/project/tank90/assets/image/water_2.png");
                lastImage = System.currentTimeMillis();

                lastLoad = true;
            }
        }

    }

    public River(int x, int y) {
        super(x, y);
        loadImage("src/jsd/project/tank90/assets/image/water_1.png");
        getImageDimensions();
        setHealth(1);
        setType(4);
    }

}
