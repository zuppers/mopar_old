package net.scapeemulator.game.model.shop;

import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.player.Item;

/**
 * Represents an in-game Shop with a certain databaseId, name & shopId.
 * A {@link Shop} can have a main stock with a (in)finite supply.
 * A {@link Shop} can have a player stock.
 * 
 */
public class Shop {

    private final int databaseId;
    private final String name;
    private final int shopId;

    protected static final int AMOUNT_STOCK = 40;

    private Item[] mainStock;
    private final boolean isFinite;

    protected Item[] playerStock;

    /**
     * Creates a {@link Shop} with a certain databaseId, name and shopId. The
     * provided stockIds are used to fill up the stock of this {@link Shop}
     * along with the stockAmounts. The rest of the stock is filled with -1. If
     * stockAmounts contains less elements than stockIds, amount 1 is further
     * used instead.
     *
     * @param databaseId The databaseId for this {@link Shop}
     * @param name The name for this {@link Shop}.
     * @param shopId The id for this {@link Shop}.
     * @param isFinite Whether the main stock of this {@link Shop} is finite.
     * @param hasPlayerStock Whether this {@link Shop} has a player stock.
     * @param stockIds The itemId's which are meant to be in this {@link Shop},
     * this shouldn't be more than this shop can hold, {@link AMOUNT_STOCK}.
     * @param stockAmounts The amount for each itemId this {@link Shop} has in
     * its main stock.
     */
    public Shop(int databaseId, String name, int shopId, boolean isFinite, boolean hasPlayerStock, int[] stockIds, int[] stockAmounts) {
        this.databaseId = databaseId;
        this.name = name;
        this.shopId = shopId;

        this.isFinite = isFinite;
        mainStock = new Item[AMOUNT_STOCK];
        if (hasPlayerStock) {
            playerStock = new Item[AMOUNT_STOCK];
        }

        for (int i = 0; i < mainStock.length && i < stockIds.length; i++) {
            int amount = (i >= stockAmounts.length) ? 1 : stockAmounts[i];
            mainStock[i] = new Item(stockIds[i], amount);
        }
    }

    /**
     * Creates a {@link Shop} with a certain databaseId, name and shopId. The
     * provided stockIds are used to fill up the stock of this {@link Shop} with
     * amount being 1. The rest of the stock is filled with -1.
     *
     * @param databaseId The databaseId for this {@link Shop}
     * @param name The name for this {@link Shop}.
     * @param shopId The id for this {@link Shop}.
     * @param isFinite Whether the main stock of this {@link Shop} is finite.
     * @param hasPlayerStock Whether this {@link Shop} has a player stock.
     * @param stockIds The itemId's which are meant to be in this {@link Shop},
     * this shouldn't be more than this shop can hold, {@link AMOUNT_STOCK}.
     */
    public Shop(int databaseId, String name, int shopId, boolean isFinite, boolean hasPlayerStock, int[] stockIds) {
        this(databaseId, name, shopId, isFinite, hasPlayerStock, stockIds, new int[0]);
    }

    /**
     * Creates a {@link Shop} with a certain databaseId, name and shopId. The
     * provided mainStock is used to fill the main stock of this
     * {@link Shop}link Shop}.
     *
     * @param databaseId The databaseId for this {@link Shop}
     * @param name The name for this {@link Shop}.
     * @param shopId The id for this {@link Shop}.
     * @param isFinite Whether the main stock of this {@link Shop} is finite.
     * @param hasPlayerStock Whether this {@link Shop} has a player stock.
     * @param mainStock The itemsInStock used for the mainStock. A copy of this
     * array will be used, up to {@link AMOUNT_STOCK} elements maximum.
     */
    public Shop(int databaseId, String name, int shopId, boolean isFinite, boolean hasPlayerStock, Item[] mainStock) {
        this.databaseId = databaseId;
        this.name = name;
        this.shopId = shopId;

        this.isFinite = isFinite;
        mainStock = new Item[AMOUNT_STOCK];
        if (hasPlayerStock) {
            playerStock = new Item[AMOUNT_STOCK];
        }

        System.arraycopy(mainStock, 0, this.mainStock, 0, Math.min(this.mainStock.length, mainStock.length));
    }

