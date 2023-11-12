package tech.GemQuest.asset;

import tech.GemQuest.GamePanel;
import tech.GemQuest.asset.entity.monster.MON_EggSlime;
import tech.GemQuest.asset.entity.monster.MON_GreenSlime;
import tech.GemQuest.asset.entity.monster.MON_RedSlime;
import tech.GemQuest.asset.entity.npc.NPC_Merchant;
import tech.GemQuest.asset.entity.npc.NPC_OldMan;
import tech.GemQuest.asset.object.equipment.OBJ_Axe;
import tech.GemQuest.asset.object.equipment.OBJ_Shield_Blue;
import tech.GemQuest.asset.object.usable.inventory.OBJ_Potion_Red;
import tech.GemQuest.asset.object.usable.pickuponly.OBJ_Coin_Bronze;
import tech.GemQuest.asset.object.usable.pickuponly.OBJ_Heart;
import tech.GemQuest.asset.object.usable.pickuponly.OBJ_ManaCrystal;
import tech.GemQuest.asset.tile.interactive.IT_DryTree;

import java.util.ArrayList;
import java.util.List;

public class AssetManager {

    private final GamePanel gamePanel;
    private final int tileSize;
    private int map = 0;

    // Spawn Records
    public boolean[] monSpawnZero = new boolean[]{true, true, true, true, true, true};

    // Respawn thoughts - a list HERE that syncs with the monster assignment, we flip this list boolean and check each
    // slot we add based on if THIS list is true or false. In other words, we can store the "respawnability" of a monster
    // even if we nullify the monster itself, and then just prevent it from being added again

