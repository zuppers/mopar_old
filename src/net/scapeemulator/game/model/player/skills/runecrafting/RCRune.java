package net.scapeemulator.game.model.player.skills.runecrafting;

import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.requirement.SkillRequirement;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.magic.Rune;

public enum RCRune {

    AIR(Rune.AIR, 5, false, 1, 11, 22, 33, 44, 55, 66, 77, 88, 99),
    MIND(Rune.MIND, 5.5, false, 1, 14, 28, 42, 56, 70, 84, 98),
    WATER(Rune.WATER, 6, false, 5, 19, 38, 57, 76, 95),
    EARTH(Rune.EARTH, 6.5, false, 9, 26, 52, 78, 104),
    FIRE(Rune.FIRE, 7, false, 14, 35, 70, 105),
    BODY(Rune.BODY, 7.5, false, 20, 46, 92),
    COSMIC(Rune.COSMIC, 8, true, 27, 59),
    CHAOS(Rune.CHAOS, 8.5, true, 35, 74),
    ASTRAL(Rune.ASTRAL, 8.7, true, 40, 82),
    NATURE(Rune.NATURE, 9, true, 44, 91),
    LAW(Rune.LAW, 9.5, true, 54),
    DEATH(Rune.DEATH, 10, true, 65),
    BLOOD(Rune.BLOOD, 10.5, true, 77);

    private final Rune rune;
    private final double xp;
    private final boolean pure;
    private final int[] levels;
    private final Requirements reqs;

    private RCRune(Rune rune, double xp, boolean pure, int ... levels) {
        this.rune = rune;
        this.xp = xp;
        this.pure = pure;
        this.levels = levels;
        reqs = new Requirements();
        // reqs.addRequirement(requirement); TODO quest req
        reqs.addRequirement(new SkillRequirement(Skill.RUNECRAFTING, levels[0], true, "craft " + name() + " runes"));
    }

    public Rune getRune() {
        return rune;
    }

    public boolean pureReq() {
        return pure;
    }

    public double getXP() {
        return xp;
    }

    public Requirements getRequirements() {
        return reqs;
    }

    public int getMultiplier(int level) {
        for (int i = 0; i < levels.length; i++) {
            if (level < levels[i]) {
                return i;
            }
        }
        return levels.length;
    }
}
