package net.scapeemulator.game.msg.handler.item;

import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.item.ItemExamineMessage;

/**
 * @author David Insley
 */
public final class ItemExamineMessageHandler extends MessageHandler<ItemExamineMessage> {

    @Override
    public void handle(Player player, ItemExamineMessage msg) {
        player.sendMessage(ItemDefinitions.forId(msg.getId()).getExamine());
    }
}
