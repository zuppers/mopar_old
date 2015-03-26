package net.scapeemulator.game.model.npc.drops;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.scapeemulator.cache.def.NPCDefinition;

public class DropTables {

    private static final Map<String, TableDefinition> dropTables = new HashMap<>();
    private static final Map<NPCDefinition, TableDefinition> npcMap = new HashMap<>();

    public static TableDefinition newTable(String name) {
        if (dropTables.containsKey(name)) {
            throw new IllegalArgumentException("Table " + name + " already exists!");
        }
        TableDefinition newTable = new TableDefinition(name);
        dropTables.put(name, newTable);
        return newTable;
    }

    public static TableDefinition getTable(String name) {
        return dropTables.get(name);
    }

    public static TableDefinition getTable(NPCDefinition npc) {
        return npcMap.get(npc);
    }

    public static Collection<TableDefinition> getTables() {
        return dropTables.values();
    }

    public static void removeTable(String name) {
        TableDefinition removed = dropTables.remove(name);
        if (removed != null) {
            removed.clear();
        }
    }

    public static void clear() {
        dropTables.clear();
        npcMap.clear();
    }

    public static class TableDefinition {

        private String name;
        private DropTable table;
        private Set<NPCDefinition> npcs;

        private TableDefinition(String name) {
            this.name = name;
            table = new DropTable();
            npcs = new TreeSet<>(new Comparator<NPCDefinition>() {

                @Override
                public int compare(NPCDefinition def1, NPCDefinition def2) {
                    if (def1.getId() == def2.getId()) {
                        return 0;
                    }
                    return def1.getId() > def2.getId() ? 1 : -1;
                }
            });
        }

        public void addNPCDefinitions(Collection<NPCDefinition> defs) {
            for (NPCDefinition def : defs) {
                addNPCDefinition(def);
            }
        }

        public void addNPCDefinition(NPCDefinition def) {
            TableDefinition oldDef = npcMap.get(def);
            if (oldDef != null) {
                oldDef.removeNPCDefinition(def);
            }
            npcs.add(def);
            npcMap.put(def, this);
        }

        public void removeNPCDefinitions(Collection<NPCDefinition> defs) {
            for (NPCDefinition def : defs) {
                removeNPCDefinition(def);
            }
        }

        public void removeNPCDefinition(NPCDefinition def) {
            npcs.remove(def);
            npcMap.remove(def);
        }

        public void clear() {
            for (NPCDefinition def : npcs) {
                npcMap.remove(def);
            }
            npcs.clear();
        }

        public NPCDefinition[] getNPCDefinitions() {
            return npcs.toArray(new NPCDefinition[] {});
        }

        public DropTable getTable() {
            return table;
        }

        public String getName() {
            return name;
        }
    }
}
