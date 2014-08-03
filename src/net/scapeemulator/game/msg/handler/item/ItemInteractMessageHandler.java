package net.scapeemulator.game.msg.handler.item;

import net.scapeemulator.game.dispatcher.item.ItemInteractDispatcher;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.item.ItemInteractMessage;

/**
 * Created by David Insley
 */
public final class ItemInteractMessageHandler extends MessageHandler<ItemInteractMessage> {
	
    private final ItemInteractDispatcher dispatcher;

    public ItemInteractMessageHandler(ItemInteractDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
    @Override
    public void handle(Player player, ItemInteractMessage msg) {
        dispatcher.handle(player, msg.getId(), msg.getSlot());
    }
}
