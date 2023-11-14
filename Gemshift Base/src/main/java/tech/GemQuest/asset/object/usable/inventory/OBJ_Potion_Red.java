package tech.GemQuest.asset.object.usable.inventory;

import tech.GemQuest.GamePanel;
import tech.GemQuest.asset.object.Object;
import tech.GemQuest.util.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
//OBJECTS WE CAN "USE"//
public class OBJ_Potion_Red extends Object {

    private final GamePanel gamePanel;

    public OBJ_Potion_Red(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

        setName("Red Potion");
        setValue(5);
        setDescription("[" + getName() + "]\nRestores " + getValue() + " health");
        setPrice(25);
        setIsBattleItem(true);

        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/images/objects/potion_red.png")));
            setImage1(UtilityTool.scaleImage(image, gamePanel.getTileSize(), gamePanel.getTileSize()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void use() {
        gamePanel.setGameState(gamePanel.getDialogueState());
        gamePanel.getUi().setCurrentDialogue("You drink the " + getName() + "!\n" +
                "You have restored " + getValue() + " life!");

        gamePanel.getPlayer().setCurrentLife(gamePanel.getPlayer().getCurrentLife() + getValue());

        gamePanel.playSoundEffect(2);
    }
}
