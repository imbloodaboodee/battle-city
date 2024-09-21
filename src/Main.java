//import entities.BulletType;
//import entities.PlayerTankControl;
//import entities.Tank;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class Main {
//    public static void main(String[] args) {
//        Tank t = new Tank();
//        PlayerTankControl ptm = new PlayerTankControl(new Tank(), BulletType.NORMAL);
//        JFrame frame = new JFrame("Battle City");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(1000, 1000);
//        frame.setBackground(Color.black);
//        frame.add(ptm);
//        frame.setVisible(true);
//    }
//}

//function main to test map
import environment.GameView;
import environment.Menu;

public class Main {
    public static void main(String[] args) {
        GameView theView = new GameView();
        Menu menu = new Menu(theView);
        theView.getGamePanel().add(menu);
        theView.setVisible(true);
    }
}



