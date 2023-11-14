package tech.GemQuest.asset.entity.monster;

import tech.GemQuest.GamePanel;
import tech.GemQuest.asset.entity.Entity;
import tech.GemQuest.util.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
//LOGIC FOR MONSTERS FOR SLIME TO INHERIT
public class Monster extends Entity {
    //VARIABLES
    public int monsterIndex;

    public Monster(GamePanel gamePanel, int monsterIndex) {
        super(gamePanel);
        this.monsterIndex = monsterIndex;
    }

    //THESE ARE THE MONSTER MOVEMENT "LOGIC"
    @Override
    public void setupAI() {
        super.setupAI();
        setupProjectileAI();
    }

    @Override
    public void resetDefaultSpeed() {
        System.out.println("SpeedReset");
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
