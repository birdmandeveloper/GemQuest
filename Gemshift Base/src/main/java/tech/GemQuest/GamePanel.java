package tech.GemQuest;

import tech.GemQuest.ai.PathFinder;
import tech.GemQuest.asset.Asset;
import tech.GemQuest.asset.AssetManager;
import tech.GemQuest.asset.entity.Entity;
import tech.GemQuest.asset.entity.player.Player;
import tech.GemQuest.asset.object.Object;
import tech.GemQuest.asset.tile.TileManager;
import tech.GemQuest.asset.tile.interactive.InteractiveTile;
import tech.GemQuest.event.EventHandler;
import tech.GemQuest.sound.SoundManager;
import tech.GemQuest.ui.UI;
import tech.GemQuest.util.BattleManager;
import tech.GemQuest.util.CollisionChecker;
import tech.GemQuest.util.Config;
import tech.GemQuest.util.KeyHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS // This sets the size of each tile, and the size of the screen by tile
    private final int originalTileSize = 16; // 16x16 tile
    private final int scale = 3;

    private final int tileSize = originalTileSize * scale; // 48x48 tile
    private final int maxScreenColumns = 20;
    private final int maxScreenRows = 12;

    // Window mode// This sets the size of the window in standard
    private final int screenWidth = tileSize * maxScreenColumns; // 960 px
    private final int screenHeight = tileSize * maxScreenRows; // 576 px

    // Full screen mode. This sets the mode to Full screen in options
    private int fullScreenWidth = screenWidth; //+ (screenWidth) ;
    private int fullScreenHeight = screenHeight; //+ (screenHeight) ;

    //These draw the images that we import into resources
    private BufferedImage tempScreen;
    private Graphics2D graphics2D;

    // This establishes FPS
    private final int FPS = 60;

    // WORLD SETTINGS
    private final int maxWorldColumns = 50;
    private final int maxWorldRows = 50;
    private final int worldWidth = tileSize * maxWorldColumns;
    private final int worldHeight = tileSize * maxWorldRows;
    private final int maxMaps = 10;

    // SYSTEM. Supporting game functionality to GamePanel.
    private final KeyHandler keyHandler = new KeyHandler(this);
    private final CollisionChecker collisionChecker = new CollisionChecker(this);
    private final TileManager tileManager = new TileManager(this);
    private final AssetManager assetManager = new AssetManager(this);
    private final SoundManager music = new SoundManager();
    private final SoundManager soundEffect = new SoundManager();
    private final UI ui = new UI(this);
    private final EventHandler eventHandler = new EventHandler(this);
    private final Config config = new Config(this);
    private final BattleManager battleM = new BattleManager(this);
    public PathFinder pFinder = new PathFinder(this);

    private int dynamicSpeedCounter = 0;

    private boolean fullScreenOn;

    // Game State. Different variable represents different game response
    public int gameState;
    public final int TITLE_STATE = 0;
    public final int PLAY_STATE = 1;
    public final int PAUSE_STATE = 2;
    public final int DIALOGUE_STATE = 3;
    public final int CHARACTER_STATE = 4;
    public final int OPTION_STATE = 5;
    public final int GAME_OVER_STATE = 6;
    public final int TRANSITION_STATE = 7;
    public final int TRADE_STATE = 8;
    public final int BATTLE_STATE = 9;

    // ENTITIES & OBJECTS. Player and non player entities in here.
    private final List<Asset> assets = new ArrayList<>();
    private final List<Asset> projectiles = new ArrayList<>();
    private final List<Asset> particles = new ArrayList<>();

    public final Player player = new Player(this, keyHandler);
    private final Asset[][] npcs = new Entity[maxMaps][10];
    public final Asset[][] monsters = new Entity[maxMaps][20];

    private final Asset[][] objects = new Object[maxMaps][20];


    // GAME THREAD. This starts a thread
    private Thread gameThread;
    private int currentMap = 0;

    // Interactive. Assets that are interactive with player.
    private final InteractiveTile[][] interactiveTiles = new InteractiveTile[maxMaps][50];

    // Sets Attributes of the gamePanel
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    // Creates the Objects in the actual game
    public void setUpGame() {
        assetManager.setObjects();
        assetManager.setNPCs();
        assetManager.setMonsters();
        assetManager.setInteractiveTiles();
        gameState = TITLE_STATE;

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        graphics2D = (Graphics2D) tempScreen.getGraphics();

        if (fullScreenOn) {
            setFullScreen();
        }
    }
    // GET FULLSCREEN WIDTH & HEIGHT, GET LOCAL SCREEN DEVICE. Sets Fullscreen.
    public void setFullScreen() {


        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();

        graphicsDevice.setFullScreenWindow(Main.window);


        fullScreenWidth = Main.window.getWidth();
        fullScreenHeight = Main.window.getHeight();
    }

    // Begins the game
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    // GAME OVER
    public void retry() {
        player.setDefaultPosition();
        player.restoreLifeAndMana();
        assetManager.setNPCs();
        assetManager.setMonsters();
        gameState = PLAY_STATE;
    }
    public void restart() {
        player.setItems();
        player.setDefaultValues();
        assetManager.setObjects();
        assetManager.setNPCs();
        assetManager.setMonsters();
        assetManager.setInteractiveTiles();
        gameState = TITLE_STATE;
        stopMusic();
    }

    // RunTime, Nanotime converted to FPS. (This is time)
    @Override
    public void run() {
        double drawInterval = 1_000_000_000 / FPS; // 60 FPS
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                drawToTempScreen();
                drawToScreen();
                delta--;
            }
        }
    }

    // Updates enemies in real time. (This is movement)
    public void update() {
        if (gameState == PLAY_STATE) {
            if (!player.fromBattleState) {
                player.update();
                updateNPCs();
                updateMonsters();
                updateProjectiles();
                updateParticles();
                updateInteractiveTiles();
            }
        }

        if (gameState == PAUSE_STATE) {
            // These just catch inputs and do NOTHING
        }

        if (gameState == BATTLE_STATE) {
            // Same down here
        }
    }
    // Basically update but only for NPC
    private void updateNPCs() {
        for (int i = 0; i < npcs[1].length; i++) {
            if (npcs[currentMap][i] != null) {
                npcs[currentMap][i].update();
            }
        }
    }
    // Update for monster
    private void updateMonsters() {
        for (int i = 0; i < monsters[1].length; i++) {

            if (monsters[currentMap][i] != null) {
                if (monsters[currentMap][i].isAlive() && !monsters[currentMap][i].isDying()) {
                    monsters[currentMap][i].update();
                    dynamicSpeedCounter++;


                    if(dynamicSpeedCounter > 60) {
                        monsters[currentMap][i].resetDefaultSpeed();
                        dynamicSpeedCounter = 0;
                    }
                }

                if (!monsters[currentMap][i].isAlive()) {
                    monsters[currentMap][i].checkDrop();
                    removeMonster(monsters[currentMap][i].getIndex());
                }
            }
        }
    }

    // Monster Defeated
    private void removeMonster(int index) {
        monsters[currentMap][index] = null;
    }

    // Projectile Movement
    private void updateProjectiles() {
        for (int i = 0; i < projectiles.size(); i++) {
            if (projectiles.get(i) != null) {
                if (projectiles.get(i).isAlive()) {
                    projectiles.get(i).update();
                }

                if (!projectiles.get(i).isAlive()) {
                    projectiles.remove(projectiles.get(i));
                }
            }
        }
    }

    // Particle Movement
    private void updateParticles() {
        for (int i = 0; i < particles.size(); i++) {
            if (particles.get(i) != null) {
                if (particles.get(i).isAlive()) {
                    particles.get(i).update();
                }

                if (!particles.get(i).isAlive()) {
                    particles.remove(particles.get(i));
                }
            }
        }
    }

    // Movement on interactive tile, like Dry Tree disappearing
    private void updateInteractiveTiles() {
        for (int i = 0; i < interactiveTiles[1].length; i++) {
            if (interactiveTiles[currentMap][i] != null) {
                interactiveTiles[currentMap][i].update();
            }
        }
    }

    public void drawToTempScreen() {

        // DEBUG
        long drawStart = 0;
        if (keyHandler.isShowDebugText()) {
            drawStart = System.nanoTime();
        }

        if (gameState == TITLE_STATE) {
            ui.draw(graphics2D);
        } else {

            // TILES
            tileManager.draw(graphics2D);
            drawInteractiveTiles(graphics2D);

            // ASSETS
            addAssets();
            sortAssets();
            drawAssets(graphics2D);
            assets.clear();

            // UI
            ui.draw(graphics2D);
        }

        // DEBUG
        if (keyHandler.isShowDebugText()) {
            drawDebugInfo(graphics2D, drawStart);
        }
    }

    private void drawInteractiveTiles(Graphics2D graphics2D) {
        for (int i = 0; i < interactiveTiles[1].length; i++) {
            if (interactiveTiles[currentMap][i] != null) {
                interactiveTiles[currentMap][i].draw(graphics2D);
            }
        }
    }

    // Add Assets and manipulate them
    private void addAssets() {
        assets.add(player);

        for (int i = 0; i < npcs[1].length; i++) {
            if (npcs[currentMap][i] != null) {
                assets.add(npcs[currentMap][i]);
            }
        }

        for (int i = 0; i < objects[1].length; i++) {
            if (objects[currentMap][i] != null) {
                assets.add(objects[currentMap][i]);
            }
        }

        for (int i = 0; i < monsters[1].length; i++) {
            if (monsters[currentMap][i] != null) {
                assets.add(monsters[currentMap][i]);
            }
        }

        for (Asset projectile : projectiles) {
            if (projectile != null) {
                assets.add(projectile);
            }
        }

        for (Asset particle : particles) {
            if (particle != null) {
                assets.add(particle);
            }
        }
    }

    private void sortAssets() {
        assets.sort(Comparator.comparingInt(Asset::getWorldY));
    }
    private void drawAssets(Graphics2D graphics2D) {
        for (Asset asset : assets) {
            asset.draw(graphics2D);
        }
    }
    public void drawToScreen() {
        Graphics graphics = getGraphics();
        graphics.drawImage(tempScreen, 0, 0, fullScreenWidth, fullScreenHeight, null);
        graphics.dispose();
    }

    // DEBUG
    private void drawDebugInfo(Graphics2D graphics2D, long drawStart) {
        long drawEnd = System.nanoTime();
        long passedTime = drawEnd - drawStart;
        int x = 10;
        int y = 400;
        int lineHeight = 20;

        graphics2D.setFont(new Font("Arial", Font.PLAIN, 20));
        graphics2D.setColor(Color.WHITE);

        graphics2D.drawString("WorldX: " + player.getWorldX(), x, y);
        y += lineHeight;
        graphics2D.drawString("WorldY: " + player.getWorldY(), x, y);
        y += lineHeight;
        graphics2D.drawString("Col: " + (player.getWorldX() + player.getCollisionArea().x) / tileSize, x, y);
        y += lineHeight;
        graphics2D.drawString("Row: " + (player.getWorldY() + player.getCollisionArea().y) / tileSize, x, y);
        y += lineHeight;
        graphics2D.drawString("Draw Time: " + passedTime, x, y);
    }

    // MUSIC CONTROLLERS
    public void playMusic(int index) {
        music.setFile(index);
        music.play();
        music.loop();
    }
    public void stopMusic() {
        music.stop();
    }
    public void playSoundEffect(int index) {
        soundEffect.setFile(index);
        soundEffect.play();
    }

    // SCREEN GETTERS
    public int getTileSize() {
        return tileSize;
    }
    public int getMaxScreenColumns() {
        return maxScreenColumns;
    }
    public int getMaxScreenRows() {
        return maxScreenRows;
    }
    public int getWorldWidth() {
        return worldWidth;
    }
    public int getWorldHeight() {
        return worldHeight;
    }
    public int getScreenWidth() {
        return screenWidth;
    }
    public int getScreenHeight() {
        return screenHeight;
    }
    public int getMaxWorldColumns() {
        return maxWorldColumns;
    }
    public int getMaxWorldRows() {
        return maxWorldRows;
    }
    public boolean isFullScreenOn() {
        return fullScreenOn;
    }
    public GamePanel setFullScreenOn(boolean fullScreenOn) {
        this.fullScreenOn = fullScreenOn;
        return this;
    }

    // UTILITY GETTERS
    public KeyHandler getKeyHandler() {
        return keyHandler;
    }
    public TileManager getTileManager() {
        return tileManager;
    }
    public AssetManager getAssetManager() {
        return assetManager;
    }
    public CollisionChecker getCollisionChecker() {
        return collisionChecker;
    }
    public AssetManager getObjectManager() {
        return assetManager;
    }
    public Thread getGameThread() {
        return gameThread;
    }
    public GamePanel setGameThread(Thread gameThread) {
        this.gameThread = gameThread;
        return this;
    }

    // OTHER GETTERS AND SETTERS
    public Player getPlayer() {
        return player;
    }
    public Asset[][] getObjects() {
        return objects;
    }
    public Asset[][] getNpcs() {
        return npcs;
    }
    public Asset[][] getMonsters() {
        return monsters;
    }
    public UI getUi() {
        return ui;
    }
    public EventHandler getEventHandler() {
        return eventHandler;
    }
    public Config getConfig() {
        return config;
    }
    public int getGameState() {
        return gameState;
    }
    public GamePanel setGameState(int gameState) {
        this.gameState = gameState;
        return this;
    }
    public int getTitleState() {
        return TITLE_STATE;
    }
    public int getPlayState() {
        return PLAY_STATE;
    }
    public int getPauseState() {
        return PAUSE_STATE;
    }
    public int getDialogueState() {
        return DIALOGUE_STATE;
    }
    public int getCharacterState() {
        return CHARACTER_STATE;
    }
    public int getOptionState() {
        return OPTION_STATE;
    }
    public int getGameOverState() {
        return GAME_OVER_STATE;
    }
    public List<Asset> getProjectiles() {
        return projectiles;
    }
    public InteractiveTile[][] getInteractiveTiles() {
        return interactiveTiles;
    }
    public List<Asset> getParticles() {
        return particles;
    }
    public SoundManager getMusic() {
        return music;
    }
    public SoundManager getSoundEffect() {
        return soundEffect;
    }
    public int getMaxMaps() {
        return maxMaps;
    }
    public int getCurrentMap() {
        return currentMap;
    }
    public GamePanel setCurrentMap(int currentMap) {
        this.currentMap = currentMap;
        return this;
    }
    public int getTransitionState() {
        return TRANSITION_STATE;
    }
    public int getTradeState() {
        return TRADE_STATE;
    }
    public int getBattleState() {
        return BATTLE_STATE;
    }
    public BattleManager getBattleM() {
        return battleM;
    }
}