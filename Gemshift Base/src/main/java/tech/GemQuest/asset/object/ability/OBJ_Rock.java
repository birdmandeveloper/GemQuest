package tech.GemQuest.asset.object.ability;

import tech.GemQuest.GamePanel;
import tech.GemQuest.asset.entity.Entity;
import tech.GemQuest.asset.entity.ability.Projectile;

import java.awt.*;
//THIS IS THE ENEMY PROJECTILE. WE COULD POTENTIALLY USE THIS FOR A SEPARATE PLAYER PROJECTILE

public class OBJ_Rock extends Projectile {

    public OBJ_Rock(GamePanel gamePanel) {
        super(gamePanel);

        setName("Rock Projectile");
        setSpeed(8);
        setMaxLife(80);
        setCurrentLife(getMaxLife());
        setAttackPower(2);
        setUseCost(1);
        setAlive(false);

        getAnimationImages();
    }

    //GETTERS
    public void getAnimationImages() {
        setUp1(setup("/images/ability/rock_down_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setUp2(setup("/images/ability/rock_down_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setDown1(setup("/images/ability/rock_down_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setDown2(setup("/images/ability/rock_down_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setLeft1(setup("/images/ability/rock_down_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setLeft2(setup("/images/ability/rock_down_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setRight1(setup("/images/ability/rock_down_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setRight2(setup("/images/ability/rock_down_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
    }

    @Override
    public boolean haveEnoughResource(Entity user) {
        return user.getCurrentAmmo() >= getUseCost();
    }

    @Override
    public void subtractResource(Entity user) {
        user.setCurrentAmmo(user.getCurrentAmmo() - getUseCost());
    }

    @Override
    public Color getParticleColor() {
        return new Color(40, 50, 0);
    }

    @Override
    public int getParticleSize() {
        return 10; // pixels
    }

    @Override
    public int getParticleSpeed() {
        return 1;
    }

    @Override
    public int getParticleMaxLife() {
        return 20;
    }

    @Override
    public void resetDefaultSpeed() {

    }
}