    public AssetManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.tileSize = gamePanel.getTileSize();
    }

    public void setObjects() {

        // MAP 0
        map = 0;
        gamePanel.getObjects()[map][0] = new OBJ_Coin_Bronze(gamePanel);
        gamePanel.getObjects()[map][0].setWorldX(tileSize * 25);
        gamePanel.getObjects()[map][0].setWorldY(tileSize * 23);
        gamePanel.getObjects()[map][0].setIndex(0);

        gamePanel.getObjects()[map][1] = new OBJ_Coin_Bronze(gamePanel);
        gamePanel.getObjects()[map][1].setWorldX(tileSize * 21);
        gamePanel.getObjects()[map][1].setWorldY(tileSize * 19);
        gamePanel.getObjects()[map][1].setIndex(1);

        gamePanel.getObjects()[map][2] = new OBJ_Coin_Bronze(gamePanel);
        gamePanel.getObjects()[map][2].setWorldX(tileSize * 26);
        gamePanel.getObjects()[map][2].setWorldY(tileSize * 21);
        gamePanel.getObjects()[map][2].setIndex(2);

        gamePanel.getObjects()[map][3] = new OBJ_Axe(gamePanel);
        gamePanel.getObjects()[map][3].setWorldX(tileSize * 33);
        gamePanel.getObjects()[map][3].setWorldY(tileSize * 7);
        gamePanel.getObjects()[map][3].setIndex(3);

        gamePanel.getObjects()[map][4] = new OBJ_Shield_Blue(gamePanel);
        gamePanel.getObjects()[map][4].setWorldX(tileSize * 39);
        gamePanel.getObjects()[map][4].setWorldY(tileSize * 42);
        gamePanel.getObjects()[map][4].setIndex(4);

        gamePanel.getObjects()[map][5] = new OBJ_Potion_Red(gamePanel);
        gamePanel.getObjects()[map][5].setWorldX(tileSize * 22);
        gamePanel.getObjects()[map][5].setWorldY(tileSize * 27);
        gamePanel.getObjects()[map][5].setIndex(5);

        gamePanel.getObjects()[map][6] = new OBJ_Heart(gamePanel);
        gamePanel.getObjects()[map][6].setWorldX(tileSize * 22);
        gamePanel.getObjects()[map][6].setWorldY(tileSize * 29);
        gamePanel.getObjects()[map][6].setIndex(6);

        gamePanel.getObjects()[map][7] = new OBJ_ManaCrystal(gamePanel);
        gamePanel.getObjects()[map][7].setWorldX(tileSize * 22);
        gamePanel.getObjects()[map][7].setWorldY(tileSize * 31);
        gamePanel.getObjects()[map][7].setIndex(7);
    }

    public void setNPCs() {

        // MAP 0
        map = 0;
        gamePanel.getNpcs()[map][0] = new NPC_OldMan(gamePanel);
        gamePanel.getNpcs()[map][0].setWorldX(tileSize * 21);
        gamePanel.getNpcs()[map][0].setWorldY(tileSize * 21);
        gamePanel.getNpcs()[map][0].setIndex(0);

        // MAP 1
        map = 1;
        gamePanel.getNpcs()[map][0] = new NPC_Merchant(gamePanel);
        gamePanel.getNpcs()[map][0].setWorldX(tileSize * 12);
        gamePanel.getNpcs()[map][0].setWorldY(tileSize * 7);
        gamePanel.getNpcs()[map][0].setIndex(0);
    }

    public void setMonsters() {
        // MAP 0
        map = 0;

        // This has been altered slightly, it feeds the index position number into the Monster itself so we can use it later in battle
        if(monSpawnZero[0]) {
            gamePanel.getMonsters()[map][0] = new MON_GreenSlime(gamePanel, 0);
            gamePanel.getMonsters()[map][0].setWorldX(tileSize * 22);
            gamePanel.getMonsters()[map][0].setWorldY(tileSize * 19);
            gamePanel.getMonsters()[map][0].setIndex(0);
        }

        if(monSpawnZero[1]) {
            gamePanel.getMonsters()[map][1] = new MON_GreenSlime(gamePanel, 1);
            gamePanel.getMonsters()[map][1].setWorldX(tileSize * 23);
            gamePanel.getMonsters()[map][1].setWorldY(tileSize * 35);
            gamePanel.getMonsters()[map][1].setIndex(1);
        }
        if(monSpawnZero[2]) {
            gamePanel.getMonsters()[map][2] = new MON_EggSlime(gamePanel, 2);
            gamePanel.getMonsters()[map][2].setWorldX(tileSize * 24);
            gamePanel.getMonsters()[map][2].setWorldY(tileSize * 33);
            gamePanel.getMonsters()[map][2].setIndex(2);
        }
        if(monSpawnZero[3]) {
            gamePanel.getMonsters()[map][3] = new MON_RedSlime(gamePanel, 3);
            gamePanel.getMonsters()[map][3].setWorldX(tileSize * 21);
            gamePanel.getMonsters()[map][3].setWorldY(tileSize * 38);
            gamePanel.getMonsters()[map][3].setIndex(3);
        }
        if(monSpawnZero[4]) {
            gamePanel.getMonsters()[map][4] = new MON_GreenSlime(gamePanel, 4);
            gamePanel.getMonsters()[map][4].setWorldX(tileSize * 38);
            gamePanel.getMonsters()[map][4].setWorldY(tileSize * 42);
            gamePanel.getMonsters()[map][4].setIndex(4);
        }
        if(monSpawnZero[5]) {
            gamePanel.getMonsters()[map][5] = new MON_GreenSlime(gamePanel, 5);
            gamePanel.getMonsters()[map][5].setWorldX(tileSize * 35);
            gamePanel.getMonsters()[map][5].setWorldY(tileSize * 42);
            gamePanel.getMonsters()[map][5].setIndex(5);
        }
    }

    public void setInteractiveTiles() {

        // MAP 0
        map = 0;
        gamePanel.getInteractiveTiles()[map][0] = new IT_DryTree(gamePanel);
        gamePanel.getInteractiveTiles()[map][0].setWorldX(tileSize * 27);
        gamePanel.getInteractiveTiles()[map][0].setWorldY(tileSize * 12);
        gamePanel.getInteractiveTiles()[map][0].setIndex(0);

        gamePanel.getInteractiveTiles()[map][1] = new IT_DryTree(gamePanel);
        gamePanel.getInteractiveTiles()[map][1].setWorldX(tileSize * 28);
        gamePanel.getInteractiveTiles()[map][1].setWorldY(tileSize * 12);
        gamePanel.getInteractiveTiles()[map][1].setIndex(1);

        gamePanel.getInteractiveTiles()[map][2] = new IT_DryTree(gamePanel);
        gamePanel.getInteractiveTiles()[map][2].setWorldX(tileSize * 29);
        gamePanel.getInteractiveTiles()[map][2].setWorldY(tileSize * 12);
        gamePanel.getInteractiveTiles()[map][2].setIndex(2);

        gamePanel.getInteractiveTiles()[map][3] = new IT_DryTree(gamePanel);
        gamePanel.getInteractiveTiles()[map][3].setWorldX(tileSize * 30);
        gamePanel.getInteractiveTiles()[map][3].setWorldY(tileSize * 12);
        gamePanel.getInteractiveTiles()[map][3].setIndex(3);

        gamePanel.getInteractiveTiles()[map][4] = new IT_DryTree(gamePanel);
        gamePanel.getInteractiveTiles()[map][4].setWorldX(tileSize * 31);
        gamePanel.getInteractiveTiles()[map][4].setWorldY(tileSize * 12);
        gamePanel.getInteractiveTiles()[map][4].setIndex(4);

        gamePanel.getInteractiveTiles()[map][5] = new IT_DryTree(gamePanel);
        gamePanel.getInteractiveTiles()[map][5].setWorldX(tileSize * 32);
        gamePanel.getInteractiveTiles()[map][5].setWorldY(tileSize * 12);
        gamePanel.getInteractiveTiles()[map][5].setIndex(5);

        gamePanel.getInteractiveTiles()[map][6] = new IT_DryTree(gamePanel);
        gamePanel.getInteractiveTiles()[map][6].setWorldX(tileSize * 33);
        gamePanel.getInteractiveTiles()[map][6].setWorldY(tileSize * 12);
        gamePanel.getInteractiveTiles()[map][6].setIndex(6);

        gamePanel.getInteractiveTiles()[map][7] = new IT_DryTree(gamePanel);
        gamePanel.getInteractiveTiles()[map][7].setWorldX(tileSize * 31);
        gamePanel.getInteractiveTiles()[map][7].setWorldY(tileSize * 21);
        gamePanel.getInteractiveTiles()[map][7].setIndex(7);

        gamePanel.getInteractiveTiles()[map][8] = new IT_DryTree(gamePanel);
        gamePanel.getInteractiveTiles()[map][8].setWorldX(tileSize * 18);
        gamePanel.getInteractiveTiles()[map][8].setWorldY(tileSize * 40);
        gamePanel.getInteractiveTiles()[map][8].setIndex(8);

        gamePanel.getInteractiveTiles()[map][9] = new IT_DryTree(gamePanel);
        gamePanel.getInteractiveTiles()[map][9].setWorldX(tileSize * 17);
        gamePanel.getInteractiveTiles()[map][9].setWorldY(tileSize * 40);
        gamePanel.getInteractiveTiles()[map][9].setIndex(9);

        gamePanel.getInteractiveTiles()[map][10] = new IT_DryTree(gamePanel);
        gamePanel.getInteractiveTiles()[map][10].setWorldX(tileSize * 16);
        gamePanel.getInteractiveTiles()[map][10].setWorldY(tileSize * 40);
        gamePanel.getInteractiveTiles()[map][10].setIndex(10);

        gamePanel.getInteractiveTiles()[map][11] = new IT_DryTree(gamePanel);
        gamePanel.getInteractiveTiles()[map][11].setWorldX(tileSize * 15);
        gamePanel.getInteractiveTiles()[map][11].setWorldY(tileSize * 40);
        gamePanel.getInteractiveTiles()[map][11].setIndex(11);

        gamePanel.getInteractiveTiles()[map][12] = new IT_DryTree(gamePanel);
        gamePanel.getInteractiveTiles()[map][12].setWorldX(tileSize * 14);
        gamePanel.getInteractiveTiles()[map][12].setWorldY(tileSize * 40);
        gamePanel.getInteractiveTiles()[map][12].setIndex(12);

        gamePanel.getInteractiveTiles()[map][13] = new IT_DryTree(gamePanel);
        gamePanel.getInteractiveTiles()[map][13].setWorldX(tileSize * 13);
        gamePanel.getInteractiveTiles()[map][13].setWorldY(tileSize * 40);
        gamePanel.getInteractiveTiles()[map][13].setIndex(13);

        gamePanel.getInteractiveTiles()[map][14] = new IT_DryTree(gamePanel);
        gamePanel.getInteractiveTiles()[map][14].setWorldX(tileSize * 13);
        gamePanel.getInteractiveTiles()[map][14].setWorldY(tileSize * 41);
        gamePanel.getInteractiveTiles()[map][14].setIndex(14);

        gamePanel.getInteractiveTiles()[map][15] = new IT_DryTree(gamePanel);
        gamePanel.getInteractiveTiles()[map][15].setWorldX(tileSize * 12);
        gamePanel.getInteractiveTiles()[map][15].setWorldY(tileSize * 41);
        gamePanel.getInteractiveTiles()[map][15].setIndex(15);

        gamePanel.getInteractiveTiles()[map][16] = new IT_DryTree(gamePanel);
        gamePanel.getInteractiveTiles()[map][16].setWorldX(tileSize * 11);
        gamePanel.getInteractiveTiles()[map][16].setWorldY(tileSize * 41);
        gamePanel.getInteractiveTiles()[map][16].setIndex(16);

        gamePanel.getInteractiveTiles()[map][17] = new IT_DryTree(gamePanel);
        gamePanel.getInteractiveTiles()[map][17].setWorldX(tileSize * 10);
        gamePanel.getInteractiveTiles()[map][17].setWorldY(tileSize * 41);
        gamePanel.getInteractiveTiles()[map][17].setIndex(17);

        gamePanel.getInteractiveTiles()[map][18] = new IT_DryTree(gamePanel);
        gamePanel.getInteractiveTiles()[map][18].setWorldX(tileSize * 10);
        gamePanel.getInteractiveTiles()[map][18].setWorldY(tileSize * 40);
        gamePanel.getInteractiveTiles()[map][18].setIndex(18);
    }
}
