package net.scapeemulator.game.model.player;

import java.util.HashMap;
import java.util.Map;

import static net.scapeemulator.game.model.mob.combat.AttackType.*;
import net.scapeemulator.game.model.mob.combat.AttackType;

/**
 * @author David Insley
 */
public class EquipmentBonuses {

    private final Map<AttackType, Integer> attackBonuses;
    private final Map<AttackType, Integer> defenceBonuses;

    private int prayerBonus;
    private int strengthBonus;
    private int rangeStrength;

    public EquipmentBonuses() {
        attackBonuses = new HashMap<>();
        defenceBonuses = new HashMap<>();
    }

    public void setAttackBonus(AttackType type, int bonus) {
        attackBonuses.put(type, bonus);
        if (type == ALL_MAGIC) {
            attackBonuses.put(AIR, bonus);
            attackBonuses.put(WATER, bonus);
            attackBonuses.put(EARTH, bonus);
            attackBonuses.put(FIRE, bonus);
        }
    }

    public void setDefenceBonus(AttackType type, int bonus) {
        defenceBonuses.put(type, bonus);
        if (type == ALL_MAGIC) {
            defenceBonuses.put(AIR, bonus);
            defenceBonuses.put(WATER, bonus);
            defenceBonuses.put(EARTH, bonus);
            defenceBonuses.put(FIRE, bonus);
        }
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
        if (type == DRAGONFIRE) {
            return 0;
        }
        return attackBonuses.get(type);
    }

    public int getDefenceBonus(AttackType type) {
        if (type == DRAGONFIRE) {
            return 0;
        }
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
