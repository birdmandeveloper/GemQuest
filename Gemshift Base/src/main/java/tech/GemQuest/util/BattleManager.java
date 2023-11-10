package tech.GemQuest.util;

import tech.GemQuest.GamePanel;
import tech.GemQuest.asset.Asset;

// Exists specifically to run Battle State operations
public class BattleManager {
    GamePanel gp;

    public BattleManager(GamePanel gp) {
        this.gp = gp;
    }

    // VERY bare bones right now. Should work though
    public int getDamageCalc(Asset attacker, Asset defender) {
        int damageDealt = 0;

        if(attacker != null && defender != null) {
            // Now some bullshit happens
            damageDealt = attacker.getAttackPower() - defender.getDefensePower();
        }

        return damageDealt;
    }
}
