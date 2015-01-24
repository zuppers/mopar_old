package net.scapeemulator.game.model.mob.action;

import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.task.Action;

/**
 * This action executes when the mob target has been reached. Utilizes following.
 * 
 * @author Hayden
 * @author David Insley
 */
public abstract class MobInteractionAction<T extends Mob, O extends Mob> extends Action<T> {

    private FollowAction followAction;
    private boolean reached;
    private int distance;
    protected O target;

    public MobInteractionAction(T mob, O target, int distance) {
        super(mob, 1, true);
        followAction = new FollowAction(mob, target, false, distance);
        this.distance = distance;
        this.target = target;
        this.distance = distance;
        init();
    }

    private void init() {
        mob.turnToTarget(target);
    }

    @Override
    public void execute() {
        if (reached) {
            executeAction();
            return;
        } else if (mob.getWalkingQueue().isEmpty() && !target.getBounds().anyWithinArea(mob.getPosition(), mob.getSize(), distance, false)) {
            followAction.execute();
        } else {
            reached = true;
            executeAction();
        }

    }

    public abstract void executeAction();
}
