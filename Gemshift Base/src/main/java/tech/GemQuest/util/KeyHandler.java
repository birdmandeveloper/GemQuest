package tech.GemQuest.util;

import tech.GemQuest.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    private final GamePanel gamePanel;

    // Key status booleans
    private boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, spacePressed, projectileKeyPressed, shiftPressed;

    // DEBUG
    private boolean showDebugText = false;

    // Constructor
    public KeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    // Checks what key is pressed based on game state
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (gamePanel.getGameState() == gamePanel.getTitleState()) {
            checkTitleStateKeyPressed(code);
        } else if (gamePanel.getGameState() == gamePanel.getPlayState()) {
            checkPlayStateKeyPressed(code);

        } else if (gamePanel.getGameState() == gamePanel.getPauseState()) {
            checkPauseStateKeyPressed(code);

        } else if (gamePanel.getGameState() == gamePanel.getDialogueState()) {
            checkDialogueStateKeyPressed(code);

        } else if (gamePanel.getGameState() == gamePanel.getCharacterState()) {
            checkCharacterStateKeyPressed(code);

        } else if (gamePanel.getGameState() == gamePanel.getOptionState()) {
            checkOptionStateKeyPressed(code);

        } else if (gamePanel.getGameState() == gamePanel.getGameOverState()) {
            checkGameOverStateKeyPressed(code);
        } else if (gamePanel.getGameState() == gamePanel.getTradeState()) {
            checkTradeStateKeyPressed(code);
        } else if (gamePanel.getGameState() == gamePanel.getBattleState()) {
            checkBattleStateKeyPressed(code);
        }
    }

    private void checkTitleStateKeyPressed(int code) {
        if (gamePanel.getUi().getTitleScreenState() == 0) {
            checkMainTitleScreenKeyPressed(code);

        } else if (gamePanel.getUi().getTitleScreenState() == 1) {
            checkCharacterSelectionScreenKeyPressed(code);
        }
    }
    private void checkMainTitleScreenKeyPressed(int code) {
        if (code == KeyEvent.VK_W) {
            gamePanel.getUi().setCommandNumber(gamePanel.getUi().getCommandNumber() - 1);
            gamePanel.playSoundEffect(9);
            if (gamePanel.getUi().getCommandNumber() < 0) {
                gamePanel.getUi().setCommandNumber(2);
            }
        }

        if (code == KeyEvent.VK_S) {
            gamePanel.getUi().setCommandNumber(gamePanel.getUi().getCommandNumber() + 1);
            gamePanel.playSoundEffect(9);
            if (gamePanel.getUi().getCommandNumber() > 2) {
                gamePanel.getUi().setCommandNumber(0);
            }
        }

        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
    }
    private void checkCharacterSelectionScreenKeyPressed(int code) {
        if (code == KeyEvent.VK_W) {
            gamePanel.getUi().setCommandNumber(gamePanel.getUi().getCommandNumber() - 1);
            gamePanel.playSoundEffect(9);
            if (gamePanel.getUi().getCommandNumber() < 0) {
                gamePanel.getUi().setCommandNumber(3);
            }
        }

        if (code == KeyEvent.VK_S) {
            gamePanel.getUi().setCommandNumber(gamePanel.getUi().getCommandNumber() + 1);
            gamePanel.playSoundEffect(9);
            if (gamePanel.getUi().getCommandNumber() > 3) {
                gamePanel.getUi().setCommandNumber(0);
            }
        }

        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
    }

    // To be implemented later

