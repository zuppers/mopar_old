package net.scapeemulator.game.model.player.skills.runecrafting;

import net.scapeemulator.game.dispatcher.object.ObjectHandler;
import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.util.HandlerContext;

/**
 * @author David Insley
 */
public class AltarObjectHandler extends ObjectHandler {

    public AltarObjectHandler() {
        super(Option.ONE);
    }

    @Override
    public void handle(Player player, GroundObject object, String optionName, HandlerContext context) {
        RCAltar altar = RCAltar.forAltarId(object.getId());
        if (altar != null) {
            context.stop();
            player.startAction(new RunecraftingAction(player, altar.getRune(), object));
        }
    }
}
