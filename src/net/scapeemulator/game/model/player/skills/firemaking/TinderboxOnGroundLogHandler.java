package net.scapeemulator.game.model.player.skills.firemaking;

import net.scapeemulator.game.dispatcher.item.ItemOnGroundItemHandler;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.action.ReachDistancedAction;

/**
 * @author zuppers
 * @author David Insley
 */
public class TinderboxOnGroundLogHandler extends ItemOnGroundItemHandler {

    private final Log log;

    public TinderboxOnGroundLogHandler(Log log) {
        super(Firemaking.TINDERBOX, log.getItemId());
        this.log = log;
    }

    @Override
    public void handle(Player player, SlottedItem item, Position groundPosition) {
        player.startAction(new ReachDistancedAction(1, false, player, groundPosition, 0) {

            @Override
            public void executeAction() {
                mob.startAction(new FiremakingAction(mob, log, null));
            }

        });

    }

}