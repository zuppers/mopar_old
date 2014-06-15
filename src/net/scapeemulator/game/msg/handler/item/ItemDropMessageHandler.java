package net.scapeemulator.game.msg.handler.item;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.item.ItemDropMessage;

/**
 * Created by David Insley
 */
public final class ItemDropMessageHandler extends MessageHandler<ItemDropMessage> {
	    
    @Override
    public void handle(Player player, ItemDropMessage msg) {
    	if(player.actionsBlocked()) {
    		return;
    	}
    	if(!player.getInventory().check(msg.getSlot(), msg.getId())) {
    		return;
    	}
    	player.getGroundItems().add(msg.getId(), player.getInventory().get(msg.getSlot()).getAmount(), player.getPosition());
    	player.getInventory().remove(player.getInventory().get(msg.getSlot()), msg.getSlot());
    }
}
