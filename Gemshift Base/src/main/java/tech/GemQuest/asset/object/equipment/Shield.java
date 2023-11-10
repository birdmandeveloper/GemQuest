package tech.GemQuest.asset.object.equipment;

import tech.GemQuest.GamePanel;
import tech.GemQuest.asset.object.Object;

public class Shield extends Object {
    private int defenseValue;

    public Shield(GamePanel gamePanel) {
        super(gamePanel);
    }

    public int getDefenseValue() {
        return defenseValue;
    }

    public void setDefenseValue(int defenseValue) {
        this.defenseValue = defenseValue;
    }
}
