
package net.scapeemulator.game.dispatcher.item;

import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.player.Equipment;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.inventory.Inventory;
import net.scapeemulator.game.util.HandlerContext;

/**
 * Created by David Insley
 */
public class ItemEquipHandler extends ItemHandler {

	public ItemEquipHandler() {
		super(Option.TWO);
	}

	@Override
	public void handle(Player player, Inventory inventory, SlottedItem item, String option, HandlerContext context) {
		if(player.actionsBlocked()) {
			return;
		}
		if (option.equals("equip") || option.equals("wield") || option.equals("wear")) {
			Equipment.equip(player, item.getSlot());
			context.stop();
		}
	}
	
}
