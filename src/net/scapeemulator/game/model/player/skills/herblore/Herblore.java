package net.scapeemulator.game.model.player.skills.herblore;

import net.scapeemulator.game.dispatcher.item.ItemDispatcher;
import net.scapeemulator.game.dispatcher.item.ItemOnItemDispatcher;

/**
 * @author David Insley
 */
public class Herblore {

    public static void initialize() {

        ItemDispatcher.getInstance().bind(new GrimyHerbHandler());

        for (Secondary secondary : Secondary.values()) {
            if (secondary.getUngroundId() != -1) {
                ItemOnItemDispatcher.getInstance().bind(new GrindingHandler(secondary));
            }
        }

        for (Herb herb : Herb.values()) {
            ItemOnItemDispatcher.getInstance().bind(new HerbOnVialHandler(herb));
        }

        for (Potion potion : Potion.values()) {
            ItemOnItemDispatcher.getInstance().bind(new CombineSecondaryHandler(potion));
        }
    }

}