    /**
     * Creates a very basic, empty {@link Shop} with the main stock disabled.
     *
     * @param databaseId The databaseId for this {@link Shop}
     * @param name The name for this {@link Shop}.
     * @param shopId The id for this {@link Shop}.
     */
    public Shop(int databaseId, String name, int shopId) {
        this.databaseId = databaseId;
        this.name = name;
        this.shopId = shopId;
        this.isFinite = true; // Doesn't matter, no main stock.
        this.playerStock = new Item[AMOUNT_STOCK];
    }

    /**
     * Gets the databaseId of this {@link Shop}.
     * @return
     */
    public int getDatabaseId() {
        return this.databaseId;
    }

    /**
     * Gets the name of this {@link Shop}.
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the Id of this {@link Shop}.
     * @return
     */
    public int getShopId() {
        return this.shopId;
    }

    /**
     * Gets whether this {@link Shop} its main stock is a finite supply or not.
     * @return
     */
    public boolean isMainFinite() {
        return this.isFinite;
    }

    /**
     * Gets whether this {@link Shop} has the stock specified.
     *
     * @param stock The {@link StockType} specifies which "stock" we want the
     * information of.
     * @return True if such stock exists in this {@link Shop}.
     */
    public boolean hasStock(StockType stock) {
        switch (stock) {
            case MAIN:
                return this.mainStock != null;
            case PLAYER:
                return this.playerStock != null;
        }
        return false;
    }

    /**
     * Gets the specified "stock".
     *
     * @param stock The {@link StockType} that has to be returned.
     * @return The Item[] used as the specified "stock". null if that "stock"
     * isn't used.
     */
    public Item[] getStock(StockType stock) {
        switch (stock) {
            case MAIN:
                return this.mainStock;
            case PLAYER:
                return this.playerStock;
        }
        return null;
    }

