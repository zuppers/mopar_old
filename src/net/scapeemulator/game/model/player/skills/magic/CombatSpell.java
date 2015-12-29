package net.scapeemulator.game.model.player.skills.magic;

import net.scapeemulator.game.model.SpotAnimation;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.mob.combat.DelayedMagicHit;
import net.scapeemulator.game.msg.impl.CreateProjectileMessage;

/**
 * @author David Insley
 */
public abstract class CombatSpell extends Spell {

    public static SpotAnimation SPLASH_GRAPHIC = new SpotAnimation(85, 0, 100);

    protected int projGraphic;
    protected int projStartHeight;
    protected int projEndHeight;
    protected int projStartDelay;
    protected SpotAnimation explosionGraphic;

    public CombatSpell(SpellType type, int animation, int graphic) {
        super(type, new Animation(animation), new SpotAnimation(graphic, 0, 100));
        // TODO anim height should be 0 for teleblock and miasmic spells
    }

    public void cast(Mob caster, Mob target, int damage) {
        caster.playAnimation(animation);
        caster.playSpotAnimation(graphic);
        CreateProjectileMessage cpm = new CreateProjectileMessage(caster.getPosition(), target.getPosition(), target, projGraphic, projStartHeight,
                projEndHeight, projStartDelay, 20, 16, true);
        World.getWorld().createGlobalProjectile(caster.getPosition(), cpm);
        World.getWorld().getTaskScheduler().schedule(new DelayedMagicHit(cpm.getDurationTicks(), caster, target, explosionGraphic, damage));
    }

    public void setProjectileInformation(int projGraphic, int explosionGraphic, int projStartHeight, int projEndHeight, int projStartDelay) {
        this.projGraphic = projGraphic;
        this.explosionGraphic = new SpotAnimation(explosionGraphic, 0, 100);
        this.projStartHeight = projStartHeight;
        this.projEndHeight = projEndHeight;
        this.projStartDelay = projStartDelay;
    }

}
