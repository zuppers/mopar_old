package net.scapeemulator.game.model.shop;

import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.player.Item;

/**
 * Represents a type of {@link Shop} which has a {@link StockType#MAIN} and a
 * {@link StockType#PLAYER} "stock". This shop accepts most items
 * ({@link acceptsItem(int)}). 
 * When an item added/removed is in the {@link StockType#MAIN} "stock" won't be effected.
 * When an item added/removed is in the {@link StockType#PLAYER}, the amount will increase/decrease.
 */
public class GeneralStore extends Shop {

    protected Item[] playerStock = new Item[Shop.AMOUNT_STOCK];

    /**
     * Creates a {@link GeneralStore} with a certain databaseId, name, shopId.
     * The provided stockIds are used to fill up the stock of this as
     * {@link Shop#Shop(int, java.lang.String, int, int[])} specifies. The
     * {@link StockyType#PLAYER} "stock" gets initialized as well with a size
     * specified by {@link Shop#AMOUNT_STOCK}.
     *
     * @param databaseId The databaseId for this {@link Shop}
     * @param name The name for this {@link Shop}.
     * @param shopId The id for this {@link Shop}.
     * @param stockIds The itemId's which are meant to be in this {@link Shop},
     * this shouldn't be more than this shop can hold, {@link AMOUNT_STOCK}.
     */
    public GeneralStore(int databaseId, String name, int shopId, int[] stockIds) {
        super(databaseId, name, shopId, stockIds);
    }

    /**
     * @return True if {@link StockType#Main} "stock" contains the itemId OR
     * (getBestSlot(itemId) > -1 and the Item is tradeable).
     * @see getBestSlot(int)
     */
    @Override
    public boolean acceptsItem(int itemId) {
        //TODO replace getValue() > 0 with a tradable check
        return contains(StockType.MAIN, itemId) || (getBestSlot(itemId) > -1 && ItemDefinitions.forId(itemId).getValue() > 0);
    }

    /**
     *
     * @param stock The {@link StockType} we go look in for an id.
     * @param index The index of the id we want to know.
     * @return The id of the item at the specified index in the specified stock
     * in this {@link Shop}. -1 if no real itemId resides in the index or if the
     * index is out of boundaries.
     */
    @Override
    public int getItemAtIndex(StockType stock, int index) {
        switch (stock) {
            case MAIN:
                return super.getItemAtIndex(stock, index);
            case PLAYER:
                if (index < 0 || index >= playerStock.length) {
                    return -1;
                }
                return playerStock[index].getId();
        }
        return -1;
    }

    /**
     *
     * @param stock The {@link StockType} to specify in which "stock" we look.
     * @param id The id to find in the specified "stock".
     * @return The index of the id in the "stock" of this {@link Shop}. -1 if no
     * match was found.
     */
    @Override
    public int getItemIndex(StockType stock, int id) {
        switch (stock) {
            case MAIN:
                super.getItemIndex(stock, id);
            case PLAYER:
                for (int i = 0; i < playerStock.length; i++) {
                    if (playerStock[i] != null && playerStock[i].getId() == id) {
                        return i;
                    }
                }
        }
        return -1;
    }
    
    /**
     *
     * @param stock The {@link StockType} to return.
     * @return The array of Items in the specified "stock".
     * Returns null if there is no such "stock" for this {@link Shop}.
     * Returns null if stock.equals(StockType.MAIN}, use {@link getMainStock()}.
     */
    @Override
    public Item[] getStock(StockType stock) {
        if(stock.equals(StockType.PLAYER)) {
            return playerStock;
        }
        return null;
    }
    
    /**
     *
     * @param itemId The id of the item we are looking the best slot for.
     * @return The best slot for the itemId as described above. -1 if no slot was found. 
     * Note: This only searches the best slot in the playerStock. For mainStock use super.getBestSlot(int) ({@link Shop#getBestSlot(int)}).
     */
    @Override
    public int getBestSlot(int itemId) {
        int foundSlot = -1;

        for (int i = 0; i < playerStock.length; i++) {
            //First empty slot found
            if (foundSlot == -1 && playerStock[i] == null) {
                foundSlot = i;
            }
            //Slot with same item found
            if (playerStock[i] != null && playerStock[i].getId() == itemId) {
                return i;
            }
        }
        return foundSlot;
    }

    /**
     * Add a certain amount of the {@link Item} itemId to a "stock" of this shop.
     * 
     * @param itemId The itemId to add an {@link Item} of.
     * @param amount The amount of the itemId to add.
     * @return The amount that was added to this {@link Shop}. 
     * If the itemId resides in the {@link StockType#Main} or amount {@literal <}= 0, 0 is returned.
     */
    @Override
    public int add(int itemId, int amount) {
        if (contains(StockType.MAIN, itemId) || amount <= 0) {
            return 0;
        }
        int index = getBestSlot(itemId);
        if (index <= -1) {
            return 0;
        }

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

    /**
     * Removes the specified amount of the {@link Item} with the itemId out of
     * the {@link StockType#PLAYER} "stock" of this shop.
     *
     * @param itemId The id of the item to remove.
     * @param amount The amount of the item to remove.
     * @return The amount of items of the itemId removed in this {
     * @GeneralStore}. If the itemId isn't in this {@link Shop} or the amount is
     * less than or equal to 0, 0 is returned.
     */
    @Override
    public int remove(int itemId, int amount) {
        int index = getItemIndex(StockType.PLAYER, itemId);
        if (index < 0 || amount <= 0) {
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
