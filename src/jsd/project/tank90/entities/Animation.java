package jsd.project.tank90.entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.Timer;

public abstract class Animation {
    protected int x, y;
    protected BufferedImage[] frames;
    protected int currentFrame = 0;
    protected boolean finished = false;
    private boolean loop;  // Determines if the animation should loop continuously

    /**
     * Constructor for Animation, which initializes position, frame delay, and looping behavior.
     */
    public Animation(int x, int y, BufferedImage[] frames, int frameDelay, double resizeRatio, boolean loop) {
        this.x = x;
        this.y = y;
        this.frames = resizeFrames(frames, resizeRatio);
        this.loop = loop;

        // Timer to progress frames every frameDelay milliseconds
        new Timer(frameDelay, e -> updateFrame()).start();
    }

    private void updateFrame() {
        currentFrame++;
        if (currentFrame >= frames.length) {
            if (isLoop()) {
                currentFrame = 0;  // Reset to the first frame if looping
            } else {
                finished = true;  // Mark animation as finished if not looping
            }
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public void draw(Graphics g) {
        if (!isFinished() && currentFrame < frames.length) {
            g.drawImage(frames[currentFrame], x, y, null);
        }
    }


    private BufferedImage[] resizeFrames(BufferedImage[] frames, double resizeRatio) {
        BufferedImage[] resizedFrames = new BufferedImage[frames.length];

        for (int i = 0; i < frames.length; i++) {
            int newWidth = (int) (frames[i].getWidth() * resizeRatio);
            int newHeight = (int) (frames[i].getHeight() * resizeRatio);

            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, frames[i].getType());
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.drawImage(frames[i].getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
            g2d.dispose();

            resizedFrames[i] = resizedImage;
        }

        return resizedFrames;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
