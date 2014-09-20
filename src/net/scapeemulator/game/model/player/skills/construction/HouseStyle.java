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
    TROPICAL_WOOD(40, 0, true),
    FANCY_STONE(50, 1, true);

    private final int level;
    private final int height;
    private final boolean xMod;

    private HouseStyle(int level, int height) {
        this(level, height, false);
    }

    private HouseStyle(int level, int height, boolean xMod) {
        this.level = level;
        this.height = height;
        this.xMod = xMod;
    }

    public int getLevel() {
        return level;
    }

    public Position getRoomPosition(RoomType room) {
        return new Position(room.getX() + (xMod ? 64 : 0), room.getY(), height);
    }
}
