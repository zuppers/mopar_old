package net.scapeemulator.game.model.player.skills.prayer;

import net.scapeemulator.game.item.ItemInteractHandler;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;

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
    public void handle(Player player, SlottedItem item) {
        player.startAction(new BuryBoneAction(player, bone, item));
    }

}