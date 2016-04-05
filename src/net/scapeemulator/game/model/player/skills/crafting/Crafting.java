package net.scapeemulator.game.model.player.skills.crafting;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.requirement.ItemRequirement;

/**
 * @author David Insley
 */
public class Crafting {

    public static final ItemRequirement THREAD_REQUIREMENT = new ItemRequirement(1734, true, "You need a needle and thread to craft leather armor.") {
        @Override
        public void fulfill(Player player) {
            if (Math.random() < 0.2) {
                super.fulfill(player);
                player.sendMessage("You use up one of your reels of thread.");
            }
        }
    };

    public static final ItemRequirement NEEDLE_REQUIREMENT = new ItemRequirement(1733, false, "You need a needle and thread to craft leather armor.");
    public static final ItemRequirement SOFT_CLAY_REQUIREMENT = new ItemRequirement(1761, true);

}
