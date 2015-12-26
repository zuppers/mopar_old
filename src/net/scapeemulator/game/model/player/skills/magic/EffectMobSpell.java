package net.scapeemulator.game.model.player.skills.magic;

import net.scapeemulator.game.model.mob.Mob;

/**
 * @author David Insley
 */
public class EffectMobSpell extends CombatSpell {

    // TODO add actual effects :^)
    public EffectMobSpell(int animation, int graphic) {
        super(SpellType.EFFECT_MOB, animation, graphic);
    }

    @Override
    public void cast(Mob caster, Mob target, int damage) {
        throw new UnsupportedOperationException();
    }

    public void cast(Mob caster, Mob target, boolean shouldHit) {
        super.cast(caster, target, shouldHit ? -1 : 0);
    }

}
