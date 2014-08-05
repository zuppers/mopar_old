package net.scapeemulator.game.model.player.skills.prayer;

import net.scapeemulator.game.dispatcher.item.ItemHandler;
import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.inventory.Inventory;
import net.scapeemulator.game.util.HandlerContext;

/**
 * @author David Insley
 */
public class BuryBoneHandler extends ItemHandler {

    public BuryBoneHandler() {
        super(Option.ONE);
    }

    @Override
    public void handle(Player player, Inventory inventory, SlottedItem item, String option, HandlerContext context) {
        Bone bone = Bone.forId(item.getItem().getId());
        if (bone != null) {
            context.stop();
            player.startAction(new BuryBoneAction(player, bone, item));
        }
    }

}