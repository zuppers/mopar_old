package net.scapeemulator.game.model.player.skills.construction;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.area.QuadArea;

/**
 * @author David Insley
 */
public enum HousePortal {

    RIMMINGTON(1, 5000),
    TAVERLY(1, 5000),
    POLLNIVNEACH(20, 7500),
    RELLEKKA(30, 10000),
    BRIMHAVEN(40, 20000),
    YANILLE(50, 25000);

    private final int level;
    private final int cost;
    //private final int x;
    //private final int y;
    private QuadArea tpLoc;
    
    private HousePortal(int level, int cost) {
        this.level = level;
        this.cost = cost;
    }

    public int getLevel() {
        return level;
    }

    public int getCost() {
        return cost;
    }

    public Position getRandomTeleport() {
        return tpLoc.randomPosition(0);
    }
}
