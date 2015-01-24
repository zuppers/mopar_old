package net.scapeemulator.game.dispatcher.grounditem;

import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.util.HandlerContext;

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

    public abstract void handle(Player player, int itemId, Position position, String option, HandlerContext context);

    /**
     * Gets the option that this handler will be for.
     * 
     * @return The option.
     */
    public Option getOption() {
        return option;
    }

}
