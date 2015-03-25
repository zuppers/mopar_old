package net.scapeemulator.game.model.player.consumable;

import net.scapeemulator.game.dispatcher.item.ItemDispatcher;

/**
 * @author David Insley
 */
public class Consumables {

    public static void initialize() {
        ItemDispatcher.getInstance().bind(new FoodHandler());
    }

}
