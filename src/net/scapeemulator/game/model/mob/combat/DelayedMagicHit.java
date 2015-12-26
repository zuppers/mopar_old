package net.scapeemulator.game.model.mob.combat;

import net.scapeemulator.game.model.SpotAnimation;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.player.skills.magic.CombatSpell;
import net.scapeemulator.game.task.Task;

public class DelayedMagicHit extends Task {

    private final Mob source;
    private final Mob target;
    private final SpotAnimation explosion;
    private final int damage;

    public DelayedMagicHit(int delay, Mob source, Mob target, SpotAnimation explosion, int damage) {
        super(delay, false);
        this.source = source;
        this.target = target;
        this.explosion = damage != 0 ? explosion : CombatSpell.SPLASH_GRAPHIC;
        this.damage = damage;
    }

    @Override
    public void execute() {
        if (damage >= 0) {
            target.processHit(source, damage > target.getCurrentHitpoints() ? target.getCurrentHitpoints() : damage);
            target.playAnimation(target.getCombatHandler().getDefendAnimation());
        }
        target.playSpotAnimation(explosion);
        stop();
        return;
    }

}