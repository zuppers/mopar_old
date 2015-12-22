package net.scapeemulator.game.model.npc;

import net.scapeemulator.cache.def.NPCDefinition;

public final class NPCSkillSet {

    public static final int ATTACK = 0;
    public static final int DEFENCE = 1;
    public static final int STRENGTH = 2;
    public static final int HITPOINTS = 3;
    public static final int RANGED = 4;
    public static final int MAGIC = 5;
 
    private int hpRegenCounter;
    private int regenCounter;

    private final int[] curLevel = new int[6];
    private final int[] level = new int[curLevel.length];

    public NPCSkillSet(NPCDefinition definition) {
        curLevel[ATTACK] = definition.getHPLevel();
        level[ATTACK] = curLevel[ATTACK];
        curLevel[DEFENCE] = definition.getDefenceLevel();
        level[DEFENCE] = curLevel[DEFENCE];
        curLevel[STRENGTH] = definition.getStrengthLevel();
        level[STRENGTH] = curLevel[STRENGTH];
        curLevel[HITPOINTS] = definition.getHPLevel();
        level[HITPOINTS] = curLevel[HITPOINTS];
        curLevel[RANGED] = definition.getRangeLevel();
        level[RANGED] = curLevel[RANGED];
        curLevel[MAGIC] = definition.getMagicLevel();
        level[MAGIC] = curLevel[MAGIC];
    }

    public int getCurrentLevel(int skill) {
        return curLevel[skill];
    }

    public void setCurrentLevel(int skill, int curLvl) {
        if (curLevel[skill] == curLvl) {
            return;
        }
        curLvl = curLvl < 0 ? 0 : curLvl;
        curLevel[skill] = curLvl;
    }

    public int getLevel(int skill) {
        return level[skill];
    }

    public void restoreStats() {
        for (int skill = 0; skill < curLevel.length; skill++) {
            curLevel[skill] = level[skill];
        }
    }

    public void tick(int hpRegen, int skillRegen) {
        if (curLevel[HITPOINTS] < level[HITPOINTS]) {
            hpRegenCounter += hpRegen;
            if (hpRegenCounter >= 100) {
                hpRegenCounter = 0;
                curLevel[HITPOINTS] = curLevel[HITPOINTS] + 1;
            }
        }
        regenCounter += skillRegen;
        if (regenCounter >= 100) {
            regenCounter = 0;
            for (int skill = 0; skill < curLevel.length; skill++) {
                if (skill == HITPOINTS) {
                    continue;
                }
                if (curLevel[skill] != level[skill]) {
                    if (curLevel[skill] < level[skill]) {
                        curLevel[skill] = curLevel[skill] + 1;
                    } else {
                        curLevel[skill] = curLevel[skill] - 1;
                    }
                }
            }
        }
    }

}
