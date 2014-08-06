package net.scapeemulator.game.model.player.consumable;

import net.scapeemulator.game.GameServer;

/**
 * @author David Insley
 */
public class Consumables {

    public static void initialize() {
        GameServer.getInstance().getMessageDispatcher().getItemDispatcher().bind(new FoodHandler());
    }

}
