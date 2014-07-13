package net.scapeemulator.game.model.player.skills.woodcutting;

import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.object.ObjectHandler;
import net.scapeemulator.game.util.HandlerContext;

/**
 * @author David Insley
 */
public class TreeObjectHandler extends ObjectHandler {

    public TreeObjectHandler() {
        super(Option.ONE);
    }

    @Override
    public void handle(Player player, GroundObject object, String optionName, HandlerContext context) {
        if (!optionName.equals("chop down")) {
            return;
        }
        TreeType type = TreeType.forId(object.getId());
        if (type == null) {
            System.out.println("Chop down option found with no tree type: " + object.getId());
            return;
        }
        player.startAction(new WoodcuttingAction(player, type, object));
    }
}
