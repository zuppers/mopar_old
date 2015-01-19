package net.scapeemulator.game.model.player.inventory;

import java.util.ArrayList;
import java.util.List;

import net.scapeemulator.cache.def.ItemDefinition;
import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.grounditem.GroundItemList;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.util.math.BasicMath;

public final class Inventory {

    public enum StackMode {
        ALWAYS, STACKABLE_ONLY;
    }

    private final Player player;
    private final StackMode stackMode;
    private final Item[] items;
    private final boolean weighted;
    private final List<InventoryListener> listeners = new ArrayList<>();

    /**
     * The total weight of this inventory in grams.
     */
    private int weight;

    /**
     * If the inventory is locked, the items cannot be modified in any way.
     */
    private boolean locked;

    /**
     * If the inventory is silent, the listeners are not called.
     */
    private boolean silent;

    public Inventory(Player player, int slots) {
        this(player, slots, StackMode.STACKABLE_ONLY, true);
    }

    public Inventory(Player player, int slots, StackMode stackMode, boolean weighted) {
        this.player = player;
        this.stackMode = stackMode;
        this.items = new Item[slots];
        this.weighted = weighted;
    }

    public Inventory(Inventory inventory) {
        this.stackMode = inventory.stackMode;
        this.items = inventory.toArray();
        this.player = inventory.player;
        this.weight = inventory.weight;
        this.weighted = inventory.weighted;
    }

    public Item[] toArray() {
        Item[] array = new Item[items.length];
        System.arraycopy(items, 0, array, 0, items.length);
        return array;
    }

    public void addListener(InventoryListener listener) {
        listeners.add(listener);
    }

    public void removeListener(InventoryListener listener) {
        listeners.remove(listener);
    }

    public void removeListeners() {
        listeners.clear();
    }

    public void refresh() {
        fireItemsChanged();
    }

    public Item get(int slot) {
        checkSlot(slot);
        return items[slot];
    }

    /**
     * Sets the item at the specified slot to the specified item.
     * 
     * @param slot the slot id to set
     * @param item the item to set
     * @return the item that was in the slot before changing it, if any
     */
    public Item set(int slot, Item item) {
        if (locked) {
            return null;
        }
        checkSlot(slot);
        if (weighted) {
            if (items[slot] != null) {
                weight -= items[slot].getDefinition().getWeight();
            }
            if (item != null) {
                weight += item.getDefinition().getWeight();
            }
        }
        Item old = items[slot];
        items[slot] = item;
        fireItemChanged(slot, old);
        return old;
    }

    /**
     * Swaps the position of two items in the inventory.
     * 
     * @param slot1 original slot
     * @param slot2 destination slot
     */
    public void swap(int slot1, int slot2) {
        if (locked) {
            return;
        }
        checkSlot(slot1);
        checkSlot(slot2);

        Item tmp = items[slot1];
        items[slot1] = items[slot2];
        items[slot2] = tmp;

        fireItemChanged(slot1, items[slot2]);
        fireItemChanged(slot2, items[slot1]);
    }

    public Item add(Item item) {
        return add(item, -1);
    }

    public Item add(Item item, int preferredSlot) {
        if (locked) {
            return item;
        }
        int id = item.getId();
        boolean stackable = isStackable(item);
        if (stackable) {

            /* try to add this item to an existing stack */
            int slot = slotOf(id);
            if (slot != -1) {
                Item other = items[slot];
                int amount;

                /* check if there are too many items in the stack */
                int overflow = BasicMath.integerOverflow(other.getAmount(), item.getAmount());
                Item remaining = null;
                if (overflow != 0) {
                    amount = Integer.MAX_VALUE;
                    remaining = new Item(id, overflow);
                    fireCapacityExceeded();
                } else {
                    amount = other.getAmount() + item.getAmount();
                }

                /* update stack and return any remaining items */
                set(slot, new Item(item.getId(), amount));
                return remaining;
            }

            /* try to add this item to the preferred slot */
            if (preferredSlot != -1) {
                checkSlot(preferredSlot);
                if (items[preferredSlot] == null) {
                    set(preferredSlot, item);
                    return null;
                }
            }

            /* try to add this item to any slot */
            for (slot = 0; slot < items.length; slot++) {
                if (items[slot] == null) {
                    set(slot, item);
                    return null;
                }
            }

            /* give up */
            fireCapacityExceeded();
            return item;
        } else {
            final Item single = new Item(id, 1);
            int remaining = item.getAmount();

            if (remaining == 0)
                return null;

            /* try to first place item at the preferred slot */
            if (preferredSlot != -1) {
                checkSlot(preferredSlot);
                if (items[preferredSlot] == null) {
                    set(preferredSlot, single);
                    remaining--;
                }
            }

            if (remaining == 0)
                return null;

            /* place any subsequent remaining items wherever space is available */
            for (int slot = 0; slot < items.length; slot++) {
                if (items[slot] == null) {
                    set(slot, single);
                    remaining--;
                }

                if (remaining == 0)
                    return null;
            }

            /* give up */
            fireCapacityExceeded();
            return new Item(id, remaining);
        }
    }

    public Item remove(SlottedItem item) {
        return remove(item.getItem(), item.getSlot());
    }

    public Item remove(Item item) {
        return remove(item, -1);
    }

