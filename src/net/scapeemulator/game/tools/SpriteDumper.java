package net.scapeemulator.game.tools;

import java.io.File;
import javax.imageio.ImageIO;

import net.scapeemulator.cache.Cache;
import net.scapeemulator.cache.Container;
import net.scapeemulator.cache.FileStore;
import net.scapeemulator.cache.ReferenceTable;
import net.scapeemulator.cache.def.Sprite;

public class SpriteDumper {

    public static void main(String[] args) throws Exception {
        Cache cache = new Cache(FileStore.open("data/game/cache"));
        int count = 0;

        Container tableContainer = Container.decode(cache.getStore().read(255, 9));
        ReferenceTable table = ReferenceTable.decode(tableContainer.getData());

        int files = table.capacity();
        Sprite[] definitions = new Sprite[files];
        System.out.println(files);
        for (int file = 0; file < files; file++) {
            ReferenceTable.Entry entry = table.getEntry(file);
            if (entry == null) {
                System.out.println("NULL ENTRY");
                continue;
            }
            for (int member = 0; member < entry.capacity(); member++) {
                ReferenceTable.ChildEntry childEntry = entry.getEntry(member);
                if (childEntry == null) {
                    System.out.println("no child");
                    continue;
                }
                
                if(member != 0) 
                //definitions[file] = Sprite.decode(cache.read(8, file, member, false));
                count++;
                break;
            }
        }
        System.out.println(count);
        /*
        System.out.println("Loaded " + count + " sprites. Dumping...");

        try {
            for (int i = 0; i < definitions.length; i++) {
                Sprite sprite = definitions[i];
                if (sprite == null) {
                    continue;
                }
                for (int idx = 0; idx < sprite.size(); idx++) {
                    File outputFile = new File("data/game/dumps/sprites/gif/" + i + "_" + idx + ".gif");
                    ImageIO.write(sprite.getFrame(idx), "gif", outputFile);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        cache.close();*/
        System.out.println("Successfully dumped sprites.");
    }
}