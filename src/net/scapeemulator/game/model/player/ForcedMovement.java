package net.scapeemulator.game.model.player;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.mob.Direction;

public class ForcedMovement {

    private int direction;
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private int speed1 = 10;
    private int speed2 = 60;
    private Position destination;

    private ForcedMovement() {
    }

    // TODO createExact using exact positions instead of relative

    public static ForcedMovement createRelative(Player player, int xOffset1, int yOffset1, int xOffset2, int yOffset2) {
        ForcedMovement fm = new ForcedMovement();
        Position p = player.getPosition();
        fm.x1 = p.getLocalX() + xOffset1;
        fm.y1 = p.getLocalY() + yOffset1;
        fm.x2 = p.getLocalX() + xOffset2;
        fm.y2 = p.getLocalY() + yOffset2;
        fm.destination = new Position(p.getX() + xOffset2, p.getY() + yOffset2);
        return fm;
    }

    public void setSpeeds(int speed1, int speed2) {
        this.speed1 = speed1;
        this.speed2 = speed2;
    }

    public void setDirection(Direction dir) {
        switch (dir) {
        case NORTH:
            direction = 0;
            break;
        case SOUTH:
            direction = 2;
            break;
        case EAST:
            direction = 1;
            break;
        case WEST:
            direction = 3;
            break;
        default:
            throw new IllegalArgumentException("Only N/S/E/W directions allowed for force movement.");
        }
    }

    public int getDirection() {
        return direction;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public int getSpeed1() {
        return speed1;
    }

    public int getSpeed2() {
        return speed2;
    }

    public Position getDestination() {
        return destination;
    }
}
