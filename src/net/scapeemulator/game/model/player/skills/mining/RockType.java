package net.scapeemulator.game.model.player.skills.mining;

import static net.scapeemulator.game.model.player.skills.mining.Mining.ADAMANT_ROCKS;
import static net.scapeemulator.game.model.player.skills.mining.Mining.BLURITE_ROCKS;
import static net.scapeemulator.game.model.player.skills.mining.Mining.CLAY_ROCKS;
import static net.scapeemulator.game.model.player.skills.mining.Mining.COAL_ROCKS;
import static net.scapeemulator.game.model.player.skills.mining.Mining.COPPER_ROCKS;
import static net.scapeemulator.game.model.player.skills.mining.Mining.GOLD_ROCKS;
import static net.scapeemulator.game.model.player.skills.mining.Mining.IRON_ROCKS;
import static net.scapeemulator.game.model.player.skills.mining.Mining.MITHRIL_ROCKS;
import static net.scapeemulator.game.model.player.skills.mining.Mining.RUNITE_ROCKS;
import static net.scapeemulator.game.model.player.skills.mining.Mining.SILVER_ROCKS;
import static net.scapeemulator.game.model.player.skills.mining.Mining.TIN_ROCKS;
import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.requirement.SkillRequirement;
import net.scapeemulator.game.model.player.skills.Skill;

/**
 * @author David Insley
 */
public enum RockType {
    
    CLAY(1, 5, 434, 2, CLAY_ROCKS),
    COPPER(1, 17.5, 436, 3, COPPER_ROCKS),
    TIN(1, 17.5, 438, 3, TIN_ROCKS),
    BLURITE(10, 17.5, 668, 30, BLURITE_ROCKS),
    IRON(15, 35, 440, 7, IRON_ROCKS),
    SILVER(20, 40, 442, 80, SILVER_ROCKS),
    COAL(30, 50, 453, 40, COAL_ROCKS),
    GOLD(40, 65, 444, 80, GOLD_ROCKS),
    MITHRIL(55, 80, 447, 120, MITHRIL_ROCKS),
    ADAMANT(70, 95, 449, 360, ADAMANT_ROCKS),
    RUNITE(85, 125, 451, 900, RUNITE_ROCKS);
    
    private final int level;
    private final double xp;
    private final int oreId;
    private final int respawnTicks;
    private final Requirements requirements;
    private final int[] objectIds;
    
    private RockType(int level, double xp, int oreId, int respawnTime, int... objectIds) {
        this.level = level;
        this.xp = xp;
        this.oreId = oreId;
        respawnTicks = (int) (respawnTime * (5.0 / 3.0));
        requirements = new Requirements();
        requirements.addRequirement(new SkillRequirement(Skill.MINING, level, true, "mine that rock"));
        this.objectIds = objectIds;
    }
    
    /**
     * Finds the RockType for the given objectId.
     * 
     * @param objectId the objectId to search for
     * @return the RockType that corresponds to the given objectId, or null if none found
     */
    public static RockType forId(int objectId) {
        for (RockType type : values()) {
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
    
    public int getOreId() {
        return oreId;
    }
        
    public int getRespawnTicks() {
        return respawnTicks;
    }
        
    public Requirements getRequirements() {
        return requirements;
    }
    
}
