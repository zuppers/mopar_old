package net.scapeemulator.game.model.player.skills.prayer;

import net.scapeemulator.game.dispatcher.item.ItemDispatcher;

/**
 * @author David Insley
 */
public class PrayerSkill {

    public static void initialize() {
        ItemDispatcher.getInstance().bind(new BuryBoneHandler());
    }
}
