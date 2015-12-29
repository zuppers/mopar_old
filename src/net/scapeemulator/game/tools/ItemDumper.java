package net.scapeemulator.game.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import net.scapeemulator.cache.Cache;
import net.scapeemulator.cache.FileStore;
import net.scapeemulator.cache.def.ItemDefinition;
import net.scapeemulator.game.model.definition.ItemDefinitions;

public final class ItemDumper {

    public static void main(String[] args) throws IOException {

        Cache cache = new Cache(FileStore.open("data/game/cache"));
        ItemDefinitions.init(cache);

        System.out.println("Dumping cache item data...");
        try (BufferedWriter output = new BufferedWriter(new FileWriter("data/game/dumps/cache_item_defs", false))) {
            for (int id = 0; id < ItemDefinitions.count(); id++) {
                ItemDefinition def = ItemDefinitions.forId(id);
                if (def != null) {
                    output.write(id + "\t" + def.getName() + "\t\t");
                    output.write("stacks:" + def.isStackable() + "\t" + "noted:" + !def.getUnnoted() + "\t");
                    output.write("value:" + def.getValue() + "\t" + "noted/unnoted:" + def.getUnnotedItemId() + "/" + def.getNotedItemId());
                    output.write("\t" + Arrays.toString(def.getInventoryOptions()));
                    output.newLine();
                    output.flush();
                }
            }
            output.close();
        }

        System.out.println("Successfully dumped item data.");
    }

}
