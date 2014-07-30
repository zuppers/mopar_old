package net.scapeemulator.game.model.player;

import java.util.HashMap;
import java.util.Map;

import net.scapeemulator.game.model.player.bank.BankSession;
import net.scapeemulator.game.model.player.interfaces.Interface;
import net.scapeemulator.game.model.player.inventory.Inventory;
import net.scapeemulator.game.model.player.inventory.Inventory.StackMode;
import net.scapeemulator.game.model.player.inventory.InventoryAppearanceListener;
import net.scapeemulator.game.model.player.inventory.InventoryFullListener;
import net.scapeemulator.game.model.player.inventory.InventoryMessageListener;

/**
 * @author Hadyn Richard
 */
public final class InventorySet {

    /**
     * The equipment inventory hash.
     */
    public static final int EQUIPMENT_HASH = Interface.EQUIPMENT << 16 | Interface.EQUIPMENT_CONAINER;

    /**
     * The inventory container hash.
     */
    public static final int INVENTORY_HASH = Interface.INVENTORY << 16 | Interface.INVENTORY_CONTAINER;

    /**
     * The bank container hash.
     */
    public static final int BANK_HASH = Interface.BANK << 16 | Interface.BANK_CONTAINER;

    /**
     * Each of the inventories registered to the inventory set.
     */
    private final Map<Integer, Inventory> inventories = new HashMap<>();

    /**
     * Constructs a new {@link InventorySet};
     */
    public InventorySet(Player player) {

        /* Initialize and register the equipment to the set */
        Inventory equipment = new Inventory(player, 14);
        equipment.addListener(new InventoryMessageListener(player, Interface.EQUIPMENT, Interface.EQUIPMENT_CONAINER, 94));
        equipment.addListener(new InventoryFullListener(player, "equipment"));
        equipment.addListener(new InventoryAppearanceListener(player));
        register(equipment, EQUIPMENT_HASH);

        /* Initialize and register the inventory to the set */
        Inventory inventory = new Inventory(player, 28);
        inventory.addListener(new InventoryMessageListener(player, Interface.INVENTORY, Interface.INVENTORY_CONTAINER, 93));
        inventory.addListener(new InventoryFullListener(player, "inventory"));
        register(inventory, INVENTORY_HASH);

        Inventory bank = new Inventory(player, BankSession.BANK_SLOTS, StackMode.ALWAYS, false);
        bank.addListener(new InventoryMessageListener(player, -1, -1, 95));
        bank.addListener(new InventoryFullListener(player, "bank"));
        bank.lock();
        register(bank, BANK_HASH);
    }

    /**
     * Registers an inventory to the inventory set.
     * 
     * @param inventory The inventory to register.
     * @param parent The parent widget id.
     * @param child The child widget id.
     */
    public void register(Inventory inventory, int parent, int child) {
        register(inventory, getHash(parent, child));
    }

    /**
     * Registers an inventory to the inventory set.
     * 
     * @param inventory The inventory to register.
     * @param hash The hash of the inventory.
     */
    public void register(Inventory inventory, int hash) {
        inventories.put(hash, inventory);
    }

    public Inventory get(int hash) {
        return inventories.get(hash);
    }

    /**
     * Gets the inventory container.
     * 
     * @return The inventory.
     */
    public Inventory getInventory() {
        return inventories.get(INVENTORY_HASH);
    }

    /**
     * Gets the equipment inventory.
     * 
     * @return The equipment.
     */
    public Inventory getEquipment() {
        return inventories.get(EQUIPMENT_HASH);
    }

    /**
     * Gets the bank inventory.
     * 
     * @return the players bank
     */
    public Inventory getBank() {
        return inventories.get(BANK_HASH);
    }

    /**
     * Calculates a widget hash.
     * 
     * @param parent The parent id.
     * @param child The child id.
     * @return The calculated hash.
     */
    private static int getHash(int parent, int child) {
        return parent << 16 | child;
    }
}
