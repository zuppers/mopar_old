package net.scapeemulator.game.dispatcher.grounditem;

import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.action.PickupItemAction;

public final class TakeGroundItemHandler extends GroundItemHandler {

    /**
     * Constructs a new {@link TakeGroundItemHandler};
     */
    public TakeGroundItemHandler() {
        super(Option.THREE);
    }

    @Override
    public boolean handle(Player player, int itemId, Position position, String option) {
        if (!option.equals("take")) {
            return false;
        }
        player.startAction(new PickupItemAction(player, itemId, position));
        return true;
    }
}
