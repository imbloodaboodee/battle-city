import render.GameFrame;
import render.GameScreen;

public class Main {
    public static void main(String[] args) {
        GameFrame gameFrame = new GameFrame();
        GameScreen gameScreen = GameScreen.getInstance();
        gameFrame.getGamePanel().add(gameScreen);
        gameFrame.setVisible(true);
    }
}