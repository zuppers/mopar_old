package net.scapeemulator.game.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import net.scapeemulator.cache.Cache;
import net.scapeemulator.cache.FileStore;
import net.scapeemulator.cache.def.ObjectDefinition;
import net.scapeemulator.game.model.definition.ObjectDefinitions;

public final class ObjectDumper {

    public static void main(String[] args) throws IOException {

        Cache cache = new Cache(FileStore.open("data/game/cache"));
        ObjectDefinitions.init(cache);

        System.out.println("Dumping cache object data...");
        try (BufferedWriter output = new BufferedWriter(new FileWriter("data/game/dumps/cache_object_defs", false))) {
            for (int id = 0; id < ObjectDefinitions.count(); id++) {
                ObjectDefinition def = ObjectDefinitions.forId(id);
                if (def != null) {
                    try {
                        output.write(id + "\t" + def.getName() + "\t\t\t");
                        output.write("len/width:[" + def.getLength() + "/" + def.getWidth() + "]\t" + "solid/impen:" + def.isSolid() + "/"
                                + def.isImpenetrable() + "\t");
                        if (def.getAnimationId() > 0) {
                            output.write("anim[" + def.getAnimationId() + "]\t");
                        }
                        if (def.hasOptions()) {
                            output.write("opts" + Arrays.toString(def.getOptions()) + "\t");
                        }
                        if (def.getValidInteractSides() > 0) {
                            output.write("validInteract[" + def.getValidInteractSides() + "]\t");
                        }
                        if (def.getConfigId() > 0) {
                            output.write("config[" + def.getConfigId() + "]\t");
                        }
                        if (def.getVarbitId() > 0) {
                            output.write("varbit[" + def.getVarbitId() + "]\t");
                        }
                        if(def.getChildIds() != null) {
                            output.write("childids" + Arrays.toString(def.getChildIds()) + "\t");
                        }
                        output.newLine();
                        output.flush();
                    } catch (RuntimeException re) {

                    }
                }
            }
            output.close();
        }

        System.out.println("Successfully dumped item data.");
    }

}
