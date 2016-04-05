package net.scapeemulator.game.model.player.skills.farming.object;

import net.scapeemulator.game.dispatcher.object.ObjectHandler;
import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.farming.Farming;
import net.scapeemulator.game.model.player.skills.farming.patch.IFarmPatch;
import net.scapeemulator.game.util.HandlerContext;

public class OptionOneHandler extends ObjectHandler {

    public OptionOneHandler() {
        super(Option.ONE);
    }

    @Override
    public void handle(Player player, GroundObject object, String optionName, HandlerContext context) {
        IFarmPatch patch = Farming.patchForObjectId(object.getId());
        if(patch != null) {
            //context.stop();
        }
        
    }

}