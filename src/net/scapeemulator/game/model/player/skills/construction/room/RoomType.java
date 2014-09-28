package net.scapeemulator.game.model.player.skills.construction.room;

import java.util.List;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.object.ObjectGroup;
import net.scapeemulator.game.model.player.skills.construction.hotspot.HotspotType;

/**
 * @author David Insley
 */
public enum RoomType {

    /* @formatter:off */
    NONE(-1, -1, -1, false, -1),
    GRASS(0, 1864, 5056, false, -1),
    PARLOUR(1, 1856, 5112, 160),
    GARDEN(1, 1856, 5064, false, 161),
    KITCHEN(5, 1872, 5112, 162),
    DINING_ROOM(10, 1888, 5112, 163),
    WORKSHOP(15, 1856, 5096, 164),
    BEDROOM(20, 1904, 5112, 165),
    SKILL_HALL(25, 1864, 5104, 166), //  up stairs and rug
    //SKILL_HALL(25, 1880, 5104, 166)  up and down stairs and rug
    GAMES_ROOM(30, 1896, 5088, 167),
    COMBAT_ROOM(32, 1880, 5088, 168),
    QUEST_HALL(35, 1912, 5104, 169), // rug and down stairs space
    STUDY(40, 1888, 5096, 170),
    COSTUME_ROOM(42, 1904, 5064, 171),
    CHAPEL(45, 1872, 5096, 172),
    PORTAL_CHAMBER(50, 1864, 5088, 173),
    FORMAL_GARDEN(55, 1872, 5064, false, 174),
    THRONE_ROOM(60, 1904, 5096, 175),
    
    DUNGEON_CLEAR(0, 1880, 5056, -1),
    OUBLIETTE(65, 1904, 5080, 176),
    DUNGEON_CORRIDOR(70, 1888, 5080, 177),
    DUNGEON_JUNCTION(70, 1856, 5080, 178),
    DUNGEON_STAIRS(70, 1872, 5080, 179),
    TREASURE_ROOM(75, 1912, 5088, 180),
    
    ROOF(0, 1864, 5072, -1),
    ROOF_TRI(0, 1880, 5072, -1),
    ROOF_QUAD(0, 1896, 5072, -1);
    /* @formatter:on */

    private final int srcX;
    private final int srcY;
    private final boolean solid;
    private final int interfaceId;

    private final GroundObject[][][] hotspots;

    private RoomType(int level, int srcX, int srcY, int interfaceId) {
        this(level, srcX, srcY, true, interfaceId);
    }

    private RoomType(int level, int srcX, int srcY, boolean solid, int interfaceId) {
        this.srcX = srcX;
        this.srcY = srcY;
        this.solid = solid;
        this.interfaceId = interfaceId;
        hotspots = new GroundObject[Room.ROOM_SIZE][Room.ROOM_SIZE][ObjectGroup.values().length];
        for (int roomX = 0; roomX < Room.ROOM_SIZE; roomX++) {
            for (int roomY = 0; roomY < Room.ROOM_SIZE; roomY++) {
                List<GroundObject> objs = World.getWorld().getGroundObjects().getAll(new Position(srcX + roomX, srcY + roomY, 0));
                if (objs != null) {
                    for (GroundObject obj : objs) {
                        int group = obj.getType().getObjectGroup().getId();
                        HotspotType type = HotspotType.forObjectId(obj.getId());
                        if (type != null) {
                            hotspots[roomX][roomY][group] = obj;
                        }
                    }
                }
            }
        }

    }

    public static RoomType forInterfaceId(int interfaceId) {
        if (interfaceId < 160 || interfaceId > 180) {
            return null;
        }
        for (RoomType type : values()) {
            if (type.interfaceId == interfaceId) {
                return type;
            }
        }
        return null;
    }

    public int getX() {
        return srcX;
    }

    public int getY() {
        return srcY;
    }

    public boolean isSolid() {
        return solid;
    }

    public GroundObject[] getHotspotObjs(int x, int y) {
        return hotspots[x][y];
    }

}
