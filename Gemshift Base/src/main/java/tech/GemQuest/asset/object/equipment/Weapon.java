package tech.GemQuest.asset.object.equipment;

import tech.GemQuest.GamePanel;
import tech.GemQuest.asset.object.Object;

import java.awt.*;
//OBJECT. THE OTHER "WEAPONS" INHERIT FROM THIS
public class Weapon extends Object {

    //VARIABLES
    private Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    private int attackValue;

    public Weapon(GamePanel gamePanel) {
        super(gamePanel);
    }

    //GETTERS AND SETTERS
    public Rectangle getAttackArea() {
        return attackArea;
    }

    public void setAttackArea(Rectangle attackArea) {
        this.attackArea = attackArea;
    }

    public int getAttackValue() {
        return attackValue;
    }

    public void setAttackValue(int attackValue) {
        this.attackValue = attackValue;
    }
}
