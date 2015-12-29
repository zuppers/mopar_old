package net.scapeemulator.game.model.player.skills.runecrafting;

import net.scapeemulator.game.dispatcher.item.ItemOnObjectHandler;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;

/**
 * @author David Insley
 */
public class TalismanOnRuinsHandler extends ItemOnObjectHandler {

    private final RCAltar altar;

    public TalismanOnRuinsHandler(RCAltar altar) {
        super(altar.getTalismanId(), altar.getRuinsId());
        this.altar = altar;
    }

    @Override
    public void handle(Player player, GroundObject object, SlottedItem item) {
        player.startAction(new RuinsTeleportAction(player, altar, object, RuinsTeleportAction.Type.TALISMAN));
    }

}