package net.scapeemulator.game.model.mob.action;

import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.mob.Direction;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.task.Action;
import net.scapeemulator.game.task.DistancedAction;

public final class InitiateCombatAction extends Action<Player> {

	private int distance;
	private final Mob target;
	private final boolean once;

	public InitiateCombatAction(Player player, Mob target, boolean once) {
		super(player, 1, true);
		player.turnToTarget(target);
		this.distance = player.getCombatHandler().getAttackRange();
		this.target = target;
		this.once = once;
	}

	public InitiateCombatAction(Player player, Mob target) {
		this(player, target, false);
	}

	@Override
	public void execute() {
		distance = mob.getCombatHandler().getAttackRange();
		if (mob.getPosition().getDistance(target.getPosition()) <= distance) {
			if (distance <= 2) {
				if (!Direction.isTraversable(mob.getPosition(), Direction.between(mob.getPosition(), target.getPosition()), mob.getSize())) {
					return;
				}
			} else {
				if(!mob.getWalkingQueue().isEmpty() && mob.isRunning()) {
					if (!World.getWorld().getTraversalMap().isProjectilePathClear(mob.getWalkingQueue().peek(), target.getPosition())) {
						return;
					}
				} else if (!World.getWorld().getTraversalMap().isProjectilePathClear(mob.getPosition(), target.getPosition())) {
					return;
				}
			}
			mob.startAction(new CombatAction(mob, target, once));
		}
	}

}
