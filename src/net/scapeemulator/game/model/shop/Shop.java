package net.scapeemulator.game.model.shop;

import net.scapeemulator.game.model.player.Item;

/**
 * Represents an in-game Shop with the following attributes:
 * databaseId, name, shopId, mainStock, playerStock.
 * NOTE: The playerStock is NOT initialized by default.
 */
public abstract class Shop {

    private final int databaseId;
    private final String name;
    private final int shopId;
    
    protected static final int AMOUNT_STOCK = 40;
    
    protected int[] mainStock = new int[AMOUNT_STOCK];
    protected Item[] playerStock;
    
    /**
     * Creates a {@link Shop} with a certain databaseId, name and shopId.
     * The provided stockIds are used to fill up the stock of this {@link Shop}.
     * The rest of the stock is filled with -1.
     * 
     * @param databaseId The databaseId for this {@link Shop}
     * @param name The name for this {@link Shop}.
     * @param shopId The id for this {@link Shop}.
     * @param stockIds The itemId's which are meant to be in this {@link Shop}, this shouldn't be more than this shop can hold, {@link AMOUNT_STOCK}.
     */
    public Shop(int databaseId, String name, int shopId, int[] stockIds) {
        this.databaseId = databaseId;
        this.name = name;
        this.shopId = shopId;
        
        System.arraycopy(stockIds, 0, mainStock, 0, stockIds.length);
        for (int i = stockIds.length; i < mainStock.length; i++) {
            mainStock[i] = -1;
        }
    }

    /**
     * Whether this {@link Shop} accepts the item associated with the provided itemId.
     * @param itemId The Id of the item we which to know about.
     * @return True if this {@link Shop} accepts the Item.
     */
    public abstract boolean acceptsItem(int itemId);

    /**
     * Whether or not the specified "stock" in this {@link Shop} contains the provided id.
     * @param stock The {@link StockType} to specify in which "stock" we check for the id.
     * @param id The id to check for in the specified "stock".
     * @return True if this {@link Shop} has the id in the specified "stock".
     */
    public boolean contains(StockType stock, int id) {
        return getItemIndex(stock, id) >= 0;
    }

    /**
     * Gets the id of the item at the provided index in the specified stock.
     * @param stock The {@link StockType} we go look in for an id.
     * @param index The index of the id we want to know.
     * @return The id of the item at the specified index in the specified stock in this {@link Shop}. -1 if no real itemId resides in the index or if the index is out of boundaries.
     */
    public int getItemAtIndex(StockType stock, int index) {
        if (index < 0 || index >= AMOUNT_STOCK) {
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

    /**
     * Gets the index of the item associated with this id in the specified {@link StockType} in this {@link Shop}.
     * Iterates trough the "stock" associated with the {@link StockType} to find the index of the id.
     * 
     * @param stock The {@link StockType} to specify in which "stock" we look.
     * @param id The id to find in the specified "stock".
     * @return The index of the id in the "stock" of this {@link Shop}. -1 if no match was found.
     */
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

    /**
     * Gets the array of {@link Item} this {@link Shop} has in its {@link Player} stock. ({@link StockType#PLAYER})
     * @return 
     */
    public Item[] getPlayerStock() {
        return playerStock;
    }

    /**
     * Gets the Id of this {@link Shop}.
     * @return 
     */
    public int getShopId() {
        return shopId;
    }

}
