package net.scapeemulator.game.model.player.skills.crafting;

import net.scapeemulator.game.model.player.requirement.ItemRequirement;
import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.requirement.SkillRequirement;
import net.scapeemulator.game.model.player.skills.Skill;

/**
 * @author David Insley
 */
public enum Pottery {

    POT(1, 6.3, 6.3, 1787, 1931),
    PIE_DISH(7, 15.0, 10.0, 1789, 2313),
    BOWL(8, 18.0, 15.0, 1791, 1923),
    PLANT_POT(19, 20.0, 17.5, 5352, 5350),
    POT_LID(25, 20.0, 20.0, 4438, 4440);

    private final Requirements spinningReq = new Requirements();
    private final Requirements firingReq = new Requirements();

    private final int unfiredId;
    private final int itemId;

    private Pottery(int level, double spinningXp, double firingXp, int unfiredId, int itemId) {
        this.itemId = itemId;
        this.unfiredId = unfiredId;
        spinningReq.addRequirement(new SkillRequirement(Skill.CRAFTING, level, true, "spin that", spinningXp));
        spinningReq.addRequirement(Crafting.SOFT_CLAY_REQUIREMENT);
        firingReq.addRequirement(new SkillRequirement(Skill.CRAFTING, level, true, "fire that", firingXp));
        firingReq.addRequirement(new ItemRequirement(unfiredId, true, "You"));
    }

    public int getUnfiredId() {
        return unfiredId;
    }

    public int getItemId() {
        return itemId;
    }
}
