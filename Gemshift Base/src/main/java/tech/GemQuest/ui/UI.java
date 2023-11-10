package tech.GemQuest.ui;

import tech.GemQuest.GamePanel;
import tech.GemQuest.asset.Asset;
import tech.GemQuest.asset.entity.Entity;
import tech.GemQuest.asset.object.Object;
import tech.GemQuest.asset.object.usable.pickuponly.OBJ_Coin_Bronze;
import tech.GemQuest.asset.object.usable.pickuponly.OBJ_Heart;
import tech.GemQuest.asset.object.usable.pickuponly.OBJ_ManaCrystal;
import tech.GemQuest.util.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UI {

    private final GamePanel gamePanel;
    private Graphics2D graphics2D;
    private UtilityTool utilityTool = new UtilityTool();
    private final BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank, coin;
    private Font maruMonica, battleMaru, arial_32, arial_40_bold;
    private List<String> messages = new ArrayList<>();
    private List<Integer> messageCounter = new ArrayList<>();
    private boolean gameFinished = false;
    private String currentDialogue;
    private int commandNumber;
    private int titleScreenState;
    private int playerSlotCol;
    private int playerSlotRow;
    private int npcSlotCol;
    private int npcSlotRow;
    private int subState;
    private int counter;
    private Entity npc;
    public int interactingMonster;
    public int battleRow;
    private boolean battleOver;
    private String battleCharacterText = "";

    // COUNTERS
    public int genericCounter;
    public int battleCounter = 0;
    public int colorCounter = 0;
    public int blueCounter = 0;
    public int greenCounter = 0;
    public int redCounter = 0;
    public int bgRedValue = 0;
    public int bgGreenValue = 0;
    public int bgBlueValue = 0;
    public int transitionCounter = 1;
    public int playStateShuffle = 0;
    public int idleAnimationCounter = 0;
    public int turnTimeCounter = 0;
    public int currentDamageDealt = 0;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        setupFonts();

        Object heart = new OBJ_Heart(gamePanel);
        this.heart_full = heart.getImage1();
        this.heart_half = heart.getImage2();
        this.heart_blank = heart.getImage3();

        Object manaCrystal = new OBJ_ManaCrystal(gamePanel);
        this.crystal_full = manaCrystal.getImage1();
        this.crystal_blank = manaCrystal.getImage2();

        Object coin = new OBJ_Coin_Bronze(gamePanel);
        this.coin = coin.getImage1();
    }

    private void setupFonts() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/fonts/x12y16pxMaruMonica.ttf");
            this.maruMonica = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(inputStream));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        this.battleMaru = maruMonica.deriveFont(Font.BOLD, 60);
        this.arial_32 = new Font("Arial", Font.PLAIN, 32);
        this.arial_40_bold = new Font("Arial", Font.BOLD, 40);
    }

    public void draw(Graphics2D graphics2D) {
        this.graphics2D = graphics2D;

        setupDefaultGraphics(graphics2D);

        if (gamePanel.getGameState() == gamePanel.getTitleState()) {
            drawTitleScreen();
        }
        if (gamePanel.getGameState() == gamePanel.getPlayState()) {
            // Transition out of Battle State
            if(gamePanel.player.fromBattleState) {
                gamePanel.player.setInvincible(true);
                if(playStateShuffle == 0) {
                    if (transitionCounter <= 45) {
                        // Draw last battle screen.
                        drawBattleBackground();

                        drawBattleTopFrame(currentDialogue);
                        drawBattleBottomFrame(1);

                        graphics2D.setColor(new Color(0, 0, 0, transitionCounter * 5));
                        graphics2D.fillRect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());

                        transitionCounter++;
                    }
                    if (transitionCounter > 45) {
                        playStateShuffle++;
                        transitionCounter = 1;
                        gamePanel.player.battleMenuOn = false;
                        gamePanel.player.setInvincibleCounter(0);
                    }
                }
                if(playStateShuffle == 1) {
                    // No need to deliberately call to draw the Play State

                    if (transitionCounter <= 45) {
                        graphics2D.setColor(new Color(0, 0, 0, 255 - (transitionCounter * 5)));
                        graphics2D.fillRect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());

                        transitionCounter++;
                    }
                    else {
                        playStateShuffle++;
                        transitionCounter = 1;
                        gamePanel.player.setInvincibleCounter(0);
                    }
                }
                if(playStateShuffle == 2) {
                    playStateShuffle = 0;
                    transitionCounter = 1;
                    genericCounter = 0;
                    gamePanel.player.fromBattleState = false;
                    gamePanel.player.setInvincibleCounter(0);
                    gamePanel.player.battleMonsterID = 0; // Reset this just to be safe
                }
            }

            else { // Standard operating procedure
                drawPlayerLife();
                drawPlayerMana();
                drawMessages();

                // Resetting counters from other states
                battleCounter = 0;
                colorCounter = 0;
                redCounter = 0;
                blueCounter = 0;
                greenCounter = 0;
                battleRow = 0;
            }
        }
        if (gamePanel.getGameState() == gamePanel.getPauseState()) {
            drawPlayerLife();
            drawPlayerMana();
            drawPauseScreen();
        }
        if (gamePanel.getGameState() == gamePanel.getDialogueState()) {
            drawDialogueScreen();
        }
        if (gamePanel.getGameState() == gamePanel.getCharacterState()) {
            colorCounter = 0;
            redCounter = 0;
            blueCounter = 0;
            greenCounter = 0;
            battleCounter = 0; // Have to reset all of these when Play State is entered to make sure the Battle State loads properly

            drawCharacterScreen();
            drawInventoryScreen(gamePanel.getPlayer(), true);
        }
        if (gamePanel.getGameState() == gamePanel.getOptionState()) {
            drawOptionScreen();
        }
        if (gamePanel.getGameState() == gamePanel.getGameOverState()) {
            drawGameOverScreen();
        }
        if (gamePanel.getGameState() == gamePanel.getTransitionState()) {
            drawTransitionScreen();
        }
        if (gamePanel.getGameState() == gamePanel.getTradeState()) {
            drawTradeScreen();
        }
        if (gamePanel.getGameState() == gamePanel.getBattleState()) {
            gamePanel.player.fromBattleState = true;
            transitionCounter = 1;
            drawBattleScreen();
        }
    }


    private void setupDefaultGraphics(Graphics2D graphics2D) {
        graphics2D.setFont(maruMonica);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setColor(Color.WHITE);
    }

    private void drawTitleScreen() {
        graphics2D.setColor(Color.black);
        graphics2D.fillRect(0, 0, gamePanel.getScreenWidth(), gamePanel.getScreenHeight());

        switch (titleScreenState) {
            case 0 -> drawStartScreen();
            case 1 -> drawClassScreen();
        }

        gamePanel.getKeyHandler().setEnterPressed(false);
    }

    private void drawStartScreen() {

        // TITLE TEXT
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 96F));

        String text = "Gem Quest";
        int x = UtilityTool.getXForCenterOfText(text, gamePanel, graphics2D);
        int y = gamePanel.getTileSize() * 3;

        // Shadow
        graphics2D.setColor(Color.GRAY);
        graphics2D.drawString(text, x + 5, y + 5);

        // Text
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(text, x, y);

        // GAME IMAGE
        x = gamePanel.getScreenWidth() / 2 - (gamePanel.getTileSize() * 2) / 2;
        y += gamePanel.getTileSize() * 2;
        graphics2D.drawImage(gamePanel.getPlayer().getDown1(), x, y, gamePanel.getTileSize() * 2, gamePanel.getTileSize() * 2, null);

        // MENU
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 48F));

        text = "NEW GAME";
        x = UtilityTool.getXForCenterOfText(text, gamePanel, graphics2D);
        y += gamePanel.getTileSize() * 3.5;
        graphics2D.drawString(text, x, y);
        if (commandNumber == 0) {
            graphics2D.drawString(">", x - gamePanel.getTileSize(), y);
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                titleScreenState = 1;
                commandNumber = 0;
            }
        }

        text = "LOAD GAME";
        x = UtilityTool.getXForCenterOfText(text, gamePanel, graphics2D);
        y += gamePanel.getTileSize();
        graphics2D.drawString(text, x, y);
        if (commandNumber == 1) {
            graphics2D.drawString(">", x - gamePanel.getTileSize(), y);
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                // Later
            }
        }

        text = "QUIT";
        x = UtilityTool.getXForCenterOfText(text, gamePanel, graphics2D);
        y += gamePanel.getTileSize();
        graphics2D.drawString(text, x, y);
        if (commandNumber == 2) {
            graphics2D.drawString(">", x - gamePanel.getTileSize(), y);
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                System.exit(0);
            }
        }
    }

    private void drawClassScreen() {
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 42F));

        String text = "Select your class!";
        int x = UtilityTool.getXForCenterOfText(text, gamePanel, graphics2D);
        int y = gamePanel.getTileSize() * 3;
        graphics2D.drawString(text, x, y);

        text = "Fighter";
        x = UtilityTool.getXForCenterOfText(text, gamePanel, graphics2D);
        y += gamePanel.getTileSize() * 3;
        graphics2D.drawString(text, x, y);
        if (commandNumber == 0) {
            graphics2D.drawString(">", x - gamePanel.getTileSize(), y);
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                gamePanel.setGameState(gamePanel.getPlayState());
                gamePanel.playMusic(0);
                commandNumber = 0;
                titleScreenState = 0;
            }
        }

        text = "Rogue";
        x = UtilityTool.getXForCenterOfText(text, gamePanel, graphics2D);
        y += gamePanel.getTileSize();
        graphics2D.drawString(text, x, y);
        if (commandNumber == 1) {
            graphics2D.drawString(">", x - gamePanel.getTileSize(), y);
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                gamePanel.setGameState(gamePanel.getPlayState());
                gamePanel.playMusic(0);
                commandNumber = 0;
                titleScreenState = 0;
            }
        }

        text = "Sorcerer";
        x = UtilityTool.getXForCenterOfText(text, gamePanel, graphics2D);
        y += gamePanel.getTileSize();
        graphics2D.drawString(text, x, y);
        if (commandNumber == 2) {
            graphics2D.drawString(">", x - gamePanel.getTileSize(), y);
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                gamePanel.setGameState(gamePanel.getPlayState());
                gamePanel.playMusic(0);
                commandNumber = 0;
                titleScreenState = 0;
            }
        }

        text = "Cancel";
        x = UtilityTool.getXForCenterOfText(text, gamePanel, graphics2D);
        y += gamePanel.getTileSize() * 2;
        graphics2D.drawString(text, x, y);
        if (commandNumber == 3) {
            graphics2D.drawString(">", x - gamePanel.getTileSize(), y);
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                titleScreenState = 0;
                commandNumber = 0;
            }
        }
    }

    private void drawPlayerLife() {
        int x = gamePanel.getTileSize() / 2;
        int y = gamePanel.getTileSize() / 2;

        for (int i = 0; i < gamePanel.getPlayer().getMaxLife() / 2; i++) {
            graphics2D.drawImage(heart_blank, x, y, null);
            x += gamePanel.getTileSize();
        }

        x = gamePanel.getTileSize() / 2;
        y = gamePanel.getTileSize() / 2;

        for (int i = 0; i < gamePanel.getPlayer().getCurrentLife(); i++) {
            graphics2D.drawImage(heart_half, x, y, null);
            i++;

            if (i < gamePanel.getPlayer().getCurrentLife()) {
                graphics2D.drawImage(heart_full, x, y, null);
            }

            x += gamePanel.getTileSize();
        }
    }

    private void drawPlayerMana() {
        int x = (gamePanel.getTileSize() / 2) - 5;
        int y = (int) (gamePanel.getTileSize() * 1.5);

        for (int i = 0; i < gamePanel.getPlayer().getMaxMana(); i++) {
            graphics2D.drawImage(crystal_blank, x, y, null);
            x += 35;
        }

        x = (gamePanel.getTileSize() / 2) - 5;
        y = (int) (gamePanel.getTileSize() * 1.5);

        for (int i = 0; i < gamePanel.getPlayer().getCurrentMana(); i++) {
            graphics2D.drawImage(crystal_full, x, y, null);
            x += 35;
        }
    }

    public void addMessage(String text) {
        messages.add(text);
        messageCounter.add(0);
    }

    private void drawMessages() {
        int messageX = gamePanel.getTileSize();
        int messageY = gamePanel.getTileSize() * 4;
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 32F));

        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i) != null) {
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawString(messages.get(i), messageX + 2, messageY + 2);
                graphics2D.setColor(Color.WHITE);
                graphics2D.drawString(messages.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1;
                messageCounter.set(i, counter);
                messageY += 50;

                if (messageCounter.get(i) > 180) {
                    messages.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }

    private void drawPauseScreen() {
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 80F));

        String text = "PAUSED";
        int x = UtilityTool.getXForCenterOfText(text, gamePanel, graphics2D);
        int y = gamePanel.getScreenHeight() / 2;

        graphics2D.drawString(text, x, y);
    }

    private void drawDialogueScreen() {
        int x = gamePanel.getTileSize() * 3;
        int y = gamePanel.getTileSize() / 2;
        int width = gamePanel.getScreenWidth() - (gamePanel.getTileSize() * 6);
        int height = gamePanel.getTileSize() * 4;

        drawSubWindow(x, y, width, height);

        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 32F));
        x += gamePanel.getTileSize();
        y += gamePanel.getTileSize();

        splitAndDrawDialogue(x, y);
    }

    private void drawCharacterScreen() {
        int frameX = gamePanel.getTileSize() * 2;
        int frameY = gamePanel.getTileSize();
        int frameWidth = gamePanel.getTileSize() * 5;
        int frameHeight = (int) (gamePanel.getTileSize() * 10.5);

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 32F));

        int textX = frameX + 20;
        int textY = frameY + gamePanel.getTileSize();
        int lineHeight = 35;

        drawText(textX, textY, lineHeight);

        int tailX = (frameX + frameWidth) - 30;

        drawValues(textY, lineHeight, tailX);
    }

    private void drawText(int textX, int textY, int lineHeight) {
        graphics2D.drawString("Level", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Life", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Mana", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Strength", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Attack", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Defense", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Exp", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Next Level", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Coins", textX, textY);
        textY += lineHeight + 20;
        graphics2D.drawString("Weapon", textX, textY);
        textY += lineHeight + 15;
        graphics2D.drawString("Shield", textX, textY);
    }

    private void drawValues(int textY, int lineHeight, int tailX) {
        int textX;
        String value;

        value = String.valueOf(gamePanel.getPlayer().getLevel());
        textX = UtilityTool.getXForAlightToRightOfText(value, tailX, gamePanel, graphics2D);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = gamePanel.getPlayer().getCurrentLife() + "/" + gamePanel.getPlayer().getMaxLife();
        textX = UtilityTool.getXForAlightToRightOfText(value, tailX, gamePanel, graphics2D);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = gamePanel.getPlayer().getCurrentMana() + "/" + gamePanel.getPlayer().getMaxMana();
        textX = UtilityTool.getXForAlightToRightOfText(value, tailX, gamePanel, graphics2D);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.getPlayer().getStrength());
        textX = UtilityTool.getXForAlightToRightOfText(value, tailX, gamePanel, graphics2D);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.getPlayer().getDexterity());
        textX = UtilityTool.getXForAlightToRightOfText(value, tailX, gamePanel, graphics2D);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.getPlayer().getAttackPower());
        textX = UtilityTool.getXForAlightToRightOfText(value, tailX, gamePanel, graphics2D);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.getPlayer().getDefensePower());
        textX = UtilityTool.getXForAlightToRightOfText(value, tailX, gamePanel, graphics2D);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.getPlayer().getExp());
        textX = UtilityTool.getXForAlightToRightOfText(value, tailX, gamePanel, graphics2D);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.getPlayer().getNextLevelExp());
        textX = UtilityTool.getXForAlightToRightOfText(value, tailX, gamePanel, graphics2D);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.getPlayer().getCoins());
        textX = UtilityTool.getXForAlightToRightOfText(value, tailX, gamePanel, graphics2D);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        drawCurrentEquipment(textY, tailX);
    }

    private void drawCurrentEquipment(int textY, int tailX) {
        graphics2D.drawImage(gamePanel.getPlayer().getCurrentWeapon().getImage1(), tailX - gamePanel.getTileSize(), textY - 14, null);
        textY += gamePanel.getTileSize();

        graphics2D.drawImage(gamePanel.getPlayer().getCurrentShield().getImage1(), tailX - gamePanel.getTileSize(), textY - 14, null);
    }

    private void drawInventoryScreen(Entity entity, boolean cursor) {

        int frameX = 0;
        int frameY = 0;
        int frameWidth = 0;
        int frameHeight = 0;
        int slotCol = 0;
        int slotRow = 0;

        // ITEM FRAME BOX
        if (entity == gamePanel.getPlayer()) {
            frameX = gamePanel.getTileSize() * 12;
            slotCol = playerSlotCol;
            slotRow = playerSlotRow;
        } else {
            frameX = gamePanel.getTileSize() * 2;
            slotCol = npcSlotCol;
            slotRow = npcSlotRow;
        }

        frameY = gamePanel.getTileSize();
        frameWidth = gamePanel.getTileSize() * 6;
        frameHeight = gamePanel.getTileSize() * 5;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // ITEM SLOTS
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gamePanel.getTileSize() + 3;

        List<Asset> inventory = entity.getInventory();

        drawItemsInInventory(entity, slotXStart, slotX, slotY, slotSize, inventory);

        if (cursor) {
            drawSelectionBox(slotXStart, slotYStart, slotSize, slotCol, slotRow);

            // DESCRIPTION FRAME
            int descriptionFrameX = frameX;
            int descriptionFrameY = frameY + frameHeight;
            int descriptionFrameWidth = frameWidth;
            int descriptionFrameHeight = gamePanel.getTileSize() * 3;

            drawItemDescriptionText(inventory, descriptionFrameX, descriptionFrameY, descriptionFrameWidth, descriptionFrameHeight, slotCol, slotRow);
        }
    }

    private void drawItemsInInventory(Entity entity, int slotXStart, int slotX, int slotY, int slotSize, List<Asset> inventory) {
        for (int i = 0; i < inventory.size(); i++) {
            Asset object = inventory.get(i);

            // EQUIPPED BOX COLOR
            if (object == entity.getCurrentWeapon()
                    || object == entity.getCurrentShield()) {
                graphics2D.setColor(new Color(240, 190, 90));
                graphics2D.fillRoundRect(slotX, slotY, gamePanel.getTileSize(), gamePanel.getTileSize(), 10, 10);
            }

            graphics2D.drawImage(object.getImage1(), slotX, slotY, null);

            slotX += slotSize;

            if (i == 4 || i == 9 || i == 14) {
                slotX = slotXStart;
                slotY += slotSize;
            }
        }
    }

    private void drawSelectionBox(int slotXStart, int slotYStart, int slotSize, int slotCol, int slotRow) {
        // CURSOR selection box
        int cursorX = slotXStart + (slotSize * slotCol);
        int cursorY = slotYStart + (slotSize * slotRow);
        int cursorWidth = gamePanel.getTileSize();
        int cursorHeight = gamePanel.getTileSize();

        graphics2D.setColor(Color.WHITE);
        graphics2D.setStroke(new BasicStroke(3));
        graphics2D.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
    }

    private void drawItemDescriptionText(List<Asset> inventory, int descriptionFrameX, int descriptionFrameY, int descriptionFrameWidth, int descriptionFrameHeight, int slotCol, int slotRow) {
        // DRAW DESCRIPTION TEXT
        int textX = descriptionFrameX + 20;
        int textY = descriptionFrameY + gamePanel.getTileSize();

        graphics2D.setFont(graphics2D.getFont().deriveFont(28F));

        int itemIndex = getItemIndexFromSlot(slotCol, slotRow);

        if (itemIndex < inventory.size()) {

            drawSubWindow(descriptionFrameX, descriptionFrameY, descriptionFrameWidth, descriptionFrameHeight);

            for (String line : inventory.get(itemIndex).getDescription().split("\n")) {
                graphics2D.drawString(line, textX, textY);
                textY += 32;
            }
        }
    }

    public int getItemIndexFromSlot(int slotCol, int slotRow) {
        int itemIndex = slotCol + (slotRow * 5);
        return itemIndex;
    }

    private void drawOptionScreen() {
        graphics2D.setColor(Color.white);
        graphics2D.setFont(graphics2D.getFont().deriveFont(32F));

        // SUB WINDOW
        int frameX = gamePanel.getTileSize() * 6;
        int frameY = gamePanel.getTileSize();
        int frameWidth = gamePanel.getTileSize() * 8;
        int frameHeight = gamePanel.getTileSize() * 10;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch (subState) {
            case 0 -> optionsTop(frameX, frameY);
            case 1 -> optionsFullScreenNotification(frameX, frameY);
            case 2 -> optionsControls(frameX, frameY);
            case 3 -> optionsEndGameConfirmation(frameX, frameY);
        }

        gamePanel.getKeyHandler().setEnterPressed(false);
    }

    private void optionsTop(int frameX, int frameY) {
        int textX;
        int textY;

        // TITLE
        String text = "Options";
        textX = UtilityTool.getXForCenterOfText(text, gamePanel, graphics2D);
        textY = frameY + gamePanel.getTileSize();
        graphics2D.drawString(text, textX, textY);

        // FULLSCREEN ON/OFF
        textX = frameX + gamePanel.getTileSize();
        textY += gamePanel.getTileSize() * 2;
        graphics2D.drawString("Full Screen", textX, textY);
        if (commandNumber == 0) {
            graphics2D.drawString(">", textX - 25, textY);
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                gamePanel.setFullScreenOn(!gamePanel.isFullScreenOn());
                subState = 1;
                commandNumber = 0;
            }
        }

        // MUSIC
        textY += gamePanel.getTileSize();
        graphics2D.drawString("Music", textX, textY);
        if (commandNumber == 1) {
            graphics2D.drawString(">", textX - 25, textY);
        }

        // SOUND EFFECT
        textY += gamePanel.getTileSize();
        graphics2D.drawString("Sound Effects", textX, textY);
        if (commandNumber == 2) {
            graphics2D.drawString(">", textX - 25, textY);
        }

        // CONTROLS
        textY += gamePanel.getTileSize();
        graphics2D.drawString("Controls", textX, textY);
        if (commandNumber == 3) {
            graphics2D.drawString(">", textX - 25, textY);
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                subState = 2;
                commandNumber = 0;
            }
        }

        // END GAME
        textY += gamePanel.getTileSize();
        graphics2D.drawString("End Game", textX, textY);
        if (commandNumber == 4) {
            graphics2D.drawString(">", textX - 25, textY);
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                subState = 3;
                commandNumber = 0;
            }
        }

        // BACK
        textY += gamePanel.getTileSize() * 2;
        graphics2D.drawString("Back", textX, textY);
        if (commandNumber == 5) {
            graphics2D.drawString(">", textX - 25, textY);
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                gamePanel.setGameState(gamePanel.getPlayState());
                commandNumber = 0;
            }
        }

        // FULLSCREEN CHECK BOX
        textX = frameX + (int) (gamePanel.getTileSize() * 4.5);
        textY = frameY + gamePanel.getTileSize() * 2 + 24;
        graphics2D.setStroke(new BasicStroke(3));
        graphics2D.drawRect(textX, textY, 24, 24);
        if (gamePanel.isFullScreenOn()) {
            graphics2D.fillRect(textX, textY, 24, 24);
        }

        // MUSIC VOLUME
        textY += gamePanel.getTileSize();
        graphics2D.drawRect(textX, textY, 120, 24); // 120 / 5 = 24

        int volumeWidth = 24 * gamePanel.getMusic().getVolumeScale();
        graphics2D.fillRect(textX, textY, volumeWidth, 24);

        // SOUND EFFECT VOLUME
        textY += gamePanel.getTileSize();
        graphics2D.drawRect(textX, textY, 120, 24);

        volumeWidth = 24 * gamePanel.getSoundEffect().getVolumeScale();
        graphics2D.fillRect(textX, textY, volumeWidth, 24);

        // SAVE CONFIGURATION
        gamePanel.getConfig().saveConfig();
    }

    public void optionsFullScreenNotification(int frameX, int frameY) {
        int textX = frameX + gamePanel.getTileSize();
        int textY = frameY + gamePanel.getTileSize() * 3;

        currentDialogue = "The change will take \neffect after restarting \nthe game.";

        splitAndDrawDialogue(textX, textY);

        // BACK
        textY = frameY + gamePanel.getTileSize() * 9;
        graphics2D.drawString("Back", textX, textY);
        graphics2D.drawString(">", textX - 25, textY);
        if (gamePanel.getKeyHandler().isEnterPressed()) {
            subState = 0;
            commandNumber = 0;
        }
    }

    private void optionsControls(int frameX, int frameY) {
        int textX;
        int textY;

        // TITLE
        String text = "Controls";
        textX = UtilityTool.getXForCenterOfText(text, gamePanel, graphics2D);
        textY = frameY + gamePanel.getTileSize();
        graphics2D.drawString(text, textX, textY);

        textX = frameX + gamePanel.getTileSize();
        textY += gamePanel.getTileSize();
        graphics2D.drawString("Move", textX, textY);
        graphics2D.drawString("WASD", textX + gamePanel.getTileSize() * 5, textY);
        textY += gamePanel.getTileSize();
        graphics2D.drawString("Confirm/Interact", textX, textY);
        graphics2D.drawString("ENTER", textX + gamePanel.getTileSize() * 5, textY);
        textY += gamePanel.getTileSize();
        graphics2D.drawString("Attack", textX, textY);
        graphics2D.drawString("SPACE", textX + gamePanel.getTileSize() * 5, textY);
        textY += gamePanel.getTileSize();
        graphics2D.drawString("Shoot Projectile", textX, textY);
        graphics2D.drawString("F", textX + gamePanel.getTileSize() * 5, textY);
        textY += gamePanel.getTileSize();
        graphics2D.drawString("Character Screen", textX, textY);
        graphics2D.drawString("C", textX + gamePanel.getTileSize() * 5, textY);
        textY += gamePanel.getTileSize();
        graphics2D.drawString("Pause", textX, textY);
        graphics2D.drawString("P", textX + gamePanel.getTileSize() * 5, textY);

        // BACK
        textX = frameX + gamePanel.getTileSize();
        textY = frameY + gamePanel.getTileSize() * 9;
        graphics2D.drawString("Back", textX, textY);
        graphics2D.drawString(">", textX - 25, textY);
        if (gamePanel.getKeyHandler().isEnterPressed()) {
            subState = 0;
            commandNumber = 3;
        }
    }

    private void optionsEndGameConfirmation(int frameX, int frameY) {
        int textX = frameX + gamePanel.getTileSize();
        int textY = frameY + gamePanel.getTileSize() * 3;

        currentDialogue = "Quit the game and \nreturn to the title Screen?";

        splitAndDrawDialogue(textX, textY);

        // YES
        String text = "Yes";
        textX = UtilityTool.getXForCenterOfText(text, gamePanel, graphics2D);
        textY += gamePanel.getTileSize() * 3;
        graphics2D.drawString(text, textX, textY);
        if (commandNumber == 0) {
            graphics2D.drawString(">", textX - 25, textY);
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                subState = 0;
                gamePanel.setGameState(gamePanel.getTitleState());
                titleScreenState = 0;
                commandNumber = 0;
                gamePanel.stopMusic();
            }
        }

        // NO
        text = "No";
        textX = UtilityTool.getXForCenterOfText(text, gamePanel, graphics2D);
        textY += gamePanel.getTileSize();
        graphics2D.drawString(text, textX, textY);
        if (commandNumber == 1) {
            graphics2D.drawString(">", textX - 25, textY);
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                subState = 0;
                commandNumber = 4;
            }
        }
    }

    private void drawGameOverScreen() {
        graphics2D.setColor(new Color(0, 0, 0, 150));
        graphics2D.fillRect(0, 0, gamePanel.getScreenWidth(), gamePanel.getScreenHeight());

        int textX;
        int textY;
        String text;
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 110f));

        // TITLE TEXT
        text = "Game Over";

        // Shadow
        graphics2D.setColor(Color.black);
        textX = UtilityTool.getXForCenterOfText(text, gamePanel, graphics2D);
        textY = gamePanel.getTileSize() * 4;
        graphics2D.drawString(text, textX, textY);

        // Text
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(text, textX - 4, textY - 4);

        // RETRY
        graphics2D.setFont(graphics2D.getFont().deriveFont(50f));
        text = "Retry";
        textX = UtilityTool.getXForCenterOfText(text, gamePanel, graphics2D);
        textY += gamePanel.getTileSize() * 4;
        graphics2D.drawString(text, textX, textY);
        if (commandNumber == 0) {
            graphics2D.drawString(">", textX - 40, textY);
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                commandNumber = 0;
                gamePanel.retry();
            }
        }

        // BACK TO TITLE
        text = "Quit";
        textX = UtilityTool.getXForCenterOfText(text, gamePanel, graphics2D);
        textY += 55;
        graphics2D.drawString(text, textX, textY);
        if (commandNumber == 1) {
            graphics2D.drawString(">", textX - 40, textY);
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                commandNumber = 0;
                gamePanel.restart();
            }
        }

        gamePanel.getKeyHandler().setEnterPressed(false);
    }

    private void drawTransitionScreen() {
        counter++;
        graphics2D.setColor(new Color(0, 0, 0, counter * 5));
        graphics2D.fillRect(0, 0, gamePanel.getScreenWidth(), gamePanel.getScreenHeight());

        if (counter == 50) {
            counter = 0;
            gamePanel.setGameState(gamePanel.getPlayState());
            gamePanel.setCurrentMap(gamePanel.getEventHandler().getTempMap());
            gamePanel.getPlayer().setWorldX(gamePanel.getTileSize() * gamePanel.getEventHandler().getTempCol());
            gamePanel.getPlayer().setWorldY(gamePanel.getTileSize() * gamePanel.getEventHandler().getTempRow());
            gamePanel.getEventHandler().setPreviousEventX(gamePanel.getPlayer().getWorldX());
            gamePanel.getEventHandler().setPreviousEventY(gamePanel.getPlayer().getWorldY());
        }
    }

    private void drawTradeScreen() {
        switch (subState) {
            case 0 -> tradeSelect();
            case 1 -> tradeBuy();
            case 2 -> tradeSell();
        }
        gamePanel.getKeyHandler().setEnterPressed(false);
    }

    private void tradeSelect() {
        drawDialogueScreen();

        // DRAW WINDOW
        int x = gamePanel.getTileSize() * 15;
        int y = gamePanel.getTileSize() * 4;
        int width = gamePanel.getTileSize() * 3;
        int height = (int) (gamePanel.getTileSize() * 3.5);
        drawSubWindow(x, y, width, height);

        // DRAW TEXT
        x += gamePanel.getTileSize();
        y += gamePanel.getTileSize();
        graphics2D.drawString("Buy", x, y);
        if (commandNumber == 0) {
            graphics2D.drawString(">", x - 24, y);
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                subState = 1;
            }
        }
        y += gamePanel.getTileSize();
        graphics2D.drawString("Sell", x, y);
        if (commandNumber == 1) {
            graphics2D.drawString(">", x - 24, y);
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                subState = 2;
            }
        }
        y += gamePanel.getTileSize();
        graphics2D.drawString("Leave", x, y);
        if (commandNumber == 2) {
            graphics2D.drawString(">", x - 24, y);
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                commandNumber = 0;
                subState = 0;
                gamePanel.setGameState(gamePanel.getDialogueState());
                currentDialogue = "Come again, hehe!";
            }
        }

    }

    private void tradeBuy() {

        // DRAW PLAYER INVENTORY
        drawInventoryScreen(gamePanel.getPlayer(), false);

        // DRAW NPC INVENTORY
        drawInventoryScreen(npc, true);

        // DRAW HINT WINDOW
        int x = gamePanel.getTileSize() * 2;
        int y = gamePanel.getTileSize() * 9;
        int width = gamePanel.getTileSize() * 6;
        int height = gamePanel.getTileSize() * 2;
        drawSubWindow(x, y, width, height);
        graphics2D.drawString("[ESC] Back", x + 24, y + 60);

        // DRAW PLAYER COIN WINDOW
        x = gamePanel.getTileSize() * 12;
        drawSubWindow(x, y, width, height);
        graphics2D.drawString("Your Coin: " + gamePanel.getPlayer().getCoins(), x + 24, y + 60);

        // DRAW PRICE WINDOW
        int itemIndex = getItemIndexFromSlot(npcSlotCol, npcSlotRow);

        if (itemIndex < npc.getInventory().size()) {
            x = (int) (gamePanel.getTileSize() * 5.5);
            y = (int) (gamePanel.getTileSize() * 5.5);
            width = (int) (gamePanel.getTileSize() * 2.5);
            height = gamePanel.getTileSize();
            drawSubWindow(x, y, width, height);
            graphics2D.drawImage(coin, x + 10, y + 8, 32, 32, null);

            int price = npc.getInventory().get(itemIndex).getPrice();
            String text = String.valueOf(price);
            x = UtilityTool.getXForAlightToRightOfText(text, gamePanel.getTileSize() * 8 - 20, gamePanel, graphics2D);
            graphics2D.drawString(text, x, y + 34);

            // BUY
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                if (npc.getInventory().get(itemIndex).getPrice() > gamePanel.getPlayer().getCoins()) {
                    subState = 0;
                    gamePanel.setGameState(gamePanel.getDialogueState());
                    currentDialogue = "You need more coin to buy that!";
                } else if (gamePanel.getPlayer().getInventory().size() == gamePanel.getPlayer().getMaxInventorySize()) {
                    subState = 0;
                    gamePanel.setGameState(gamePanel.getDialogueState());
                    currentDialogue = "You cannot carry anymore!";
                } else {
                    gamePanel.getPlayer().setCoins(gamePanel.getPlayer().getCoins() - npc.getInventory().get(itemIndex).getPrice());
                    gamePanel.getPlayer().getInventory().add(npc.getInventory().get(itemIndex));
                }
            }
        }
    }

    private void tradeSell() {

        // DRAW PLAYER INVENTORY
        drawInventoryScreen(gamePanel.getPlayer(), true);

        // DRAW HINT WINDOW
        int x = gamePanel.getTileSize() * 2;
        int y = gamePanel.getTileSize() * 9;
        int width = gamePanel.getTileSize() * 6;
        int height = gamePanel.getTileSize() * 2;
        drawSubWindow(x, y, width, height);
        graphics2D.drawString("[ESC] Back", x + 24, y + 60);

        // DRAW PLAYER COIN WINDOW
        x = gamePanel.getTileSize() * 12;
        drawSubWindow(x, y, width, height);
        graphics2D.drawString("Your Coin: " + gamePanel.getPlayer().getCoins(), x + 24, y + 60);

        // DRAW PRICE WINDOW
        int itemIndex = getItemIndexFromSlot(playerSlotCol, playerSlotRow);

        if (itemIndex < gamePanel.getPlayer().getInventory().size()) {
            x = (int) (gamePanel.getTileSize() * 15.5);
            y = (int) (gamePanel.getTileSize() * 5.5);
            width = (int) (gamePanel.getTileSize() * 2.5);
            height = gamePanel.getTileSize();
            drawSubWindow(x, y, width, height);
            graphics2D.drawImage(coin, x + 10, y + 8, 32, 32, null);

            int price = gamePanel.getPlayer().getInventory().get(itemIndex).getPrice() / 2;
            String text = String.valueOf(price);
            x = UtilityTool.getXForAlightToRightOfText(text, gamePanel.getTileSize() * 18 - 20, gamePanel, graphics2D);
            graphics2D.drawString(text, x, y + 34);

            // SELL
            if (gamePanel.getKeyHandler().isEnterPressed()) {
                if (gamePanel.getPlayer().getInventory().get(itemIndex) == gamePanel.getPlayer().getCurrentWeapon()
                        || gamePanel.getPlayer().getInventory().get(itemIndex) == gamePanel.getPlayer().getCurrentShield()) {
                    commandNumber = 0;
                    subState = 0;
                    gamePanel.setGameState(gamePanel.getDialogueState());
                    currentDialogue = "You cannot sell an equipped item!";
                } else {
                    gamePanel.getPlayer().getInventory().remove(itemIndex);
                    gamePanel.getPlayer().setCoins(gamePanel.getPlayer().getCoins() + price);
                }
            }
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color color = new Color(0, 0, 0, 210);
        graphics2D.setColor(color);
        graphics2D.fillRoundRect(x, y, width, height, 35, 35);

        color = new Color(255, 255, 255);
        graphics2D.setColor(color);
        graphics2D.setStroke(new BasicStroke(5));
        graphics2D.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    private void splitAndDrawDialogue(int x, int y) {
        for (String line : currentDialogue.split("\n")) {
            graphics2D.drawString(line, x, y);
            y += 40;
        }
    }


    public UI setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
        return this;
    }

    public String getCurrentDialogue() {
        return currentDialogue;
    }

    public UI setCurrentDialogue(String currentDialogue) {
        this.currentDialogue = currentDialogue;
        return this;
    }

    public int getCommandNumber() {
        return commandNumber;
    }

    public UI setCommandNumber(int commandNumber) {
        this.commandNumber = commandNumber;
        return this;
    }

    public int getTitleScreenState() {
        return titleScreenState;
    }

    public UI setTitleScreenState(int titleScreenState) {
        this.titleScreenState = titleScreenState;
        return this;
    }

    public int getPlayerSlotCol() {
        return playerSlotCol;
    }

    public UI setPlayerSlotCol(int playerSlotCol) {
        this.playerSlotCol = playerSlotCol;
        return this;
    }

    public int getPlayerSlotRow() {
        return playerSlotRow;
    }

    public UI setPlayerSlotRow(int playerSlotRow) {
        this.playerSlotRow = playerSlotRow;
        return this;
    }

    public int getSubState() {
        return subState;
    }

    public UI setSubState(int subState) {
        this.subState = subState;
        return this;
    }

    public Entity getNpc() {
        return npc;
    }

    public UI setNpc(Entity npc) {
        this.npc = npc;
        return this;
    }

    public int getNpcSlotCol() {
        return npcSlotCol;
    }

    public UI setNpcSlotCol(int npcSlotCol) {
        this.npcSlotCol = npcSlotCol;
        return this;
    }

    public int getNpcSlotRow() {
        return npcSlotRow;
    }

    public UI setNpcSlotRow(int npcSlotRow) {
        this.npcSlotRow = npcSlotRow;
        return this;
    }

    // Battle State on a plate. Right now the Monster gets no turn and always dies in one hit
    public void drawBattleScreen() {
        interactingMonster = gamePanel.player.battleMonsterID;
        updateColorCounter(colorCounter);

        // This first block is the fade to black from Play State
        if (battleCounter == 0) {
            if (genericCounter <= 45) {
                genericCounter++;
                graphics2D.setColor(new Color(0, 0, 0, genericCounter * 5));
                graphics2D.fillRect(0, 0, gamePanel.getScreenWidth(), gamePanel.getScreenHeight());
            }
            if (genericCounter > 45) {
                battleCounter++;
                genericCounter = 0;
            }
        }

        // Pretty much just calls draw methods and adjusts color counters
        if (battleCounter == 1) {
            if(colorCounter == 0 ) {
                drawBattleBase(interactingMonster, gamePanel.monsters[0][interactingMonster].getName() + " approaches!", 1);

                // Pretty sure THIS is where we sneak in the reverse transition
                if (genericCounter <= 45) {
                    genericCounter++;
                    graphics2D.setColor(new Color(0, 0, 0, 255 - (genericCounter * 5)));
                    graphics2D.fillRect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
                } else {
                    gamePanel.player.battleMenuOn = true;
                }
            }
            if (colorCounter > 0) {
                gamePanel.player.battleMenuOn = true;
                drawBattleBase(interactingMonster, gamePanel.monsters[0][interactingMonster].getIdleMessage(), 1);
            }

            // Battle Menu conditional
            if(gamePanel.player.battleMenuOn) {
                drawBattleMenu();
            }
        }

        // Turn based combat
        if(battleCounter == 2) {
            gamePanel.player.battleMenuOn = false;
            if(gamePanel.player.isTakingTurn) {
                turnTimeCounter++;

                // Because of the way the Color Counters are set up, the background should continue flowing normally
                if (turnTimeCounter <= 89) {
                    drawBattleBase(interactingMonster, gamePanel.player.playerName + " takes a swing --", 1);
                }
                // Damage calc
                if (turnTimeCounter == 90) {
                    drawBattleBase(interactingMonster, gamePanel.player.playerName + " takes a swing --", 1);

                    currentDamageDealt = gamePanel.getBattleM().getDamageCalc(gamePanel.player, gamePanel.monsters[0][interactingMonster]);
                    gamePanel.playSoundEffect(5);
                    gamePanel.monsters[0][interactingMonster].setCurrentLife(gamePanel.monsters[0][interactingMonster].getCurrentLife() - currentDamageDealt);
                }
                if (turnTimeCounter > 90 && turnTimeCounter <= 180) {
                    drawBattleBase(interactingMonster, "BOOM! " + currentDamageDealt + " damage!", 1);

                    // Visual effects, SUPER bare bones right now
                    if (turnTimeCounter <= 93) {
                        idleAnimationCounter = 0; // This holds a still image for the first part of this loop, but realistically we'll have a unique "Hurting" sprite
                        screenFlash();
                    }
                    if (turnTimeCounter > 97 && turnTimeCounter <= 100) {
                        screenFlash();
                    }
                    if (turnTimeCounter > 103 && turnTimeCounter <= 106) {
                        screenFlash();
                    }
                    if (turnTimeCounter == 113 || turnTimeCounter == 120) {
                        idleAnimationCounter = 0;
                    }
                }

                if (turnTimeCounter > 180) {
                    drawBattleBase(interactingMonster, "BOOM! " + currentDamageDealt + " damage!", 1);

                    // End turn, reset counters
                    gamePanel.player.isTakingTurn = false;
                    turnTimeCounter = 0;
                }
            }

            // Monster turn code goes here, have to shuffle some booleans later

            // THIS needs to check that the Monster turn has ended also, once that code is in
            if(!gamePanel.player.isTakingTurn) {
                // Monster death check BEFORE this stuff

                battleCounter = 3;
                genericCounter = 0;
                battleOver = true;
            }
        }

        // Exiting Battle State
        if(battleCounter == 3) {
            if(battleOver) {
                genericCounter++;

                if (genericCounter <= 49) {
                    double doubleGCounter = genericCounter;
                    float alphaValue = (float) (100 - (doubleGCounter * 2)) / 100;

                    currentDialogue = gamePanel.monsters[0][interactingMonster].getName() + " was defeated!";

                    // Standard battle draw
                    drawBattleBackground();
                    drawBattleTopFrame(currentDialogue);
                    drawBattleBottomFrame(1);

                    // Sprite gets tricky
                    BufferedImage deathSprite = utilityTool.scaleImage(gamePanel.monsters[0][interactingMonster].getIdleImage1(),
                            (gamePanel.getTileSize() * 3), (gamePanel.getTileSize() * 3));

                    graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
                    graphics2D.drawImage(deathSprite, gamePanel.getTileSize() * 8 + 36, gamePanel.getTileSize() * 5 - 24, null);
                }
                else if (genericCounter <= 98) {
                    drawBattleBackground();
                    drawBattleTopFrame(currentDialogue);
                    drawBattleBottomFrame(1);
                }
                else {
                    currentDialogue = "You got some free stuff. Wow!";
                    drawBattleBackground();
                    drawBattleTopFrame(currentDialogue);
                    drawBattleBottomFrame(1);

                    // Adds exp once, dodges null pointer
                    if(gamePanel.monsters[0][interactingMonster] != null) {
                        gamePanel.player.setExp(gamePanel.player.getExp() + gamePanel.monsters[0][interactingMonster].getExp());
                        gamePanel.monsters[0][interactingMonster] = null;
                    }
                }
            }
            if(!battleOver) {
                currentDialogue = "You got away safely!";
                drawBattleBase(interactingMonster, currentDialogue, 1);
            }
        }

    }

    public void drawBattleBase(int monsterIndex, String topFrameText, int playerCount) {
        drawBattleBackground();
        drawBattleEnemies(monsterIndex);
        drawBattleTopFrame(topFrameText);
        drawBattleBottomFrame(playerCount);
    }
    public void drawBattleBackground() {
        // Y'all know me, I hop in that bitch and have a HEART ATTACK
        if(redCounter % 2 == 0) {
            bgRedValue = redCounter / 2;
        }
        if(greenCounter % 2 == 0) {
            bgGreenValue = greenCounter / 2;
        }
        if(blueCounter % 2 == 0) {
            bgBlueValue = blueCounter / 2;
        }

        graphics2D.setColor(new Color(bgRedValue, bgGreenValue, bgBlueValue));
        graphics2D.fillRect(0, 0, gamePanel.getWidth(), gamePanel.getTileSize() * 3); // Background behind top box

        graphics2D.setColor(new Color(bgRedValue + 15, bgGreenValue + 15, bgBlueValue + 15)); // Six rectangles, one tile each
        graphics2D.fillRect(0, gamePanel.getTileSize() * 3, gamePanel.getWidth(), gamePanel.getTileSize() + 24);

        graphics2D.setColor(new Color(bgRedValue + 30, bgGreenValue + 30, bgBlueValue + 30));
        graphics2D.fillRect(0, (gamePanel.getTileSize() * 4) + 24, gamePanel.getWidth(), gamePanel.getTileSize() + 24);

        graphics2D.setColor(new Color(bgRedValue + 45, bgGreenValue + 45, bgBlueValue + 45));
        graphics2D.fillRect(0, gamePanel.getTileSize() * 6, gamePanel.getWidth(), gamePanel.getTileSize() + 24);

        graphics2D.setColor(new Color(bgRedValue + 60, bgGreenValue + 60, bgBlueValue + 60));
        graphics2D.fillRect(0, (gamePanel.getTileSize() * 7) + 24, gamePanel.getWidth(), gamePanel.getTileSize() + 24);

        graphics2D.setColor(new Color(bgRedValue + 75, bgGreenValue + 75, bgBlueValue + 75));
        graphics2D.fillRect(0, gamePanel.getTileSize() * 9, gamePanel.getWidth(), gamePanel.getTileSize() + 24);

        graphics2D.setColor(new Color(bgRedValue + 90, bgGreenValue + 90, bgBlueValue + 90)); // Background behind bottom box
        graphics2D.fillRect(0, (gamePanel.getTileSize() * 10) + 24, gamePanel.getWidth(), gamePanel.getTileSize() + 24);}
    public void drawBattleEnemies(int monsterId) {
        // Scaling image in battle for now, probably not ideal for all time
        BufferedImage fightSprite;
        idleAnimationCounter++;

        if(idleAnimationCounter <= 20) {
            fightSprite = gamePanel.monsters[0][monsterId].getIdleImage1();
            graphics2D.drawImage(fightSprite, gamePanel.getTileSize() * 8 + 36, gamePanel.getTileSize() * 5 - 24, null);
        }
        if(idleAnimationCounter > 20 && idleAnimationCounter < 40) {
            fightSprite = gamePanel.monsters[0][monsterId].getIdleImage2();
            graphics2D.drawImage(fightSprite, gamePanel.getTileSize() * 8 + 36, gamePanel.getTileSize() * 5 - 24, null);
        }
        if(idleAnimationCounter == 40) {
            // No real way around drawing this for a single frame while we reset the counter
            fightSprite = gamePanel.monsters[0][monsterId].getIdleImage2();
            graphics2D.drawImage(fightSprite, gamePanel.getTileSize() * 8 + 36, gamePanel.getTileSize() * 5 -24, null);
            idleAnimationCounter = 0;
        }
    }
    public void drawBattleTopFrame(String displayText) {
        drawSubWindow(0, 0, gamePanel.getWidth(), gamePanel.getTileSize() * 3);

        graphics2D.setColor(Color.white);
        graphics2D.setFont(battleMaru);
        graphics2D.drawString(displayText, 32, gamePanel.getTileSize() + 44);
    }

    // Can't forget to format the counters eventually
    public void drawBattleBottomFrame(int playerCount) {
        if(playerCount == 1) {
            battleCharacterText = "";

            drawSubWindow(gamePanel.getTileSize() * 8 + 12, gamePanel.getTileSize() * 8, gamePanel.getTileSize() * 4, (gamePanel.getTileSize() * 4) - 24);

            graphics2D.setColor(Color.white);

            graphics2D.setFont(new Font("Monospaced", Font.BOLD, 32));
            graphics2D.drawString(gamePanel.player.playerName, gamePanel.getTileSize() * 9 - 6, gamePanel.getTileSize() * 9 - 12);

            graphics2D.fillRect(gamePanel.getTileSize() * 8 + 18, gamePanel.getTileSize() * 9, gamePanel.getTileSize() * 4 - 12, 4);

            graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 27));
            graphics2D.drawString("Life: ", gamePanel.getTileSize() * 9 - 20, gamePanel.getTileSize() * 10 - 6);
            graphics2D.drawString("Mana: ", gamePanel.getTileSize() * 9 - 20, gamePanel.getTileSize() * 11 - 6);

            battleCharacterText = "0" + gamePanel.player.getCurrentLife();
            graphics2D.drawString(battleCharacterText, gamePanel.getTileSize() * 11 + 12, gamePanel.getTileSize() * 10 - 6);

            battleCharacterText = "0" + gamePanel.player.getCurrentMana();
            graphics2D.drawString(battleCharacterText, gamePanel.getTileSize() * 11 + 12, gamePanel.getTileSize() * 11 - 6);
        }

        /* These are for adjusting the displayed screens based on party size, just structural
           placeholders for now. I think it makes the most sense to feed an ARRAY of some kind into
           the Battle State itself, or the Battle Manager we eventually code. This works fine
           for 1 on 1s which is all we have right now, but iterating through and array of Players
           to get different party member stats HAS to happen at some point. No sense not making the monster
           methods follow suit. */
        if(playerCount == 2) { }
        if(playerCount == 3) { }
        if(playerCount == 4) { }
    }

    public void drawBattleMenu() {
        final int INITIAL_TEXT_Y = gamePanel.getTileSize() * 9 - 12;
        int textY = INITIAL_TEXT_Y;

        // Initial sub window
        drawSubWindow(gamePanel.getTileSize() * 12 + 12, gamePanel.getTileSize() * 8, gamePanel.getTileSize() * 4 - 12, (gamePanel.getTileSize() * 3) - 24);

        // Slots
        final int ROW_X = gamePanel.getTileSize() * 12 + 24;
        final int rowYstart = gamePanel.getTileSize() * 8 + 12; // Y of first row
        int rowY = rowYstart;
        int rowSize = gamePanel.getTileSize() - 12; // Sets space between items

        // Looks like it's misaligned, but only because drawString and fillRoundRect use different coordinate systems
        int cursorY = rowYstart + (32 * battleRow);
        int cursorWidth = gamePanel.getTileSize() * 4 - 12;
        int cursorHeight = gamePanel.getTileSize() - 18;

        // DRAW CURSOR
        graphics2D.setColor(Color.gray);
        graphics2D.fillRoundRect(ROW_X,cursorY,cursorWidth - 24,cursorHeight, 25, 25);

        graphics2D.setColor(Color.white);
        for(int i = 0; i < gamePanel.player.BATTLE_MENU_OPTIONS.length; i++) {
            graphics2D.drawString("- " + gamePanel.player.BATTLE_MENU_OPTIONS[i], gamePanel.getTileSize() * 13 - 32, textY );
            textY += 32;
        }
    }

    public void updateColorCounter(int currentColorCount) {
        if(currentColorCount == 0) {
            if (redCounter < 330) { // 165 * 2 (Maximum value floor times play rate inverted)
                redCounter++;
            } else {
                colorCounter++;
            }
        } // Initial fade in
        if(currentColorCount == 1) {
            if (blueCounter < 330) {
                blueCounter++;
                redCounter--;
            } else {
                colorCounter++;
            }
        } // Main loop begins
        if(currentColorCount == 2) {
            if (greenCounter < 330) {
                greenCounter++;
                blueCounter--;
            } else {
                colorCounter++;
            }
        }
        if(currentColorCount == 3) {
            if (redCounter < 330) {
                redCounter++;
                greenCounter--;
            } else {
                colorCounter = 1; // Reset to loop start
            }
        }
    }

    // White flash, mostly made to save space and help as a visual cue for event checks
    public void screenFlash() {
        graphics2D.setColor(Color.white);
        graphics2D.fillRect(0,0, gamePanel.getWidth(), gamePanel.getHeight());
    }
}