package net.scapeemulator.game.model.player.skills.firemaking;

import net.scapeemulator.game.dispatcher.grounditem.GroundItemDispatcher;
import net.scapeemulator.game.dispatcher.item.ItemOnGroundItemDispatcher;
import net.scapeemulator.game.dispatcher.item.ItemOnItemDispatcher;

/**
 * @author David Insley
 */
public class Firemaking {

    public static final int TINDERBOX = 590;

    public static void initialize() {
        for (Log log : Log.values()) {
            ItemOnItemDispatcher.getInstance().bind(new LogOnTinderboxHandler(log));
            ItemOnGroundItemDispatcher.getInstance().bind(new TinderboxOnGroundLogHandler(log));
        }
        GroundItemDispatcher.getInstance().bind(new LightGroundLogHandler());
    }

}
