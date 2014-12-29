package net.scapeemulator.game.model.player.skills.woodcutting;

import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.player.Equipment;
import net.scapeemulator.game.model.player.requirement.EquipmentRequirement;
import net.scapeemulator.game.model.player.requirement.ItemRequirement;
import net.scapeemulator.game.model.player.requirement.OneOfRequirement;
import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.requirement.SkillRequirement;
import net.scapeemulator.game.model.player.skills.Skill;

/**
 * @author David Insley
 */
public enum Hatchet {
    
    BRONZE(1351, 879, 1, 1),
    IRON(1349, 877, 1, 2),
    STEEL(1353, 875, 6, 3),
    BLACK(1361, 873, 6, 4),
    MITHRIL(1355, 871, 21, 5),
    ADAMANT(1357, 869, 31, 6),
    RUNE(1359, 867, 41, 7),
    DRAGON(6739, 2846, 61, 8),
    INFERNO_ADZE(13661, 10251, 61, 9);
    
    private final int itemId;
    private final Animation animation;
    private final Requirements requirements;
    private final int speed;
    
    private Hatchet(int itemId, int animationId, int level, int speed) {
        this.itemId = itemId;
        animation = new Animation(animationId);
        requirements = new Requirements();
        requirements.addRequirement(new SkillRequirement(Skill.WOODCUTTING, level, true, "use that axe"));
        EquipmentRequirement er = new EquipmentRequirement(Equipment.WEAPON, itemId);
        ItemRequirement ir = new ItemRequirement(itemId, false);
        requirements.addRequirement(new OneOfRequirement(er, ir));
        this.speed = speed;
    }
    
    public int getItemId() {
        return itemId;
    }
    
    public Animation getAnimation() {
        return animation;
    }
    
    public Requirements getRequirements() {
        return requirements;
    }
    
    public int getSpeed() {
        return speed;
    }
}
