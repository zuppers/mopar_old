package net.scapeemulator.game.model.player.skills.prayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.requirement.PrayerPointRequirement;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.msg.impl.ConfigMessage;

/**
 * @author David Insley
 */
public class Prayers {

    private final Player player;
    private final Set<Prayer> activePrayers;
    private final Map<Integer, Double> bonuses;

    /**
     * The rate at which prayer points are drained per tick.
     */
    private int drainRate;

    /**
     * The current prayer point drain counter. Once it reaches 1000 a prayer point is removed from
     * the player.
     */
    private int drainCounter;

    public Prayers(Player player) {
        this.player = player;
        activePrayers = new HashSet<>();
        bonuses = new HashMap<>();
    }

    public void toggle(Prayer prayer) {
        if (prayer == null) {
            return;
        }
        if (activePrayers.contains(prayer)) {
            player.send(new ConfigMessage(prayer.getConfigId(), 0));
            drainRate -= prayer.getDrainRate();
            activePrayers.remove(prayer);
            if (player.getHeadIcon().getPrayer() == prayer) {
                player.setHeadIcon(HeadIcon.NONE);
                player.appearanceUpdated();
            }
        } else {
            if (!prayer.getRequirements().hasRequirementsDisplayOne(player)) {
                return;
            }
            player.send(new ConfigMessage(prayer.getConfigId(), 1));
            drainRate += prayer.getDrainRate();

            // TODO change when you can have summoning + other icon
            if (prayer.getHeadIcon() != null) {
                player.setHeadIcon(prayer.getHeadIcon());
                player.appearanceUpdated();
            }

            Iterator<Prayer> it = activePrayers.iterator();
            while (it.hasNext()) {
                Prayer activePrayer = it.next();
                if (prayer.conflicts(activePrayer)) {
                    player.send(new ConfigMessage(activePrayer.getConfigId(), 0));
                    drainRate -= activePrayer.getDrainRate();
                    it.remove();
                }
            }
            activePrayers.add(prayer);
        }
        calculateBonuses();
    }

    // Not happy with this design for bonuses. Should rewrite.
    /**
     * Iterates through all active prayers and sets the bonus for each skill.
     */
    private void calculateBonuses() {
        bonuses.clear();
        for (Prayer activePrayer : activePrayers) {

            int skillId = activePrayer.getTypes()[0].getSkillId();
            if (skillId < 0) {
                continue;
            }

            switch (activePrayer) {

            // Tier one prayer bonuses
            case THICK_SKIN:
            case BURST_OF_STRENGTH:
            case CLARITY_OF_THOUGHT:
            case SHARP_EYE:
            case MYSTIC_WILL:
                bonuses.put(skillId, 1.05);
                break;

            // Tier two prayer bonuses
            case ROCK_SKIN:
            case SUPERHUMAN_STRENGTH:
            case IMPROVED_REFLEXES:
            case HAWK_EYE:
            case MYSTIC_LORE:
                bonuses.put(skillId, 1.1);
                break;

            // Tier three prayer bonuses
            case STEEL_SKIN:
            case ULTIMATE_STRENGTH:
            case INCREDIBLE_REFLEXES:
            case EAGLE_EYE:
            case MYSTIC_MIGHT:
                bonuses.put(skillId, 1.15);
                break;

            case CHIVALRY:
                bonuses.put(Skill.ATTACK, 1.15);
                bonuses.put(Skill.STRENGTH, 1.18);
                bonuses.put(Skill.DEFENCE, 1.2);
                break;

            case PIETY:
                bonuses.put(Skill.ATTACK, 1.2);
                bonuses.put(Skill.STRENGTH, 1.23);
                bonuses.put(Skill.DEFENCE, 1.25);
                break;

            default:
                break;
            }
        }
    }

    /**
     * Deactivates all active prayers.
     */
    public void deactivateAll() {
        for (Prayer activePrayer : activePrayers) {
            player.send(new ConfigMessage(activePrayer.getConfigId(), 0));
        }
        if (player.getHeadIcon() != HeadIcon.NONE) {
            player.setHeadIcon(HeadIcon.NONE);
            player.appearanceUpdated();
        }
        activePrayers.clear();
        bonuses.clear();
        drainCounter = 0;
        drainRate = 0;

    }

    /**
     * Called once per tick, increments the prayer drain counter using the drain rate of all active
     * prayers and then removes prayer points from the player if necessary.
     */
    public void tick() {
        if (drainRate > 0) {
            int prayerBonus = player.getEquipmentBonuses().getPrayerBonus();
            double modifier = 1 + (prayerBonus * (0.01 / 0.3));
            int modifiedDrainRate = (int) (drainRate / modifier);
            drainCounter += modifiedDrainRate;
            int drain = drainCounter / 1000;
            player.reducePrayerPoints(drain);
            drainCounter -= drain * 1000;
            if (!PrayerPointRequirement.NON_ZERO_POINTS.hasRequirement(player)) {
                deactivateAll();
                player.sendMessage("You have run out of Prayer points.");
            }
        }
    }

    public double getPrayerBonus(int skillId) {
        if (bonuses.containsKey(skillId)) {
            return bonuses.get(skillId);
        }
        return 1;
    }

    public boolean prayerActive(Prayer prayer) {
        return activePrayers.contains(prayer);
    }

    public boolean protectingItem() {
        return activePrayers.contains(Prayer.PROTECT);
    }

}
