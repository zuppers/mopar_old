
package net.scapeemulator.game.model.player.skills.construction;

/**
 * @author David Insley
 */
public enum RoomType {
    GRASS(0, 1864, 5056),
    GARDEN(1, 1856, 5064),
    PARLOUR(1, 1856, 5112),
    KITCHEN(5, 1872, 5112),
    DINING_ROOM(10, 1888, 5112),
    WORKSHOP(15, 1856, 5096),
    BEDROOM(20, 1904, 5112),
    SKILL_HALL(25, 1864, 5104), //  up stairs and rug
    //SKILL_HALL(25, 1880, 5104)  up and down stairs and rug
    GAMES_ROOM(30, 1896, 5088),
    COMBAT_ROOM(32, 1880, 5088),
    QUEST_HALL(35, 1912, 5104), // rug and down stairs space
    // Menagerie 37
    STUDY(40, 1888, 5096),
    COSTUME_ROOM(42, 1904, 5064),
    CHAPEL(45, 1872, 5096),
    PORTAL_CHAMBER(50, 1864, 5088),
    FORMAL_GARDEN(55, 1872, 5064),
    THRONE_ROOM(60, 1904, 5096),
    
    DUNGEON_CLEAR(0, 1880, 5056),
    OUBLIETTE(65, 1904, 5080),
    DUNGEON_CORRIDOR(70, 1888, 5080),
    DUNGEON_JUNCTION(70, 1856, 5080),
    DUNGEON_STAIRS(70, 1872, 5080),
    TREASURE_ROOM(75, 1912, 5088),
    
    ROOF(0, 1864, 5072),
    ROOF_TRI(0, 1880, 5072),
    ROOF_QUAD(0, 1896, 5072);
    
    private final int x;
    private final int y;
    
    private RoomType(int level, int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}
