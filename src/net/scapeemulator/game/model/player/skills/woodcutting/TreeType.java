package net.scapeemulator.game.model.player.skills.woodcutting;

import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.requirement.SkillRequirement;
import net.scapeemulator.game.model.player.skills.Skill;

/**
 * @author David Insley
 */
public enum TreeType {
    
    NORMAL(1, 25, 1511, 1, 5, 1342, Woodcutting.NORMAL_TREES),
    ACHEY(1, 25, 2862, 1, 10, 3371, 2023),
    OAK(15, 37.5, 1521, 6, 10, 1342, 1281, 3037),
    WILLOW(30, 67.5, 1519, 6, 20, 1342, 5551, 5552, 5553),
    TEAK(35, 85, 6333, 6, 30, 1342, 9036),
    MAPLE(45, 100, 1517, 6, 35, 1343, 1307, 4674),
    MAHOGANY(50, 125, 6332, 7, 45, 1342, 9034),
    ARCTIC_PINE(54, 140, 10810, 7, 45, 1342),
    EUCALYPTUS(58, 165, 12581, 7, 45, 1342),
    YEW(60, 175, 1515, 8, 60, 1342, 1309),
    MAGIC(75, 250, 1513, 10, 120, 1342, 1292, 1306);
    
    private final int level;
    private final double xp;
    private final int logId;
    private final int averageLogs;
    private final int respawnTicks;
    private final int stumpId;
    private final Requirements requirements;
    private final int[] objectIds;
    
    private TreeType(int level, double xp, int logId, int averageLogs, int respawnTime, int stumpId, int... objectIds) {
        this.level = level;
        this.xp = xp;
        this.logId = logId;
        this.averageLogs = averageLogs;
        respawnTicks = (int) (respawnTime * (5.0 / 3.0));
        this.stumpId = stumpId;
        requirements = new Requirements();
        requirements.addRequirement(new SkillRequirement(Skill.WOODCUTTING, level, true, "cut that tree"));
        this.objectIds = objectIds;
    }
    
    public static TreeType forId(int objectId) {
        for (TreeType type : values()) {
            for (int typeId : type.objectIds) {
                if (typeId == objectId) {
                    return type;
                }
            }
        }
        return null;
    }
    
    public int getLevel() {
        return level;
    }
    
    public double getXp() {
        return xp;
    }
    
    public int getLogId() {
        return logId;
    }
    
    public int getAverageLogs() {
        return averageLogs;
    }
    
    public int getRespawnTicks() {
        return respawnTicks;
    }
    
    public int getStumpId() {
        return stumpId;
    }
    
    public Requirements getRequirements() {
        return requirements;
    }
    
}
