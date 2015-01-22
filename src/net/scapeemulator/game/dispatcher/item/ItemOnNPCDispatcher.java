package net.scapeemulator.game.dispatcher.item;

import java.util.HashMap;
import java.util.Map;

import net.scapeemulator.game.GameServer;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.inventory.Inventory;

/**
 * @author David Insley
 */
public final class ItemOnNPCDispatcher {

    private Map<Integer, ItemOnNPCHandler> handlers = new HashMap<>();

    public static ItemOnNPCDispatcher getInstance() {
        return GameServer.getInstance().getMessageDispatcher().getItemOnNPCDispatcher();
    }

    public void bind(ItemOnNPCHandler handler) {
        int hash = getHash(handler.getItemId(), handler.getNPCId());
        handlers.put(hash, handler);
    }

    public void handle(Player player, int itemId, int slot, int npcIndex, int hash) {
        if (player.actionsBlocked()) {
            return;
        }
        NPC npc = World.getWorld().getNpcs().get(npcIndex);
        if (npc == null || !player.getPosition().isWithinScene(npc.getPosition()) || npc.isHidden()) {
            return;
        }
        Inventory inventory = player.getInventorySet().get(hash);

        if (inventory == null || inventory != player.getInventory() || !inventory.verify(slot, itemId)) {
            return;
        }

        SlottedItem item = new SlottedItem(slot, inventory.get(slot));
        ItemOnNPCHandler handler = handlers.get(getHash(itemId, npc.getType()));

        if (handler != null) {
            handler.handle(player, item, npc);
        } else {
            player.sendMessage("Nothing interesting happens.");
        }
    }

    private static int getHash(int itemId, int npcId) {
        return itemId << 16 | npcId;
    }
}
