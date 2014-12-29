package net.scapeemulator.game.model.player.bank;

import net.scapeemulator.game.dispatcher.button.WindowHandler;
import net.scapeemulator.game.model.ExtendedOption;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.interfaces.Interface;

/**
 * @author David Insley
 */
public class BankInterfaceHandler extends WindowHandler {

    public BankInterfaceHandler() {
        super(Interface.BANK, BankSession.BANK_INVENTORY);
    }

    @Override
    public boolean handle(Player player, int windowId, int child, ExtendedOption option, int dyn) {
        if (windowId == Interface.BANK) {
            if (player.getBankSession() != null) {
                player.getBankSession().handleInterfaceClick(child, dyn, option);
            }
        } else if (windowId == BankSession.BANK_INVENTORY) {
            if (player.getBankSession() != null) {
                player.getBankSession().handleInventoryClick(child, dyn, option);
            }
        }
        return true;
    }

}
