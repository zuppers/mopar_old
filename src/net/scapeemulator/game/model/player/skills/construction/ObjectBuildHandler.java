package net.scapeemulator.game.model.player.skills.construction;

import net.scapeemulator.game.dispatcher.object.ObjectHandler;
import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.action.ReachDistancedAction;
import net.scapeemulator.game.model.player.skills.construction.House.BuildingSession;
import net.scapeemulator.game.util.HandlerContext;

/**
 * @author David Insley
 */
public class ObjectBuildHandler extends ObjectHandler {

    public ObjectBuildHandler() {
        super(Option.FIVE);
    }

    @Override
    public void handle(final Player player, final GroundObject object, String optionName, HandlerContext context) {
        context.stop();
        player.startAction(new ReachDistancedAction(1, true, player, object.getBounds(), 1) {
            @Override
            public void executeAction() {
                stop();
                if (player.getInHouse() != player.getHouse()) {
                    player.sendMessage("You must be in your house to do that.");
                    return;
                }
                BuildingSession session = player.getHouse().getBuildingSession();
                if (session == null) {
                    player.sendMessage("You must be in building mode to modify your house.");
                    return;
                }
                session.handleBuildOption(object);
            }
        });
    }

}
