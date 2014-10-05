package net.scapeemulator.game.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import net.scapeemulator.cache.Cache;
import net.scapeemulator.cache.FileStore;
import net.scapeemulator.cache.def.VarbitDefinition;
import net.scapeemulator.game.model.definition.VarbitDefinitions;

public final class VarbitDumper {

    public static void main(String[] args) throws IOException {
        VarbitDefinitions.init(new Cache(FileStore.open("data/game/cache")));
        System.out.println("Dumping varbit data...");
        try (BufferedWriter output = new BufferedWriter(new FileWriter("data/game/dumps/varbits", false))) {
            for (int id = 0; id < VarbitDefinitions.count(); id++) {
                VarbitDefinition def = VarbitDefinitions.forId(id);
                if (def != null) {
                    output.write("Varbit " + id + ": state[" + def.getState() + "] hi/lo[" + def.getHighBit() + "/" + def.getLowBit() + "]");
                    output.newLine();
                    output.flush();
                }
            }
            output.close();
        }
        System.out.println("Successfully dumped varbit data.");
    }

}
