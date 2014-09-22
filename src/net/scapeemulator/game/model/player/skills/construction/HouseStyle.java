package net.scapeemulator.game.model.player.skills.construction;

import net.scapeemulator.game.model.Position;

/**
 * @author David Insley
 */
public enum HouseStyle {

    BASIC_WOOD(1, 0),
    BASIC_STONE(10, 1),
    WHITEWASHED_STONE(20, 2),
    FREMENNIK_WOOD(30, 3),
    TROPICAL_WOOD(40, 4),
    FANCY_STONE(50, 5);

    private final int level;
    private final int id;

    private HouseStyle(int level, int id) {
        this.level = level;
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public Position getRoomPosition(RoomType room) {
        return new Position(room.getX() + (id >= 4 ? 64 : 0), room.getY(), id % 4);
    }  
}
