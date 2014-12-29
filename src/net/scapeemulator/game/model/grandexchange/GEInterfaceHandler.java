package net.scapeemulator.game.model.grandexchange;

import net.scapeemulator.game.dispatcher.button.WindowHandler;
import net.scapeemulator.game.model.ExtendedOption;
import net.scapeemulator.game.model.player.Player;

/**
 * @author David Insley
 */
public class GEInterfaceHandler extends WindowHandler {

    public GEInterfaceHandler() {
        super(105, 107);
    }

    @Override
    public boolean handle(Player player, int windowId, int child, ExtendedOption option, int dyn) {
        switch (windowId) {
        case 105:
            player.getGrandExchangeHandler().handleMainInterface(child, option);
            break;
        case 107:
            player.getGrandExchangeHandler().handleOfferInventoryClick(child, dyn);
            break;
        }
        return true;
    }

}
