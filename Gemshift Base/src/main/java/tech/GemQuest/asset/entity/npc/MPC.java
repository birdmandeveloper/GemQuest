package tech.GemQuest.asset.entity.npc;

import tech.GemQuest.GamePanel;

import tech.GemQuest.asset.entity.Entity;


public class MPC extends Entity {




    public MPC (GamePanel gamePanel) {
        super(gamePanel);
    }
    private String[] dialogues = new String[20];
    private int dialogueIndex;





    @Override
    public void speak() {
        if (dialogues[dialogueIndex] == null) {
            setDialogueIndex(0);
        }

        getGamePanel().getUi().setCurrentDialogue(getDialogues()[dialogueIndex]);
        dialogueIndex++;


        }


    @Override
    public void resetDefaultSpeed() {

    }

    //GETTER AND SETTER
    public String[] getDialogues() {
        return dialogues;
    }

    public tech.GemQuest.asset.entity.Entity setDialogues(String[] dialogues) {
        this.dialogues = dialogues;
        return this;
    }

    public int getDialogueIndex() {
        return dialogueIndex;
    }

    public tech.GemQuest.asset.entity.Entity setDialogueIndex(int dialogueIndex) {
        this.dialogueIndex = dialogueIndex;
        return this;
    }


}


