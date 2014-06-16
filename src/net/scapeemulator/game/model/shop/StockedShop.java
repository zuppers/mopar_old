package net.scapeemulator.game.model.shop;

/**
 * Represents a type of {@link Shop} which only has a {@link StockType#MAIN}
 * "stock". The itemId's accepted are therefor only itemId's the shop has.
 */
public class StockedShop extends Shop {

    /**
     * Creates a {@link StockedShop} with a certain databaseId, name and shopId.
     * The provided stockIds are used to fill up the stock of this as {@link Shop#Shop(int, java.lang.String, int, int[])} specifies.
     * 
     * @param databaseId The databaseId for this {@link StockedShop}
     * @param name The name for this {@link StockedShop}.
     * @param shopId The id for this {@link StockedShop}.
     * @param stockIds The itemId's which are meant to be in this {@link StockedShop}, this shouldn't be more than this shop can hold, {@link Shop#AMOUNT_STOCK}.
     */
    public StockedShop(int databaseId, String name, int shopId, int[] stockIds) {
        super(databaseId, name, shopId, stockIds);
    }

    /**
     * @return True if the {@link StockType#MAIN} "stock" contains the itemId.
     */
    @Override
    public boolean acceptsItem(int itemId) {
        return contains(StockType.MAIN, itemId);
    }

}
