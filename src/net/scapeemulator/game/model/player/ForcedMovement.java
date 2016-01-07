package net.scapeemulator.game.model.player;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.mob.Direction;
import net.scapeemulator.game.util.math.ClientFrameTickConversion;

public class ForcedMovement {

    private int direction;
    private Position firstPosition;
    private Position secondPosition;
    private int midDuration;
    private int duration;
    private int durationTicks;

    private ForcedMovement() {
    }

    // TODO createExact using exact positions instead of relative

    public static ForcedMovement createRelative(Player player, int xOffset1, int yOffset1, int midDuration, int xOffset2, int yOffset2, int totalDuration) {
        ForcedMovement fm = new ForcedMovement();
        fm.firstPosition = player.getPosition().copy(xOffset1, yOffset1);
        fm.secondPosition = player.getPosition().copy(xOffset2, yOffset2);
        fm.midDuration = ClientFrameTickConversion.ticksToFrames(midDuration);
        fm.duration = ClientFrameTickConversion.ticksToFrames(totalDuration);
        fm.durationTicks = totalDuration;
        return fm;
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

    public Position getFirstPosition() {
        return firstPosition;
    }

    public Position getSecondPosition() {
        return secondPosition;
    }

    public int getMidDuration() {
        return midDuration;
    }

    public int getDuration() {
        return duration;
    }

    public int getDurationTicks() {
        return durationTicks;
    }

}
