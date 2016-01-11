package net.scapeemulator.game.model.player.action;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Player;

/**
 * @author David Insley
 */
public abstract class ReachObjectAction extends ReachDistancedAction {

    private final GroundObject object;

    public ReachObjectAction(int delay, boolean immediate, Player player, GroundObject object, int distance, boolean waitForStop) {
        super(delay, immediate, player, object.getBounds(), distance, waitForStop);
        this.object = object;
    }

    @Override
    public void execute() {
        if (mob.getWalkingQueue().isEmpty()) {
            if (!object.getValidInteractPositions().contains(mob.getPosition())) {
                cantReach();
                stop();
                return;
            }
        }
        super.execute();
    }

}
