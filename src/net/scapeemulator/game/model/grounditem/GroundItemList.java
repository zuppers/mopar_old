package net.scapeemulator.game.model.grounditem;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import net.scapeemulator.game.model.Entity;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.util.math.BasicMath;

/**
 * @author Hadyn Richard
 * @author David Insley
 */
public final class GroundItemList {

    /**
     * The owner identifier for a public item.
     */
    public static final int PUBLIC_ITEM = -1;

    /**
     * The owner identifier for a public item that doesn't expire after a timer. TODO
     */
    public static final int PUBLIC_ITEM_PERSIST = -2;

    /**
     * The UID counter for all the ground items that are created.
     */
    private static final AtomicInteger counter = new AtomicInteger(1);

    /**
     * The ground item stacks in this list.
     */
    private final Map<Position, GroundItemStack> stacks = new LinkedHashMap<>();

    /**
     * The list of listeners for this list.
     */
    private final List<GroundItemListener> listeners = new LinkedList<>();

    /**
     * The class that represents a stack of ground items at a specific location.
     */
    private class GroundItemStack {

        /**
         * The ground items that are contained in the stack.
         */
        private final List<GroundItem> groundItems = new LinkedList<>();

        /**
         * The position at which the stack is located at.
         */
        private final Position position;

        /**
         * Constructs a new {@link GroundItemStack};
         * 
         * @param position The position of the stack.
         */
        public GroundItemStack(Position position) {
            this.position = position;
        }

        /**
         * Appends a new ground item to the stack.
         * 
         * @param itemId The id of the item to append.
         * @param amount The amount of the item.
         * @return the GroundItem modified or created
         */
        public GroundItem add(int itemId, int amount, int owner) {
            if (ItemDefinitions.forId(itemId).isStackable()) {
                for (GroundItem groundItem : groundItems) {
                    if (groundItem.getItemId() != itemId || groundItem.getOwner() != owner) {
                        continue;
                    }
                    if (BasicMath.integerOverflow(groundItem.getAmount(), amount) != 0) {
                        continue;
                    }
                    /* update the ground item */
                    groundItem.setAmount(groundItem.getAmount() + amount);
                    groundItem.resetTimer();
                    return groundItem;
                }
            }

            /* add the ground item to the ground item list */
            GroundItem groundItem = new GroundItem(counter.getAndIncrement(), position, itemId, amount, owner);
            groundItems.add(groundItem);

            /* alert the listeners a new ground item was created */
            for (GroundItemListener listener : listeners) {
                if (listener.shouldFireEvents(groundItem)) {
                    listener.groundItemCreated(groundItem);
                }
            }
            return groundItem;
        }

        /**
         * Gets if the stack is empty.
         * 
         * @return If the ground item map is empty.
         */
        public boolean isEmpty() {
            return groundItems.isEmpty();
        }

        /**
         * Gets the ground item in the list with the specified item id.
         * 
         * @param itemId The item id for the ground item to get.
         * @return The found ground item.
         */
        public GroundItem get(int itemId, int owner) {
            for (GroundItem groundItem : groundItems) {
                if (groundItem.getItemId() != itemId || (groundItem.getOwner() != -1 && groundItem.getOwner() != owner)) {
                    continue;
                }
                return groundItem;
            }
            return null;
        }

        /**
         * Gets if the stack contains a ground item with the specified item id visible to the
         * specified player.
         * 
         * @param player the player to check visibility for
         * @param itemId the item id for the ground item to check
         * @return true if the ground item with the item id exists
         */
        public boolean contains(Player player, int itemId) {
            for (GroundItem groundItem : groundItems) {
                if (groundItem.getItemId() != itemId) {
                    continue;
                }
                if (groundItem.getOwner() != PUBLIC_ITEM && groundItem.getOwner() != player.getDatabaseId()) {
                    continue;
                }
                return true;
            }
            return false;
        }

        public GroundItem remove(GroundItem toRemove) {
            if (groundItems.remove(toRemove)) {
                for (GroundItemListener listener : listeners) {
                    if (listener.shouldFireEvents(toRemove)) {
                        listener.groundItemRemoved(toRemove);
                    }
                }
                return toRemove;
            }
            return null;
        }

        /**
         * Gets the ground items in the stack.
         * 
         * @return The ground item list.
         */
        public List<GroundItem> getGroundItems() {
            return groundItems;
        }

    }

    /**
     * The representation of a ground item for this list.
     */
    public class GroundItem extends Entity {

        /**
         * The unique id of the ground item.
         */
        private final int uid;

        /**
         * The item id that the ground item represents.
         */
        private final int itemId;

        /**
         * The amount of the item that the ground item represents.
         */
        private int amount;

        /**
         * The database id of the owner of this ground item, or -1 for a public item.
         */
        private int owner;

        private int timer;

        public GroundItem(int uid, Position position, int itemId, int amount, int owner) {
            this.uid = uid;
            this.position = position;
            this.itemId = itemId;
            this.amount = amount;
            this.owner = owner;
            resetTimer();
        }

