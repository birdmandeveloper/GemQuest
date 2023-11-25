package tech.GemQuest.asset.entity.player;

import tech.GemQuest.GamePanel;
import tech.GemQuest.asset.Asset;
import tech.GemQuest.asset.entity.Entity;
import tech.GemQuest.asset.object.ability.OBJ_Fireball;
import tech.GemQuest.asset.object.equipment.*;
import tech.GemQuest.asset.object.usable.inventory.OBJ_Key;
import tech.GemQuest.asset.object.usable.inventory.OBJ_Potion_Red;
import tech.GemQuest.asset.object.usable.pickuponly.PickUpOnlyObject;
import tech.GemQuest.util.KeyHandler;
import tech.GemQuest.util.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;
//PLAYER OBJECT CLASS
public class Player extends Entity {
    private final KeyHandler keyHandler;
    private final int screenX;
    private final int screenY;
    private int resetTimer;
    public String playerName;
    public boolean fromBattleState;
    public int battleMonsterID = 0; // Defaults to Monster 0
    public boolean battleMenuOn;
    public boolean battleItemMenu;
    public boolean usedItem;
    public String[] BATTLE_MENU_OPTIONS = new String[] {"Attack",  "Item", "Retreat"};

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        super(gamePanel);
        this.keyHandler = keyHandler;

        this.screenX = gamePanel.getScreenWidth() / 2 - (gamePanel.getTileSize() / 2);
        this.screenY = gamePanel.getScreenHeight() / 2 - (gamePanel.getTileSize() / 2);

