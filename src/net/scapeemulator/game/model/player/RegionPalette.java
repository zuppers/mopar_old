package net.scapeemulator.game.model.player;

import net.scapeemulator.game.model.Position;

/**
 * Represents a region constructed of <tt>13 x 13 x 4</tt> individual
 * <tt>8 x 8</tt> map tiles taken from other parts of the world.
 * 
 * @author Graham
 * @author David Insley
 */
public class RegionPalette {

    public static final int PALETTE_SIZE = 13;

    private final Tile[][][] tiles = new Tile[4][PALETTE_SIZE][PALETTE_SIZE];

    public void setTile(int height, int x, int y, Tile tile) {
        tiles[height][x][y] = tile;
    }

    public int getHash(int height, int x, int y) {
        return tiles[height][x][y] != null ? tiles[height][x][y].hash : -1;
    }

    public static class Tile {

        public enum Rotation {
            NONE(0),
            CW_90(1),
            CW_180(2),
            CW_270(3);

            private final int id;

            private Rotation(int id) {
                this.id = id;
            }

            public static Rotation forId(int id) {
                id %= 4;
                for (Rotation rotation : values()) {
                    if (rotation.id == id) {
                        return rotation;
                    }
                }
                throw new RuntimeException();
            }

            public int getId() {
                return id;
            }

            public Rotation rotate(Rotation rotate) {
                return forId(rotate.getId() + id);
            }

        }

        private final int hash;

        public Tile(int x, int y) {
            this(x, y, 0, Rotation.NONE);
        }

        public Tile(int x, int y, int height) {
            this(x, y, height, Rotation.NONE);
        }

        public Tile(Position position) {
            this(position.getX(), position.getY(), position.getHeight(), Rotation.NONE);
        }

        public Tile(Position position, Rotation rotation) {
            this(position.getX(), position.getY(), position.getHeight(), rotation);
        }

        public Tile(int x, int y, int height, Rotation rotation) {
            hash = ((x / 8) << 14 | (y / 8) << 3 | (height % 4) << 24 | (rotation.getId() % 4) << 1);
        }

    }

}
