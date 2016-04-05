package net.scapeemulator.game.model.player.skills.farming.object;

import net.scapeemulator.game.dispatcher.object.ObjectHandler;
import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.action.ReachObjectAction;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.farming.Farming;
import net.scapeemulator.game.model.player.skills.farming.patch.IFarmPatch;
import net.scapeemulator.game.util.HandlerContext;

public class GuideOptionHandler extends ObjectHandler {

    public GuideOptionHandler() {
        super(Option.FOUR);
    }

    @Override
    public void handle(Player player, GroundObject object, String optionName, HandlerContext context) {
        IFarmPatch patch = Farming.patchForObjectId(object.getId());
        if (patch != null) {
            context.stop();
            player.startAction(new ReachObjectAction(1, true, player, object, 1, true) {

                @Override
                public void executeAction() {
                    player.getStateSet().setBitState(3289, patch.getGuideIndex()); // TODO: set selected view id
                    player.getStateSet().setBitState(3288, Skill.getConfigValue(Skill.FARMING));
                    player.getInterfaceSet().openWindow(499);
                    stop();
                }
            });
        }
    }

}