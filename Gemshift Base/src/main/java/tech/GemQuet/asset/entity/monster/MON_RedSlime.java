package tech.GemQuet.asset.entity.monster;

import tech.GemQuet.GamePanel;
import tech.GemQuet.asset.object.ability.OBJ_Rock;
import tech.GemQuet.asset.object.usable.pickuponly.OBJ_Coin_Bronze;
import tech.GemQuet.asset.object.usable.pickuponly.OBJ_Heart;
import tech.GemQuet.asset.object.usable.pickuponly.OBJ_ManaCrystal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class MON_RedSlime extends Monster {

    public MON_RedSlime(GamePanel gamePanel) {
        super(gamePanel);

        setName("Red Slime");
        setDirection("down");
        setSpeed(1);
        setMaxLife(3);
        setCurrentLife(getMaxLife());
        setAttackPower(0);
        setDefensePower(0);
        setExp(2);

        setProjectile(new OBJ_Rock(gamePanel));
        setMaxAmmo(5);
        setCurrentAmmo(getMaxAmmo());

        setCollisionArea(new Rectangle(3, 12, 42, 36));
        setCollisionDefaultX(getCollisionArea().x);
        setCollisionDefaultY(getCollisionArea().y);

        getAnimationImages();
    }

    public void getAnimationImages() {
        setUp1(setup("/images/monster/redslime_down_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setUp2(setup("/images/monster/redslime_down_2", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setDown1(setup("/images/monster/redslime_down_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setDown2(setup("/images/monster/redslime_down_2", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setLeft1(setup("/images/monster/redslime_down_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setLeft2(setup("/images/monster/redslime_down_2", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setRight1(setup("/images/monster/redslime_down_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setRight2(setup("/images/monster/redslime_down_2", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
    }

    @Override
    public void damageReaction() {
        setActionLockCounter(0);
        setSpeed(3);
        setDirection(getGamePanel().getPlayer().getDirection());
    }

    @Override
    public void setupAI() {
        super.setupAI();
    }

    @Override
    public void checkDrop() {
        int i = new Random().nextInt(100) + 1;

        if (i < 50) {
            dropObject(new OBJ_Coin_Bronze(getGamePanel()));
        }

        if (i >= 50 && i < 75) {
            dropObject(new OBJ_Heart(getGamePanel()));
        }

        if (i >= 75 && i < 100) {
            dropObject(new OBJ_ManaCrystal(getGamePanel()));
        }
    }

    @Override
    public void resetDefaultSpeed() {
        setSpeed(1);
    }
    @Override
    public BufferedImage getIdleImage1() {
        return setup("/images/monster/redslime_down_1", getGamePanel().getTileSize() * 3, getGamePanel().getTileSize() * 3);
    }

    @Override
    public BufferedImage getIdleImage2() {
        return setup("/images/monster/redslime_down_2", getGamePanel().getTileSize() * 3, getGamePanel().getTileSize() * 3);
    }
}
