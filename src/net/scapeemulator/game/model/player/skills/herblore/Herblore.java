package net.scapeemulator.game.model.player.skills.herblore;

import net.scapeemulator.game.GameServer;

/**
 * @author David Insley
 */
public class Herblore {

    public static void initialize() {
        for (Secondary secondary : Secondary.values()) {
            if (secondary.getUngroundId() != -1) {
                GameServer.getInstance().getMessageDispatcher().getItemOnItemDispatcher().bind(new GrindingHandler(secondary));
            }
        }
        
        GameServer.getInstance().getMessageDispatcher().getItemDispatcher().bind(new GrimyHerbHandler());
        
        for (Herb herb : Herb.values()) {
            GameServer.getInstance().getMessageDispatcher().getItemOnItemDispatcher().bind(new HerbOnVialHandler(herb));
        }
        
        for (Potion potion : Potion.values()) {
            GameServer.getInstance().getMessageDispatcher().getItemOnItemDispatcher().bind(new CombineSecondaryHandler(potion));
        }
    }

}
