package net.scapeemulator.game.model.player.action;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.task.Action;

/**
 * A player Action that sets the player blocked when it starts and then unblocks when it is stopped.
 * 
 * @author David Insley
 */
public abstract class BlockedAction extends Action<Player> {

    private boolean blocked;

    public BlockedAction(Player player, int delay, boolean immediate) {
        super(player, delay, immediate);
    }

    public void execute() {
        if (!blocked) {
            mob.setActionsBlocked(true);
            blocked = true;
        }
        executeAction();
    }

    public abstract void executeAction();

    @Override
    public void stop() {
        mob.setActionsBlocked(false);
    }
}
