package render;

import SpriteClasses.*;
import constants.GameConstants;
import entities.BulletType;
import entities.PlayerTank;
import entities.Tank;
import environment.BlockType;
import environment.MapLoader;
import physics.CollisionHandling;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GameScreen extends JPanel {
    public GameFrame theView;
    public static ArrayList<Block> blocks = new ArrayList<>();
    private static int stage = 1;
    private Timer gameLoopTimer;

    public GameScreen(GameFrame theView) {
        this.theView = theView;
        PlayerTank pt = new PlayerTank(new Tank(), BulletType.NORMAL);
        this.add(pt);
        this.setVisible(true);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setLayout(null);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pt.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pt.keyReleased(e);
            }
        });
        initBlocks();
        gameLoopTimer = new Timer(GameConstants.DELAY, e -> {
            CollisionHandling.checkMovingCollisions(pt.getTank(), blocks);  // Check for collisions with blocks and bullets
        });
        gameLoopTimer.start();
    }

    public void initBlocks() {
        int[][] map = MapLoader.getMap(stage);
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
        Graphics2D g2d = (Graphics2D) g.create();
        for (Block block : blocks) {
            if (block.isVisible()) {
                g2d.drawImage(block.getImage(), block.getX(), block.getY(), this);
                block.setHitbox(new Rectangle(block.getX(), block.getY(), 16, 16));
                g2d.setColor(Color.RED);
                g2d.draw(block.getHitbox());
            }
        }
        Toolkit.getDefaultToolkit().sync();
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }
}