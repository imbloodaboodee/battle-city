import entities.PlayerTankControl;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        PlayerTankControl ptm = new PlayerTankControl();
        JFrame frame = new JFrame("Battle City");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        frame.setBackground(Color.black);
        frame.add(ptm);
        frame.setVisible(true);
    }
}
