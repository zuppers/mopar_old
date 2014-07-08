package net.scapeemulator.game.model.player.skills.herblore;

import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.player.requirement.ItemRequirement;
import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.requirement.SkillRequirement;
import net.scapeemulator.game.model.player.skills.Skill;

/**
 * @author David Insley
 */
public class HerbloreRecipe {

    private final int product;
    private final double xp;
    private final String message;
    private final Requirements requirements = new Requirements();
    private final Animation animation;
    private final int delay;

    HerbloreRecipe(int itemOne, int itemTwo, int level, double xp, int product, String message, Animation animation, int delay) {
        this(itemOne, true, itemTwo, true, level, xp, product, message, animation, delay);
    }

    HerbloreRecipe(int itemOne, boolean consumeOne, int itemTwo, boolean consumeTwo, int level, double xp, int product, String message, Animation animation, int delay) {
        if (level > 1) {
            requirements.addRequirement(new SkillRequirement(Skill.HERBLORE, level, true, "make this potion"));
        }
        requirements.addRequirement(new ItemRequirement(itemOne, consumeOne));
        requirements.addRequirement(new ItemRequirement(itemTwo, consumeTwo));
        this.product = product;
        this.xp = xp;
        this.message = message;
        this.animation = animation;
        this.delay = delay;
    }

    public int getProduct() {
        return product;
    }

    public double getXp() {
        return xp;
    }

    public String getMessage() {
        return message;
    }

    public Requirements getRequirements() {
        return requirements;
    }

    public Animation getAnimation() {
        return animation;
    }

    public int getDelay() {
        return delay;
    }

}
