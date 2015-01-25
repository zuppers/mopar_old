package net.scapeemulator.game.model.npc;

import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.mob.combat.AttackStyle;
import net.scapeemulator.game.model.mob.combat.AttackType;
import net.scapeemulator.game.model.mob.combat.CombatHandler;
import net.scapeemulator.game.model.mob.combat.DelayedMagicHit;
import net.scapeemulator.game.model.player.skills.magic.CombatSpell;
import net.scapeemulator.game.model.player.skills.prayer.HeadIcon;

public class NPCCombatHandler extends CombatHandler<NPC> {

    public NPCCombatHandler(NPC npc) {
        super(npc);
        attackStyle = AttackStyle.ACCURATE;
        attackType = npc.getDefinition().getAttackType();
        // autoCast = npc.getDefinition().getAutoCast();
    }

    @Override
    public boolean canAttack(Mob target) {
        return mob.alive() && target.alive();
    }

    @Override
    public boolean attack() {
        if (combatDelay > 0) {
            return false;
        }

        boolean shouldHit = attackRoll() > target.getCombatHandler().defenceRoll(attackType);
        switch (attackType) {
        case AIR:
        case WATER:
        case EARTH:
        case FIRE:
        case ALL_MAGIC:
            if (target.getHeadIcon() == HeadIcon.MAGIC) {
                shouldHit = false;
            }
            break;
        case SLASH:
        case STAB:
        case CRUSH:
            if (target.getHeadIcon() == HeadIcon.MELEE) {
                shouldHit = false;
            }
            break;
        case RANGE:
            if (target.getHeadIcon() == HeadIcon.RANGED) {
                shouldHit = false;
            }
            break;
        default:
            break;
        }

        int damage = !shouldHit ? 0 : 1 + (int) (Math.random() * mob.getDefinition().getMaxHit());
        damage = damage > target.getCurrentHitpoints() ? target.getCurrentHitpoints() : damage;

        if (nextSpell != null) {
            CombatSpell cs = (CombatSpell) nextSpell;
            cs.cast(mob, target);
            mob.playAnimation(new Animation(mob.getDefinition().getAttackEmote()));
            World.getWorld().getTaskScheduler().schedule(new DelayedMagicHit(mob, target, cs.getExplosionGraphic(), damage));
            nextSpell = autoCast;
        } else {
            mob.playAnimation(new Animation(mob.getDefinition().getAttackEmote()));
            hitTarget(damage);
        }

        combatDelay = mob.getDefinition().getAttackDelay();
        return true;
    }

    @Override
    public double attackRoll() {
        double power = 1.0;
        power += mob.getDefinition().getCombatLevel() * 1.25;
        power *= 1.0; // TODO other modifiers?
        System.out.println("NPC AR: " + power);
        return Math.random() * power;
    }

    @Override
    public double defenceRoll(AttackType other) {
        double power = 1.0;
        power += mob.getDefinition().getCombatLevel() * 1.25;
        if (mob.getDefinition().getWeakness() == other) {
            power *= 0.7;
        }
        power *= 1.0; // TODO other modifiers?
        System.out.println("NPC DR: " + power);
        return Math.random() * power;
    }

    @Override
    public Animation getDefendAnimation() {
        return new Animation(mob.getDefinition().getDefendEmote());
    }

    @Override
    public int getAttackRange() {
        return mob.getDefinition().getAttackRange();
    }

    @Override
    public boolean shouldRetaliate() {
        return target == null && noRetaliate < 1;
    }

}
