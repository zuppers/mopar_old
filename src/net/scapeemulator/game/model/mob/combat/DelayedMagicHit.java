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
    private boolean stop;

    public DelayedMagicHit(Mob source, Mob target, SpotAnimation explosion, int damage) {
        super(4, false);
        this.source = source;
        this.target = target;
        this.explosion = damage != 0 ? explosion : CombatSpell.SPLASH_GRAPHIC;
        this.damage = damage;
    }

    @Override
    public void execute() {
        if (stop) {
            target.processHit(source, damage > target.getCurrentHitpoints() ? target.getCurrentHitpoints() : damage);
            stop();
            return;
        }
        target.playSpotAnimation(explosion);

        if (damage > 0) {
            target.playAnimation(target.getCombatHandler().getDefendAnimation());
        }
        if (damage < 0) { // We set this for spells that aren't supposed to deal any damage, not ones that miss
            stop();
            return;
        }
        setDelay(1);
        stop = true;
    }

}