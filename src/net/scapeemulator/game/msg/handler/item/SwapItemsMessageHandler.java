package net.scapeemulator.game.msg.handler.item;

import net.scapeemulator.game.model.player.Interface;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.bank.BankSession;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.SwapItemsMessage;

public final class SwapItemsMessageHandler extends MessageHandler<SwapItemsMessage> {

    @Override
    public void handle(Player player, SwapItemsMessage message) {
        if (player.actionsBlocked()) {
            return;
        }
        if (message.getId() == Interface.INVENTORY && message.getSlot() == Interface.INVENTORY_CONTAINER || message.getId() == BankSession.BANK_INVENTORY) {
            player.getInventory().swap(message.getSource(), message.getDestination());
        } else if (message.getId() == Interface.BANK) {
            if (player.getBankSession() != null) {
                player.getBankSession().handleBankSwap(message.getType(), message.getSource(), message.getDestination());
            }
        }
    }

}
