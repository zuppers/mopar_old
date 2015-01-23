package net.scapeemulator.game.dispatcher.grounditem;

import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.grounditem.GroundItemList.GroundItem;
import net.scapeemulator.game.model.player.Player;

/**
 * @author Hadyn Richard
 */
public abstract class GroundItemHandler {

    /**
     * The option that the ground item handler will be bound to.
     */
    private final Option option;

    /**
     * Constructs a new {@link GroundItemHandler};
     * 
     * @param option The option that the ground item handler will be bound to.
     */
    public GroundItemHandler(Option option) {
        this.option = option;
    }

    public abstract boolean handle(Player player, GroundItem groundItem, String option);

    /**
     * Gets the option that this handler will be for.
     * 
     * @return The option.
     */
    public Option getOption() {
        return option;
    }

}
