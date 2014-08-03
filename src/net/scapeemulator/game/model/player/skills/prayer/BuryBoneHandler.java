package net.scapeemulator.game.model.player.skills.prayer;

import net.scapeemulator.game.dispatcher.item.ItemInteractHandler;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.interfaces.Interface;
import net.scapeemulator.game.model.player.inventory.Inventory;

/**
 * @author David Insley
 */
public class BuryBoneHandler extends ItemInteractHandler {

    private final Bone bone;

    public BuryBoneHandler(Bone bone) {
        super(bone.getItemId());
        this.bone = bone;
    }

    @Override
    public void handle(Inventory inventory, Player player, SlottedItem item) {
        if(inventory != player.getInventory() || player.getInterfaceSet().getInventory().getCurrentId() != Interface.INVENTORY) {
            return;
        }
        player.startAction(new BuryBoneAction(player, bone, item));
    }

}