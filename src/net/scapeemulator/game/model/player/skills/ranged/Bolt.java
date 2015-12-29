package net.scapeemulator.game.model.player.skills.ranged;

import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.msg.impl.CreateProjectileMessage;

public enum Bolt {

    BRONZE(877),
    BARBED(881),
    OPAL(879),
    OPAL_E(9236),
    // All above for bronze crossbow

    BLURITE(9139),
    JADE(9335),
    JADE_E(9237),
    // All above for blurite crossbow

    SILVER(9145),
    IRON(9140),
    PEARL(880),
    PEARL_E(9238),
    // All above for iron crossbow

    STEEL(9141),
    TOPAZ(9336),
    TOPAZ_E(9239),
    // All above for steel crossbow

    BLACK(13083),
    // All above for black crossbow

    MITHRIL(9142),
    SAPPHIRE(9337),
    SAPPHIRE_E(9240),
    EMERALD(9338),
    EMERALD_E(9241),
    // All above for mithril crossbow

    ADAMANT(9143),
    RUBY(9339),
    RUBY_E(9242),
    DIAMOND(9340),
    DIAMOND_E(9243),
    // All above for adamant crossbow

    RUNE(9144),
    DRAGON(9341),
    DRAGON_E(9244),
    ONYX(9342),
    ONYX_E(9245),
    // All above for rune crossbow

    BOLT_RACK(4740);

    private final int itemId;
    private final int projectileGraphic;

    private Bolt(int itemId) {
        this.itemId = itemId;
        this.projectileGraphic = 27;
    }

    public static Bolt forId(int itemId) {
        for (Bolt bolt : values()) {
            if (bolt.itemId == itemId) {
                return bolt;
            }
        }
        return null;
    }

    public CreateProjectileMessage createProjectile(Mob source, Mob target) {
        return new CreateProjectileMessage(source.getPosition(), target.getPosition(), target, projectileGraphic, 40, 35, 50, 1, 4, true);

    }
}
