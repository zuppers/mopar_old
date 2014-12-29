package net.scapeemulator.game.model.player.skills.magic;

import net.scapeemulator.game.dispatcher.button.WindowHandler;
import net.scapeemulator.game.model.ExtendedOption;
import net.scapeemulator.game.model.player.Player;

/**
 * @author David Insley
 */
public class SpellbookHandler extends WindowHandler {

    public SpellbookHandler(int... spellbookIds) {
        super(spellbookIds);
    }

    @Override
    public boolean handle(Player player, int windowId, int child, ExtendedOption option, int dyn) {
        Spell spell = player.getSpellbook().getSpell(child);
        if (spell == null) {
            return true;
        }
        switch (spell.getType()) {
        case COMBAT:
        case EFFECT_MOB:
        case ITEM:
            return true;
        case TELEPORT:
            ((TeleportSpell) spell).cast(player);
            break;
        }
        return true;
    }

}
