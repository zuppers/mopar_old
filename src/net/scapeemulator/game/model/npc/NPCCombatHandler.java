package net.scapeemulator.game.model.npc;

import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.mob.combat.AttackStyle;
import net.scapeemulator.game.model.mob.combat.AttackType;
import net.scapeemulator.game.model.mob.combat.CombatHandler;
import net.scapeemulator.game.model.mob.combat.DelayedMagicHit;
import net.scapeemulator.game.model.player.skills.magic.DamageSpell;
import net.scapeemulator.game.model.player.skills.prayer.HeadIcon;

public class NPCCombatHandler extends CombatHandler<NPC> {

    public NPCCombatHandler(NPC npc) {
        super(npc);
        attackStyle = AttackStyle.SHARED;
        setRawAttackType(npc.getDefinition().getAttackType());
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

        boolean shouldHit = shouldHit();

        switch (getNextAttackType()) {
        case MAGIC:
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

        int damage = !shouldHit ? 0 : 1 + (int) (Math.random() * getMaxHit());

        if (nextSpell != null) {
            nextSpell.cast(mob, target);
            mob.playAnimation(new Animation(mob.getDefinition().getAttackEmote()));
            World.getWorld().getTaskScheduler().schedule(new DelayedMagicHit(mob, target, nextSpell.getExplosionGraphic(), damage));
            nextSpell = autoCast;
        } else {
            mob.playAnimation(new Animation(mob.getDefinition().getAttackEmote()));
            damage = damage > target.getCurrentHitpoints() ? target.getCurrentHitpoints() : damage;
            hitTarget(damage);
        }

        combatDelay = mob.getDefinition().getAttackDelay();
        return true;
    }

    private int getMaxHit() {
        int level = 0;
        int equipmentBonus = 0;

        switch (getNextAttackType()) {
        case MAGIC:
            if (nextSpell instanceof DamageSpell) {
                return ((DamageSpell) nextSpell).getMaxHit();
            }
            return 0;
        case RANGE:
            level = mob.getSkillSet().getCurrentLevel(NPCSkillSet.RANGED);
            level += 9;
            break;
        case CRUSH:
        case SLASH:
        case STAB:
            equipmentBonus = mob.getCombatBonuses().getStrengthBonus();
            level = mob.getSkillSet().getCurrentLevel(NPCSkillSet.STRENGTH);
            level += 9;
            break;
        }
        return (int) (0.5 + ((level * (64.0 + equipmentBonus)) / 640));
    }

    @Override
    public int attackRoll() {
        int level = 0;
        int equipmentBonus = 0;

        switch (getNextAttackType()) {
        case MAGIC:
            level = mob.getSkillSet().getCurrentLevel(NPCSkillSet.MAGIC);
            equipmentBonus = mob.getCombatBonuses().getAttackBonus(AttackType.MAGIC);
            level += 8;
            break;
        case RANGE:
            equipmentBonus = mob.getCombatBonuses().getAttackBonus(AttackType.RANGE);
            level = mob.getSkillSet().getCurrentLevel(NPCSkillSet.RANGED);
            level += 9;
            break;
        case CRUSH:
        case SLASH:
        case STAB:
            equipmentBonus = mob.getCombatBonuses().getAttackBonus(getNextAttackType());
            level = mob.getSkillSet().getCurrentLevel(NPCSkillSet.ATTACK);
            level += 9;
            break;
        }
        return level * (64 + equipmentBonus);
    }

    @Override
    public int defenceRoll(AttackType other) {
        int level = 0;
        int equipmentBonus = 0;

        switch (other) {
        case MAGIC:
            equipmentBonus = mob.getCombatBonuses().getDefenceBonus(AttackType.MAGIC);
            level = (int) ((mob.getSkillSet().getCurrentLevel(NPCSkillSet.DEFENCE) + 9) * 0.3);
            level += (int) (mob.getSkillSet().getCurrentLevel(NPCSkillSet.MAGIC) * 0.7);
            break;
        case RANGE:
        case CRUSH:
        case SLASH:
        case STAB:
            equipmentBonus = mob.getCombatBonuses().getDefenceBonus(other);
            level = mob.getSkillSet().getCurrentLevel(NPCSkillSet.DEFENCE);
            level += 9;
            break;
        }
        return level * (64 + equipmentBonus);
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
