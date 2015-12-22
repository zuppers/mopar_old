package net.scapeemulator.game.model.player.skills.construction.room;

import java.util.List;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.object.ObjectGroup;
import net.scapeemulator.game.model.player.skills.construction.hotspot.FurnitureHotspotType;

/**
 * @author David Insley
 */
public enum RoomType {

    /* @formatter:off */
    NONE(0, -1, -1, -1, false, -1, ValidBuild.NONE),
    GRASS(1, 0, 1864, 5056, false, -1, ValidBuild.NONE),
    PARLOUR(2, 1, 1856, 5112, 160, ValidBuild.ANY),
    GARDEN(3, 1, 1856, 5064, false, 161, ValidBuild.GROUND_ONLY),
    KITCHEN(4, 5, 1872, 5112, 162, ValidBuild.ANY),
    DINING_ROOM(5, 10, 1888, 5112, 163, ValidBuild.ANY),
    WORKSHOP(6, 15, 1856, 5096, 164, ValidBuild.ANY),
    BEDROOM(7, 20, 1904, 5112, 165, ValidBuild.ANY),
    SKILL_HALL(8, 25, 1864, 5104, 166, ValidBuild.ANY),
    SKILL_HALL_DOWN(29, 25, 1880, 5104, -1, ValidBuild.ANY),
    GAMES_ROOM(10, 30, 1896, 5088, 167, ValidBuild.ANY),
    COMBAT_ROOM(11, 32, 1880, 5088, 168, ValidBuild.ANY),
    QUEST_HALL(12, 35, 1896, 5104, 169, ValidBuild.ANY),
    QUEST_HALL_DOWN(28, 35, 1912, 5104, -1, ValidBuild.ANY),
    STUDY(13, 40, 1888, 5096, 170, ValidBuild.ANY),
    COSTUME_ROOM(14, 42, 1904, 5064, 171, ValidBuild.ANY),
    CHAPEL(15, 45, 1872, 5096, 172, ValidBuild.ANY),
    PORTAL_CHAMBER(16, 50, 1864, 5088, 173, ValidBuild.ANY),
    FORMAL_GARDEN(17, 55, 1872, 5064, false, 174, ValidBuild.GROUND_ONLY),
    THRONE_ROOM(18, 60, 1904, 5096, 175, ValidBuild.GROUND_ONLY),
    
    DUNGEON_CLEAR(19, 0, 1880, 5056, -1, ValidBuild.DUNGEON_ONLY),
    OUBLIETTE(20, 65, 1904, 5080, 176, ValidBuild.DUNGEON_ONLY),
    DUNGEON_CORRIDOR(21, 70, 1888, 5080, 177, ValidBuild.DUNGEON_ONLY),
    DUNGEON_JUNCTION(22, 70, 1856, 5080, 178, ValidBuild.DUNGEON_ONLY),
    DUNGEON_STAIRS(23, 70, 1872, 5080, 179, ValidBuild.DUNGEON_ONLY),
    TREASURE_ROOM(24, 75, 1912, 5088, 180, ValidBuild.DUNGEON_ONLY),
    
    ROOF(25, 0, 1864, 5072, -1, ValidBuild.NONE),
    ROOF_TRI(26, 0, 1880, 5072, -1, ValidBuild.NONE),
    ROOF_QUAD(27, 0, 1896, 5072, -1, ValidBuild.NONE);
    /* @formatter:on */

    private enum ValidBuild {
        NONE,
        DUNGEON_ONLY,
        ANY,
        GROUND_ONLY
    }

    private final int id;
    private final int srcX;
    private final int srcY;
    private final boolean solid;
    private final int interfaceId;
    private final ValidBuild validBuild;
    private final GroundObject[][][] hotspots;

    private RoomType(int id, int level, int srcX, int srcY, int interfaceId, ValidBuild validBuild) {
        this(id, level, srcX, srcY, true, interfaceId, validBuild);
    }

    private RoomType(int id, int level, int srcX, int srcY, boolean solid, int interfaceId, ValidBuild validBuild) {
        this.id = id;
        this.srcX = srcX;
        this.srcY = srcY;
        this.solid = solid;
        this.interfaceId = interfaceId;
        this.validBuild = validBuild;
        hotspots = new GroundObject[Room.ROOM_SIZE][Room.ROOM_SIZE][ObjectGroup.values().length];
        for (int roomX = 0; roomX < Room.ROOM_SIZE; roomX++) {
            for (int roomY = 0; roomY < Room.ROOM_SIZE; roomY++) {
                List<GroundObject> objs = World.getWorld().getGroundObjects().getAll(new Position(srcX + roomX, srcY + roomY, 0));
                if (objs != null) {
                    for (GroundObject obj : objs) {
                        int group = obj.getType().getObjectGroup().getId();
                        FurnitureHotspotType type = FurnitureHotspotType.forObjectId(obj.getId());
                        if (type != null) {
                            hotspots[roomX][roomY][group] = obj;
                        }
                    }
                }
            }
        }

    }

    public static RoomType forId(int id) {
        for (RoomType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
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

    public int getId() {
        return id;
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

    public boolean validBuild(int height) {
        switch (validBuild) {
        case ANY:
            return height <= 2;
        case DUNGEON_ONLY:
            return height == 0;
        case GROUND_ONLY:
            return height == 1;
        default:
            return false;
        }
    }

    public GroundObject[] getHotspotObjs(int x, int y) {
        return hotspots[x][y];
    }

}
