package tech.GemQuest.asset.entity.ability;

import tech.GemQuest.GamePanel;
import tech.GemQuest.asset.entity.Entity;
//THIS IS IN CHARGE OF FIREBALL/ROCK LOGIC
public abstract class Projectile extends Entity {
    //VARIABLES
    private Entity user;

    //CONSTRUCTOR
    public Projectile(GamePanel gamePanel) {
        super(gamePanel);
    }

    public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {
        this.setWorldX(worldX);
        this.setWorldY(worldY);
        this.setDirection(direction);
        this.setAlive(alive);
        this.user = user;
        this.setCurrentLife(this.getMaxLife());
    }

    //TRACES MOVEMENT
    @Override
    public void update() {

        if (user == getGamePanel().getPlayer()) {

            int monsterIndex = getGamePanel().getCollisionChecker().checkEntity(this, getGamePanel().getMonsters());
            if (monsterIndex != 999) {
                getGamePanel().getPlayer().damageMonster(monsterIndex, getAttackPower());
                generateParticle(user.getProjectile(), getGamePanel().getMonsters()[getGamePanel().getCurrentMap()][monsterIndex]);
                setAlive(false);
            }

        } else {

            boolean contactPlayer = getGamePanel().getCollisionChecker().checkPlayer(this);
            if (!getGamePanel().getPlayer().isInvincible() && contactPlayer) {
                damagePlayer(getAttackPower());
                generateParticle(user.getProjectile(), getGamePanel().getPlayer());
                setAlive(false);
            }
        }

        switch (getDirection()) {
            case "up" -> setWorldY(getWorldY() - getSpeed());
            case "down" -> setWorldY(getWorldY() + getSpeed());
            case "left" -> setWorldX(getWorldX() - getSpeed());
            case "right" -> setWorldX(getWorldX() + getSpeed());
        }

        setCurrentLife(getCurrentLife() - 1);
        if (getCurrentLife() <= 0) {
            setAlive(false);
        }

        checkAndChangeSpriteAnimationImage();
    }

    //THIS AFFECTS MANA
    public abstract boolean haveEnoughResource(Entity user);

    public abstract void subtractResource(Entity user);

}
