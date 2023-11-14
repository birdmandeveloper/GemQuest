package tech.GemQuest.asset.entity.npc;

import tech.GemQuest.GamePanel;
import tech.GemQuest.asset.entity.Entity;
//DEFINES NPC FOR THE OTHER NPC CLASSES TO INHERIT FROM
public class NPC extends Entity {
    //VARIABLES
    private String[] dialogues = new String[20];
    private int dialogueIndex;

    public NPC(GamePanel gamePanel) {
        super(gamePanel);
    }

    @Override
    public void setupAI() {
        super.setupAI();
    }

    @Override
    public void speak() {
        if (dialogues[dialogueIndex] == null) {
            setDialogueIndex(0);
        }

        getGamePanel().getUi().setCurrentDialogue(getDialogues()[dialogueIndex]);
        dialogueIndex++;

        switch (getGamePanel().getPlayer().getDirection()) {
            case "up" -> setDirection("down");
            case "down" -> setDirection("up");
            case "left" -> setDirection("right");
            case "right" -> setDirection("left");
        }
    }

    //GETTER AND SETTER
    public String[] getDialogues() {
        return dialogues;
    }

    public Entity setDialogues(String[] dialogues) {
        this.dialogues = dialogues;
        return this;
    }

    public int getDialogueIndex() {
        return dialogueIndex;
    }

    public Entity setDialogueIndex(int dialogueIndex) {
        this.dialogueIndex = dialogueIndex;
        return this;
    }

    @Override
    public void resetDefaultSpeed() {
        // Just imagine they get hit by another NPC or enemy
    }
}
