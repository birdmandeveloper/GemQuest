package tech.GemQuet.util;

import tech.GemQuet.GamePanel;
import tech.GemQuet.asset.Asset;
import tech.GemQuet.asset.AssetManager;
import tech.GemQuet.asset.entity.Entity;
import tech.GemQuet.asset.entity.player.Player;
import tech.GemQuet.asset.object.Object;
import tech.GemQuet.asset.tile.TileManager;
import tech.GemQuet.asset.tile.interactive.InteractiveTile;
import tech.GemQuet.event.EventHandler;
import tech.GemQuet.sound.SoundManager;
import tech.GemQuet.ui.UI;
import tech.GemQuet.util.CollisionChecker;
import tech.GemQuet.util.Config;
import tech.GemQuet.util.KeyHandler;

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
