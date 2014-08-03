package net.scapeemulator.game.model.player.skills.mining;

import net.scapeemulator.game.dispatcher.object.ObjectHandler;
import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.util.HandlerContext;

/**
 * @author David Insley
 */
public class RockObjectHandler extends ObjectHandler {

    public RockObjectHandler() {
        super(Option.ONE);
    }

    @Override
    public void handle(Player player, GroundObject object, String optionName, HandlerContext context) {
        if (!optionName.equals("mine")) {
            return;
        }
        RockType type = RockType.forId(object.getId());
        if (type == null) {
            System.out.println("Mine option found with norock type: " + object.getId());
            return;
        }
        player.startAction(new MiningAction(player, type, object));
    }
}
