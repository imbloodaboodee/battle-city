package render;

import SpriteClasses.*;
import entities.BulletType;
import entities.SmartTank;
import entities.PlayerTank;
import entities.Tank;
import entities.PowerUps.PowerUp;
import environment.BlockType;
import environment.MapLoader;
import physics.BoardUtility;
import physics.CollisionHandling;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GameScreen extends JPanel {
    public static ArrayList<Block> blocks = new ArrayList<>();
    public static ArrayList<SmartTank> enemyTanks = new ArrayList<>(); // To hold multiple SmartTanks
    public static PlayerTank pt = new PlayerTank(new Tank(), BulletType.NORMAL);
    public static SmartTank st = new SmartTank(new Tank(120,120), BulletType.NORMAL);
    private static int stage = 10;
    private Timer gameLoopTimer;
    private Timer powerUpSpawnTimer;

    public GameScreen() {
        // Add PlayerTank and SmartTank to the panel for automatic drawing
        this.add(pt);
        this.add(st);
        enemyTanks.add(st); // Add SmartTank to the enemyTanks list

        this.setVisible(true);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setLayout(null);

        // Add key listener for player tank control
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

        // Initialize blocks, start the game loop, and power-up spawner
        initBlocks();
        initGameLoop();
        initPowerUpSpawner();
    }

    private void initBlocks() {
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

    private void initGameLoop() {
        // Game loop to update the game state
        gameLoopTimer = new Timer(16, e -> {
            // Check for collisions with PowerUps
            BoardUtility.checkTankPowerUpCollision(pt.getTank(), enemyTanks);
            for (SmartTank enemyTank : enemyTanks) {
                CollisionHandling.checkCollisionBulletsTank(enemyTank.getTank().getBullets(), pt.getTank());
            }

            // Kiểm tra va chạm giữa đạn của PlayerTank và enemy tanks
            CollisionHandling.checkCollisionBulletsTankAI(pt.getTank().getBullets(), enemyTanks);

            CollisionHandling.checkCollisionTankTankAI(pt.getTank(), enemyTanks);

            // Ensure the panel is repainted to reflect changes in SmartTank and PowerUp states
            repaint();
        });
        gameLoopTimer.start();
    }

    private void initPowerUpSpawner() {
        // Timer to spawn PowerUps periodically
        powerUpSpawnTimer = new Timer(1000, e -> BoardUtility.spawnRandomPowerUp()); // Spawn every second
        powerUpSpawnTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Draw blocks on the game screen
        for (Block block : blocks) {
            if (block.isVisible()) {
                g2d.drawImage(block.getImage(), block.getX(), block.getY(), this);
                block.setHitbox(new Rectangle(block.getX(), block.getY(), 16, 16));
                g2d.setColor(Color.RED);
                g2d.draw(block.getHitbox());
            }
        }

        // Draw PowerUps
        for (PowerUp powerUp : BoardUtility.getPowerUps()) {
            g2d.drawImage(powerUp.getImage(), powerUp.getX(), powerUp.getY(), this);
            g2d.setColor(Color.YELLOW);
            g2d.draw(powerUp.getHitbox());
        }

        // Sync the graphics
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public ArrayList<SmartTank> getEnemyTanks() {
        return enemyTanks;
    }
}
