package net.scapeemulator.game.msg.handler.item;

import net.scapeemulator.game.dispatcher.item.ItemDispatcher;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.item.MagicOnItemMessage;

/**
 * @author David Insley
 */
public final class MagicOnItemMessageHandler extends MessageHandler<MagicOnItemMessage> {

    private final ItemDispatcher dispatcher;

    public MagicOnItemMessageHandler(ItemDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void handle(Player player, MagicOnItemMessage msg) {
        dispatcher.handleMagic(player, msg.getTabId(), msg.getSpellId(), msg.getSlot(), msg.getItemId());
    }

}