    /**
     * Whether this {@link Shop} accepts the item associated with the provided
     * itemId.
     *
     * @param itemId The Id of the item we which to know about.
     * @return True if the {@link StockType#MAIN} "stock" contains the itemId.
     * True if this {@link Shop} has a {@link StockType#PLAYER} "stock" and it
     * can be sold in there. This means {@link getBestSlot(int)} > -1 and the
     * {@link Item} associated with the itemId is tradeable.
     */
    public boolean acceptsItem(int itemId) {
        if (contains(StockType.MAIN, itemId)) {
            return true;
        }
        if (hasStock(StockType.PLAYER)) {
            //TODO replace getValue() > 0 with a tradable check
            return (getBestSlot(StockType.PLAYER, itemId) > -1 && ItemDefinitions.forId(itemId).getValue() > 0);
        }
        return false;
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
     * Gets the {@link Item} at the provided index in the specified stock.
     *
     * @param stock The {@link StockType} we go look in for the {@link Item}.
     * @param index The index of the {@link Item} we want to retrieve.
     * @return The {@link Item} at the specified index in the specified stock in
     * this {@link Shop}. null if no item resides in the index or if the index
     * is out of boundaries or the stock isn't in use.
     */
    public Item getItemAtIndex(StockType stock, int index) {
        if (!hasStock(stock) || index < 0 || index >= getStock(stock).length) {
            return null;
        }
        switch (stock) {
            case MAIN:
                return mainStock[index];
            case PLAYER:
                return playerStock[index];
        }
        return null;
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
        if (!hasStock(stock) || id <= -1) {
            return -1;
        }
        Item[] items = getStock(stock);

        for (int i = 0; i < items.length; i++) {
            if (items[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets the best slot for the specified itemId in the "stock". This implies
     * we seek a slot with the same itemId, else the first empty slot is chosen.
     *
     * @param stock The {@link StockType} to find the best slot in.
     * @param itemId The {@link Item}'s id to find the best slot for.
     * @return The best slot for the itemId as described above. -1 if no slot
     * was found, itemId {@literal <}= -1 or !{@link hasStock(StockType)}.
     */
    public int getBestSlot(StockType stock, int itemId) {
        if (!hasStock(stock) || itemId <= -1) {
            return -1;
        }

        int foundSlot = -1;
        Item[] itemsInStock = getStock(stock);
        for (int i = 0; i < itemsInStock.length; i++) {
            //Slot with same item found
            if (itemsInStock[i] != null && itemsInStock[i].getId() == itemId) {
                return i;
            }

            //First empty slot found
            if (foundSlot == -1 && itemsInStock[i] == null) {
                foundSlot = i;
            }
        }
        return foundSlot;
    }

    /**
     * Removes the specified amount of the {@link Item} with the itemId out of this {@link Shop}.
     * If {@link StockType#MAIN} contains the itemId, the item will be removed from
     * the {@link StockType#MAIN} "stock", else the {@link StockType#PLAYER}
     * "stock".
     * 
     * @param itemId The id of the item to remove.
     * @param amount The amount of the item to remove.
     * @return The amount of itemsInStock of the itemId removed in this {@link Shop}.
     * @see remove(StockType, int, int)
     */
    public int remove(int itemId, int amount) {
        if (contains(StockType.MAIN, itemId)) {
            return remove(StockType.MAIN, itemId, amount);
        } else {
            return remove(StockType.PLAYER, itemId, amount);
        }
    }
    
    /**
     * Removes the specified amount of the {@link Item} with the itemId out of
     * the specified "stock" of this shop.
     *
     * @param stock The stock of which we remove the item.
     * @param itemId The id of the item to remove.
     * @param amount The amount of the item to remove.
     * @return The amount of itemsInStock of the itemId removed in this
     * {@link Shop}. If the itemId isn't in this {@link Shop} or the amount is
     * {@literal <}= 0, 0 is returned. If
     * stock.equals({@link StockType#MAIN}) && !{@link isMainFinite()}, 0 is
     * returned as well as when the stock specified isn't used by this
     * {@link Shop}.
     */
    public int remove(StockType stock, int itemId, int amount) {
        int index = getItemIndex(stock, itemId);
        if (!hasStock(stock) || index < 0 || amount <= 0 || stock.equals(StockType.MAIN) && !isMainFinite()) {
            return 0;
        }
        Item[] items = getStock(stock);
        Item item = items[index];

        if (amount >= item.getAmount()) {
            items[index] = null;
            return item.getAmount();
        }

        items[index] = new Item(itemId, item.getAmount() - amount);
        return amount;
    }

    /**
     * Add the specified amount of the {@link Item} itemId to this {@link Shop}.
     * If {@link StockType#MAIN} contains the itemId, the item will be added to
     * the {@link StockType#MAIN} "stock", else the {@link StockType#PLAYER}
     * "stock".
     *
     * @param itemId The itemId to add an {@link Item} of.
     * @param amount The amount of the {@link Item} to add.
     * @return The amount added to the {@link Shop}.
     * @see add(StockType, int, int)
     */
    public int add(int itemId, int amount) {
        if (contains(StockType.MAIN, itemId)) {
            return add(StockType.MAIN, itemId, amount);
        } else {
            return add(StockType.PLAYER, itemId, amount);
        }
    }

    /**
     * Add the specified amount of the {@link Item} itemId to the specified
     * "stock" of this {@link Shop}.
     *
     * @param stock The {@link StockType} that specifies which stock to add this
     * {@link Item} in.
     * @param itemId The itemId to add an {@link Item} of.
     * @param amount The amount of the {@link Item} to add.
     * @return The amount that was added to this {@link Shop}. If this
     * {@link Shop} does not have the stock specified or the amount
     * {@literal <}= 0, 0 is returned. If
     * stock.equals({@link StockType#MAIN}) && !{@link isMainFinite()}, 0 is
     * returned. If there is no slot available to add to in this "stock", 0 is
     * returned as well. If the amount to add is too high, the amount is scaled
     * down to Integer.MAX_VALUE.
     */
    public int add(StockType stock, int itemId, int amount) {
        if (!hasStock(stock) || (stock.equals(StockType.MAIN) && !isMainFinite()) || amount <= 0) {
            return 0;
        }
        int index = getBestSlot(stock, itemId);
        if (index <= -1) {
            return 0;
        }

        Item[] itemsInStock = getStock(stock);
        if (itemsInStock[index] != null) {
            long totalAmount = amount + itemsInStock[index].getAmount();
            if (totalAmount > Integer.MAX_VALUE) {
                amount = Integer.MAX_VALUE - itemsInStock[index].getAmount();
            }
            itemsInStock[index] = itemsInStock[index].add(new Item(itemId, amount));
        } else {
            itemsInStock[index] = new Item(itemId, amount);
        }
        return amount;
    }

}
