package net.scapeemulator.game.model.mob.action;

import net.scapeemulator.game.model.World;
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
        distance = mob.getCombatHandler().getAttackRange();
        if (target.getBounds().anyWithinArea(mob.getPosition(), mob.getSize(), distance) && !target.getBounds().anyWithinArea(mob.getPosition(), mob.getSize(), 0)) {
            if (!World.getWorld().getTraversalMap().attackPathClear(mob, target.getPosition(), distance > 2)) {
                super.execute();
                return;
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
