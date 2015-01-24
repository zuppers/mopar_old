package net.scapeemulator.game.model.mob.action;

import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.task.Action;

public final class InitiateCombatAction extends Action<Player> {

    private final Mob target;
    private final boolean once;

    public InitiateCombatAction(Player player, Mob target, boolean once) {
        super(player, 1, true);
        player.turnToTarget(target);
        this.target = target;
        this.once = once;
    }

    public InitiateCombatAction(Player player, Mob target) {
        this(player, target, false);
    }

    @Override
    public void execute() {

        /*
         * We use the initial walking queue that the client sends using normal pathfinding. If the
         * target has moved and the walking queue is empty, start the combat action anyway which
         * uses dummy pathfinding. Or, if the target moved closer and we get in range sooner, make
         * sure it's a valid attack position and start it then.
         */
        if (mob.getWalkingQueue().isEmpty()) {
            mob.startAction(new CombatAction(mob, target, once));
            return;
        }
        if (target.getBounds().anyWithinArea(mob.getPosition(), mob.getSize(), mob.getCombatHandler().getAttackRange(), false)) {
            if (World.getWorld().getTraversalMap().attackPathClear(mob, target.getPosition(), mob.getCombatHandler().getAttackRange() > 2)) {
                mob.startAction(new CombatAction(mob, target, once));
            }
        }
    }

}
