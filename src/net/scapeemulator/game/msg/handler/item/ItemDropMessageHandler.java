package net.scapeemulator.game.msg.handler.item;

import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.item.ItemDropMessage;

/**
 * @author David Insley
 */
public final class ItemDropMessageHandler extends MessageHandler<ItemDropMessage> {

    @Override
    public void handle(Player player, ItemDropMessage msg) {
        if (player.actionsBlocked()) {
            return;
        }
        if (!player.getInventory().verify(msg.getSlot(), msg.getId())) {
            return;
        }
        if (player.getInHouse() != null) {
            player.sendMessage("You can't drop items in a house.");
            return;
        }
        Item removed = player.getInventory().remove(player.getInventory().get(msg.getSlot()), msg.getSlot());
        player.getGroundItems().add(removed.getId(), removed.getAmount(), player.getPosition());
    }
}
