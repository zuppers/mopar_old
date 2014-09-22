package net.scapeemulator.game.model.player.minigame.stealingcreation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.RegionPalette;
import net.scapeemulator.game.model.player.RegionPalette.Tile;
import net.scapeemulator.game.model.player.RegionPalette.Tile.Rotation;

/**
 * @author David
 */
public class StealingCreation {

    public static final StealingCreation sc = new StealingCreation(9);
    
    public static final int ARENA_X = 4200;
    public static final int ARENA_Y = 4200;

    private static final int SPAWN_AREA_SIZE = 3;
    private final int paletteOffset;
    private final int arenaSize;
    private final Node[][] arena;

    public StealingCreation(int arenaSize) {
        if (arenaSize < 5 || arenaSize > 9 || arenaSize % 2 == 0) {
            throw new IllegalArgumentException("arena size must 5, 7, or 9");
        }
        this.arenaSize = arenaSize;
        arena = new Node[arenaSize][arenaSize];
        paletteOffset = (13 - arenaSize) / 2;
        createArena();
    }

    public void createArena() {
        Set<Node> randomNodes = new HashSet<>();

        for (int classLevel = 1; classLevel <= 5; classLevel++) {
            randomNodes.add(new Node(NodeType.POOL, classLevel));
            randomNodes.add(new Node(NodeType.ROCK, classLevel));
            randomNodes.add(new Node(NodeType.SWARM, classLevel));
            randomNodes.add(new Node(NodeType.TREE, classLevel));
        }
        
        for (int fogIndex = 0; fogIndex < 5; fogIndex++) {
            randomNodes.add(new Node(NodeType.FOG));
        }

        List<Position> openNodes = new ArrayList<>();
        List<Position> botLeftNodes = new ArrayList<>();
        List<Position> topRightNodes = new ArrayList<>();

        for (int x = 0; x < arenaSize; x++) {
            for (int y = 0; y < arenaSize; y++) {
                arena[x][y] = new Node(NodeType.GROUND);
                // Check if we are in the bottom left spawn area
                if (x < SPAWN_AREA_SIZE && y < SPAWN_AREA_SIZE) {
                    // If we are inside the spawn area but outside the spawn room this is a valid
                    // spot for the furnace or lvl 1 collection
                    if (x >= 1 || y >= 1) {
                        botLeftNodes.add(new Position(x, y));
                    }
                } else if (x >= arenaSize - SPAWN_AREA_SIZE && y >= arenaSize - SPAWN_AREA_SIZE) {
                    // Check if we are in the top right spawn area

                    // If we are inside the spawn area but outside the spawn room we can put the
                    // furnace or collection here
                    if (x < arenaSize - 1 || y < arenaSize - 1) {
                        topRightNodes.add(new Position(x, y));
                    }
                } else {
                    // This means we are not in a spawn area. Normal nodes can be put here
                    openNodes.add(new Position(x, y));
                }
            }
        }

        // Bottom left furnace and collection
        Position spawnAreaNode = botLeftNodes.remove((int) (Math.random() * botLeftNodes.size()));
        arena[spawnAreaNode.getX()][spawnAreaNode.getY()] = new Node(NodeType.FURNACE);
        spawnAreaNode = botLeftNodes.remove((int) (Math.random() * botLeftNodes.size()));
        arena[spawnAreaNode.getX()][spawnAreaNode.getY()] = new Node(NodeType.POOL, 1);

        // Top right furnace and collection
        spawnAreaNode = topRightNodes.remove((int) (Math.random() * topRightNodes.size()));
        arena[spawnAreaNode.getX()][spawnAreaNode.getY()] = new Node(NodeType.FURNACE);
        spawnAreaNode = topRightNodes.remove((int) (Math.random() * topRightNodes.size()));
        arena[spawnAreaNode.getX()][spawnAreaNode.getY()] = new Node(NodeType.POOL, 1);

        for (Node type : randomNodes) {
            Position randomSpot = openNodes.remove((int) (Math.random() * openNodes.size()));
            arena[randomSpot.getX()][randomSpot.getY()] = type;
        }

        arena[0][0] = new Node(NodeType.BASE);
        arena[arenaSize - 1][arenaSize - 1] = new Node(NodeType.BASE, Rotation.CW_180);


    }

    public void loadArena(Player player) {
        new Node(null, 5);
    }

    public RegionPalette getRegionPalette() {
        RegionPalette palette = new RegionPalette();
        for (int x = 0; x < arenaSize; x++) {
            for (int y = 0; y < arenaSize; y++) {
                if (arena[x][y] != null) {
                    palette.setTile(0, x + paletteOffset, y + paletteOffset, arena[x][y].toTile());
                }
            }
        }

        return palette;
    }

    private class Node {
        private final NodeType type;
        private final Rotation rotation;
        private final int classLevel;

        Node(NodeType type) {
            this(type, 0);
        }

        Node(NodeType type, Rotation rotation) {
            this(type, rotation, 0);
        }

        Node(NodeType type, int classLevel) {
            this(type, Rotation.NONE, classLevel);
        }

        Node(NodeType type, Rotation rotation, int classLevel) {
            if (classLevel > 5 || classLevel < 0) {
                throw new IllegalArgumentException();
            }
            this.type = type;
            this.rotation = rotation;
            this.classLevel = classLevel;
        }

        Tile toTile() {
            return new Tile(type.getX() + (classLevel != 0 ? ((5 - classLevel) * 8) : 0), type.getY(), 0, rotation);
        }
    }
}
