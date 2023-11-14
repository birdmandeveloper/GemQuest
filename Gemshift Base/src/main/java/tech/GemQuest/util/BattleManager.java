package tech.GemQuest.util;

import tech.GemQuest.GamePanel;
import tech.GemQuest.asset.Asset;

// Exists specifically to run Battle State operations
public class BattleManager {
    GamePanel gp;
    public BattleManager(GamePanel gp) {
        this.gp = gp;
    }

    // Basic formula
    public int getDamageCalc(Asset attacker, Asset defender) {
        int damageDealt = 0;

        if(attacker != null && defender != null) {
            // This is the actual arithmetic
            damageDealt = attacker.getAttackPower() - defender.getDefensePower();
        }

        return damageDealt;
    }
}