        setItems();
        setDefaultValues();
        setCollision();
        getAnimationImages();
    }

    public void setDefaultValues() {
        setDefaultPosition();

        this.playerName = "B I R D"; // Test placeholder for now

        setSpeed(4);
        setMaxLife(6);
        setCurrentLife(getMaxLife());
        setMaxMana(4);
        setCurrentMana(getMaxMana());
        setMaxAmmo(10);
        setCurrentAmmo(getMaxAmmo());
        setLevel(1);
        setStrength(1);
        setDexterity(1);
        setExp(0);
        setNextLevelExp(5);
        setCoins(0);
        setAttackPower(getAttack());
        setDefensePower(getDefense());

    }

    public void setItems() {
        getInventory().clear();
        setDefaultWeapon();
        setCurrentShield(new OBJ_Shield_Wood(getGamePanel()));
        setProjectile(new OBJ_Fireball(getGamePanel()));

        getInventory().add(getCurrentWeapon());
        getInventory().add(getCurrentShield());
        getInventory().add(new OBJ_Potion_Red(getGamePanel()));
        getInventory().add(new OBJ_Key(getGamePanel()));
        getInventory().add(new OBJ_Key(getGamePanel()));
        getInventory().add(new OBJ_Axe(getGamePanel()));
    }

    public void setDefaultPosition() {
        setWorldX(getGamePanel().getTileSize() * 23);
        setWorldY(getGamePanel().getTileSize() * 21);
        setDirection("down");
    }

    private void setDefaultWeapon() {
        setCurrentWeapon(new OBJ_Sword_Normal(getGamePanel()));
        setPlayerAttackArea();
        getAttackImages();
    }

    public void restoreLifeAndMana() {
        setCurrentLife(getMaxLife());
        setCurrentMana(getMaxMana());
        setInvincible(false);
    }

    public int getAttack() {
        return getStrength() * getCurrentWeapon().getAttackValue();
    }
    public int getDefense() {
        return getDexterity() * getCurrentShield().getDefenseValue();
    }
    private void setCollision() {
        setCollisionArea(new Rectangle(8, 16, 32, 32));
        setCollisionDefaultX(getCollisionArea().x);
        setCollisionDefaultY(getCollisionArea().y);
    }
    private void setPlayerAttackArea() {
        setAttackArea(getCurrentWeapon().getAttackArea());
    }
    public void getAnimationImages() {
        setUp1(setup("/images/player/clark_back_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setUp2(setup("/images/player/clark_back_2", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setDown1(setup("/images/player/clark_front_0", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setDown2(setup("/images/player/clark_front_2", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setLeft1(setup("/images/player/clark_left_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setLeft2(setup("/images/player/clark_left_2", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setRight1(setup("/images/player/clark_right_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setRight2(setup("/images/player/clark_right_2", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
    }

    // THESE CURRENTLY HAVE A STAND IN ANIMATION AND STAND IN FILES
    public void getAttackImages() {
        if (getCurrentWeapon() instanceof OBJ_Sword_Normal) {
            setAttackUp1(setup("/images/player/boy_attack_up_1", getGamePanel().getTileSize(), getGamePanel().getTileSize() * 2));
            setAttackUp2(setup("/images/player/boy_attack_up_2", getGamePanel().getTileSize(), getGamePanel().getTileSize() * 2));
            setAttackDown1(setup("/images/player/boy_attack_down_1", getGamePanel().getTileSize(), getGamePanel().getTileSize() * 2));
            setAttackDown2(setup("/images/player/boy_attack_down_2", getGamePanel().getTileSize(), getGamePanel().getTileSize() * 2));
            setAttackLeft1(setup("/images/player/boy_attack_left_1", getGamePanel().getTileSize() * 2, getGamePanel().getTileSize()));
            setAttackLeft2(setup("/images/player/boy_attack_left_2", getGamePanel().getTileSize() * 2, getGamePanel().getTileSize()));
            setAttackRight1(setup("/images/player/boy_attack_right_1", getGamePanel().getTileSize() * 2, getGamePanel().getTileSize()));
            setAttackRight2(setup("/images/player/boy_attack_right_2", getGamePanel().getTileSize() * 2, getGamePanel().getTileSize()));
        }

        if (getCurrentWeapon() instanceof OBJ_Axe) {
            setAttackUp0(setup("/images/player/clark_back_axe_0", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
            setAttackUp1(setup("/images/player/clark_back_axe_1", getGamePanel().getTileSize(), getGamePanel().getTileSize() * 2));
            setAttackUp2(setup("/images/player/clark_back_axe_2", getGamePanel().getTileSize(), getGamePanel().getTileSize() *2));

            setAttackDown0(setup("/images/player/clark_front_axe_0", getGamePanel().getTileSize(), getGamePanel().getTileSize() * 2)); // !!!
            setAttackDown1(setup("/images/player/clark_front_axe_1", getGamePanel().getTileSize(), getGamePanel().getTileSize() * 2));
            setAttackDown2(setup("/images/player/clark_front_axe_2", getGamePanel().getTileSize(), getGamePanel().getTileSize() * 2));

            setAttackLeft0(setup("/images/player/clark_left_axe_0", getGamePanel().getTileSize(), getGamePanel().getTileSize() * 2));
            setAttackLeft1(setup("/images/player/clark_left_axe_1", getGamePanel().getTileSize() * 2, getGamePanel().getTileSize() * 2));
            setAttackLeft2(setup("/images/player/clark_left_axe_2", getGamePanel().getTileSize() * 2, getGamePanel().getTileSize()));

            setAttackRight0(setup("/images/player/clark_right_axe_0", getGamePanel().getTileSize(), getGamePanel().getTileSize() * 2));
            setAttackRight1(setup("/images/player/clark_right_axe_1", getGamePanel().getTileSize() * 2, getGamePanel().getTileSize() * 2));
            setAttackRight2(setup("/images/player/clark_right_axe_2", getGamePanel().getTileSize() * 2, getGamePanel().getTileSize()));
        }
    }

    // IN CHARGE OF MOVEMENT
    @Override
    public void update() {
        if (isAttacking()) {
            attacking();
        } else if (keyHandler.isUpPressed()
                || keyHandler.isDownPressed()
                || keyHandler.isLeftPressed()
                || keyHandler.isRightPressed()
                || keyHandler.isEnterPressed()
                || keyHandler.isSpacePressed()) {

            if (keyHandler.isUpPressed()) {
                setDirection("up");
            } else if (keyHandler.isDownPressed()) {
                setDirection("down");
            } else if (keyHandler.isLeftPressed()) {
                setDirection("left");
            } else if (keyHandler.isRightPressed()) {
                setDirection("right");
            }

            checkIfAttacking();
            checkCollision();
            checkEvent();
            moveIfCollisionNotDetected();
            resetEnterPressedValue();
            checkAndChangeSpriteAnimationImage();
        } else {
            resetSpriteToDefault();
        }

        fireProjectileIfKeyPressed();
        checkIfInvincible();
        updateLifeAndMana();
        checkIfAlive();
    }

    private void attacking() {
        setSpriteCounter(getSpriteCounter() + 1);

        if (getSpriteCounter() <= 10) {
            setSpriteNumber(1);
        }

        if (getSpriteCounter() > 10 && getSpriteCounter() <= 15) {
            setSpriteNumber(2);
        }

        if (getSpriteCounter() > 15 && getSpriteCounter() <= 30) {
            setSpriteNumber(3);

            // Save current worldX, worldY and CollisionArea
            int currentWorldX = getWorldX();
            int currentWorldY = getWorldY();
            int collisionAreaWidth = getCollisionArea().width;
            int collisionAreaHeight = getCollisionArea().height;

            // Adjust player's worldX/Y to the attackArea

            if(direction.equals("down") && getSpriteNumber() == 1) {
                setWorldY(currentWorldY - getAttackArea().height);
            }
            if(direction.equals("down") && getSpriteNumber() == 2 ||
                    direction.equals("down") && getSpriteNumber() == 3) {
                setWorldY(currentWorldY + getAttackArea().height);
            }

            if(direction.equals("up")) {
                setWorldY(currentWorldY - getAttackArea().height);
            }

            // NONE of these have functional hit boxes yet, I'll have to take a closer look at it later
            if(direction.equals("left") && getSpriteNumber() == 1) {
                // setWorldX(currentWorldX - getGamePanel().getTileSize());
            }
            if(direction.equals("left") && getSpriteNumber() == 2) {
                // setWorldX(currentWorldX - getGamePanel().getTileSize());
            }
            if(direction.equals("left") && getSpriteNumber() == 3) {
                // setWorldY(currentWorldY - getGamePanel().getTileSize());
            }

            if(direction.equals("right") && getSpriteNumber() == 1) {
                // setWorldY(currentWorldY - getGamePanel().getTileSize());
            }
            if(direction.equals("right") && getSpriteNumber() == 2) {
                // setWorldX(currentWorldX + getAttackArea().width);
                // setWorldY(currentWorldY - getAttackArea().height);
            }
            if(direction.equals("right") && getSpriteNumber() == 3) {
                // setWorldX(currentWorldX + getAttackArea().width);
            }

//            switch (getDirection()) {
//                case "up" -> setWorldY(currentWorldY - getAttackArea().height);
//                case "down" -> setWorldY(currentWorldY + getAttackArea().height);
//                case "left" -> setWorldX(currentWorldX - getAttackArea().width);
//                case "right" -> setWorldX(currentWorldX + getAttackArea().width);
//            }

            // Make collisionArea into attackArea
            getCollisionArea().width = getAttackArea().width;
            getCollisionArea().height = getAttackArea().height;

            // Check monster collision with updated collisionArea
            int monsterIndex = getGamePanel().getCollisionChecker().checkEntity(this, getGamePanel().getMonsters());
            damageMonster(monsterIndex, getAttackPower());

            // Check interactiveTile collision
            int interactiveTileIndex = getGamePanel().getCollisionChecker().checkEntity(this, getGamePanel().getInteractiveTiles());
            damageInteractiveTile(interactiveTileIndex);

            // Reset collisionArea to player
            setWorldX(currentWorldX);
            setWorldY(currentWorldY);
            getCollisionArea().width = collisionAreaWidth;
            getCollisionArea().height = collisionAreaHeight;
        }

        if (getSpriteCounter() > 30) {
            setSpriteNumber(1);
            setSpriteCounter(0);
            setAttacking(false);
        }
    }

    public void damageMonster(int index, int attackPower) {
        if (index != 999) {
            if (!getGamePanel().getMonsters()[getGamePanel().getCurrentMap()][index].isInvincible()) {

                getGamePanel().playSoundEffect(5);

                int damage = attackPower - getGamePanel().getMonsters()[getGamePanel().getCurrentMap()][index].getDefensePower();
                if (damage < 0) {
                    damage = 0;
                }

                getGamePanel().getMonsters()[getGamePanel().getCurrentMap()][index].setCurrentLife(getGamePanel().getMonsters()[getGamePanel().getCurrentMap()][index].getCurrentLife() - damage);
                getGamePanel().getUi().addMessage(damage + " damage!");

                getGamePanel().getMonsters()[getGamePanel().getCurrentMap()][index].setInvincible(true);
                getGamePanel().getMonsters()[getGamePanel().getCurrentMap()][index].damageReaction();

                if (getGamePanel().getMonsters()[getGamePanel().getCurrentMap()][index].getCurrentLife() <= 0) {
                    getGamePanel().getMonsters()[getGamePanel().getCurrentMap()][index].setDying(true);
                    getGamePanel().getUi().addMessage("Killed the " + getGamePanel().getMonsters()[getGamePanel().getCurrentMap()][index].getName() + "!");
                    // setExp(getExp() + getGamePanel().getMonsters()[getGamePanel().getCurrentMap()][index].getExp());
                    getGamePanel().getUi().addMessage("Exp + " + getGamePanel().getMonsters()[getGamePanel().getCurrentMap()][index].getExp());

                    checkLevelUp();
                }
            }
        }
    }

    private void damageInteractiveTile(int index) {
        if (index != 999
                && getGamePanel().getInteractiveTiles()[getGamePanel().getCurrentMap()][index].isDestructible()
                && getGamePanel().getInteractiveTiles()[getGamePanel().getCurrentMap()][index].isCorrectWeapon(getCurrentWeapon())
                && !getGamePanel().getInteractiveTiles()[getGamePanel().getCurrentMap()][index].isInvincible()) {

            getGamePanel().getInteractiveTiles()[getGamePanel().getCurrentMap()][index].playSoundEffect();
            getGamePanel().getInteractiveTiles()[getGamePanel().getCurrentMap()][index].setCurrentLife(getGamePanel().getInteractiveTiles()[getGamePanel().getCurrentMap()][index].getCurrentLife() - 1);
            getGamePanel().getInteractiveTiles()[getGamePanel().getCurrentMap()][index].setInvincible(true);

            generateParticle(getGamePanel().getInteractiveTiles()[getGamePanel().getCurrentMap()][index], getGamePanel().getInteractiveTiles()[getGamePanel().getCurrentMap()][index]);

            if (getGamePanel().getInteractiveTiles()[getGamePanel().getCurrentMap()][index].getCurrentLife() == 0) {
                getGamePanel().getInteractiveTiles()[getGamePanel().getCurrentMap()][index] = getGamePanel().getInteractiveTiles()[getGamePanel().getCurrentMap()][index].getDestroyedForm();
            }
        }
    }

    public void checkLevelUp() {
        if (getExp() >= getNextLevelExp()) {
            setLevel(getLevel() + 1);
            setNextLevelExp(getNextLevelExp() * 3);
            setMaxLife(getMaxLife() + 2);
            setStrength(getStrength() + 1);
            setDexterity(getDexterity() + 1);
            setAttackPower(getAttack());
            setDefensePower(getDefense());

            getGamePanel().playSoundEffect(8);
            getGamePanel().setGameState(getGamePanel().getDialogueState());
            getGamePanel().getUi().setCurrentDialogue("You are level " + getLevel() + " now!\n" +
                    "You feel stronger!");
        }
    }

    private void checkIfAttacking() {
        if (getGamePanel().getKeyHandler().isSpacePressed()) {
            getGamePanel().playSoundEffect(7);
            setAttacking(true);
        }
    }

    private void checkCollision() {
        setCollisionOn(false);

        checkTileCollision();
        checkInteractiveTileCollision();
        checkObjectCollision();
        checkNPCCollision();
        checkMonsterCollision();
    }

    private void checkTileCollision() {
        getGamePanel().getCollisionChecker().checkTile(this);
    }

    private void checkInteractiveTileCollision() {
        getGamePanel().getCollisionChecker().checkEntity(this, getGamePanel().getInteractiveTiles());
    }

    private void checkObjectCollision() {
        int objectIndex = getGamePanel().getCollisionChecker().checkObject(this, true);
        pickUpObject(objectIndex);
    }

    private void pickUpObject(int index) {
        if (index != 999) {

            // PICK-UP ONLY ITEMS
            if (getGamePanel().getObjects()[getGamePanel().getCurrentMap()][index] instanceof PickUpOnlyObject) {
                getGamePanel().getObjects()[getGamePanel().getCurrentMap()][index].use();
            }

            // INVENTORY ITEMS
            else {
                String text;

                if (getInventory().size() != getMaxInventorySize()) {
                    getInventory().add(getGamePanel().getObjects()[getGamePanel().getCurrentMap()][index]);
                    getGamePanel().playSoundEffect(1);
                    text = "Got a " + getGamePanel().getObjects()[getGamePanel().getCurrentMap()][index].getName() + "!";
                } else {
                    text = "You cannot carry anymore!";
                }

                getGamePanel().getUi().addMessage(text);
            }

            getGamePanel().getObjects()[getGamePanel().getCurrentMap()][index] = null;
        }
    }

    private void checkNPCCollision() {
        int npcIndex = getGamePanel().getCollisionChecker().checkEntity(this, getGamePanel().getNpcs());
        interactWithNPC(npcIndex);
    }

    private void interactWithNPC(int index) {
        if (index != 999) {
            if (getGamePanel().getKeyHandler().isEnterPressed()) {
                getGamePanel().setGameState(getGamePanel().getDialogueState());
                getGamePanel().getNpcs()[getGamePanel().getCurrentMap()][index].speak();
            }
        }
    }

    private void checkMonsterCollision() {
        int monsterIndex = getGamePanel().getCollisionChecker().checkEntity(this, getGamePanel().getMonsters());
        interactWithMonster(monsterIndex);
    }

    private void interactWithMonster(int index) {
        if (index != 999) {
            if (!isInvincible() && !getGamePanel().getMonsters()[getGamePanel().getCurrentMap()][index].isDying() && !getGamePanel().getMonsters()[getGamePanel().getCurrentMap()][index].isInvincible()) {
                getGamePanel().playSoundEffect(6);

                this.battleMonsterID = index;
                getGamePanel().getMonsters()[getGamePanel().getCurrentMap()][index].damageReaction();
                getGamePanel().setGameState(getGamePanel().BATTLE_STATE);
            }
        }
    }

    private void checkEvent() {
        getGamePanel().getEventHandler().checkEvent();
    }

    private void resetEnterPressedValue() {
        keyHandler.setEnterPressed(false);
    }

    private void resetSpriteToDefault() {
        resetTimer++;
        if (resetTimer == 20) {
            setSpriteNumber(1);
            resetTimer = 0;
        }
    }

    private void fireProjectileIfKeyPressed() {
        if (getGamePanel().getKeyHandler().isProjectileKeyPressed()
                && !getProjectile().isAlive()
                && getProjectileAvailableCounter() == 30
                && getProjectile().haveEnoughResource(this)) {

            // Set default coordinates, direction and user
            getProjectile().set(getWorldX(), getWorldY(), getDirection(), true, this);

            // Subtract use cost
            getProjectile().subtractResource(this);

            // Add it to the projectiles list
            getGamePanel().getProjectiles().add(getProjectile());

            setProjectileAvailableCounter(0);

            getGamePanel().playSoundEffect(10);
        }

        if (getProjectileAvailableCounter() < 30) {
            setProjectileAvailableCounter(getProjectileAvailableCounter() + 1);
        }
    }

    private void updateLifeAndMana() {
        if (getCurrentLife() > getMaxLife()) {
            setCurrentLife(getMaxLife());
        }

        if (getCurrentLife() < 0) {
            setCurrentLife(0);
        }

        if (getCurrentMana() > getMaxMana()) {
            setCurrentMana(getMaxMana());
        }

        if (getCurrentMana() < 0) {
            setCurrentMana(0);
        }
    }

    private void checkIfAlive() {
        if (getCurrentLife() <= 0) {
            getGamePanel().playSoundEffect(11);
            getGamePanel().setGameState(getGamePanel().getGameOverState());
            setInvincible(false);
        }
    }

    public void selectItem() {
        int itemIndex = getGamePanel().getUi().getItemIndexFromSlot(getGamePanel().getUi().getPlayerSlotCol(), getGamePanel().getUi().getPlayerSlotRow());

        if (itemIndex < getInventory().size()) {
            Asset selectedItem = getInventory().get(itemIndex);

            if (selectedItem instanceof Weapon) {
                setCurrentWeapon((Weapon) selectedItem);
                setAttackPower(getAttack());
                setPlayerAttackArea();
                getAttackImages();
            }

            if (selectedItem instanceof Shield) {
                setCurrentShield((Shield) selectedItem);
                setDefensePower(getDefense());
            }

            if (selectedItem instanceof OBJ_Potion_Red) {
                selectedItem.use();
                getInventory().remove(itemIndex);
            }
        }
    }

    // Item use in battle, utilizes the battleRow of UI and the visibleBattleItems List
    public void inBattleSelectItem(int row) {
        // Healing items only right now
        if(getCurrentLife() < getMaxLife()) {
            getGamePanel().getUi().setCurrentDialogue("Recovered 5HP!");
            setCurrentLife(getCurrentLife() + 5);
            if (getCurrentLife() > getMaxLife()) {
                setCurrentLife(getMaxLife());
            }
            getGamePanel().playSoundEffect(2);

            getInventory().remove(getGamePanel().getUi().visibleBattleInventory.get(row));
        } else {
            getGamePanel().getUi().setCurrentDialogue("You're already at full health!");
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        int rightOffset = getGamePanel().getScreenWidth() - screenX;
        int x = checkIfAtEdgeOfXAxis(rightOffset);

        int bottomOffset = getGamePanel().getScreenHeight() - screenY;
        int y = checkIfAtEdgeOfYAxis(bottomOffset);

        if (isInvincible()) {
            int i = 5; // Frame interval

            // Utilizes the already running invincibleCounter
            if(getInvincibleCounter() <= i) { UtilityTool.changeAlpha(graphics2D, 1f); }
            if(getInvincibleCounter() > i && getInvincibleCounter() <= i * 2) { UtilityTool.changeAlpha(graphics2D, 0.3f); }
            if(getInvincibleCounter() > i * 2 && getInvincibleCounter() <= i * 3) { UtilityTool.changeAlpha(graphics2D, 1f); }
            if(getInvincibleCounter() > i * 3 && getInvincibleCounter() <= i * 4) { UtilityTool.changeAlpha(graphics2D, 0.3f); }
            if(getInvincibleCounter() > i * 4 && getInvincibleCounter() <= i * 5) { UtilityTool.changeAlpha(graphics2D, 1f); }
            if(getInvincibleCounter() > i * 5 && getInvincibleCounter() <= i * 6) { UtilityTool.changeAlpha(graphics2D, 0.3f); }
        }

        // Adjusts player x and y to allow for more sprite variance (taller and/or wider attack sprites)
        if (isAttacking()) {
            if ((getDirection().equals("up") && getSpriteNumber() == 1)) {
                graphics2D.drawImage(getDirectionalAnimationImage(), x, y, null);

            } else if (getDirection().equals("up") && getSpriteNumber() == 2 ||
                    (getDirection().equals("up") && getSpriteNumber() == 3)) {
                graphics2D.drawImage(getDirectionalAnimationImage(), x, y - getGamePanel().getTileSize(), null);

            } else if ((getDirection().equals("down") && getSpriteNumber() == 1)) {
                graphics2D.drawImage(getDirectionalAnimationImage(), x, y - getGamePanel().getTileSize(), null);
            } else if ((getDirection().equals("down") && getSpriteNumber() == 3) ||
                    (getDirection().equals("down") && getSpriteNumber() == 2)) {
                graphics2D.drawImage(getDirectionalAnimationImage(), x, y, null);

            } else if (getDirection().equals("left") && getSpriteNumber() == 1) {
                graphics2D.drawImage(getDirectionalAnimationImage(), x, y - getGamePanel().getTileSize(), null);
            } else if (getDirection().equals("left") && getSpriteNumber() == 2) {
                graphics2D.drawImage(getDirectionalAnimationImage(), x - getGamePanel().getTileSize(), y - getGamePanel().getTileSize(), null);
            } else if (getDirection().equals("left") && getSpriteNumber() == 3) {
                graphics2D.drawImage(getDirectionalAnimationImage(), x - getGamePanel().getTileSize(), y, null);

            } else if (getDirection().equals("right") && getSpriteNumber() == 1) {
                graphics2D.drawImage(getDirectionalAnimationImage(), x, y - getGamePanel().getTileSize(), null);
            } else if (getDirection().equals("right") && getSpriteNumber() == 2) {
                graphics2D.drawImage(getDirectionalAnimationImage(), x, y - getGamePanel().getTileSize(), null);
            } else if (getDirection().equals("right") && getSpriteNumber() == 3) {
                graphics2D.drawImage(getDirectionalAnimationImage(), x, y, null);

                // Default x and y
            } else {
                graphics2D.drawImage(getDirectionalAnimationImage(), x, y, null);
            }
        } else {
            graphics2D.drawImage(getDirectionalAnimationImage(), x, y, null);
        }

            // Old directional adjustment mechanic. Replaced by above if statements, but I haven't deleted it yet.
//            switch (getDirection()) {
//                case "up" ->
//                    graphics2D.drawImage(getDirectionalAnimationImage(), x, y - getGamePanel().getTileSize(), null);
//
//                case "down" ->
//                    graphics2D.drawImage(getDirectionalAnimationImage(), x, y - getGamePanel().getTileSize(), null);
//                case "left" ->
//                        graphics2D.drawImage(getDirectionalAnimationImage(), x - getGamePanel().getTileSize(), y, null);
//                default -> graphics2D.drawImage(getDirectionalAnimationImage(), x, y, null);
//            }
//        } else {
//            graphics2D.drawImage(getDirectionalAnimationImage(), x, y, null);
//        }

        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));
    }

    private int checkIfAtEdgeOfXAxis(int rightOffset) {
        if (screenX > getWorldX()) {
            return getWorldX();
        }

        if (rightOffset > getGamePanel().getWorldWidth() - getWorldX()) {
            return getGamePanel().getScreenWidth() - (getGamePanel().getWorldWidth() - getWorldX());
        }

        return screenX;
    }

    private int checkIfAtEdgeOfYAxis(int bottomOffset) {
        if (screenY > getWorldY()) {
            return getWorldY();
        }

        if (bottomOffset > getGamePanel().getWorldHeight() - getWorldY()) {
            return getGamePanel().getScreenHeight() - (getGamePanel().getWorldHeight() - getWorldY());
        }

        return screenY;
    }


    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    @Override
    public BufferedImage getImage1() {
        return getDown1();
    }


    // NOT USED
    @Override
    public void damageReaction() {
        // Not used yet
    }

    @Override
    public boolean isCollision() {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void use() {
        // Not used
    }

    @Override
    public void resetDefaultSpeed() {
        setSpeed(4);
    }

    public boolean getIsBattleMenuVisible() {
        return this.isBattleMenuVisible;
    }

    // THIS is important, haven't fully fleshed it out, it just auto runs healing items at the moment
    public boolean checkItemUsability(int itemType) {
        boolean usable = false;

        if(itemType == 1) {
            if(getCurrentLife() < getMaxLife()) {
                usable = true;
            }
        }

        return usable;
    }
}
