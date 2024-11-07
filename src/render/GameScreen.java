package render;

import SpriteClasses.*;
import constants.GameConstants;
import entities.*;
import entities.BulletType;
import entities.PlayerTank;
import entities.Tank;
import entities.PowerUps.PowerUp;
import environment.BlockType;
import environment.MapLoader;
import manager.TankSpawner;
import physics.BoardUtility;
import physics.CollisionHandling;

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
    public static ArrayList<Tank> enemyTanks = new ArrayList<>(); // To hold multiple SmartTanks
    public PlayerTankRender ptRenderer = new PlayerTankRender(new PlayerTank(BulletType.NORMAL), this);
    private static int stage = 5;
    private Timer gameLoopTimer;
    private Timer powerUpSpawnTimer;
    private TankSpawner tankSpawner = new TankSpawner(enemyTanks, stage);
    private DumbTankRender dumbTankRender;
    private SmartTankRender smartTankRender;
    // Private constructor to prevent external instantiations
    private GameScreen() {
        this.setVisible(true);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setLayout(null);
        // Add key listener for player tank control
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


        // Initialize blocks, start the game loop, and power-up spawner
        tankSpawner.startSpawning();
        initBlocks();
        initGameLoop();
        initPowerUpSpawner();
    }

    // Public method to get the single instance
    public static GameScreen getInstance() {
        if (instance == null) {
            instance = new GameScreen();
        }
        return instance;
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
//            BoardUtility.checkTankPowerUpCollision(ptRenderer.getPlayerTank(), enemyTanks);
            for (Tank enemyTank : enemyTanks) {
//                CollisionHandling.checkCollisionBulletsTank(enemyTank.getBullets(), ptRenderer.getPlayerTank());
            }
            // Kiểm tra va chạm giữa đạn của PlayerTank và enemy tanks
//            CollisionHandling.checkCollisionBulletsTankAI(ptRenderer.getPlayerTank().getBullets(), enemyTanks);

//            CollisionHandling.checkCollisionTankTankAI(ptRenderer.getPlayerTank(), enemyTanks);

            // Ensure the panel is repainted to reflect changes in SmartTank and PowerUp states
            ptRenderer.getPlayerTank().updateTankPosition();
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

        for (Block block : GameScreen.blocks) {
            if (block.getType() != BlockType.TREE.getValue()) {
                g2d.drawImage(block.getImage(), block.getX(), block.getY(), this);
            }
        }

        ptRenderer.paintComponent(g2d);
//        for (DumbTankRender dumbTankRender : dumbTankRenders) {
//            dumbTankRender.paintComponent(g2d);
//        }
        for (Tank enemyTank:enemyTanks){
            if (enemyTank instanceof DumbTank){
            dumbTankRender= new DumbTankRender((DumbTank) enemyTank, this);
            dumbTankRender.paintComponent(g2d);}
            else if(enemyTank instanceof SmartTank){
                smartTankRender = new SmartTankRender((SmartTank) enemyTank, this);
                smartTankRender.paintComponent(g2d);
            }
        }

        for (Block block : GameScreen.blocks) {
            if (block.getType() == BlockType.TREE.getValue()) {
                g2d.drawImage(block.getImage(), block.getX(), block.getY(), this);
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

    public PlayerTankRender getPlayerTankRender() {
        return ptRenderer;
    }


    public static int getStage() {
        return stage;
    }

    public ArrayList<Tank> getEnemyTanks() {
        return enemyTanks;
    }
}
