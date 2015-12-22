package net.scapeemulator.game.model.player.skills.magic;

import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.mob.combat.DelayedMagicHit;

/**
 * @author David Insley
 */
public class EffectMobSpell extends CombatSpell {

    // TODO add actual effects :^)
    public EffectMobSpell(String name, int animation, int graphic) {
        super(SpellType.EFFECT_MOB, name, animation, graphic);
    }

    public void cast(Mob caster, Mob target) {
        super.cast(caster, target);
        World.getWorld().getTaskScheduler().schedule(new DelayedMagicHit(caster, target, explosionGraphic, -1));
    }

}
