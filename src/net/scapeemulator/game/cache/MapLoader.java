package net.scapeemulator.game.cache;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipException;

import net.scapeemulator.cache.Cache;
import net.scapeemulator.cache.Container;
import net.scapeemulator.cache.ReferenceTable;
import net.scapeemulator.cache.util.ByteBufferUtils;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.model.npc.stateful.impl.NormalNPC;
import net.scapeemulator.game.model.object.ObjectType;
import net.scapeemulator.game.util.LandscapeKeyTable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MapLoader {

    /**
     * Flags that the tile is below a roof, for removing the roof during rendering.
     */
    public static final int ROOF_FLAG = 0x4;
    public static final int FLAG_CLIP = 0x1;
    public static final int BRIDGE_FLAG = 0x2;

    public static final boolean LOAD_NPCS = false;

    private static final Logger logger = LoggerFactory.getLogger(MapLoader.class);

    private final List<MapListener> listeners = new LinkedList<MapListener>();
    private final Cache cache;
    private final LandscapeKeyTable keyTable;
    private final boolean[][] loaded = new boolean[256][256];

    private ReferenceTable rt;

    public MapLoader(Cache cache, LandscapeKeyTable keyTable) {
        this.cache = cache;
        this.keyTable = keyTable;
    }

    public void addListener(MapListener listener) {
        listeners.add(listener);
    }

    public void load(boolean loadAll) throws IOException {
        rt = ReferenceTable.decode(Container.decode(cache.getStore().read(255, 5)).getData());
        if (loadAll) {
            logger.info("Reading all map and landscape files...");
            for (int x = 0; x < loaded.length; x++) {
                for (int y = 0; y < loaded[x].length; y++) {
                    load(x, y);
                }
            }
        }
    }

    public void load(int mX, int mY) {
        for (int x = mX - 4; x <= mX + 4 && x < loaded.length; x++) {
            if (x < 0) {
                continue;
            }
            for (int y = mY - 4; y <= mY + 4 && y < loaded[x].length; y++) {
                if (y < 0) {
                    continue;
                }
                if (loaded[x][y]) {
                    continue;
                }
                try {
                    int landscapeId = rt.getEntryId("l" + x + "_" + y);
                    if (landscapeId != -1) {
                        readLandscape(x, y, landscapeId);
                    }

                    int mapId = rt.getEntryId("m" + x + "_" + y);
                    if (mapId != -1) {
                        readMap(x, y, mapId);
                    }

                    int npcId = rt.getEntryId("n" + x + "_" + y);
                    if (LOAD_NPCS && npcId != -1) {
                        readNPC(x, y, npcId);
                    }

                    loaded[x][y] = true;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void readLandscape(int x, int y, int fileId) throws IOException {
        ByteBuffer buffer = cache.getStore().read(5, fileId);

        int[] keys = keyTable.getKeys(x, y);
        try {
            buffer = Container.decode(buffer, keys).getData();
        } catch (ZipException ze) {
            System.err.println(ze.getMessage() + " readLandscape(" + x + ", " + y + ", " + fileId + ")");
        }

        int id = -1;

        while (true) {
            int deltaId = ByteBufferUtils.getSmart(buffer);
            if (deltaId == 0) {
                break;
            }

            id += deltaId;

            int pos = 0;

            while (true) {

                int deltaPos = ByteBufferUtils.getSmart(buffer);
                if (deltaPos == 0) {
                    break;
                }

                pos += deltaPos - 1;

                int localX = (pos >> 6) & 0x3F;
                int localY = pos & 0x3F;
                int height = (pos >> 12) & 0x3;

                int temp = buffer.get() & 0xFF;
                int type = temp >> 2;
                int rotation = temp & 0x3;

                Position position = new Position(x * 64 + localX, y * 64 + localY, height);

                for (MapListener listener : listeners) {
                    listener.objectDecoded(id, rotation, ObjectType.forId(type), position);
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private void readMap(int x, int y, int id) throws IOException {
        ByteBuffer buffer = cache.read(5, id).getData();

        for (int plane = 0; plane < 4; plane++) {
            for (int localX = 0; localX < 64; localX++) {
                for (int localY = 0; localY < 64; localY++) {

                    Position position = new Position(x * 64 + localX, y * 64 + localY, plane);

                    /* The tile variables */
                    int flags = 0;

                    for (;;) {
                        int config = buffer.get() & 0xFF;

                        if (config == 0) {
                            for (MapListener listener : listeners) {
                                listener.tileDecoded(flags, position);
                            }
                            break;
                        } else if (config == 1) {
                            int i = buffer.get() & 0xFF;

                            for (MapListener listener : listeners) {
                                listener.tileDecoded(flags, position);
                            }
                            break;
                        } else if (config <= 49) {
                            int i = buffer.get() & 0xFF;
                        } else if (config <= 81) {
                            flags = config - 49;
                        }
                    }
                }
            }
        }
    }

    private void readNPC(int x, int y, int fileId) throws IOException {
        ByteBuffer buffer = cache.read(5, fileId).getData();
        while (buffer.hasRemaining()) {
            int compressedData = buffer.getShort() & 0xFFFFF;
            int height = compressedData >> 14;
            int localX = 63 & compressedData >> 7;
            int localY = compressedData & 63;
            int npcId = buffer.getShort();
            NPC npc = new NormalNPC(npcId);
            npc.setPosition(new Position((x * 64) + localX, (y * 64) + localY, height));
            World.getWorld().addNpc(npc);
        }
    }

}
