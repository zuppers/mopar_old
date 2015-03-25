package net.scapeemulator.game.dispatcher.item;

import java.util.HashMap;
import java.util.Map;

import net.scapeemulator.game.GameServer;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.inventory.Inventory;

/**
 * @author Hadyn Richard
 */
public final class ItemOnItemDispatcher {

    private Map<Integer, ItemOnItemHandler> handlers = new HashMap<>();

    public ItemOnItemDispatcher() {
    }

    public void bind(ItemOnItemHandler handler) {
        int hash = calculateHash(handler.getItemOne(), handler.getItemTwo());
        if (handlers.containsKey(hash)) {
            System.out.println("Overwriting existing ItemOnItem handler! <id1: " + handler.getItemOne() + "; id2: " + handler.getItemTwo() + ">");
        }
        handlers.put(hash, handler);
    }

    public void unbindAll() {
        handlers.clear();
    }

    public void handle(Player player, int idOne, int idTwo, int hashOne, int hashTwo, int slotOne, int slotTwo) {
        if (player.actionsBlocked()) {
            return;
        }
        Inventory inventory = player.getInventorySet().get(hashOne);
        if (inventory != player.getInventory() || hashOne != hashTwo) {
            return;
        }

        if (!checkInventory(inventory, slotOne, idOne) || !checkInventory(inventory, slotTwo, idTwo)) {
            return;
        }

        ItemOnItemHandler handler = handlers.get(calculateHash(idOne, idTwo));
        if (handler != null) {

            SlottedItem itemOne = new SlottedItem(slotOne, inventory.get(slotOne));
            SlottedItem itemTwo = new SlottedItem(slotTwo, inventory.get(slotTwo));

            /* Swap the items if they are out of place */
            if (idOne != handler.getItemOne()) {
                SlottedItem tempItem = itemOne;
                itemOne = itemTwo;
                itemTwo = tempItem;
            }

            handler.handle(player, itemOne, itemTwo);
        } else {
            player.sendMessage("Nothing interesting happens.");
        }
    }

    private static boolean checkInventory(Inventory inventory, int slot, int itemId) {
        return inventory.get(slot) != null && inventory.get(slot).getId() == itemId;
    }

    static int calculateHash(int itemOne, int itemTwo) {

        int high = itemOne;
        int low = itemTwo;

        if (itemTwo > itemOne) {
            high = itemTwo;
            low = itemOne;
        }

        return high << 16 | low;
    }

    static int[] reverseHash(int hash) {
        int[] ids = new int[2];
        ids[0] = hash & 0xffff;
        ids[1] = hash >> 16;
        return ids;
    }

    public static ItemOnItemDispatcher getInstance() {
        return GameServer.getInstance().getMessageDispatcher().getItemOnItemDispatcher();
    }
}
