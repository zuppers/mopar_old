package net.scapeemulator.game.model.player.skills.prayer;

import net.scapeemulator.game.model.player.skills.Skill;

/**
 * @author David Insley 
 */
public enum PrayerType {
    
    DEFENCE(Skill.DEFENCE),
    STRENGTH(Skill.STRENGTH),
    ATTACK(Skill.ATTACK),
    RANGE(Skill.RANGED),
    MAGIC(Skill.MAGIC),
    RAPID_RESTORE, RAPID_HEAL, PROTECT_ITEM, OVERHEAD;
    
    private final int skillId;
    
    private PrayerType() {
        this(-1);
    }
    
    private PrayerType(int skillId) {
        this.skillId = skillId;
    }
    
    public int getSkillId() {
        return skillId;
    }
    
}