        /**
         * Gets the unique id.
         * 
         * @return The unique id.
         */
        public int getUid() {
            return uid;
        }

        /**
         * Sets the amount of the item.
         * 
         * @param newAmount The new amount.
         */
        public void setAmount(int newAmount) {
            if (newAmount != amount) {
                int oldAmount = amount;
                amount = newAmount;
                for (GroundItemListener listener : listeners) {
                    if (listener.shouldFireEvents(this)) {
                        listener.groundItemUpdated(this, oldAmount);
                    }
                }
            }
        }

        @Override
        public void setPosition(Position position) {
            // Don't allow modification of the position
        }

        /**
         * Gets the id of the item that the ground item represents.
         * 
         * @return The item id.
         */
        public int getItemId() {
            return itemId;
        }

        /**
         * Gets the amount of the item that the ground item represents.
         * 
         * @return The item amount.
         */
        public int getAmount() {
            return amount;
        }

        public int getOwner() {
            return owner;
        }

        public void setOwner(int owner) {
            this.owner = owner;
        }

        public int getTimeLeft() {
            return timer;
        }

        public void resetTimer() {
            timer = owner != PUBLIC_ITEM ? 100 : 200;
        }

        public void decrementTimer() {
            timer--;
        }

        /**
         * Converts the ground item to a immutable item.
         * 
         * @return The created immutable item.
         */
        public Item toItem() {
            return new Item(itemId, amount);
        }

    }

    /**
     * Adds a listener for the list.
     */
    public void addListener(GroundItemListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener from the list.
     */
    public void removeListener(GroundItemListener listener) {
        listeners.remove(listener);
    }

    /**
     * Fires a ground item created event for each ground item in the list.
     */
    public void fireEvents(GroundItemListener listener) {
        for (Entry<Position, GroundItemStack> entry : stacks.entrySet()) {
            for (GroundItem groundItem : entry.getValue().getGroundItems()) {
                if (listener.shouldFireEvents(groundItem)) {
                    listener.groundItemCreated(groundItem);
                }
            }
        }
    }

    /**
     * Adds a ground item to the list.
     * 
     * @param itemId The id of the item.
     * @param amount The amount of the item.
     * @param position The position of the ground item.
     * @return
     */
    public GroundItem add(int itemId, int amount, Position position, Player owner) {
        GroundItemStack stack = stacks.get(position);
        if (stack == null) {
            stack = new GroundItemStack(position);
            stacks.put(position, stack);
        }
        return stack.add(itemId, amount, owner != null ? owner.getDatabaseId() : PUBLIC_ITEM);
    }

    /**
     * Gets a ground item.
     * 
     * @param itemId The item id.
     * @param position The position of the item.
     * @return The ground item.
     */
    public GroundItem get(Player player, int itemId, Position position) {
        GroundItemStack stack = stacks.get(position);
        if (stack == null) {
            return null;
        }
        return stack.get(itemId, player.getDatabaseId());
    }

    /**
     * Checks if the list contains a ground item visible to the player.
     * 
     * @param player the player to check visibility for
     * @param itemId the id of the item to check for
     * @param position the position of the item
     * @return If the ground item exists.
     */
    public boolean contains(Player player, int itemId, Position position) {
        GroundItemStack stack = stacks.get(position);
        if (stack == null) {
            return false;
        }
        return stack.contains(player, itemId);
    }

    /**
     * Checks if a ground item is still in the ground item list.
     * 
     * @param groundItem the ground item to search for
     * @return true if the ground item still exists
     */
    public boolean contains(GroundItem groundItem) {
        GroundItemStack stack = stacks.get(groundItem.getPosition());
        if (stack == null) {
            return false;
        }
        return stack.getGroundItems().contains(groundItem);
    }

    public GroundItem remove(GroundItem groundItem) {
        GroundItemStack stack = stacks.get(groundItem.getPosition());
        if (stack == null) {
            return null;
        }

        /* Will be null if not removed */
        GroundItem removed = stack.remove(groundItem);

        /* Remove the stack if its empty */
        if (stack.isEmpty()) {
            stacks.remove(groundItem.getPosition());
        }

        return removed;
    }

    public void tick() {
        List<GroundItem> toRemove = new LinkedList<>();
        for (GroundItemStack stack : stacks.values()) {
            for (GroundItem groundItem : stack.getGroundItems()) {
                groundItem.decrementTimer();
                if (groundItem.getTimeLeft() < 1) {
                    toRemove.add(groundItem);
                }
            }
        }
        for (GroundItem groundItem : toRemove) {
            if (groundItem.getOwner() == PUBLIC_ITEM) {
                remove(groundItem);
            } else {
                groundItem.setOwner(PUBLIC_ITEM);
                for (GroundItemListener listener : listeners) {
                    if (listener.shouldFireEvents(groundItem)) {
                        listener.groundItemCreated(groundItem);
                    }
                }
            }
        }

    }
}
