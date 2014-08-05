package net.scapeemulator.game.model.player.skills.prayer;

import net.scapeemulator.game.GameServer;

/**
 * @author David Insley
 */
public class PrayerSkill {

    public static void initialize() {

        // TODO bone on altar
        GameServer.getInstance().getMessageDispatcher().getItemDispatcher().bind(new BuryBoneHandler());
    }
}
