package tech.GemQuest.asset.object.usable.inventory;

import tech.GemQuest.GamePanel;
import tech.GemQuest.asset.object.Object;
import tech.GemQuest.util.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class OBJ_Key extends Object {

    public OBJ_Key(GamePanel gamePanel) {
        super(gamePanel);

        setName("Key");
        setDescription("[" + getName() + "]\nIt opens a door");
        setPrice(100);

        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/images/objects/key.png")));
            setImage1(UtilityTool.scaleImage(image, gamePanel.getTileSize(), gamePanel.getTileSize()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
