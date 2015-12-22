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

    @Override
    public void cantReach() {
        mob.sendMessage("You can't reach that!");
    }
}
