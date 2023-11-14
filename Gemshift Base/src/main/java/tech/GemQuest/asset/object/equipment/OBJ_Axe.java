package tech.GemQuest.asset.object.equipment;

import tech.GemQuest.GamePanel;
import tech.GemQuest.util.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
//OBJECTS WE CAN "USE".
public class OBJ_Axe extends Weapon {

    public OBJ_Axe(GamePanel gamePanel) {
        super(gamePanel);

        setName("Woodcutter's Axe");
        setDescription("[" + getName() + "]\nA bit rusty, but still \nenough for cutting trees");
        setAttackValue(2);
        getAttackArea().width = 30;
        getAttackArea().height = 30;
        setPrice(75);

        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/images/objects/axe.png")));
            setImage1(UtilityTool.scaleImage(image, gamePanel.getTileSize(), gamePanel.getTileSize()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
