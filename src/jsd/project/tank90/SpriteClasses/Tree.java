/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2016
 *
 * Name: Tongyu Yang, Peter Unrein, Hung Giang, Adrian Berg
 * Date: Apr 21, 2016
 * Time: 10:47:34 PM
 *
 * Project: csci205FinalProject
 * Package: SpriteClasses
 * File: Tree
 * Description: Tree class
 *
 * ****************************************
 */
package jsd.project.tank90.SpriteClasses;

/**
 * Tree is a block with type 5 and health 1
 *
 * @param int x represents the starting x location in pixels
 * @param int y represents the starting y location in pixels
 * @author Adrian Berg
 */
public class Tree extends Block {
    public Tree(int x, int y) {
        super(x, y);
        loadImage("src/jsd/project/tank90/assets/image/trees.png");
        getImageDimensions();
        setType(5);
        setHealth(1);
    }

}
