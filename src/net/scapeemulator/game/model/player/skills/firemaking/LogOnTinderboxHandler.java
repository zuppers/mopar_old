package net.scapeemulator.game.model.player.skills.firemaking;

import net.scapeemulator.game.dispatcher.item.ItemOnItemHandler;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.inventory.Inventory;

/**
 * @author David Insley
 */
public class LogOnTinderboxHandler extends ItemOnItemHandler {

    private final Log log;

    public LogOnTinderboxHandler(Log log) {
        super(log.getItemId(), Firemaking.TINDERBOX);
        this.log = log;
    }

    @Override
    public void handle(Player player, Inventory inventoryOne, Inventory inventoryTwo, SlottedItem itemOne, SlottedItem itemTwo) {
        if (inventoryOne != player.getInventory() || inventoryTwo != player.getInventory()) {
            return;
        }

        // We assume ItemOnItemDispatcher has correctly checked to make sure the player actually has
        // the items in the right place
        player.startAction(new FiremakingAction(player, log, itemOne.getItem().getId() == log.getItemId() ? itemOne : itemTwo));

    }

}