package net.scapeemulator.game.model.npc.drops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.scapeemulator.game.model.player.Item;

/**
 * Represents a drop table definition, including various sub tables.
 * 
 * @author David Insley
 */
public class DropTable {

    private static final Random RANDOM = new Random();

    private Map<TableType, SubDropTable> tables = new HashMap<>();

    /**
     * Adds a drop table with the default table chance. Overwrites any existing
     * table for that type.
     * 
     * @param type the TableType to add
     */
    public void addTable(TableType type) {
        tables.put(type, new SubDropTable(type.getDefaultChance()));
    }

    /**
     * Adds a drop table with the specified table chance. Overwrites any
     * existing table for that type. The chance should be a value between 0
     * (inclusive) and 1 (exclusive), with lower values being more rare.
     * 
     * @param type the TableType to add
     * @param chance the double chance to use for this table
     */
    public void addTable(TableType type, double chance) {
        if (type == TableType.ALWAYS) {
            throw new IllegalArgumentException("Cannot change drop chance for the always drop table");
        }
        tables.put(type, new SubDropTable(chance));
    }

    /**
     * Adds a drop to the specified table with the special string amount
     * argument. Amount argument should be in the format: "
     * <tt>amt1,amt2,amt3min-amt3max,amt4</tt>". More specifically, amounts
     * should be separated by commas and ranges specified by dashes.
     * 
     * @param type the table to add the item to
     * @param itemId the item id
     * @param amount the special amount identifier
     */
    public void addItem(TableType type, int itemId, String amount) {
        if (!tables.containsKey(type)) {
            throw new IllegalArgumentException("Make sure to add the table before adding items.");
        }
        tables.get(type).addItem(new DropTableItem(itemId, amount));
    }

    /**
     * Adds a drop to the specified table with the given id and amount.
     * 
     * @param type the table to add the item to
     * @param itemId the item id
     * @param amount the item amount
     */
    public void addItem(TableType type, int itemId, int amount) {
        if (!tables.containsKey(type)) {
            throw new IllegalArgumentException("Make sure to add the table before adding items.");
        }
        tables.get(type).addItem(new DropTableItem(itemId, amount));
    }

    /**
     * Adds a drop to the specified table with the default amount 1.
     * 
     * @param type the table to add the item to
     * @param itemId the item id
     */
    public void addItem(TableType type, int itemId) {
        addItem(type, itemId, 1);
    }

    public Map<TableType, SubDropTable> getTables() {
        return tables;
    }

    /**
     * Populates a List with the items this table collection randomly generates.
     * 
     * @return the generated random drop list
     */
    public List<Item> getRandomDrops() {
        List<Item> items = new ArrayList<>();

        // Add all of the always drop items
        SubDropTable activeTable = tables.get(TableType.ALWAYS);
        if (activeTable != null) {
            activeTable.addAll(items);
        }

        // Roll for the normal tables
        double roll = RANDOM.nextDouble();
        activeTable = null;
        for (SubDropTable table : tables.values()) {
            if (table.getChance() < roll || table.isEmpty() || (activeTable != null && activeTable.getChance() < table.getChance())) {
                continue;
            }
            activeTable = table;
        }
        if (activeTable != null) {
            activeTable.addRandom(items);
        }

        // See if they should get an extra drop from the extra table
        activeTable = tables.get(TableType.EXTRA);
        if (activeTable != null) {
            double extraRoll = RANDOM.nextDouble();
            if (extraRoll < activeTable.getChance()) {
                activeTable.addRandom(items);
            }
        }

        return items;
    }

}
