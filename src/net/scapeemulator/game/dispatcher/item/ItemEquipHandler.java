package net.scapeemulator.game.dispatcher.item;

import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.player.Equipment;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.util.HandlerContext;

/**
 * @author David Insley
 */
public class ItemEquipHandler extends ItemHandler {

    public ItemEquipHandler() {
        super(Option.TWO);
    }

    @Override
    public void handle(Player player, SlottedItem item, String option, HandlerContext context) {
        if (option.equals("equip") || option.equals("wield") || option.equals("wear")) {
            Equipment.equip(player, item.getSlot());
            context.stop();
        }
    }

}
