package net.scapeemulator.game.model.player.skills;

import java.util.ArrayList;
import java.util.List;

public final class SkillSet {

    public static final double MAXIMUM_EXPERIENCE = 200000000;

    private static int getLevelFromExperience(double xp) {
        int points = 0;
        int output = 0;

        for (int level = 1; level <= 99; level++) {
            points += Math.floor(level + 300.0 * Math.pow(2.0, level / 7.0));
            output = points / 4;

            if ((output - 1) >= xp)
                return level;
        }

        return 99;
    }

    private final int[] curLevel = new int[24];
    private final int[] level = new int[curLevel.length];
    private final double[] exp = new double[curLevel.length];
    private final List<SkillListener> listeners = new ArrayList<>();
    private int combatLevel;

    public SkillSet() {
        for (int i = 0; i < curLevel.length; i++) {
            curLevel[i] = 1;
            level[i] = 1;
            exp[i] = 0;
        }

        curLevel[Skill.HITPOINTS] = 10;
        level[Skill.HITPOINTS] = 10;
        exp[Skill.HITPOINTS] = 1154;
        calculateCombatLevel();
    }

    public void addListener(SkillListener listener) {
        listeners.add(listener);
    }

    public void removeListener(SkillListener listener) {
        listeners.remove(listener);
    }

    public void removeListeners() {
        listeners.clear();
    }

    public int size() {
        return curLevel.length;
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
        for (SkillListener listener : listeners)
            listener.skillChanged(this, skill);
    }

    public int getLevel(int skill) {
        return level[skill];
    }

    public double getExperience(int skill) {
        return exp[skill];
    }

    public void setExperience(int skill, double experience) {
        exp[skill] = experience;
        level[skill] = getLevelFromExperience(exp[skill]);
        calculateCombatLevel();
    }

    public void addExperience(int skill, double xp) {
        if (xp <= 0) {
            return;
        }

        int oldLevel = level[skill];
        int oldCB = combatLevel;

        exp[skill] = Math.min(exp[skill] + xp, MAXIMUM_EXPERIENCE);
        level[skill] = getLevelFromExperience(exp[skill]);

        calculateCombatLevel();
        int delta = level[skill] - oldLevel;
        curLevel[skill] += delta;

        for (SkillListener listener : listeners) {
            listener.skillChanged(this, skill);
        }

        if (delta > 0) {
            for (SkillListener listener : listeners) {
                listener.skillLevelledUp(this, delta, skill);
            }
        }
        if (combatLevel > oldCB) {
            for (SkillListener listener : listeners) {
                listener.combatLevelledUp(this, combatLevel);
            }
        }
    }

    public void refresh() {
        for (int skill = 0; skill < curLevel.length; skill++) {
            for (SkillListener listener : listeners)
                listener.skillChanged(this, skill);
        }
    }

    public void restoreStats() {
        for (int skill = 0; skill < curLevel.length; skill++) {
            curLevel[skill] = level[skill];
            for (SkillListener listener : listeners)
                listener.skillChanged(this, skill);
        }
    }

    public int getCombatLevel() {
        return combatLevel;
    }

    public void calculateCombatLevel() {
        int defence = level[Skill.DEFENCE];
        int hitpoints = level[Skill.HITPOINTS];
        int prayer = level[Skill.PRAYER];
        int attack = level[Skill.ATTACK];
        int strength = level[Skill.STRENGTH];
        int magic = level[Skill.MAGIC];
        int ranged = level[Skill.RANGED];
        int summoning = level[Skill.SUMMONING];

        double base = 1.3 * Math.max(Math.max(attack + strength, 1.5 * magic), 1.5 * ranged);

        combatLevel = (int) Math.floor((defence + hitpoints + Math.floor(prayer / 2.0) + Math.floor(summoning / 2.0) + base) / 4.0);
    }

    public int getTotalLevel() {
        int total = 0;
        for (int skill = 0; skill < curLevel.length; skill++) {
            total += level[skill];
        }
        return total;
    }

}
