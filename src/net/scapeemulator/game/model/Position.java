package net.scapeemulator.game.model;

/**
 * Represents an (immutable) position in-game.
 * Contains x and y-coordinates as well as a height level.
 */
public final class Position {

    private final int x, y, height;

    /**
     * Creates a {@link Position} using (x,y,0) as the coordinates.
     * @param x The x-coordinate of this position.
     * @param y The y-coordinate of this position.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.height = 0;
    }

    /**
     * Creates a {@link Position} using (x,y,height) as the coordinates.
     * @param x The x-coordinate of this position.
     * @param y The y-coordinate of this position.
     * @param height The height of this position.
     */
    public Position(int x, int y, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
    }

    /**
     * Gets the x-coordinate of this position.
     * @return 
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of this position.
     * @return 
     */
    public int getY() {
        return y;
    }

    
    public int getBaseLocalX() {
        return getBaseLocalX(getCentralRegionX());
    }

    public int getBaseLocalY() {
        return getBaseLocalY(getCentralRegionX());
    }

    public int getBaseLocalX(int centralRegionX) {
        return (centralRegionX - 6) * 8;
    }

    public int getBaseLocalY(int centralRegionY) {
        return (centralRegionY - 6) * 8;
    }

    public int getLocalX() {
        return getLocalX(getCentralRegionX());
    }

    public int getLocalY() {
        return getLocalY(getCentralRegionX());
    }

    public int getLocalX(int centralRegionX) {
        return x - ((centralRegionX - 6) * 8);
    }

    public int getLocalY(int centralRegionY) {
        return y - ((centralRegionY - 6) * 8);
    }

    /**
     * Retrieve the X-coordinate of the central of this Region.
     * @return 
     */
    public int getCentralRegionX() {
        return x / 8;
    }

    /**
     * Retrieve the Y-coordinate of the central of this Region.
     * @return 
     */
    public int getCentralRegionY() {
        return y / 8;
    }

    /**
     * Gets the height of this position.
     * @return 
     */
    public int getHeight() {
        return height;
    }

    /**
     * Whether position is within distance of this.
     * This implies the difference in {@link getX()} should be >= -16 && {@literal <}= to 15.
     * And it implies the same for the difference in {@link getY()}.
     * 
     * @param position The position to calculate distance with.
     * @return Whether the position is within the distance as described above.
     */
    public boolean isWithinDistance(Position position) {
        int deltaX = position.getX() - x;
        int deltaY = position.getY() - y;
        return deltaX >= -16 && deltaX <= 15 && deltaY >= -16 && deltaY <= 15;
    }

    /**
     * Whether position is within scene of this.
     * This implies the difference in {@link getX()} should be >= -52 && {@literal <}= to 51.
     * And it implies the same for the difference in {@link getY()}.
     * 
     * @param position The position to calculate distance with.
     * @return Whether the position is within the distance as described above.
     */
    public boolean isWithinScene(Position position) {
        int deltaX = position.getX() - x;
        int deltaY = position.getY() - y;
        return deltaX >= -52 && deltaX <= 51 && deltaY >= -52 && deltaY <= 51;
    }

    /**
     * Gets the distance between this and other.
     * Uses {@link Math#floor(double)} on the formula Math.sqrt((x1-x2)^2 + (y1-y2)^2)
     * 
     * @param other The other {@link Position} to check the distance with.
     * @return The distance calculated rounded down using {@link Math#floor(double)}
     */
    public int getDistance(Position other) {
        int deltaX = x - other.x;
        int deltaY = y - other.y;
        // TODO will rounding up interfere with other stuff?
        return (int) Math.floor(Math.sqrt(deltaX * deltaX + deltaY * deltaY));
    }

    public int toPackedInt() {
        return (height << 28) | (x << 14) | y;
    }

    public int blockHash() {
        return (x & 0x7) << 4 | y & 0x7;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + height;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    /**
     * Whether this is equal to the provided Object.
     * @param obj The object to check for equality.
     * @return False if obj is null, when {@link getClass()} doesn't match
     *          or when the {@link getHeight()}, the {@link getX()} or the {@link getY()} don't match.
     *          else true.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Position other = (Position) obj;
        if (height != other.height) {
            return false;
        }
        if (x != other.x) {
            return false;
        }
        if (y != other.y) {
            return false;
        }
        return true;
    }

    /**
     * Gets a String using the format "position[x: {@link getX()}, y: {@link getY()}, height: {@link getHeight()}]" of this {@link Position}.
     * @return 
     */
    @Override
    public String toString() {
        return "position[x: " + x + ", y: " + y + ", height: " + height + "]";
    }
}
