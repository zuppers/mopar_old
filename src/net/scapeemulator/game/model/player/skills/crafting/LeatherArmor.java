package net.scapeemulator.game.model.player.skills.crafting;

import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.requirement.ItemRequirement;
import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.requirement.SkillRequirement;
import net.scapeemulator.game.model.player.skills.Skill;

/**
 * @author David Insley
 */
public enum LeatherArmor {

    LEATHER_GLOVES(1, 13.8, 1059, Leather.SOFT),
    LEATHER_BOOTS(7, 16.25, 1061, Leather.SOFT),
    LEATHER_COWL(9, 18.5, 1167, Leather.SOFT),
    LEATHER_VAMBRACES(11, 22, 1063, Leather.SOFT),
    LEATHER_BODY(14, 25, 1129, Leather.SOFT),
    LEATHER_CHAPS(18, 27, 1095, Leather.SOFT),
    HARD_LEATHER_BODY(28, 35, 1131, Leather.HARD),
    LEATHER_COIF(38, 37, 1169, Leather.SOFT),

    GREEN_VAMB(57, 62, 1065, Leather.GREEN),
    GREEN_CHAPS(60, 124, 1099, Leather.GREEN, 2),
    GREEN_BODY(63, 186, 1135, Leather.GREEN, 3),

    BLUE_VAMB(66, 70, 2487, Leather.BLUE),
    BLUE_CHAPS(68, 140, 2493, Leather.BLUE, 2),
    BLUE_BODY(71, 210, 2499, Leather.BLUE, 3),

    RED_VAMB(73, 78, 2489, Leather.RED),
    RED_CHAPS(75, 156, 2495, Leather.RED, 2),
    RED_BODY(77, 234, 2501, Leather.RED, 3),

    BLACK_VAMB(79, 86, 2491, Leather.BLACK),
    BLACK_CHAPS(82, 172, 2497, Leather.BLACK, 2),
    BLACK_BODY(84, 258, 2503, Leather.BLACK, 3);

    private final Requirements req = new Requirements();
    private final Item product;
    private final Leather leather;

    private LeatherArmor(int level, double xp, int itemId, Leather leather) {
        this(level, xp, itemId, leather, 1);
    }

    private LeatherArmor(int level, double xp, int itemId, Leather leather, int count) {
        product = new Item(itemId);
        this.leather = leather;
        req.addRequirement(new SkillRequirement(Skill.CRAFTING, level, true, "craft that", xp));
        req.addRequirements(Crafting.THREAD_REQUIREMENT, Crafting.NEEDLE_REQUIREMENT);
        req.addRequirement(new ItemRequirement(leather.getLeatherId(), count, true, "You do not have enough leather to craft that."));
    }

    public Item getProduct() {
        return product;
    }

    public Leather getLeatherType() {
        return leather;
    }
}