    /**
     * @param item
     * @param preferredSlot
     * @return Item with amount value being the number actually removed
     */
    public Item remove(Item item, int preferredSlot) {
        if (locked) {
            return null;
        }
        int id = item.getId();
        boolean stackable = isStackable(item);

        if (stackable) {
            /* try to remove this item from its stack */
            int slot = slotOf(id);
            if (slot != -1) {
                Item other = items[slot];
                if (other.getAmount() <= item.getAmount()) {
                    set(slot, null);
                    return new Item(id, other.getAmount());
                } else {
                    other = new Item(id, other.getAmount() - item.getAmount());
                    set(slot, other);
                    return item;
                }
            }
            return null;
        } else {
            int removed = 0;

            /* try to remove the item from the preferred slot first */
            if (preferredSlot != -1) {
                checkSlot(preferredSlot);
                if (items[preferredSlot] != null && items[preferredSlot].getId() == id) {
                    set(preferredSlot, null);

                    if (++removed >= item.getAmount())
                        return new Item(id, removed);
                }
            }

            /* try other slots */
            for (int slot = 0; slot < items.length; slot++) {
                Item other = items[slot];
                if (other != null && other.getId() == id) {
                    set(slot, null);

                    if (++removed >= item.getAmount())
                        return new Item(id, removed);
                }
            }

            return removed == 0 ? null : new Item(id, removed);
        }
    }

    public void shift() {
        if (locked) {
            return;
        }
        int destSlot = 0;

        for (int slot = 0; slot < items.length; slot++) {
            Item item = items[slot];
            if (item != null) {
                items[destSlot++] = item;
            }
        }

        for (int slot = destSlot; slot < items.length; slot++)
            items[slot] = null;

        fireItemsChanged();
    }

    public void empty() {
        if (locked) {
            return;
        }
        for (int slot = 0; slot < items.length; slot++)
            items[slot] = null;

        fireItemsChanged();
    }

    public boolean isEmpty() {
        for (int slot = 0; slot < items.length; slot++)
            if (items[slot] != null)
                return false;

        return true;
    }

    public int freeSlot() {
        for (int slot = 0; slot < items.length; slot++) {
            if (items[slot] == null) {
                return slot;
            }
        }
        return -1;
    }

    public int freeSlots() {
        int slots = 0;
        for (int slot = 0; slot < items.length; slot++)
            if (items[slot] == null)
                slots++;

        return slots;
    }

    public int slotOf(int id) {
        for (int slot = 0; slot < items.length; slot++) {
            Item item = items[slot];
            if (item != null && item.getId() == id)
                return slot;
        }

        return -1;
    }

    public int getAmount(int id) {
        int amount = 0;
        for (Item item : items) {
            if (item == null)
                continue;
            if (item.getId() == id) {
                amount += item.getAmount();
            }
        }
        return amount;
    }

    public int getAmountNotedAndUnnoted(int id) {
        ItemDefinition def = ItemDefinitions.forId(id);
        int unnoted = def.getUnnotedItemId();
        int noted = def.getNotedItemId();
        if (unnoted != noted) {
            return getAmount(unnoted) + getAmount(noted);
        }
        return getAmount(id);
    }

    public boolean contains(int id) {
        return slotOf(id) != -1;
    }

    public boolean contains(int id, int amount) {
        return getAmount(id) >= amount;
    }

    public boolean contains(Item item) {
        return getAmount(item.getId()) >= item.getAmount();
    }

    /**
     * Removes all given items from this inventory.
     * 
     * @param items the items to remove
     * @return a collection of items that were NOT removed, empty means the operation completed
     *         successfully
     */
    public List<Item> removeAll(Item... items) {
        List<Item> notRemoved = new ArrayList<>();
        for (Item toRemove : items) {
            if (toRemove != null) {
                Item removed = remove(toRemove);
                if (!removed.equals(toRemove)) {
                    notRemoved.add(new Item(toRemove.getId(), toRemove.getAmount() - removed.getAmount()));
                }
            }
        }
        return notRemoved;
    }

    /**
     * Adds all given items to this inventory.
     * 
     * @param items the items to add
     * @return a collection of items that were NOT added, empty means the operation completed
     *         successfully
     */
    public List<Item> addAll(Item... items) {
        List<Item> notAdded = new ArrayList<>();
        for (Item toAdd : items) {
            if (toAdd != null) {
                Item overflow = add(toAdd);
                if (overflow != null) {
                    notAdded.add(overflow);
                }
            }
        }
        return notAdded;
    }

    private void fireItemChanged(int slot, Item oldItem) {
        if (!silent) {
            for (InventoryListener listener : listeners)
                listener.itemChanged(this, slot, items[slot], oldItem);
        }
    }

    private void fireItemsChanged() {
        if (weighted) {
            weight = 0;
            for (Item item : items) {
                if (item != null) {
                    weight += item.getDefinition().getWeight();
                }
            }
        }
        if (!silent) {
            for (InventoryListener listener : listeners)
                listener.itemsChanged(this);
        }
    }

    public void fireCapacityExceeded() {
        if (!silent) {
            for (InventoryListener listener : listeners)
                listener.capacityExceeded(this);
        }
    }

    private boolean isStackable(Item item) {
        if (stackMode == StackMode.ALWAYS)
            return true;

        return item.getDefinition().isStackable();
    }

    public boolean verify(int slot, int itemId) {
        return get(slot) != null && get(slot).getId() == itemId;
    }

    public void dropAll(Mob receiver) {
        GroundItemList groundItemList = receiver instanceof Player ? ((Player) receiver).getGroundItems() : player.getGroundItems();
        for (Item item : items) {
            if (item != null) {
                groundItemList.add(item.getId(), item.getAmount(), player.getPosition());
            }
        }
        for (int slot = 0; slot < items.length; slot++)
            items[slot] = null;

        fireItemsChanged();
    }

    public void checkSlot(int slot) {
        if (slot < 0 || slot >= items.length)
            throw new IndexOutOfBoundsException("slot out of range: " + slot);
    }

    public int getWeight() {
        return weight;
    }

    public void silence() {
        silent = true;
    }

    public boolean locked() {
        return locked;
    }

    public void unsilence() {
        silent = false;
    }

    public void lock() {
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

}
