package tech.GemQuest.asset.entity.monster;

import tech.GemQuest.GamePanel;
import tech.GemQuest.asset.object.ability.OBJ_Rock;
import tech.GemQuest.asset.object.usable.pickuponly.OBJ_Coin_Bronze;
import tech.GemQuest.asset.object.usable.pickuponly.OBJ_Heart;
import tech.GemQuest.asset.object.usable.pickuponly.OBJ_ManaCrystal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

// Quintessential Monster example. Base template for Slime Class enemies
public class MON_GreenSlime extends Monster {
    public int defaultSpeed = 1;

    public MON_GreenSlime(GamePanel gamePanel, int monsterIndex) {
        super(gamePanel, monsterIndex);

        setName("Green Slime");
        setDirection("down");
        setIdleMessage("Standing on business.");
        setSpeed(defaultSpeed);
        setMaxLife(3);
        setCurrentLife(getMaxLife());
        setAttackPower(3);
        setDefensePower(0);
        setExp(2);

        setProjectile(new OBJ_Rock(gamePanel));
        setMaxAmmo(5);
        setCurrentAmmo(getMaxAmmo());

        setCollisionArea(new Rectangle(3, 12, 42, 30));
        setCollisionDefaultX(getCollisionArea().x);
        setCollisionDefaultY(getCollisionArea().y);

        getAnimationImages();
    }

    // Image assignment
    public void getAnimationImages() {
        setUp1(setup("/images/monster/greenslime_down_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setUp2(setup("/images/monster/greenslime_down_2", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setDown1(setup("/images/monster/greenslime_down_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setDown2(setup("/images/monster/greenslime_down_2", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setLeft1(setup("/images/monster/greenslime_down_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setLeft2(setup("/images/monster/greenslime_down_2", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setRight1(setup("/images/monster/greenslime_down_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setRight2(setup("/images/monster/greenslime_down_2", getGamePanel().getTileSize(), getGamePanel().getTileSize()));

        // All-white slime silhouette, used for damage animation
        setStun1(setup("/images/monster/slimeflash", getGamePanel().getTileSize() * 3, getGamePanel().getTileSize() * 3));
    }

    @Override
    public void damageReaction() {
        this.setAggro(false);

        setActionLockCounter(0);
        setSpeed(3); // Increased speed to retreat
        setDirection(getGamePanel().getPlayer().getDirection());
        this.setInvincible(true);
    }

    @Override
    public void retreatReaction() {
        this.setAggro(false);

        setActionLockCounter(3);
        String updatedDirection = "";

        if(direction.equals("up")) { updatedDirection = "down"; }
        if(direction.equals("down")) { updatedDirection = "up"; }
        if(direction.equals("left")) { updatedDirection = "right"; }
        if(direction.equals("right")) { updatedDirection = "left"; }

        setSpeed(3);
        setDirection(updatedDirection);
        this.setInvincible(true);
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

    // These Idle Image getters are used for Battle Animation
    @Override
    public BufferedImage getIdleImage1() {
        return setup("/images/monster/greenslime_down_1", getGamePanel().getTileSize() * 3, getGamePanel().getTileSize() * 3);
    }
    @Override
    public BufferedImage getIdleImage2() {
        return setup("/images/monster/greenslime_down_2", getGamePanel().getTileSize() * 3, getGamePanel().getTileSize() * 3);
    }
}
