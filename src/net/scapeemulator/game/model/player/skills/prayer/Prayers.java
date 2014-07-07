package net.scapeemulator.game.model.player.skills.prayer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.requirement.PrayerPointRequirement;
import net.scapeemulator.game.msg.impl.ConfigMessage;

/**
 * @author David Insley
 */
public class Prayers {

    private final Player player;
    private final Set<Prayer> activePrayers;

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
        this.activePrayers = new HashSet<>();
    }

    public void toggle(Prayer prayer) {
        if (prayer == null) {
            return;
        }
        if (activePrayers.contains(prayer)) {
            player.send(new ConfigMessage(prayer.getConfigId(), 0));
            drainRate -= prayer.getDrainRate();
            activePrayers.remove(prayer);
        } else {
            if (!prayer.getRequirements().hasRequirementsDisplayOne(player)) {
                return;
            }
            player.send(new ConfigMessage(prayer.getConfigId(), 1));
            drainRate += prayer.getDrainRate();
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
    }

    /**
     * Deactivates all active prayers.
     */
    public void deactivateAll() {
        for (Prayer activePrayer : activePrayers) {
            player.send(new ConfigMessage(activePrayer.getConfigId(), 0));
        }
        activePrayers.clear();
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

}
