package render;

import SpriteClasses.*;
import constants.GameConstants;
import entities.*;
import environment.BlockType;
import environment.MapLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GameScreen extends JPanel {
    private static GameScreen instance;  // Static instance for Singleton
    public static ArrayList<Block> blocks = new ArrayList<>();
    public PlayerTankRender ptRenderer = new PlayerTankRender(new PlayerTank(BulletType.NORMAL), this);
    private static int stage = 10;
    private Timer gameLoopTimer;
    public DumbTank st = new DumbTank( BulletType.NORMAL);
    public DumbTankRender dumbTankRender = new DumbTankRender(st, this);

    // Private constructor to prevent external instantiation
    private GameScreen() {
        this.setVisible(true);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setLayout(null);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                ptRenderer.getPlayerTank().keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                ptRenderer.getPlayerTank().keyReleased(e);
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ptRenderer.getPlayerTank().mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ptRenderer.getPlayerTank().mouseReleased(e);
            }
        });

        initBlocks();
        gameLoopTimer = new Timer(GameConstants.DELAY, e -> {
            ptRenderer.getPlayerTank().updateTankPosition();
            repaint();
        });
        gameLoopTimer.start();
    }

    // Public method to get the single instance
    public static GameScreen getInstance() {
        if (instance == null) {
            instance = new GameScreen();
        }
        return instance;
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

        for (Block block : GameScreen.blocks) {
            if (block.getType() != BlockType.TREE.getValue()) {
                g2d.drawImage(block.getImage(), block.getX(), block.getY(), this);
            }
        }

        ptRenderer.paintComponent(g2d);
        dumbTankRender.paintComponent(g2d);

        for (Block block : GameScreen.blocks) {
            if (block.getType() == BlockType.TREE.getValue()) {
                g2d.drawImage(block.getImage(), block.getX(), block.getY(), this);
            }
        }
        Toolkit.getDefaultToolkit().sync();
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public PlayerTankRender getPlayerTankRender() {
        return ptRenderer;
    }


    public static int getStage() {
        return stage;
    }
}
