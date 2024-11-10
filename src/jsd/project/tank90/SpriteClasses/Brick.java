/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2016
 *
 * Name: Tongyu Yang, Peter Unrein, Hung Giang
 * Date: Apr 18, 2016
 * Time: 5:05:07 PM
 *
 * Project: csci205FinalProject
 * Package: scratch
 * File: Brick
 * Description: Brick class
 *
 * ****************************************
 */
package jsd.project.tank90.SpriteClasses;


public class Brick extends Block {
    public Brick(int x, int y) {
        super(x, y);
        loadImage("src/jsd/project/tank90/assets/image/wall_brick.png");
        getImageDimensions();
        setType(1);
        setHealth(1);
    }

}
