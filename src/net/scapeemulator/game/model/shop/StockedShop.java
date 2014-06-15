package net.scapeemulator.game.model.shop;

public class StockedShop extends Shop {
	
	public StockedShop(int databaseId, String name, int shopId, int[] stockIds) {
		super(databaseId, name, shopId, stockIds);
	}

	@Override
	public boolean acceptsItem(int itemId) {
		return contains(StockType.MAIN, itemId);
	}
	
}