//    private void characterScreenEnterPressed() {
//        if (gamePanel.getUi().getCommandNumber() == 0) {
//            System.out.println("Fighter selected!");
//
//        }
//
//        if (gamePanel.getUi().getCommandNumber() == 1) {
//            System.out.println("Rogue selected!");
//            gamePanel.setGameState(gamePanel.getPlayState());
//            gamePanel.playMusic(0);
//        }
//
//        if (gamePanel.getUi().getCommandNumber() == 2) {
//            System.out.println("Sorcerer selected!");
//            gamePanel.setGameState(gamePanel.getPlayState());
//            gamePanel.playMusic(0);
//        }
//
//        if (gamePanel.getUi().getCommandNumber() == 3) {
//            gamePanel.getUi().setTitleScreenState(0);
//        }
//    }
    private void checkPlayStateKeyPressed(int code) {
        checkMovementKeys(code);
        checkGameStateKeys(code);
        checkInteractionKeys(code);
        checkAdminKeys(code);
    }

    // Character movement keys
    private void checkMovementKeys(int code) {
        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }

        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }

        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }

        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_SHIFT) {
            gamePanel.player.setSpeed(6);
        }
    }

    // Player commands for menu management
    private void checkGameStateKeys(int code) {
        if (code == KeyEvent.VK_P) {
            gamePanel.setGameState(gamePanel.getPauseState());
        }
        if (code == KeyEvent.VK_C) {
            gamePanel.setGameState(gamePanel.getCharacterState());
        }
        if (code == KeyEvent.VK_ESCAPE) {
            gamePanel.setGameState(gamePanel.getOptionState());
        }
    }

    // PLayer action checks (fireball, talk, et al.)
    private void checkInteractionKeys(int code) {
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        // Just used for attacking at the moment
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = true;
            gamePanel.getPlayer().setSpriteCounter(0); // Resets animation counter
        }
        if (code == KeyEvent.VK_F) {
            projectileKeyPressed = true;
        }
    }

    // DEBUG
    private void checkAdminKeys(int code) {
        if (code == KeyEvent.VK_T) {
            showDebugText = !showDebugText;
        }
        if (code == KeyEvent.VK_R) {
            switch (gamePanel.getCurrentMap()) {
                case 0 -> gamePanel.getTileManager().loadMap("/maps/worldV3.txt", 0);
                case 1 -> gamePanel.getTileManager().loadMap("/maps/eh.txt", 1);
            }
        }
    }

    // The only possible key press in Pause State
    private void checkPauseStateKeyPressed(int code) {
        if (code == KeyEvent.VK_P) {
            gamePanel.setGameState(gamePanel.getPlayState());
        }
    }

    private void checkBattleStateKeyPressed(int code) {
        // DEBUG, press P to escape Battle State at any time
        if (code == KeyEvent.VK_P) {
            gamePanel.setGameState(gamePanel.getPlayState());
        }

        // Typical exit route via ENTER
        if (gamePanel.getUi().battleCounter == 3 && gamePanel.getUi().genericCounter > 120) {
            if (code == KeyEvent.VK_ENTER) {
                gamePanel.setGameState(gamePanel.getPlayState());
            }
        }

        // ACTUAL MENU CONTROLS
        if (gamePanel.player.battleMenuOn) {
            // Item menu navigation
            if (gamePanel.player.battleItemMenu) {
                // Up
                if (code == KeyEvent.VK_W) {
                    if (gamePanel.getUi().battleRow != 0) {
                        gamePanel.getUi().battleRow--;
                    } else {
                        // THIS is where it needs to change the visibility of items
                        int outsideCounter = 0;

                        // This loop finds the first "true" and switches it to false, and then finds the last "false" and switches it to true
                        for (int i = 0; i < gamePanel.player.getInventory().size(); i++) {
                            if (outsideCounter == 0 && i > 0) {
                                // BECAUSE we're only showing three items right now, the position of the last false is
                                // always EXACTLY two more than the position of the first true
                                if (gamePanel.player.getInventory().get(i).getIsBattleMenuVisible()) {
                                    gamePanel.player.getInventory().get(i - 1).setIsBattleMenuVisible(true);
                                    gamePanel.player.getInventory().get(i + 2).setIsBattleMenuVisible(false);

                                    gamePanel.getUi().visibleBattleInventory.clear();

                                    gamePanel.getUi().visibleBattleInventory.add(gamePanel.player.getInventory().get(i - 1));
                                    gamePanel.getUi().visibleBattleInventory.add(gamePanel.player.getInventory().get(i));
                                    gamePanel.getUi().visibleBattleInventory.add(gamePanel.player.getInventory().get(i + 1));

                                    outsideCounter++;
                                }
                            }

                            // Resets counter
                            if (i == gamePanel.player.getInventory().size()) {
                                outsideCounter = 0;
                            }
                        }
                    }
                }

                // Down
                if (code == KeyEvent.VK_S) {
                    if (gamePanel.getUi().battleRow != 2) {
                        gamePanel.getUi().battleRow++;
                    } else {
                        int outsideCounter = 0;

                        for (int i = 0; i < gamePanel.player.getInventory().size(); i++) {
                            if (outsideCounter == 0 && i < gamePanel.player.getInventory().size() - 3) {
                                if (gamePanel.player.getInventory().get(i).getIsBattleMenuVisible()) {
                                    gamePanel.player.getInventory().get(i).setIsBattleMenuVisible(false);
                                    gamePanel.getUi().visibleBattleInventory.remove(0);

                                    gamePanel.player.getInventory().get(i + 3).setIsBattleMenuVisible(true);
                                    gamePanel.getUi().visibleBattleInventory.add(gamePanel.player.getInventory().get(i + 3));
                                    outsideCounter++;
                                }
                            }

                            if (i == gamePanel.player.getInventory().size()) {
                                outsideCounter = 0;
                            }
                        }
                    }
                }

                // Left
                if (code == KeyEvent.VK_A) {
                    gamePanel.getUi().battleRow = 1;
                    gamePanel.player.battleItemMenu = false;
                    gamePanel.getUi().setCurrentDialogue(gamePanel.monsters[0][gamePanel.getUi().interactingMonster].getIdleMessage());
                }

                // Selecting
                if (code == KeyEvent.VK_ENTER) {
                    if (gamePanel.getUi().battleRow == 0) {
                        if (!gamePanel.getUi().visibleBattleInventory.get(0).getIsBattleItem()) {
                            gamePanel.getUi().setCurrentDialogue("You can't use the " + gamePanel.getUi().visibleBattleInventory.get(0).getName() + " now.");
                        }
                        if (gamePanel.getUi().visibleBattleInventory.get(0).getIsBattleItem()) {
                            // isItemUsable can only check for 1 (Healing item) right now
                            if (!gamePanel.player.checkItemUsability(1)) {
                                gamePanel.getUi().setCurrentDialogue("You already have full health!");
                            }
                            if (gamePanel.player.checkItemUsability(1)) {
                                // Because we always add the first three items, it shouldn't matter if we stop drawing the menu and go straight to phase 2
                                gamePanel.getUi().battleRow = 1;
                                gamePanel.player.battleItemMenu = false;

                                gamePanel.player.isTakingTurn = true;
                                gamePanel.getUi().battleCounter = 2;

                                gamePanel.player.usedItem = true;
                                gamePanel.player.inBattleSelectItem(0);
                            }
                        }
                    }
                    if (gamePanel.getUi().battleRow == 1) {
                        if (!gamePanel.getUi().visibleBattleInventory.get(1).getIsBattleItem()) {
                            gamePanel.getUi().setCurrentDialogue("You can't use the " + gamePanel.getUi().visibleBattleInventory.get(1).getName() + " now.");
                        }
                        if (gamePanel.getUi().visibleBattleInventory.get(1).getIsBattleItem()) {
                            if (!gamePanel.player.checkItemUsability(1)) {
                                gamePanel.getUi().setCurrentDialogue("You already have full health!");
                            }
                            if (gamePanel.player.checkItemUsability(1)) {
                                gamePanel.player.battleItemMenu = false;

                                gamePanel.player.isTakingTurn = true;
                                gamePanel.getUi().battleCounter = 2;

                                gamePanel.player.usedItem = true;
                                gamePanel.player.inBattleSelectItem(1);
                            }
                        }
                    }
                    if (gamePanel.getUi().battleRow == 2) {
                        if (!gamePanel.getUi().visibleBattleInventory.get(2).getIsBattleItem()) {
                            gamePanel.getUi().setCurrentDialogue("You can't use the " + gamePanel.getUi().visibleBattleInventory.get(2).getName() + " now.");
                        }
                        if (gamePanel.getUi().visibleBattleInventory.get(2).getIsBattleItem()) {
                            if (!gamePanel.player.checkItemUsability(1)) {
                                gamePanel.getUi().setCurrentDialogue("You already have full health!");
                            }
                            if (gamePanel.player.checkItemUsability(1)) {
                                gamePanel.getUi().battleRow = 1;
                                gamePanel.player.battleItemMenu = false;

                                gamePanel.player.isTakingTurn = true;
                                gamePanel.getUi().battleCounter = 2;

                                gamePanel.player.usedItem = true;
                                gamePanel.player.inBattleSelectItem(2);
                            }
                        }
                    }
                }
            }

            // Main menu navigation
            if (!gamePanel.player.battleItemMenu) {
                if (code == KeyEvent.VK_W) {
                    if (gamePanel.getUi().battleRow != 0) {
                        gamePanel.getUi().battleRow--;
                    } else {
                        gamePanel.getUi().battleRow = 2;
                    }
                }
                if (code == KeyEvent.VK_S) {
                    if (gamePanel.getUi().battleRow != 2) {
                        gamePanel.getUi().battleRow++;
                    } else {
                        gamePanel.getUi().battleRow = 0;
                    }
                }

                if (code == KeyEvent.VK_ENTER) {
                    if (gamePanel.getUi().battleRow == 0) {
                        gamePanel.player.isTakingTurn = true;
                        gamePanel.getUi().battleCounter = 2;
                    }
                    if (gamePanel.getUi().battleRow == 1) {
                        gamePanel.player.battleItemMenu = true;
                        gamePanel.getUi().battleRow = 0;

                        for (int i = 0; i < gamePanel.player.getInventory().size(); i++) {
                            if (i < 3) {
                                gamePanel.player.getInventory().get(i).setIsBattleMenuVisible(true);
                                gamePanel.getUi().visibleBattleInventory.add(gamePanel.player.getInventory().get(i)); // Adds first three to visible list
                            } else {
                                gamePanel.player.getInventory().get(i).setIsBattleMenuVisible(false);
                            }
                        }

                        gamePanel.getUi().setCurrentDialogue(gamePanel.monsters[0][gamePanel.getUi().interactingMonster].getIdleMessage());
                    }
                    if (gamePanel.getUi().battleRow == 2) {
                        gamePanel.getUi().battleCounter = 3;
                    }
                }
            }
        }
    }

    // Character menu screens, dialogue game states, NPC interactions
    private void checkDialogueStateKeyPressed(int code) {
        if (code == KeyEvent.VK_ENTER) {
            gamePanel.setGameState(gamePanel.getPlayState());
        }
    }
    private void checkCharacterStateKeyPressed(int code) {
        if (code == KeyEvent.VK_C) {
            gamePanel.setGameState(gamePanel.getPlayState());
        }

        if (code == KeyEvent.VK_ENTER) {
            gamePanel.getPlayer().selectItem();
        }

        playerInventoryMovement(code);
    }
    private void playerInventoryMovement(int code) {
        if (code == KeyEvent.VK_W) {
            if (gamePanel.getUi().getPlayerSlotRow() != 0) {
                gamePanel.playSoundEffect(9);
                gamePanel.getUi().setPlayerSlotRow(gamePanel.getUi().getPlayerSlotRow() - 1);
            }
        }

        if (code == KeyEvent.VK_A) {
            if (gamePanel.getUi().getPlayerSlotCol() != 0) {
                gamePanel.playSoundEffect(9);
                gamePanel.getUi().setPlayerSlotCol(gamePanel.getUi().getPlayerSlotCol() - 1);
            }
        }

        if (code == KeyEvent.VK_S) {
            if (gamePanel.getUi().getPlayerSlotRow() != 3) {
                gamePanel.playSoundEffect(9);
                gamePanel.getUi().setPlayerSlotRow(gamePanel.getUi().getPlayerSlotRow() + 1);
            }
        }

        if (code == KeyEvent.VK_D) {
            if (gamePanel.getUi().getPlayerSlotCol() != 4) {
                gamePanel.playSoundEffect(9);
                gamePanel.getUi().setPlayerSlotCol(gamePanel.getUi().getPlayerSlotCol() + 1);
            }
        }
    }
    private void checkOptionStateKeyPressed(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            gamePanel.setGameState(gamePanel.getPlayState());
        }

        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

        int maxCommandNumber;

        switch (gamePanel.getUi().getSubState()) {
            case 0 -> maxCommandNumber = 5;
            case 3 -> maxCommandNumber = 1;
            default -> maxCommandNumber = 0;
        }

        if (code == KeyEvent.VK_W) {
            gamePanel.getUi().setCommandNumber(gamePanel.getUi().getCommandNumber() - 1);
            gamePanel.playSoundEffect(9);
            if (gamePanel.getUi().getCommandNumber() < 0) {
                gamePanel.getUi().setCommandNumber(maxCommandNumber);
            }
        }

        if (code == KeyEvent.VK_S) {
            gamePanel.getUi().setCommandNumber(gamePanel.getUi().getCommandNumber() + 1);
            gamePanel.playSoundEffect(9);
            if (gamePanel.getUi().getCommandNumber() > maxCommandNumber) {
                gamePanel.getUi().setCommandNumber(0);
            }
        }

        if (code == KeyEvent.VK_A) {
            if (gamePanel.getUi().getSubState() == 0) {
                if (gamePanel.getUi().getCommandNumber() == 1 && gamePanel.getMusic().getVolumeScale() > 0) {
                    gamePanel.getMusic().setVolumeScale(gamePanel.getMusic().getVolumeScale() - 1);
                    gamePanel.getMusic().checkVolume();
                    gamePanel.playSoundEffect(9);
                }

                if (gamePanel.getUi().getCommandNumber() == 2 && gamePanel.getSoundEffect().getVolumeScale() > 0) {
                    gamePanel.getSoundEffect().setVolumeScale(gamePanel.getSoundEffect().getVolumeScale() - 1);
                    gamePanel.playSoundEffect(9);
                }
            }
        }

        if (code == KeyEvent.VK_D) {
            if (gamePanel.getUi().getSubState() == 0) {
                if (gamePanel.getUi().getCommandNumber() == 1 && gamePanel.getMusic().getVolumeScale() < 5) {
                    gamePanel.getMusic().setVolumeScale(gamePanel.getMusic().getVolumeScale() + 1);
                    gamePanel.getMusic().checkVolume();
                    gamePanel.playSoundEffect(9);
                }

                if (gamePanel.getUi().getCommandNumber() == 2 && gamePanel.getSoundEffect().getVolumeScale() < 5) {
                    gamePanel.getSoundEffect().setVolumeScale(gamePanel.getSoundEffect().getVolumeScale() + 1);
                    gamePanel.playSoundEffect(9);
                }
            }
        }
    }
    private void checkGameOverStateKeyPressed(int code) {
        if (code == KeyEvent.VK_W) {
            gamePanel.getUi().setCommandNumber(gamePanel.getUi().getCommandNumber() - 1);
            gamePanel.playSoundEffect(9);
            if (gamePanel.getUi().getCommandNumber() < 0) {
                gamePanel.getUi().setCommandNumber(1);
            }
        }

        if (code == KeyEvent.VK_S) {
            gamePanel.getUi().setCommandNumber(gamePanel.getUi().getCommandNumber() + 1);
            gamePanel.playSoundEffect(9);
            if (gamePanel.getUi().getCommandNumber() > 1) {
                gamePanel.getUi().setCommandNumber(0);
            }
        }

        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
    }
    private void checkTradeStateKeyPressed(int code) {
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

        if (gamePanel.getUi().getSubState() == 0) {
            if (code == KeyEvent.VK_W) {
                gamePanel.getUi().setCommandNumber(gamePanel.getUi().getCommandNumber() - 1);
                if (gamePanel.getUi().getCommandNumber() < 0) {
                    gamePanel.getUi().setCommandNumber(2);
                }
                gamePanel.playSoundEffect(9);
            }

            if (code == KeyEvent.VK_S) {
                gamePanel.getUi().setCommandNumber(gamePanel.getUi().getCommandNumber() + 1);
                if (gamePanel.getUi().getCommandNumber() > 2) {
                    gamePanel.getUi().setCommandNumber(0);
                }
                gamePanel.playSoundEffect(9);
            }
        }

        if (gamePanel.getUi().getSubState() == 1) {
            npcInventoryMovement(code);
            if (code == KeyEvent.VK_ESCAPE) {
                gamePanel.getUi().setSubState(0);
            }
        }

        if (gamePanel.getUi().getSubState() == 2) {
            playerInventoryMovement(code);
            if (code == KeyEvent.VK_ESCAPE) {
                gamePanel.getUi().setSubState(0);
            }
        }
    }
    private void npcInventoryMovement(int code) {
        if (code == KeyEvent.VK_W) {
            if (gamePanel.getUi().getNpcSlotRow() != 0) {
                gamePanel.playSoundEffect(9);
                gamePanel.getUi().setNpcSlotRow(gamePanel.getUi().getNpcSlotRow() - 1);
            }
        }

        if (code == KeyEvent.VK_A) {
            if (gamePanel.getUi().getNpcSlotCol() != 0) {
                gamePanel.playSoundEffect(9);
                gamePanel.getUi().setNpcSlotCol(gamePanel.getUi().getNpcSlotCol() - 1);
            }
        }

        if (code == KeyEvent.VK_S) {
            if (gamePanel.getUi().getNpcSlotRow() != 3) {
                gamePanel.playSoundEffect(9);
                gamePanel.getUi().setNpcSlotRow(gamePanel.getUi().getNpcSlotRow() + 1);
            }
        }

        if (code == KeyEvent.VK_D) {
            if (gamePanel.getUi().getNpcSlotCol() != 4) {
                gamePanel.playSoundEffect(9);
                gamePanel.getUi().setNpcSlotCol(gamePanel.getUi().getNpcSlotCol() + 1);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }

        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }

        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }

        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }

        if (code == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }

        if (code == KeyEvent.VK_F) {
            projectileKeyPressed = false;
        }

        if(code == KeyEvent.VK_SHIFT) {
            gamePanel.player.setSpeed(3);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    // GETTERS AND SETTERS
    public boolean isUpPressed() {
        return upPressed;
    }
    public KeyHandler setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
        return this;
    }
    public boolean isDownPressed() {
        return downPressed;
    }
    public KeyHandler setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
        return this;
    }
    public boolean isLeftPressed() {
        return leftPressed;
    }
    public KeyHandler setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
        return this;
    }
    public boolean isRightPressed() {
        return rightPressed;
    }
    public KeyHandler setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
        return this;
    }
    public boolean isEnterPressed() {
        return enterPressed;
    }
    public KeyHandler setEnterPressed(boolean enterPressed) {
        this.enterPressed = enterPressed;
        return this;
    }
    public boolean isShowDebugText() {
        return showDebugText;
    }
    public KeyHandler setShowDebugText(boolean showDebugText) {
        this.showDebugText = showDebugText;
        return this;
    }
    public boolean isSpacePressed() {
        return spacePressed;
    }
    public KeyHandler setSpacePressed(boolean spacePressed) {
        this.spacePressed = spacePressed;
        return this;
    }
    public boolean isProjectileKeyPressed() {
        return projectileKeyPressed;
    }
    public KeyHandler setProjectileKeyPressed(boolean projectileKeyPressed) {
        this.projectileKeyPressed = projectileKeyPressed;
        return this;
    }
}
