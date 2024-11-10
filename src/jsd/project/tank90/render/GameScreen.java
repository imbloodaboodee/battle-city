package jsd.project.tank90.render;

import jsd.project.tank90.SpriteClasses.*;
import jsd.project.tank90.SpriteClasses.PowerUps.PowerUp;
import jsd.project.tank90.constants.GameConstants;
import jsd.project.tank90.entities.*;
import jsd.project.tank90.environment.BlockType;
import jsd.project.tank90.environment.MapLoader;
import jsd.project.tank90.manager.GameStateManager;
import jsd.project.tank90.manager.TankSpawner;
import jsd.project.tank90.physics.BoardUtility;
import jsd.project.tank90.physics.CollisionHandling;
import jsd.project.tank90.physics.ImageUtility;
import jsd.project.tank90.physics.SoundUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameScreen extends JPanel {
    private static GameScreen instance;  // Static instance for Singleton

    public static CopyOnWriteArrayList<Block> blocks;
    public static CopyOnWriteArrayList<Tank> enemyTanks; // To hold multiple SmartTanks
    public static ArrayList<Animation> animations;

    public static int stage;
    public static boolean gameOver;
    private int yPos;
    private int direction;
    private final int stopYPos;
    private ImageUtility imageInstance = ImageUtility.getInstance();
    private TankSpawner tankSpawner;

    public PlayerTankRender ptRenderer;
    private DumbTankRender dumbTankRender;
    private SmartTankRender smartTankRender;
    public static ShieldAnimation shieldAnimation;

    public static boolean isSpawning;
    private boolean isPaused;  // Variable to track pause state
    private Runnable onGameOver; // Callback for game over transition

    private Timer gameOverTimer;
    private Timer transitionTimer;
    private Timer gameLoopTimer;


    // Private constructor to prevent external instantiations
    private GameScreen() {
        blocks = new CopyOnWriteArrayList<>();
        enemyTanks = new CopyOnWriteArrayList<>();
        animations = new ArrayList<>();
        ptRenderer = new PlayerTankRender(new PlayerTank(BulletType.RAPID), this);
        stage = 1;
        gameOver = false;
        yPos = MapLoader.BOARD_HEIGHT;
        direction = -1;
        stopYPos = 250;
        tankSpawner = new TankSpawner(enemyTanks, stage);
        isSpawning = false;
        isPaused = false;  // Variable to track pause state
        imageInstance = ImageUtility.getInstance();

        this.setVisible(true);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setLayout(null);
        // Add key listener for player tank control
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameOver) return; // Ignore key presses if game over
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    SoundUtility.pause();
                    togglePause();  // Toggle the pause state when Escape is pressed
                } else {
                    ptRenderer.getPlayerTank().keyPressed(e);
                }
            }


            @Override
            public void keyReleased(KeyEvent e) {
                ptRenderer.getPlayerTank().keyReleased(e);
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isPaused && !gameOver) { // Ignore mouse presses if game over
                    ptRenderer.getPlayerTank().mousePressed(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!gameOver) {  // Ignore mouse releases if game over
                    ptRenderer.getPlayerTank().mouseReleased(e);
                }
            }
        });

        // Initialize blocks, start the game loop, and power-up spawner
        tankSpawner.startSpawning();
        initBlocks();
        initGameLoop();
    }

    public void togglePause() {
        if (isPaused) {
            gameLoopTimer.start();  // Resume the game timer
            isPaused = false;
        } else {
            gameLoopTimer.stop();  // Pause the game timer
            isPaused = true;
        }
    }

    // Public method to get the single instance
    public static GameScreen getInstance() {
        if (instance == null) {
            instance = new GameScreen();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null; // Reset the singleton instance
    }

    public void setOnGameOver(Runnable onGameOver) {
        this.onGameOver = onGameOver;
    }

    public void initBlocks() {
        int[][] map = MapLoader.getMap(stage);
        SoundUtility.startStage();
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
        // Giả sử bạn đã có danh sách blocks từ nơi khác trong game (ví dụ từ lớp Board)

        // Game loop to update the game state
        gameLoopTimer = new Timer(20, e -> {
            if (ptRenderer.getPlayerTank().isShield()) {
                if (shieldAnimation == null || shieldAnimation.isFinished()) {
                    shieldAnimation = new ShieldAnimation(ptRenderer.getPlayerTank().getX(),
                            ptRenderer.getPlayerTank().getY(), 100, 1, true);
                }
                shieldAnimation.setLoop(true);
            } else {
                if (shieldAnimation != null) {
                    shieldAnimation.setLoop(false);  // Stop the animation when shield is inactive
                    shieldAnimation = null;
                }
            }

            // Check for collisions with PowerUps
            BoardUtility.checkTankPowerUpCollision(ptRenderer.getPlayerTank(), enemyTanks, blocks);  // Truyền blocks vào đây

            for (Tank enemyTank : enemyTanks) {
                CollisionHandling.checkCollisionBulletsTank(enemyTank.getBullets(), ptRenderer.getPlayerTank());
                enemyTank.bumpMove();
                enemyTank.shoot(enemyTank.getBaseImage());
                enemyTank.getBulletManager().updateBullets();
                CollisionHandling.checkCollisionTankTankAI(ptRenderer.getPlayerTank(), enemyTanks);
                if (CollisionHandling.isBulletTouchingBase(enemyTank.getBullets(), blocks))
                    gameOver();
            }
            CollisionHandling.checkCollisionBulletsTankAI(ptRenderer.getPlayerTank().getBullets(), enemyTanks);

            // Ensure the panel is repainted to reflect changes in SmartTank and PowerUp states
            ptRenderer.getPlayerTank().updateTankPosition();
            ptRenderer.getPlayerTank().getBulletManager().updateBullets();
            ptRenderer.getPlayerTank().checkShieldStatus();
            GameStateManager.checkTankDestroyed();
            checkHealth(ptRenderer.getPlayerTank());
            repaint();
        });
        gameLoopTimer.start();
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
        if (ptRenderer.getPlayerTank().isShield() && shieldAnimation != null) {
            shieldAnimation.setPosition(ptRenderer.getPlayerTank().getX() - 3,
                    ptRenderer.getPlayerTank().getY() - 4);  // Update position to follow the tank
            shieldAnimation.draw(g2d);
        }
        for (Tank enemyTank : enemyTanks) {
            if (enemyTank instanceof DumbTank) {
                dumbTankRender = new DumbTankRender((DumbTank) enemyTank, this);
                dumbTankRender.paintComponent(g2d);
            } else {
                smartTankRender = new SmartTankRender((SmartTank) enemyTank, this);
                smartTankRender.paintComponent(g2d);
            }

        }


        for (Block block : GameScreen.blocks) {
            if (block.getType() == BlockType.TREE.getValue()) {
                g2d.drawImage(block.getImage(), block.getX(), block.getY(), this);
            }
        }
        for (Animation animation : new ArrayList<>(animations)) {
            animation.draw(g);
            if (animation.isFinished()) {
                animations.remove(animation);
            }
        }
        // Vẽ PowerUps chỉ khi chúng đang hiển thị
        for (PowerUp powerUp : BoardUtility.getPowerUps()) {
            if (powerUp.isVisible()) {
                g2d.drawImage(powerUp.getImage(), powerUp.getX(), powerUp.getY(), this);
                g2d.setColor(Color.YELLOW);
                g2d.draw(powerUp.getHitbox());
            }
        }
        // draw the lives
        int lives = PlayerTank.getLives();
        Image liveIcon = imageInstance.getLives();
        int initX = 29;
        g.drawImage(liveIcon, initX * 16, 17 * 16, this);

        Font originalFont = g.getFont();
        Font largeBoldFont = new Font("Arial", Font.BOLD, 20);
        g.setFont(largeBoldFont);
        g.drawString(String.valueOf(lives < 0 ? 0 : lives), (initX + 1) * 16, 18 * 16);
        Image flagIcon = imageInstance.getFlagIcon();
        g.drawImage(flagIcon, initX * 16, 22 * 16, this);
        g.drawString(String.valueOf(stage), (initX + 1) * 16, 25 * 16);
        g.setFont(originalFont);

        // draw the enemyIcon
        int totalEnemyTanks = TankSpawner.getTotalEnemyTanks();
        ImageUtility imageInstance = ImageUtility.getInstance();
        Image enemyIcon = imageInstance.getEnemyIcon();

        // Vẽ enemyIcon
        g.drawImage(enemyIcon, initX * 16, 5 * 16, this);
        g.setFont(largeBoldFont);
        g.drawString(String.valueOf(totalEnemyTanks < 0 ? 0 : totalEnemyTanks), (initX + 1) * 16, 6 * 16);
        g.setFont(originalFont);


        // Draw the starIcon
        Image starIcon = imageInstance.getStarIcon();
        int starIconX = initX * 16;
        int starIconY = 11 * 16;
        g.drawImage(starIcon, starIconX, starIconY, this);

        // Retrieve the current star level from BoardUtility
        int currentStarLevel = BoardUtility.getStarLevel();

        // Draw the star level next to the icon
        g.setFont(originalFont);
        g.setColor(Color.WHITE);  // Set the color for the tier level text
        g.drawString("TIER: " + currentStarLevel, starIconX - 6, starIconY + 3*16); // Adjust position as needed

        if (gameOver) {
            Font font = loadFont();
            g.setFont(font);
            g.setColor(Color.RED);
            g.drawString("GAME OVER", MapLoader.BOARD_WIDTH / 2 - 85, yPos);
        }

        // Sync the graphics
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    public void checkHealth(PlayerTank playerTank) {
        // Kiểm tra nếu sức khỏe của tank <= 0
        if (playerTank.getHealth() <= 0) {
            PlayerTank.lives -= 10;  // Giảm mạng của người chơi
            BoardUtility.resetPowerLevel();
            if (PlayerTank.lives > 0) {
                GameScreen.animations.add(new TankExplosion(playerTank.getX(), playerTank.getY(), 50, 1, false));
                SoundUtility.explosion2();
                playerTank.activateShield(3000); // Kích hoạt shield nếu còn mạng
                playerTank.resetPosition(); // Đặt lại vị trí của tank
                playerTank.setHealth(GameConstants.PLAYER_MAX_HEALTH); // Đặt lại sức khỏe
            } else {
                GameScreen.animations.add(new TankExplosion(playerTank.getX(), playerTank.getY(), 50, 1, false));
                playerTank.setX(-300);
                playerTank.setY(0);
                gameOver();
                System.out.println("Game Over");
                // Dừng vòng lặp game nếu game over
                // gameLoopTimer.stop(); // Cần tham chiếu đến gameLoopTimer hoặc dừng game theo cách khác
            }
        }
    }

    public static Font loadFont() {
        Font font = null;
        try {
            font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT,
                    new File("src/jsd/project/tank90/assets/font/prstart.ttf"));
            font = font.deriveFont(java.awt.Font.PLAIN, 15);
            GraphicsEnvironment ge
                    = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);

        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return font;
    }

    public void gameOver() {
        if (gameOver) {
            return; // Prevent restarting the game over process if already active
        }

        gameOver = true; // Set game over state

        // Start the "Game Over" animation timer
        gameOverTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                yPos += direction;
                if (yPos == stopYPos) {
                    direction = 0; // Stop the vertical movement when reaching stopYPos
                    gameOverTimer.stop(); // Stop the gameOver animation timer

                    // Start the transition timer after animation completes
                    transitionTimer = new Timer(3000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (onGameOver != null) {
                                onGameOver.run(); // Trigger the game over transition
                            }
                        }
                    });
                    transitionTimer.setRepeats(false);
                    transitionTimer.start();
                } else if (yPos > getHeight()) {
                    yPos = getHeight();
                } else if (yPos < 0) {
                    yPos = 0;
                    direction *= -1;
                }
                repaint(); // Repaint to update the "GAME OVER" text position
            }
        });
        gameOverTimer.setRepeats(true);
        gameOverTimer.setCoalesce(true);
        gameOverTimer.start();
        gameLoopTimer.stop();
    }

    public CopyOnWriteArrayList<Block> getBlocks() {
        return blocks;
    }

    public PlayerTankRender getPlayerTankRender() {
        return ptRenderer;
    }


    public static int getStage() {
        return stage;
    }

    public static void setStage(int stage) {
        GameScreen.stage = stage;
    }

    public TankSpawner getTankSpawner() {
        return tankSpawner;
    }

    public CopyOnWriteArrayList<Tank> getEnemyTanks() {
        return enemyTanks;
    }
}
