package tech.GemQuest.asset.object.equipment;

import tech.GemQuest.GamePanel;
import tech.GemQuest.util.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
////OBJECTS WE CAN "USE"//
public class OBJ_Shield_Wood extends Shield {

    public OBJ_Shield_Wood(GamePanel gamePanel) {
        super(gamePanel);

        setName("Wooden Shield");
        setDescription("[" + getName() + "]\nMade of wood");
        setDefenseValue(1);
        setPrice(35);

        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/images/objects/shield_wood.png")));
            setImage1(UtilityTool.scaleImage(image, gamePanel.getTileSize(), gamePanel.getTileSize()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
