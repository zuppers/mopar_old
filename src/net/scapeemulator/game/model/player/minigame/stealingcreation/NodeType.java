package net.scapeemulator.game.model.player.minigame.stealingcreation;

/**
 * @author David Insley
 */
public enum NodeType {
    GROUND(1928, 5720),
    BASE(1920, 5696),
    FURNACE(1920, 5712),
    ALTAR(1928, 5712),
    FOG(1920, 5720),
    RIFT(1920, 5728),
    WALL(1928, 5728),
    TREE(1936, 5696),
    SWARM(1936, 5704),
    POOL(1936, 5712),
    ROCK(1936, 5720),
    MOUNTAIN(1936, 5728);
    
    private final int x;
    private final int y;

    private NodeType(int x, int y) {
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
