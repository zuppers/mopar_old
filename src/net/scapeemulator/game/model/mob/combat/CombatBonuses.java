package net.scapeemulator.game.model.mob.combat;

import java.util.HashMap;
import java.util.Map;

import static net.scapeemulator.game.model.mob.combat.AttackType.*;

/**
 * @author David Insley
 */
public class CombatBonuses {

    private final Map<AttackType, Integer> attackBonuses;
    private final Map<AttackType, Integer> defenceBonuses;

    private int prayerBonus;
    private int strengthBonus;
    private int rangeStrength;

    public CombatBonuses() {
        attackBonuses = new HashMap<>();
        defenceBonuses = new HashMap<>();
    }

    public void setAttackBonus(AttackType type, int bonus) {
        attackBonuses.put(type, bonus);
    }

    public void setDefenceBonus(AttackType type, int bonus) {
        defenceBonuses.put(type, bonus);
    }

    public void setStrengthBonus(int bonus) {
        strengthBonus = bonus;
    }

    public void setPrayerBonus(int bonus) {
        prayerBonus = bonus;
    }

    public void setRangeStrengthBonus(int bonus) {
        rangeStrength = bonus;
    }

    public int getAttackBonus(AttackType type) {
        return attackBonuses.get(type);
    }

    public int getDefenceBonus(AttackType type) {
        return defenceBonuses.get(type);
    }

    public int getStrengthBonus() {
        return strengthBonus;
    }

    public int getPrayerBonus() {
        return prayerBonus;
    }

    public int getRangeStrengthBonus() {
        return rangeStrength;
    }
}
