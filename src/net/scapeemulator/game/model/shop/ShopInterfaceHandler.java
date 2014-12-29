package net.scapeemulator.game.model.shop;

import net.scapeemulator.game.dispatcher.button.WindowHandler;
import net.scapeemulator.game.model.ExtendedOption;
import net.scapeemulator.game.model.player.Player;

/**
 * @author David Insley
 */
public class ShopInterfaceHandler extends WindowHandler {

    public ShopInterfaceHandler() {
        super(620, 621);
    }

    @Override
    public boolean handle(Player player, int windowId, int child, ExtendedOption option, int dyn) {
        if (windowId == 620) {
            player.getShopHandler().handleInput(child, dyn, option);
        } else if (windowId == 621) {
            player.getShopHandler().handleInventoryClick(dyn, option);
        }
        return true;
    }

}
