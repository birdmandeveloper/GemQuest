package tech.GemQuet.asset.object.equipment;

import tech.GemQuet.GamePanel;
import tech.GemQuet.util.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class OBJ_Shield_Blue extends Shield {

    public OBJ_Shield_Blue(GamePanel gamePanel) {
        super(gamePanel);

        setName("Blue Shield");
        setDescription("[" + getName() + "]\nPainted blue");
        setDefenseValue(2);
        setPrice(250);

        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/images/objects/shield_blue.png")));
            setImage1(UtilityTool.scaleImage(image, gamePanel.getTileSize(), gamePanel.getTileSize()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
