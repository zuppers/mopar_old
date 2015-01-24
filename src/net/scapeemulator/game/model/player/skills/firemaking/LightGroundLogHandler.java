package net.scapeemulator.game.model.player.skills.firemaking;

import net.scapeemulator.game.dispatcher.grounditem.GroundItemHandler;
import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.action.ReachDistancedAction;
import net.scapeemulator.game.util.HandlerContext;

/**
 * @author zuppers
 * @author David Insley
 */
public class LightGroundLogHandler extends GroundItemHandler {

    public LightGroundLogHandler() {
        super(Option.FOUR);
    }

    @Override
    public void handle(Player player, int itemId, Position position, String option, HandlerContext context) {
        if (!option.equals("light")) {
            return;
        }
        final Log log = Log.forId(itemId);
        if (log != null) {
            player.startAction(new ReachDistancedAction(1, false, player, position, 0) {

                @Override
                public void executeAction() {
                    mob.sendMessage("You need a tinderbox to start a fire.");
                }

            });
            context.stop();
        }
    }

}