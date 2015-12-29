package net.scapeemulator.game.model.mob.combat;

import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.task.Task;

public class DelayedRangeHit extends Task {

    private final Mob source;
    private final Mob target;
    private final int damage;
    private final int dropId;
    private final int dropAmt;

    public DelayedRangeHit(Mob source, Mob target, int delay, int damage, int dropId, int dropAmt) {
        super(delay, false);
        this.source = source;
        this.target = target;
        this.damage = damage;
        this.dropId = dropId;
        this.dropAmt = dropAmt;
    }

    @Override
    public void execute() {
        target.processHit(source, damage);
        if (damage > 0) {
            target.playAnimation(target.getCombatHandler().getDefendAnimation());
        }
        if (dropId > 0 && dropAmt > 0) {
            World.getWorld().getGroundItems().add(dropId, dropAmt, target.getPosition(), source instanceof Player ? (Player) source : null);
        }
        stop();
    }

}