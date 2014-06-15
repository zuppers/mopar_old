/**
 * Copyright (c) 2012
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN 
 * THE SOFTWARE.
 */

package net.scapeemulator.game.item;

import java.util.HashMap;
import java.util.Map;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.inventory.Inventory;

/**
 * Created by David Insley
 */
public final class ItemInteractDispatcher {

	private Map<Integer, ItemInteractHandler> handlers = new HashMap<>();

	public ItemInteractDispatcher() {}

	public void bind(ItemInteractHandler handler) {
		handlers.put(handler.getItemId(), handler);
	}

	public void unbindAll() {
		handlers.clear();
	}

	public void handle(Player player, int itemId, int slot) {
		if(player.actionsBlocked()) {
			return;
		}
		Inventory inventory = player.getInventorySet().getInventory();

		if (!checkInventory(inventory, slot, itemId)) {
			return;
		}

		ItemInteractHandler handler = handlers.get(itemId);
		if (handler != null) {
			SlottedItem item = new SlottedItem(slot, inventory.get(slot));
			handler.handle(player, item);
		}
		player.sendMessage("Nothing interesting happens.");
	}

	private static boolean checkInventory(Inventory inventory, int slot, int itemId) {
		return inventory.get(slot) != null && inventory.get(slot).getId() == itemId;
	}

}
