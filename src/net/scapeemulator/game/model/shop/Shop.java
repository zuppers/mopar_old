package net.scapeemulator.game.model.shop;

import net.scapeemulator.game.model.player.Item;

public abstract class Shop {

	private final int databaseId;
	private final String name;
	private final int shopId;
	
	protected int[] mainStock = new int[40];
	protected Item[] playerStock;

	public Shop(int databaseId, String name, int shopId, int[] stockIds) {
		this.databaseId = databaseId;
		this.name = name;
		this.shopId = shopId;
		for(int i = 0; i < stockIds.length; i++) {
			mainStock[i] = stockIds[i];
		}
		for(int i = stockIds.length; i < mainStock.length; i++) {
			mainStock[i] = -1;
		}
	}
	
	public abstract boolean acceptsItem(int itemId);
	
	public boolean contains(StockType stock, int id) {
		return getItemIndex(stock, id) >= 0;
	}

	public int getItemAtIndex(StockType stock, int index) {
		if (index < 0 || index >= 40) {
			return -1;
		}
		switch (stock) {
		case MAIN:
			return mainStock[index];
		case PLAYER:
			return playerStock[index].getId();
		}
		return -1;
	}

	public int getItemIndex(StockType stock, int id) {
		switch (stock) {
		case MAIN:
			for (int i = 0; i < mainStock.length; i++) {
				if (mainStock[i] == id) {
					return i;
				}
			}
			break;
		case PLAYER:
			for (int i = 0; i < playerStock.length; i++) {
				if (playerStock[i] != null && playerStock[i].getId() == id) {
					return i;
				}
			}
			break;
		}
		return -1;
	}

	public Item[] getPlayerStock() {
		return playerStock;
	}
	
	public int getShopId() {
		return shopId;
	}

}