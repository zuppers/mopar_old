package net.scapeemulator.game.dispatcher.item;

import java.util.HashMap;
import java.util.Map;

import net.scapeemulator.game.GameServer;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.object.GroundObjectList;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.inventory.Inventory;

/**
 * @author Hadyn Richard
 */
public final class ItemOnObjectDispatcher {

    private Map<Integer, ItemOnObjectHandler> handlers = new HashMap<>();

    public ItemOnObjectDispatcher() {
    }

    /**
     * Shortcut method to get the GameServer instance of this dispatcher.
     * 
     * @return the GameServer instance of the ItemOnObjectDispatcher
     */
    public static ItemOnObjectDispatcher getInstance() {
        return GameServer.getInstance().getMessageDispatcher().getItemOnObjectDispatcher();
    }

    public void bind(ItemOnObjectHandler handler) {
        int hash = getHash(handler.getItemId(), handler.getObjectId());
        handlers.put(hash, handler);
    }

    public void unbindAll() {
        handlers.clear();
    }

    public void handle(Player player, int objectId, Position position, int hash, int itemId, int slot) {
        if (player.actionsBlocked()) {
            return;
        }
        GroundObjectList groundObjects = World.getWorld().getGroundObjects();

        GroundObject object = groundObjects.get(objectId, position);
        if (object == null) {
            return;
        }

        Inventory inventory = player.getInventorySet().get(hash);

        /* Validate the inventory */
        if (inventory == null || inventory != player.getInventory() || !inventory.verify(slot, itemId)) {
            return;
        }

        SlottedItem item = new SlottedItem(slot, inventory.get(slot));
        ItemOnObjectHandler handler = handlers.get(getHash(itemId, objectId));

        if (handler != null) {
            handler.handle(player, object, item);
        } else {
            player.sendMessage("Nothing interesting happens.");
        }
    }

    private static int getHash(int itemId, int objectId) {
        return itemId << 16 | objectId;
    }
}
