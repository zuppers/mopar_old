package net.scapeemulator.game.model.player.skills.magic;

import net.scapeemulator.game.dispatcher.button.WindowHandler;
import net.scapeemulator.game.model.ExtendedOption;
import net.scapeemulator.game.model.player.Player;

/**
 * @author David Insley
 */
public class AutoCastInterfaceHandler extends WindowHandler {

    public AutoCastInterfaceHandler() {
        super(310, 319, 388, 406);
    }

    @Override
    public boolean handle(Player player, int windowId, int child, ExtendedOption option, int dyn) {
        AutoCastHandler.handleSpellSelection(player, windowId, child);
        return true;
    }

}
