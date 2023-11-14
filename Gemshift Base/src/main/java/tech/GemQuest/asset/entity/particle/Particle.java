package tech.GemQuest.asset.entity.particle;

import tech.GemQuest.GamePanel;
import tech.GemQuest.asset.Asset;
import tech.GemQuest.asset.entity.Entity;

import java.awt.*;
// THIS IS IN CHARGE OF DEBRIS ANIMATIONS IN GAME
public class Particle extends Entity {
    //VARIABLES
    private final Asset generator;
    private final Color color;
    private final int size;
    private final int xd;
    private int yd;

    //CONSTRUCTOR
    public Particle(GamePanel gamePanel, Asset generator, Color color, int size, int speed, int maxLife, int xd, int yd) {
        super(gamePanel);

        this.generator = generator;
        this.color = color;
        this.size = size;
        this.setSpeed(speed);
        this.setMaxLife(maxLife);
        this.xd = xd;
        this.yd = yd;

        setCurrentLife(maxLife);
        int offSet = (gamePanel.getTileSize() / 2) - (size / 2);
        setWorldX(generator.getWorldX() + offSet);
        setWorldY(generator.getWorldY() + offSet);
    }

    @Override
    public void update() {

        setCurrentLife(getCurrentLife() - 1);

        if (getCurrentLife() < getMaxLife() / 3) {
            yd++;
        }

        setWorldX(getWorldX() + (xd * getSpeed()));
        setWorldY(getWorldY() + (yd * getSpeed()));

        if (getCurrentLife() == 0) {
            setAlive(false);
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        int screenX = getWorldX() - getGamePanel().getPlayer().getWorldX() + getGamePanel().getPlayer().getScreenX();
        int screenY = getWorldY() - getGamePanel().getPlayer().getWorldY() + getGamePanel().getPlayer().getScreenY();

        graphics2D.setColor(color);
        graphics2D.fillRect(screenX, screenY, size, size);
    }

    @Override
    public void resetDefaultSpeed() {

    }
}
