package net.scapeemulator.game.model.player.skills.magic;

import net.scapeemulator.game.model.SpotAnimation;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.player.Player;

/**
 * @author David Insley
 */
public abstract class EffectItemSpell extends Spell {

    public EffectItemSpell(int animation, int graphic) {
        super(SpellType.ITEM, new Animation(animation), new SpotAnimation(graphic, 0, 100));
    }

    public abstract void cast(Player player, int itemId, int slotId);

}
