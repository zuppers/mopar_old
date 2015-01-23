package net.scapeemulator.game.msg.handler.item;

import net.scapeemulator.game.dispatcher.item.ItemOnGroundItemDispatcher;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.item.ItemOnGroundItemMessage;

/**
 * @author zuppers
 */
public final class ItemOnGroundItemMessageHandler extends MessageHandler<ItemOnGroundItemMessage> {

    private final ItemOnGroundItemDispatcher dispatcher;

    public ItemOnGroundItemMessageHandler(ItemOnGroundItemDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void handle(Player player, ItemOnGroundItemMessage msg) {
        dispatcher.handle(player, msg.getItemId(), msg.getSlot(), msg.getX(), msg.getY(), msg.getGroundItemId(), msg.getWidgetHash());
    }

}