package tech.GemQuest.asset.tile;

import java.awt.image.BufferedImage;

public class Tile {
    //VARIABLES
    private BufferedImage image;
    private boolean collision;

    //THIS JUST MAKES A TILE INTO A TILE

    public BufferedImage getImage() {
        return image;
    }

    public Tile setImage(BufferedImage image) {
        this.image = image;
        return this;
    }

    public boolean isCollision() {
        return collision;
    }

    public Tile setCollision(boolean collision) {
        this.collision = collision;
        return this;
    }
}
