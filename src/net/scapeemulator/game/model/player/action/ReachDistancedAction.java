package net.scapeemulator.game.model.player.action;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.area.Area;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.task.DistancedAction;

/**
 * @author David Insley
 */
public abstract class ReachDistancedAction extends DistancedAction<Player> {

    public ReachDistancedAction(int delay, boolean immediate, Player player, Position position, int distance) {
        super(delay, immediate, player, position, distance);
    }

    public ReachDistancedAction(int delay, boolean immediate, Player player, Area bounds, int distance) {
        super(delay, immediate, player, bounds, distance);
    }

    public ReachDistancedAction(int delay, boolean immediate, Player player, Position position, int distance, boolean waitForStop) {
        super(delay, immediate, player, position, distance, waitForStop);
    }

    public ReachDistancedAction(int delay, boolean immediate, Player player, Area bounds, int distance, boolean waitForStop) {
        super(delay, immediate, player, bounds, distance, waitForStop);
    }

    @Override
    public void cantReach() {
        mob.sendMessage("You can't reach that!");
    }
}
