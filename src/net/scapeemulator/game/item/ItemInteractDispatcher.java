package net.scapeemulator.game.item;

import java.util.HashMap;
import java.util.Map;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.inventory.Inventory;

/**
 * Created by David Insley
 */
public final class ItemInteractDispatcher {

    private Map<Integer, ItemInteractHandler> handlers = new HashMap<>();

    public ItemInteractDispatcher() {
    }

    public void bind(ItemInteractHandler handler) {
        handlers.put(handler.getItemId(), handler);
    }

    public void unbindAll() {
        handlers.clear();
    }

    public void handle(Player player, int itemId, int slot) {
        if (player.actionsBlocked()) {
            return;
        }
        Inventory inventory = player.getInventorySet().getInventory();

        if (!checkInventory(inventory, slot, itemId)) {
            return;
        }

        ItemInteractHandler handler = handlers.get(itemId);
        if (handler != null) {
            SlottedItem item = new SlottedItem(slot, inventory.get(slot));
            handler.handle(player, item);
        } else {
            player.sendMessage("Nothing interesting happens.");
        }
    }

    private static boolean checkInventory(Inventory inventory, int slot, int itemId) {
        return inventory.get(slot) != null && inventory.get(slot).getId() == itemId;
    }

}
