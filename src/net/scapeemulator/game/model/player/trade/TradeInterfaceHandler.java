package net.scapeemulator.game.model.player.trade;

import net.scapeemulator.game.dispatcher.button.WindowHandler;
import net.scapeemulator.game.model.ExtendedOption;
import net.scapeemulator.game.model.player.Player;

/**
 * @author David Insley
 */
public class TradeInterfaceHandler extends WindowHandler {

    public TradeInterfaceHandler() {
        super(334, 335, 336);
    }

    @Override
    public boolean handle(Player player, int windowId, int child, ExtendedOption option, int dyn) {
        if (windowId == 334 || windowId == 335) {
            if (player.getTradeSession() != null) {
                player.getTradeSession().handleInterfaceClick(windowId, child, dyn, option);
            }
        } else if (windowId == 336) {
            if (player.getTradeSession() != null) {
                player.getTradeSession().handleInventoryClick(dyn, option);
            }
        }
        return true;
    }

}
