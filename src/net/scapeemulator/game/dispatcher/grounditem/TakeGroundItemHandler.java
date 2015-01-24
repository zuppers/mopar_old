package net.scapeemulator.game.dispatcher.grounditem;

import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.action.PickupItemAction;
import net.scapeemulator.game.util.HandlerContext;

public final class TakeGroundItemHandler extends GroundItemHandler {

    /**
     * Constructs a new {@link TakeGroundItemHandler};
     */
    public TakeGroundItemHandler() {
        super(Option.THREE);
    }

    @Override
    public void handle(Player player, int itemId, Position position, String option, HandlerContext context) {
        if (!option.equals("take")) {
            return;
        }
        player.startAction(new PickupItemAction(player, itemId, position));
        context.stop();
    }
}
