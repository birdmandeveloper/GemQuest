package tech.GemQuest.asset.tile;


import tech.GemQuest.GamePanel;
import tech.GemQuest.util.UtilityTool;


import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;


public class TileManager {
    // VARIABLES
    private final GamePanel gamePanel;
    private final Tile[] tiles;
    private final int[][][] mapTileNumbers;
    boolean drawPath = true; // Used to display the AI's path


    // CONSTRUCTOR
    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;


        this.tiles = new Tile[100];
        this.mapTileNumbers = new int[gamePanel.getMaxMaps()][gamePanel.getMaxWorldColumns()][gamePanel.getMaxWorldRows()];


        getTileImage();
        loadMap("/maps/worldV4.txt", 0);
        loadMap("/maps/eh.txt", 1);
    }


    // STOCK TILES. STOCK MAP. DESIGN STAGE EDIT IMPLEMENTATIONS
    public void getTileImage() {
        // PLACEHOLDER
        setup(0, "GemGrassUP", false);
        setup(1, "GemGrassUP", false);
        setup(2, "GemGrassUP", false);
        setup(3, "GemGrassUP", false);
        setup(4, "GemGrassUP", false);
        setup(5, "GemGrassUP", false);
        setup(6, "GemGrassUP", false);
        setup(7, "GemGrassUP", false);
        setup(8, "GemGrassUP", false);
        setup(9, "GemGrassUP", false);
        // PLACEHOLDER


        // TILES LOAD IN
        setup(10, "GemGrassUP", false);
        setup(11, "GemGrassUP2", false);
        setup(12, "GemMountain3", true);
        setup(13, "GemMountain4", true);
        setup(14, "GlowWormCave", true);
        setup(15, "GemWater", true);
        setup(16, "GemWater2", true);
        setup(17, "GemWater2", true);
        setup(18, "GemWater2", true);
        setup(19, "GemWater2", true);
        setup(20, "GemWater2", true);
        setup(21, "GemWater2", true);
        setup(22, "GemWater3", true);
        setup(23, "GemWater3", true);
        setup(24, "GemWater4", true);
        setup(25, "GemWater", true);
        setup(26, "GemDirt", false);
        setup(27, "GemDirt", false);
        setup(28, "GemDirt", false);
        setup(29, "GemDirt", false);
        setup(30, "GemDirt2", false);
        setup(31, "GemDirt2", false);
        setup(32, "GemDirt2", false);
        setup(33, "GemDirt2", false);
        setup(34, "GemDirt", false);
        setup(35, "GemDirt", false);
        setup(36, "GemDirt", false);
        setup(37, "GemDirt", false);
        setup(38, "GemDirt", false);
        setup(39, "GemDirt", false);
        setup(40, "GemWall", true);
        setup(41, "GemTree", true);
        setup(42, "GemHouse3", false);
        setup(43, "GemFence", false);
        setup(44, "GemHouse4", false);
    }


    // LOADS THE IMAGES
    public void setup(int index, String imageName, boolean collision) {
        try {
            tiles[index] = new Tile();
            tiles[index].setImage(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/images/tiles/" + imageName + ".png"))));
            tiles[index].setImage(UtilityTool.scaleImage(tiles[index].getImage(), gamePanel.getTileSize(), gamePanel.getTileSize()));
            tiles[index].setCollision(collision);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // LOADS THE MAP LAYOUT
    public void loadMap(String mapPath, int map) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(mapPath);
            assert inputStream != null;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));


            int column = 0;
            int row = 0;


            while (column < gamePanel.getMaxWorldColumns() && row < gamePanel.getMaxWorldRows()) {
                String line = bufferedReader.readLine();


                while (column < gamePanel.getMaxWorldColumns()) {
                    String[] numbers = line.split(" ");
                    int number = Integer.parseInt(numbers[column]);


                    mapTileNumbers[map][column][row] = number;
                    column++;
                }
                if (column == gamePanel.getMaxWorldColumns()) {
                    column = 0;
                    row++;
                }
            }


            bufferedReader.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void draw(Graphics2D graphics2D) {
        int worldColumn = 0;
        int worldRow = 0;


        while (worldColumn < gamePanel.getMaxWorldColumns() && worldRow < gamePanel.getMaxWorldRows()) {


            int tileNumber = mapTileNumbers[gamePanel.getCurrentMap()][worldColumn][worldRow];


            int worldX = worldColumn * gamePanel.getTileSize();
            int worldY = worldRow * gamePanel.getTileSize();
            int screenX = worldX - gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getScreenX();
            int screenY = worldY - gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getScreenY();


            // Stop moving the camera at world edge
            int rightOffset = gamePanel.getScreenWidth() - gamePanel.getPlayer().getScreenX();
            screenX = checkIfAtEdgeOfXAxis(worldX, screenX, rightOffset);


            int bottomOffset = gamePanel.getScreenHeight() - gamePanel.getPlayer().getScreenY();
            screenY = checkIfAtEdgeOfYAxis(worldY, screenY, bottomOffset);


            if (UtilityTool.isInsidePlayerView(worldX, worldY, gamePanel)) {
                graphics2D.drawImage(tiles[tileNumber].getImage(), screenX, screenY, null);


            } else if (gamePanel.getPlayer().getScreenX() > gamePanel.getPlayer().getWorldX()
                    || gamePanel.getPlayer().getScreenY() > gamePanel.getPlayer().getWorldY()
                    || rightOffset > gamePanel.getWorldWidth() - gamePanel.getPlayer().getWorldX()
                    || bottomOffset > gamePanel.getWorldHeight() - gamePanel.getPlayer().getWorldY()) {
                graphics2D.drawImage(tiles[tileNumber].getImage(), screenX, screenY, null);
            }


            worldColumn++;


            if (worldColumn == gamePanel.getMaxWorldColumns()) {
                worldColumn = 0;
                worldRow++;
            }


            // Turns on colored AI path


//        if(drawPath == true) {
//            graphics2D.setColor(new Color(0,0,255, 70));
//
//            for (int i = 0; i < gamePanel.pFinder.pathList.size(); i++) {
//                int worldX = gamePanel.pFinder.pathList.get(i).col * gamePanel.getTileSize();
//                int worldY = gamePanel.pFinder.pathList.get(i).row * gamePanel.getTileSize();
//                int screenX = worldX - gamePanel.player.getWorldX()  + gamePanel.getPlayer().getScreenX();
//                int screenY = worldY - gamePanel.player.getWorldY()  + gamePanel.getPlayer().getScreenY();
//
//                graphics2D.fillRect(screenX, screenY, gamePanel.getTileSize(), gamePanel.getTileSize());
        }
    }


    // Edge of the world checks
    private int checkIfAtEdgeOfXAxis(int worldX, int screenX, int rightOffset) {
        if (gamePanel.getPlayer().getScreenX() > gamePanel.getPlayer().getWorldX()) {
            return worldX;
        }


        if (rightOffset > gamePanel.getWorldWidth() - gamePanel.getPlayer().getWorldX()) {
            return gamePanel.getScreenWidth() - (gamePanel.getWorldWidth() - worldX);
        }


        return screenX;
    }
    private int checkIfAtEdgeOfYAxis(int worldY, int screenY, int bottomOffset) {
        if (gamePanel.getPlayer().getScreenY() > gamePanel.getPlayer().getWorldY()) {
            return worldY;
        }


        if (bottomOffset > gamePanel.getWorldHeight() - gamePanel.getPlayer().getWorldY()) {
            return gamePanel.getScreenHeight() - (gamePanel.getWorldHeight() - worldY);
        }


        return screenY;
    }


    // CALLED BY COLLISION CHECKER
    public Tile[] getTiles() {
        return tiles;
    }
    public int[][][] getMapTileNumbers() {
        return mapTileNumbers;
    }
}

