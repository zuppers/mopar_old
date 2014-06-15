package net.scapeemulator.game.model.shop;

import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.player.Item;

/**
 * Represents a type of {@link Shop} which has a {@link StockType#MAIN} and a
 * {@link StockType#PLAYER} "stock". This shop accepts most items
 * ({@link acceptsItem(int)}).
 */
public class GeneralStore extends Shop {

    /**
     * Creates a {@link GeneralStore} with a certain databaseId, name, shopId.
     * The provided stockIds are used to fill up the stock of this as {@link Shop#Shop(int, java.lang.String, int, int[])} specifies.
     * The {@link StockyType#PLAYER} "stock" gets initialized as well with a size specified by {@link Shop#AMOUNT_STOCK}.
     * 
     * @param databaseId The databaseId for this {@link Shop}
     * @param name The name for this {@link Shop}.
     * @param shopId The id for this {@link Shop}.
     * @param stockIds The itemId's which are meant to be in this {@link Shop}, this shouldn't be more than this shop can hold, {@link AMOUNT_STOCK}.
     */
    public GeneralStore(int databaseId, String name, int shopId, int[] stockIds) {
        super(databaseId, name, shopId, stockIds);
        playerStock = new Item[Shop.AMOUNT_STOCK];
    }

    /**
     * @return True if {@link StockType#Main} "stock" contains the itemId OR (getBestSlot(itemId) > -1 and the Item is tradeable).
     * @see getBestSlot(int)
     */
    @Override
    public boolean acceptsItem(int itemId) {
        //TODO replace getValue() > 0 with a tradable check
        return contains(StockType.MAIN, itemId) || (getBestSlot(itemId) > -1 && ItemDefinitions.forId(itemId).getValue() > 0);
    }

    /**
     * Add the specified amount of the {@link Item} itemId to the {@link StockType#PLAYER} "stock" of this shop.
     * @param itemId The itemId to add an {@link Item} of.
     * @param amount The amount of the itemId to add.
     * @return The amount that was added to this {@link Shop}.
     * If the itemId resides in the {@link StockType#Main} or amount {@literal <}= 0, 0 is returned.
     * If no slot was found 0 is returned.
     * If the amount + oldAmount > Integer.MAX_VALUE, the amount is scaled to Integer.MAX_VALUE - oldAmount.
     */
    public int add(int itemId, int amount) {
        if (contains(StockType.MAIN, itemId) || amount <= 0) {
            return 0;
        }
        int index = getBestSlot(itemId);
        if(index <= -1) return 0;
        
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
     * Gets the best slot for the specified itemId.
     * This implies we seek a slot with the same itemId, else the first empty slot is chosen.
     * @param itemId
     * @return The best slot for the itemId as described above. -1 if no slot was found.
     */
    public int getBestSlot(int itemId) {
        int foundSlot = -1;
        
        for (int i = 0; i < playerStock.length; i++) {
            //First empty slot found
            if(foundSlot != -1 && playerStock[i] == null) {
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
     * Removes the specified amount of the {@link Item} with the itemId out of
     * the {@link StockType#PLAYER} "stock" of this shop.
     *
     * @param itemId The id of the item to remove.
     * @param amount The amount of the item to remove.
     * @return The amount of items of the itemId removed in this {@GeneralStore}. 
     * If the itemId isn't in this {@link Shop} or the amount is less than or equal to 0, 0 is returned.
     */
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
