package net.scapeemulator.game.model.player.skills.mining;

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
public enum Pickaxe {
    
    BRONZE(1265, 625, 1, 1),
    IRON(1267, 626, 1, 2),
    STEEL(1269, 627, 6, 3),
    MITHRIL(1273, 629, 21, 4),
    ADAMANT(1271, 628, 31, 5),
    RUNE(1275, 624, 41, 6),
    INFERNO_ADZE(13661, 10222, 41, 7);
    
    private final Animation animation;
    private final Requirements requirements;
    private final int speed;
    
    private Pickaxe(int itemId, int animationId, int level, int speed) {
        animation = new Animation(animationId);
        requirements = new Requirements();
        requirements.addRequirement(new SkillRequirement(Skill.MINING, level, true, "use that pickaxe"));
        EquipmentRequirement er = new EquipmentRequirement(Equipment.WEAPON, itemId);
        ItemRequirement ir = new ItemRequirement(itemId, false);
        requirements.addRequirement(new OneOfRequirement(er, ir));
        this.speed = speed;
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
