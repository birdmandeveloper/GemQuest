package tech.GemQuest.asset.entity.npc;

import tech.GemQuest.GamePanel;
//OLD MAN
public class NPC_OldMan extends NPC {

    public NPC_OldMan(GamePanel gamePanel) {
        super(gamePanel);

        setName("Old Man");
        setSpeed(1);

        getAnimationImages();
        setDialogue();
    }

    // STOCK NPC ASSETS FOR NOW
    public void getAnimationImages() {
        setUp1(setup("/images/npc/oldman_up_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setUp2(setup("/images/npc/oldman_up_2", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setDown1(setup("/images/npc/oldman_down_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setDown2(setup("/images/npc/oldman_down_2", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setLeft1(setup("/images/npc/oldman_left_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setLeft2(setup("/images/npc/oldman_left_2", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setRight1(setup("/images/npc/oldman_right_1", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
        setRight2(setup("/images/npc/oldman_right_2", getGamePanel().getTileSize(), getGamePanel().getTileSize()));
    }

    @Override
    public void setupAI() {
        super.setupAI();
    }

    public void setDialogue() {
        getDialogues()[0] = "Back to vibing.";
        getDialogues()[1] = "So you've come to this island to find \nthe treasure?";
        getDialogues()[2] = "I used to be a great wizard, but now... \nI'm a bit too old for adventuring.";
        getDialogues()[3] = "Well, good luck to you lad.";
    }

    @Override
    public void speak() {
        super.speak();
    }
}
