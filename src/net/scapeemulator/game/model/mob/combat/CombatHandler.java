package net.scapeemulator.game.model.mob.combat;

import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.player.skills.magic.CombatSpell;
import net.scapeemulator.game.model.player.skills.magic.DamageSpell;

public abstract class CombatHandler<T extends Mob> {

    protected final T mob;
    protected Mob target;
    protected DamageSpell autoCast;
    protected CombatSpell nextSpell;
    protected AttackStyle attackStyle;
    private AttackType attackType;
    protected int noRetaliate;

    /**
     * The delay (in ticks) until the next attack can be administered.
     */
    protected int combatDelay;

    public CombatHandler(T mob) {
        this.mob = mob;
    }

    public void tick() {
        if (combatDelay > 0) {
            combatDelay--;
        }
        if (noRetaliate > 0) {
            noRetaliate--;
        }
    }

    public void initiate(Mob target) {
        if (nextSpell == null && autoCast != null) {
            nextSpell = autoCast;
        }
        mob.turnToTarget(target);
        this.target = target;
    }

    public void hitTarget(int damage) {
        hit(target, damage);
    }

    public void hit(Mob other, int damage) {
        other.processHit(mob, damage);
    }

    public void reset() {
        target = null;
        nextSpell = autoCast;
    }

    /**
     * Gets the attack type (SLASH, STAB, CRUSH, RANGE, MAGIC) of the next
     * attack.
     */
    public AttackType getNextAttackType() {
        if (nextSpell != null) {
            return AttackType.MAGIC;
        }
        return attackType;
    }

    /**
     * Sets the attack type (SLASH, STAB, CRUSH, RANGE) currently selected in
     * the attack tab, not taking into account potential spellcasting.
     */
    public void setRawAttackType(AttackType attackType) {
        if (attackType == AttackType.MAGIC) {
            throw new IllegalArgumentException("Cannot set attackType to magic, use nextSpell and autoCast instead");
        }
        this.attackType = attackType;
    }

    /**
     * Gets the attack type (SLASH, STAB, CRUSH, RANGE) currently selected in
     * the attack tab, not taking into account potential spellcasting.
     */
    public AttackType getRawAttackType() {
        return attackType;
    }

    public void setNoRetaliate(int delay) {
        noRetaliate = delay;
    }

    public int getNoRetaliate() {
        return noRetaliate;
    }

    public void setNextSpell(CombatSpell nextSpell) {
        this.nextSpell = nextSpell;
    }

    public DamageSpell getAutoCast() {
        return autoCast;
    }

    public void setAutoCast(DamageSpell autoCast) {
        this.autoCast = autoCast;
        nextSpell = autoCast;
    }

    public abstract boolean canAttack(Mob target);

    public abstract boolean attack();

    public abstract int attackRoll();

    public abstract int defenceRoll(AttackType other);

    protected boolean shouldHit() {
        double attackRoll = attackRoll();
        double defRoll = target.getCombatHandler().defenceRoll(attackType);
        double accuracy;
        if (attackRoll > defRoll) {
            accuracy = 1.0 - ((defRoll + 2.0) / (2.0 * (attackRoll + 1.0)));
        } else {
            accuracy = attackRoll / (2 * (defRoll + 1));
        }
        return accuracy > Math.random();
    }

    /**
     * The attack range this mob has, taking into account active spells and
     * range weapons.
     * 
     * @return this mobs attack range
     */
    public abstract int getAttackRange();

    /**
     * Whether or not a mob should retaliate an attack at the given time, taking
     * into account current combat status and things like retaliation cooldown
     * timers for retreat.
     * 
     * @return if this mob should retaliate
     */
    public abstract boolean shouldRetaliate();

    public abstract Animation getDefendAnimation();

}
