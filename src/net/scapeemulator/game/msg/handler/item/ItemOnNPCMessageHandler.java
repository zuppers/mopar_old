package net.scapeemulator.game.msg.handler.item;

import net.scapeemulator.game.dispatcher.item.ItemOnNPCDispatcher;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.item.ItemOnNPCMessage;

/**
 * @author zuppers
 */
public final class ItemOnNPCMessageHandler extends MessageHandler<ItemOnNPCMessage> {

    private final ItemOnNPCDispatcher dispatcher;

    public ItemOnNPCMessageHandler(ItemOnNPCDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void handle(Player player, ItemOnNPCMessage msg) {
        dispatcher.handle(player, msg.getItemId(), msg.getSlot(), msg.getNPCIndex(), msg.getWidgetHash());
    }

}