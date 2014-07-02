package net.scapeemulator.game.model.player.skills.firemaking;

import net.scapeemulator.game.GameServer;

/**
 * @author David Insley
 */
public class Firemaking {

    public static void bindHandlers() {
        for (Log log : Log.values()) {
            GameServer.getInstance().getMessageDispatcher().getItemOnItemDispatcher().bind(new LogOnTinderboxHandler(log));
        }
    }

}
