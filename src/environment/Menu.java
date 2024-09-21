package environment;

import SpriteClasses.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Menu extends JPanel {
    public GameView theView;
    private ArrayList<Block> blocks = new ArrayList<>();
    private static int stage = 1;

    public Menu(GameView theView) {
        this.theView = theView;
        this.setVisible(true);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setLayout(null);
        initBlocks();
    }

    public void initBlocks() {
        int[][] map = Map.getMap(stage);
        int type;
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                type = map[x][y];
                BlockType bType = BlockType.getTypeFromInt(type);
                switch (bType) {
                    case BRICK:
                        blocks.add(new Brick(y * 16, x * 16));
                        break;
                    case STEEL:
                        blocks.add(new Steel(y * 16, x * 16));
                        break;
                    case BASE:
                        blocks.add(new Base(y * 16, x * 16));
                        break;
                    case RIVER:
                        blocks.add(new River(y * 16, x * 16));
                        break;
                    case TREE:
                        blocks.add(new Tree(y * 16, x * 16));
                        break;
                    case EDGE:
                        blocks.add(new Edge(y * 16, x * 16));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawObjects(g);
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawObjects(Graphics g) {
        for (Block a : blocks) {
            if (a.isVisible()) {
                g.drawImage(a.getImage(), a.getX(), a.getY(), this);
            }
        }
    }
}