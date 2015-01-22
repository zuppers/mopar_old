package net.scapeemulator.game.dispatcher.item;

import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.util.HandlerContext;

/**
 * @author Hadyn Richard
 */
public abstract class ItemHandler {

    /**
     * The option that the item handler will be bound to.
     */
    private final Option option;

    /**
     * Constructs a new ItemHandler that handles the given Option.
     * 
     * @param option The option that the item handler will be bound to.
     */
    public ItemHandler(Option option) {
        this.option = option;
    }

    /**
     * Handles a player using an item option in their inventory. The item has been verified to
     * exist. This method should check the option text or id of the item and stop the handler
     * context if this method handles the usage.
     * 
     * @param player the player using the item
     * @param item the item used
     * @param option the item usage text for this option
     * @param context the item loop context iterating over all handlers
     */
    public abstract void handle(Player player, SlottedItem item, String option, HandlerContext context);

    /**
     * Gets the option that this handler will be for.
     * 
     * @return The option.
     */
    public Option getOption() {
        return option;
    }

}
