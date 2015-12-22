package net.scapeemulator.game.model.player.skills.construction;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.player.skills.construction.room.RoomType;
import static net.scapeemulator.game.model.player.skills.construction.DoorType.*;

/**
 * @author David Insley
 */
public enum HouseStyle {

    BASIC_WOOD(1, 0, 13099, BASIC_WOOD_1, BASIC_WOOD_2),
    BASIC_STONE(10, 1, 13091, BASIC_STONE_1, BASIC_STONE_2),
    WHITEWASHED_STONE(20, 2, 13005, WHITEWASHED_STONE_1, WHITEWASHED_STONE_2),
    FREMENNIK_WOOD(30, 3, 13112, FREMENNIK_WOOD_1, FREMENNIK_WOOD_2),
    TROPICAL_WOOD(40, 4, 10816, TROPICAL_WOOD_1, TROPICAL_WOOD_2),
    FANCY_STONE(50, 5, 13117, FANCY_STONE_1, FANCY_STONE_2);

    private final int level;
    private final int id;
    private final int windowId;
    private final DoorType doorType1;
    private final DoorType doorType2;
    
    private HouseStyle(int level, int id, int windowId, DoorType doorType1, DoorType doorType2) {
        this.level = level;
        this.id = id;
        this.windowId = windowId;
        this.doorType1 = doorType1;
        this.doorType2 = doorType2;
    }

    public int getLevel() {
        return level;
    }

    public int getId() {
        return id;
    }
    
    public int getWindowId() {
        return windowId;
    }
    
    public DoorType getDoorType1() {
        return doorType1;
    }
    
    public DoorType getDoorType2() {
        return doorType2;
    }
    
    public Position getRoomPosition(RoomType room) {
        return new Position(room.getX() + (id >= 4 ? 64 : 0), room.getY(), id % 4);
    }  
}
