package jsd.project.tank90.render;

import jsd.project.tank90.sprite.*;
import jsd.project.tank90.sprite.PowerUps.PowerUp;
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
    private static GameScreen instance;

    public static CopyOnWriteArrayList<Block> blocks;
    public static CopyOnWriteArrayList<Tank> enemyTanks;
    public static ArrayList<Animation> animations;

    public static int stage;
    public static boolean gameOver;
    public static BulletType defaultBulletType;
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
    private boolean isPaused;
    private Runnable onGameOver;

    private Timer gameOverTimer;
    private Timer transitionTimer;
    private Timer gameLoopTimer;
    private boolean gameCompleted;


    private GameScreen() {
        blocks = new CopyOnWriteArrayList<>();
        enemyTanks = new CopyOnWriteArrayList<>();
        animations = new ArrayList<>();
        stage = 2;
        gameOver = false;
        yPos = MapLoader.BOARD_HEIGHT;
        direction = -1;
        stopYPos = 250;
        tankSpawner = new TankSpawner(enemyTanks, stage);
        isSpawning = false;
        isPaused = false;
        imageInstance = ImageUtility.getInstance();
        gameCompleted = false;
        this.setVisible(true);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setLayout(null);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameOver) return;
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    SoundUtility.pause();
                    togglePause();
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
                if (!isPaused && !gameOver) {
                    ptRenderer.getPlayerTank().mousePressed(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ptRenderer.getPlayerTank().mouseReleased(e);

            }
        });

        tankSpawner.startSpawning();
        initBlocks();
        initGameLoop();
    }

    public void togglePause() {
        if (isPaused) {
            gameLoopTimer.start();
            isPaused = false;
        } else {
            gameLoopTimer.stop();
            isPaused = true;
            repaint();
        }
    }

    public static GameScreen getInstance() {
        if (instance == null) {
            instance = new GameScreen();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    public void setOnGameOver(Runnable onGameOver) {
        this.onGameOver = onGameOver;
    }


    public void setPlayerTankRender(int assignedHealth, int assignedSpeed, int assignedRotationSpeed, BulletType assignedBulletType) {
        ptRenderer = new PlayerTankRender(new PlayerTank(assignedHealth, assignedSpeed, assignedRotationSpeed, assignedBulletType), this);
        setDefaultBulletType(assignedBulletType);
    }

    public static void setDefaultBulletType(BulletType defaultBulletType) {
        GameScreen.defaultBulletType = defaultBulletType;
    }

    public void completeGame() {
        gameCompleted = true;
        gameOver();
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
        gameLoopTimer = new Timer(20, e -> {
            if (ptRenderer.getPlayerTank().isShield()) {
                if (shieldAnimation == null || shieldAnimation.isFinished()) {
                    shieldAnimation = new ShieldAnimation(ptRenderer.getPlayerTank().getX(),
                            ptRenderer.getPlayerTank().getY(), 100, 1, true);
                }
                shieldAnimation.setLoop(true);
            } else {
                if (shieldAnimation != null) {
                    shieldAnimation.setLoop(false);
                    shieldAnimation = null;
                }
            }

            BoardUtility.checkTankPowerUpCollision(ptRenderer.getPlayerTank(), enemyTanks, blocks);

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
                    ptRenderer.getPlayerTank().getY() - 4);
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
        for (PowerUp powerUp : BoardUtility.getPowerUps()) {
            if (powerUp.isVisible()) {
                g2d.drawImage(powerUp.getImage(), powerUp.getX(), powerUp.getY(), this);
                g2d.setColor(Color.YELLOW);
                g2d.draw(powerUp.getHitbox());
            }
        }
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

        int totalEnemyTanks = TankSpawner.getTotalEnemyTanks();
        ImageUtility imageInstance = ImageUtility.getInstance();
        Image enemyIcon = imageInstance.getEnemyIcon();

        g.drawImage(enemyIcon, initX * 16, 5 * 16, this);
        g.setFont(largeBoldFont);
        g.drawString(String.valueOf(totalEnemyTanks < 0 ? 0 : totalEnemyTanks), (initX + 1) * 16, 6 * 16);
        g.setFont(originalFont);


        Image starIcon = imageInstance.getStarIcon();
        int starIconX = initX * 16;
        int starIconY = 11 * 16;
        g.drawImage(starIcon, starIconX, starIconY, this);

        int currentStarLevel = BoardUtility.getStarLevel();

        g.setColor(Color.BLACK);
        g.drawString("TIER: " + currentStarLevel, starIconX - 4, starIconY + 3 * 16 + 4); // Shadow position

        g.setColor(Color.WHITE);
        g.drawString("TIER: " + currentStarLevel, starIconX - 6, starIconY + 3 * 16); // Original position

        if (gameOver) {
            Font font = loadFont();
            g.setFont(font);
            g.setColor(Color.RED);
            if (gameCompleted) {
                g.setColor(Color.YELLOW);
                g.drawString("GAME COMPLETED", MapLoader.BOARD_WIDTH / 2 - 120, yPos);
            } else {
                g.drawString("GAME OVER", MapLoader.BOARD_WIDTH / 2 - 85, yPos);
            }
        }

        if (isPaused) {
            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            Font font = loadFont().deriveFont(Font.BOLD, 36);
            g2d.setFont(font);
            g2d.setColor(Color.YELLOW);
            String pauseText = "GAME PAUSED";
            FontMetrics fm = g2d.getFontMetrics(font);
            int x = (getWidth() - fm.stringWidth(pauseText)) / 2;
            int y = getHeight() / 2 - 30;

            g2d.setColor(Color.RED);
            g2d.drawString(pauseText, x - 2, y - 2);
            g2d.drawString(pauseText, x + 2, y + 2);
            g2d.setColor(Color.YELLOW);
            g2d.drawString(pauseText, x, y);

            Font instructionFont = loadFont().deriveFont(Font.PLAIN, 16);
            g2d.setFont(instructionFont);
            g2d.setColor(Color.CYAN);
            String resumeText = "Press ESC to Resume";
            int resumeX = (getWidth() - g2d.getFontMetrics(instructionFont).stringWidth(resumeText)) / 2;
            g2d.drawString(resumeText, resumeX, y + 40);
        }


        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    public void checkHealth(PlayerTank playerTank) {
        if (playerTank.getHealth() <= 0) {
            PlayerTank.lives -= 1;
            BoardUtility.resetPowerLevel();
            if (PlayerTank.lives > 0) {
                GameScreen.animations.add(new TankExplosion(playerTank.getX(), playerTank.getY(), 50, 1, false));
                SoundUtility.explosion2();
                playerTank.activateShield(3000);
                playerTank.resetPosition();
                playerTank.setHealth(100 + playerTank.getAssignedHealth() * 50);
            } else {
                GameScreen.animations.add(new TankExplosion(playerTank.getX(), playerTank.getY(), 50, 1, false));
                playerTank.setX(-300);
                playerTank.setY(0);
                gameOver();
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
            return;
        }

        gameOver = true;
        SoundUtility.gameOver();
        gameOverTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                yPos += direction;
                if (yPos == stopYPos) {
                    direction = 0;
                    gameOverTimer.stop();

                    transitionTimer = new Timer(3000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (onGameOver != null) {
                                onGameOver.run();
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
                repaint();
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
