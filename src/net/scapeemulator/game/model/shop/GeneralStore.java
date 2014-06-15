package net.scapeemulator.game.model.shop;

import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.player.Item;

public class GeneralStore extends Shop {

	public GeneralStore(int databaseId, String name, int shopId, int[] stockIds) {
		super(databaseId, name, shopId, stockIds);
		playerStock = new Item[40];
	}

	@Override
	public boolean acceptsItem(int itemId) {
		//TODO replace getValue() > 0 with a tradable check
		return contains(StockType.MAIN, itemId) || (getBestSlot(itemId) > -1 && ItemDefinitions.forId(itemId).getValue() > 0);
	}

	public int add(int itemId, int amount) {
		if (contains(StockType.MAIN, itemId)) {
			return 0;
		}
		int index = getBestSlot(itemId);
		if (playerStock[index] != null) {
			long tAmt = (long) amount + playerStock[index].getAmount();
			if (tAmt > Integer.MAX_VALUE) {
				amount = Integer.MAX_VALUE - playerStock[index].getAmount();
			}
			playerStock[index] = playerStock[index].add(new Item(itemId, amount));
		} else {
			playerStock[index] = new Item(itemId, amount);
		}
		return amount;
	}

	public int getBestSlot(int itemId) {
		for (int i = 0; i < playerStock.length; i++) {
			if (playerStock[i] != null && playerStock[i].getId() == itemId) {
				return i;
			}
		}
		for (int i = 0; i < playerStock.length; i++) {
			if (playerStock[i] == null) {
				return i;
			}
		}
		return -1;
	}

	public int remove(int itemId, int amount) {
		int index = getItemIndex(StockType.PLAYER, itemId);
		if (index < 0) {
			return 0;
		}
		Item item = playerStock[index];
		if (amount >= item.getAmount()) {
			playerStock[index] = null;
			return item.getAmount();
		}
		playerStock[index] = new Item(itemId, item.getAmount() - amount);
		return amount;
	}

}
