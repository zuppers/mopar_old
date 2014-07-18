package net.scapeemulator.game.msg.handler.item;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.item.MagicOnItemMessage;

/**
 * @author David Insley
 */
public final class MagicOnItemMessageHandler extends MessageHandler<MagicOnItemMessage> {

    public MagicOnItemMessageHandler() {
    }

    @Override
    public void handle(Player player, MagicOnItemMessage msg) {
        System.out.println("[MagicOnItem] tab/spell: (" + msg.getTabId() + "/" + msg.getSpellId() + ") slot/itemid: (" + msg.getSlot() + "/" + msg.getItemId() + ")");
    }
}
