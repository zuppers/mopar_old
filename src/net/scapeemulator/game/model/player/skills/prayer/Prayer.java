package net.scapeemulator.game.model.player.skills.prayer;

import static net.scapeemulator.game.model.player.skills.prayer.PrayerType.*;
import net.scapeemulator.game.model.player.requirement.PrayerPointRequirement;
import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.requirement.SkillRequirement;
import net.scapeemulator.game.model.player.skills.Skill;

/**
 * @author David Insley
 */
public enum Prayer {

    // Tier one prayer bonuses
    THICK_SKIN(1, 50, 83, 5, DEFENCE),
    BURST_OF_STRENGTH(4, 50, 84, 7, STRENGTH),
    CLARITY_OF_THOUGHT(7, 50, 85, 9, ATTACK),
    SHARP_EYE(8, 50, 862, 11, RANGE, MAGIC),
    MYSTIC_WILL(9, 50, 863, 13, RANGE, MAGIC),
    
    // Tier two prayer bonuses
    ROCK_SKIN(10, 100, 86, 15, DEFENCE),
    SUPERHUMAN_STRENGTH(13, 100, 87, 17, STRENGTH),
    IMPROVED_REFLEXES(16, 100, 88, 19, ATTACK),
    HAWK_EYE(26, 100, 864, 27, RANGE, MAGIC),
    MYSTIC_LORE(27, 100, 865, 29, RANGE, MAGIC),
    
    // Tier three prayer bonuses
    STEEL_SKIN(28, 200, 92, 31, DEFENCE),
    ULTIMATE_STRENGTH(31, 200, 93, 33, STRENGTH),
    INCREDIBLE_REFLEXES(34, 200, 94, 35, ATTACK),
    EAGLE_EYE(44, 200, 866, 43, RANGE, MAGIC),
    MYSTIC_MIGHT(45, 200, 867, 45, RANGE, MAGIC),
    
    // Tier four prayer bonuses
    CHIVALRY(60, 400, 1052, 55, DEFENCE, STRENGTH, ATTACK),
    PIETY(70, 400, 1053, 57, DEFENCE, STRENGTH, ATTACK),
    
    // Stat restore
    RESTORE(19, 23, 89, 21, RAPID_RESTORE),
    
    // Health restore
    HEAL(22, 33, 90, 23, RAPID_HEAL),
    
    // Protect item
    PROTECT(25, 33, 91, 25, PROTECT_ITEM),
    
    // Overhead prayers TODO differentiate to allow summoning to pair with one other protect
    PROTECT_FROM_SUMMONING(35, 200, 1168, 53, OVERHEAD),
    PROTECT_FROM_MAGIC(37, 200, 95, 37, OVERHEAD),
    PROTECT_FROM_RANGED(40, 200, 96, 39, OVERHEAD),
    PROTECT_FROM_MELEE(43, 200, 97, 41, OVERHEAD),
    RETRIBUTION(46, 50, 98, 47, OVERHEAD),
    REDEMPTION(49, 100, 99, 49, OVERHEAD),
    SMITE(52, 300, 100, 51, OVERHEAD);
    
    static {
        // TODO chivalry/piety knights training ground requirements
        CHIVALRY.requirements.addRequirements();
        PIETY.requirements.addRequirements();
    }
    
    private final Requirements requirements;
    
    /**
     * How many points are drained per tick. One thousand is one point.
     */
    private final int drainRate;
    
    /**
     * The config id to activate and deactivate this prayer in the prayer tab.
     */
    private final int configId;
    
    private final int buttonId;
    private final PrayerType[] types;
    
    private Prayer(int levelRequirement, int drainRate, int configId, int buttonId, PrayerType ... types) {
        requirements = new Requirements();
        requirements.addRequirement(new SkillRequirement(Skill.PRAYER, levelRequirement, false, "activate that prayer"));
        requirements.addRequirement(PrayerPointRequirement.NON_ZERO_POINTS);
        this.drainRate = drainRate;
        this.configId = configId;
        this.buttonId = buttonId;
        this.types = types;     
    }
    
    public static Prayer forId(int buttonId) {
        for(Prayer prayer : values()) {
            if(prayer.buttonId == buttonId) {
                return prayer;
            }
        }
        return null;
    }
    
    public boolean conflicts(Prayer other) {
        for(PrayerType type : types) {
            for(PrayerType otherType : other.types) {
                if(type == otherType) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public Requirements getRequirements() {
        return requirements;
    }

    public double getDrainRate() {
        return drainRate;
    }

    public int getConfigId() {
        return configId;
    }
    
    public PrayerType[] getTypes() {
        return types;
    }
    
}
