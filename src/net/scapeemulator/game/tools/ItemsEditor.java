package net.scapeemulator.game.tools;

import net.scapeemulator.cache.Archive;
import net.scapeemulator.cache.Cache;
import net.scapeemulator.cache.Container;
import net.scapeemulator.cache.FileStore;
import net.scapeemulator.cache.ReferenceTable;
import net.scapeemulator.cache.def.ItemDefinition;

public final class ItemsEditor {

    private static final int ITEM_ID = 4151;

    private static void modifyDefinition(ItemDefinition def) {
        
    }

    public static void main(String[] args) {
        try {
            System.out.println("Loading cache and finding item entry... ");
            Cache cache = new Cache(FileStore.open("data/game/cache"));
            Container tableContainer = Container.decode(cache.getStore().read(255, 19));
            ReferenceTable table = ReferenceTable.decode(tableContainer.getData());
            int fileId = ITEM_ID / 256;
            ReferenceTable.Entry entry = table.getEntry(fileId);

            if (entry == null) {
                System.out.println("No entry found!");
                cache.close();
                return;
            }

            Archive archive = Archive.decode(cache.read(19, fileId).getData(), entry.size());

            int archiveMember = 0;
            for (int member = 0; member < entry.capacity(); member++) {
                ReferenceTable.ChildEntry childEntry = entry.getEntry(member);
                if (childEntry == null) {
                    continue;
                }
                int id = fileId * 256 + member;
                if (id == ITEM_ID) {
                    ItemDefinition definition = ItemDefinition.decode(id, archive.getEntry(archiveMember));
                    System.out.println("Item definition found and decoded, modifying...");
                    modifyDefinition(definition);
                    System.out.println("Writing changes to cache...");
                    cache.write(19, fileId, archiveMember, definition.encode());
                    break;
                }
                archiveMember++;
            }
            System.out.println("Cache item update complete!");
            cache.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
