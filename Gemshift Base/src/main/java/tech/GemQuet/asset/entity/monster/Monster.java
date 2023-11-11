package tech.GemQuet.asset.entity.monster;

import tech.GemQuet.GamePanel;
import tech.GemQuet.asset.entity.Entity;
import tech.GemQuet.util.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class Monster extends Entity {
    public int monsterIndex;

    public Monster(GamePanel gamePanel, int monsterIndex) {
        super(gamePanel);
        this.monsterIndex = monsterIndex;
    }

    @Override
    public void setupAI() {
        super.setupAI();
        setupProjectileAI();
    }

    @Override
    public void resetDefaultSpeed() {
        System.out.println("This method never gets called (I think)");
    }

    private void setupProjectileAI() {
        int i = new Random().nextInt(100) + 1;

        if (i > 99
                && !getProjectile().isAlive()
                && getProjectileAvailableCounter() == 30
                && getProjectile().haveEnoughResource(this)) {

            getProjectile().set(getWorldX(), getWorldY(), getDirection(), true, this);
            getProjectile().subtractResource(this);
            getGamePanel().getProjectiles().add(getProjectile());
            setProjectileAvailableCounter(0);
        }
    }

    public BufferedImage setup() {
        BufferedImage image = null;

        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/images/monster/eggslime_down_1.png")));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return UtilityTool.scaleImage(image, 48, 48);
    }
}
