package net.scapeemulator.game.model.player.skills.firemaking;

import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.requirement.SkillRequirement;
import net.scapeemulator.game.model.player.skills.Skill;

/**
 * @author David Insley
 */
public enum Log {
    
    NORMAL(1511, 1, 40, 2732),
    ACHEY(2862, 1, 40, 2732),
    OAK(1521, 15, 60, 2732),
    WILLOW(1519, 30, 90, 2732),
    TEAK(6333, 35, 105, 2732),
    MAPLE(1517, 45, 135, 2732),
    MAHOGANY(6332, 50, 157.5, 2732),
    ARCTIC_PINE(10810, 54, 175, 2732),
    EUCALYPTUS(12581, 58, 193.5, 2732),
    YEW(1515, 60, 202.5, 2732),
    MAGIC(1513, 75, 303.8, 2732);
    
    /**
     * Item id of the log
     */
    private final int itemId;
    
    /**
     * Level requirement to burn the log, used to calculate how long it takes to light
     */
    private final int level;
    
    /**
     * Requirements for the player to light the log (level, quest, etc)
     */
    private final Requirements requirements;
    
    /**
     * Amount of experience awarded when the log is successfully lit
     */
    private final double xp;
    
    /**
     * Object id of the lit fire; 
     * TODO find out if needed
     */
    private final int fireId;
    
    private Log(int itemId, int level, double xp, int fireId) {
        this.itemId = itemId;
        this.level = level;
        requirements = new Requirements();
        requirements.addRequirement(new SkillRequirement(Skill.FIREMAKING, level, "burn those logs"));
        this.xp = xp;
        this.fireId = fireId;
    }
    
    /**
     * Searches for a Firemaking log that matches the given item
     * 
     * @param itemId item id to search for
     * @return the Log with the given item id, or null if none found
     */
    public static Log forId(int itemId) {
        for(Log log : values()) {
            if(log.itemId == itemId) {
                return log;
            }
        }
        return null;
    }
    
    public int getItemId() {
        return itemId;
    }
    
    public int getLevel() {
        return level;
    }
    
    public Requirements getRequirements() {
        return requirements;
    }
   
    public double getXp() {
        return xp;
    }
    
    public int getFireId() {
        return fireId;
    }
}
