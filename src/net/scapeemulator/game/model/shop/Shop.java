package net.scapeemulator.game.model.shop;

import net.scapeemulator.game.model.player.Item;

/**
 * Represents an in-game Shop with the following attributes: databaseId, name, shopId, mainStock.
 * (Disregarding child classes: This is a stockedShop which will not allow changes to the mainStock.
 * The mainStock will remain the same after both {@link remove(int int)} and {@link add(int, int)}.)
 * 
 * Note: This is open for extension and is therefor vague with its comments. 
 * If it states: a method will return true in a certain case, it will. 
 * All other cases are left open for either true or false (dynamic binding).
 * 
 * Recommended overrides when adding a certain stock:
 * {@link getItemAtIndex(StockType, int)}, {@link getItemIndex(StockType, int)}, {@link add(int, int)}, {@link remove(int int)}
 */
public class Shop {

    private final int databaseId;
    private final String name;
    private final int shopId;

    protected static final int AMOUNT_STOCK = 40;

    protected int[] mainStock = new int[AMOUNT_STOCK];

    /**
     * Creates a {@link Shop} with a certain databaseId, name and shopId. The
     * provided stockIds are used to fill up the stock of this {@link Shop}. The
     * rest of the stock is filled with -1.
     *
     * @param databaseId The databaseId for this {@link Shop}
     * @param name The name for this {@link Shop}.
     * @param shopId The id for this {@link Shop}.
     * @param stockIds The itemId's which are meant to be in this {@link Shop},
     * this shouldn't be more than this shop can hold, {@link AMOUNT_STOCK}.
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
     * Whether this {@link Shop} accepts the item associated with the provided
     * itemId.
     *
     * @param itemId The Id of the item we which to know about.
     * @return True if this {@link Shop} has the itemId in the
     * {@link StockType#MAIN} "stock".
     *
     */
    public boolean acceptsItem(int itemId) {
        return contains(StockType.MAIN, itemId);
    }

    /**
     * Whether or not the specified "stock" in this {@link Shop} contains the
     * provided id.
     *
     * @param stock The {@link StockType} to specify in which "stock" we check
     * for the id.
     * @param id The id to check for in the specified "stock".
     * @return True if this {@link Shop} has the id in the specified "stock".
     */
    public boolean contains(StockType stock, int id) {
        return getItemIndex(stock, id) >= 0;
    }

    /**
     * Gets the id of the item at the provided index in the specified stock.
     *
     * @param stock The {@link StockType} we go look in for an id.
     * @param index The index of the id we want to know.
     * @return The id of the item at the specified index in the specified stock
     * in this {@link Shop}. -1 if no real itemId resides in the index or if the
     * index is out of boundaries.
     */
    public int getItemAtIndex(StockType stock, int index) {
        if (index < 0 || !stock.equals(StockType.MAIN) || index >= mainStock.length) {
            return -1;
        }
        return mainStock[index];
    }

    /**
     * Gets the index of the item associated with this id in the specified
     * {@link StockType} in this {@link Shop}. Iterates trough the "stock"
     * associated with the {@link StockType} to find the index of the id.
     *
     * @param stock The {@link StockType} to specify in which "stock" we look.
     * @param id The id to find in the specified "stock".
     * @return The index of the id in the "stock" of this {@link Shop}. -1 if no
     * match was found.
     */
    public int getItemIndex(StockType stock, int id) {
        if (!stock.equals(StockType.MAIN)) {
            return -1;
        }

        for (int i = 0; i < mainStock.length; i++) {
            if (mainStock[i] == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets the array of itemId's this {@link Shop} has in the  {@link StockType#MAIN} "stock".
     * @return The array of itemId's in {@link StockType#MAIN} "stock".
     */
    public int[] getMainStock() {
        return mainStock;
    }
    
    /**
     * Gets the array of {@link Item} this {@link Shop} has in its specified stock. ({@link StockType})
     *
     * @param stock The {@link StockType} to return.
     * @return The array of Items in the specified "stock".
     * Returns null if there is no such "stock" for this {@link Shop}.
     * Returns null if stock.equals(StockType.MAIN}, use {@link getMainStock()}.
     */
    public Item[] getStock(StockType stock) {
        return null;
    }

    /**
     * Gets the Id of this {@link Shop}.
     * @return
     */
    public int getShopId() {
        return shopId;
    }
    
    /**
     * Gets the best slot for the specified itemId. This implies we seek a slot
     * with the same itemId, else the first empty slot is chosen.
     *
     * @param itemId The id of the item we are looking the best slot for.
     * @return The best slot for the itemId as described above. -1 if no slot
     * was found.
     */
    public int getBestSlot(int itemId) {
        int foundSlot = -1;

        for (int i = 0; i < mainStock.length; i++) {
            //First empty slot found
            if (foundSlot == -1 && mainStock[i] == -1) {
                foundSlot = i;
            }
            //Slot with same item found
            if (mainStock[i] == itemId) {
                return i;
            }
        }
        return foundSlot;
    }
    
    /**
     * Add a certain amount of the {@link Item} itemId to a "stock" of this {@link Shop}.
     * 
     * @param itemId The itemId to add an {@link Item} of.
     * @param amount The amount of the itemId to add.
     * @return The amount that was added to this {@link Shop}. 
     * If the itemId resides in the {@link StockType#Main} or amount {@literal <}= 0, 0 is returned.
     * In other cases the given amount may be adjusted to fit the {@link Shop}'s needs and or rules.
     */
    public int add(int itemId, int amount) {
        return 0;
    }
    
    /**
     * Removes a certain amount of the {@link Item} with the itemId out of a "stock" of this {@link Shop}.
     *
     * @param itemId The id of the item to remove.
     * @param amount The amount of the item we attempt to remove.
     * @return The amount of items of the itemId removed in this {@link Shop}}. 
     * If the itemId isn't in this {@link Shop} or the amount is less than or equal to 0, 0 is returned.
     * In all other cases the given amount to remove may be adjusted to fit the {@link Shop}'s needs and or rules.
     */
    public int remove(int itemId, int amount) {
        return 0;
    }

}
