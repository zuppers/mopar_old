package net.scapeemulator.game.dispatcher.item;

import java.util.HashMap;
import java.util.Map;

import net.scapeemulator.game.GameServer;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.grounditem.GroundItemList;
import net.scapeemulator.game.model.grounditem.GroundItemList.GroundItem;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.inventory.Inventory;

/**
 * @author David Insley
 */
public final class ItemOnGroundItemDispatcher {

    private Map<Integer, ItemOnGroundItemHandler> handlers = new HashMap<>();

    public static ItemOnGroundItemDispatcher getInstance() {
        return GameServer.getInstance().getMessageDispatcher().getItemOnGroundItemDispatcher();
    }

    public void bind(ItemOnGroundItemHandler handler) {
        int hash = getHash(handler.getItemId(), handler.getGroundItemId());
        handlers.put(hash, handler);
    }

    public void handle(Player player, int itemId, int slot, int x, int y, int groundItemId, int widgetHash) {
        if (player.actionsBlocked()) {
            return;
        }
        Position position = new Position(x, y, player.getPosition().getHeight());
        if (!player.getPosition().isWithinScene(position)) {
            return;
        }
        GroundItem groundItem = World.getWorld().getGroundItems().get(player, groundItemId, position);
        if (groundItem == null) {
            return;
        }
        Inventory inventory = player.getInventorySet().get(widgetHash);
        if (inventory == null || inventory != player.getInventory() || !inventory.verify(slot, itemId)) {
            return;
        }

        SlottedItem item = new SlottedItem(slot, inventory.get(slot));
        ItemOnGroundItemHandler handler = handlers.get(getHash(itemId, groundItemId));

        if (handler != null) {
            handler.handle(player, item, groundItem);
        } else {
            player.sendMessage("Nothing interesting happens.");
        }
    }

    private static int getHash(int itemId, int groundItemId) {
        return itemId << 16 | groundItemId;
    }
}
