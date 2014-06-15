package net.scapeemulator.game.model.mob.action;

import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.mob.Direction;
import net.scapeemulator.game.model.mob.Mob;

public final class CombatAction extends FollowAction {

	private boolean once;
	private boolean stop;

	public CombatAction(Mob mob, Mob target, boolean once) {
		super(mob, target, false, mob.getCombatHandler().getAttackRange());
		mob.getCombatHandler().initiate(target);
		this.once = once;
	}

	public CombatAction(Mob mob, Mob target) {
		this(mob, target, false);
	}

	@Override
	public void execute() {
		if (!mob.getCombatHandler().canAttack(target) || stop) {
			stop();
			return;
		}
		setDistance(mob.getCombatHandler().getAttackRange());
		if (withinDistance(mob, target, mob.getCombatHandler().getAttackRange()) && !mob.getPosition().equals(target.getPosition())) {
			if (distance <= 2) {
				if (!Direction.isTraversable(mob.getPosition(), Direction.between(mob.getPosition(), target.getPosition()), mob.getSize())) {
					super.execute();
					return;
				}
			} else {
				if (!World.getWorld().getTraversalMap().isProjectilePathClear(mob.getPosition(), target.getPosition())) {
					return;
				}
			}
			if (mob.getCombatHandler().attack() && once) {
				stop = true;
			}
		} else {
			super.execute();
		}
	}

	@Override
	public void stop() {
		mob.getCombatHandler().reset();
		super.stop();
	}

}
