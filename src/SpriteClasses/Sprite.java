/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2016
 *
 * Name: Tongyu Yang, Peter Unrein, Hung Giang
 * Date: Apr 16, 2016
 * Time: 10:49:27 PM
 *
 * Project: csci205FinalProject
 * Package: scratch
 * File: Sprite
 * Description: Sprite class
 *
 * ****************************************
 */
package SpriteClasses;

/**
 * This sprite classes is TAKEN DIRECTLY FROM ONLINE Here is the link,
 * http://zetcode.com/tutorials/javagamestutorial/collision/ Almost all objects
 * in our game inherit Sprite
 *
 * @author Adrian Berg
 */

import constants.GameConstants;

import javax.swing.*;
import java.awt.*;

public class Sprite {
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean vis;
    private Image image;
    private Rectangle hitbox;

    public Sprite(int x, int y) {

        this.x = x;
        this.y = y;
        vis = true;
        hitbox = new Rectangle(x,y, GameConstants.BLOCK_SIZE, GameConstants.BLOCK_SIZE);
    }
    public Sprite (int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        vis = true;
        hitbox = new Rectangle(x,y, width, height);

    }

    protected void getImageDimensions() {
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    protected void loadImage(String imageName) {
        ImageIcon i = new ImageIcon(imageName);
        image = i.getImage();
    }

    public Image getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisible() {
        return vis;
    }

    public void setVisible(Boolean visible) {
        vis = visible;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }
}
