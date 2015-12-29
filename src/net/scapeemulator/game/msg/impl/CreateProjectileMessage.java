package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.Message;
import net.scapeemulator.game.util.math.ClientFrameTickConversion;

/**
 * @author Hadyn Richard
 * @author David Insley
 */
public final class CreateProjectileMessage extends Message {

    private final int deltaX;
    private final int deltaY;
    private final int lockon;
    private final int graphic;
    private final int startHeight;
    private final int endHeight;
    private final int startDelay;
    private final int duration;
    private final int angle;
    private final int arc;

    /**
     * @param start
     * @param end
     * @param lockon
     * @param graphic
     * @param startHeight
     * @param endHeight
     * @param startDelay the time to wait after sending the packet to start the projectile graphic
     * @param speed the flat speed of the projectile; automatically increased by distance; lower = faster
     * @param roundTicks if true, will round the duration of the projectile to the nearest game tick, for more accurate delayed actions based on the
     *            projectile
     */
    public CreateProjectileMessage(Position start, Position end, Mob lockon, int graphic, int startHeight, int endHeight, int startDelay, int speed,
            int arc, boolean roundTicks) {
        deltaX = (start.getX() - end.getX()) * -1;
        deltaY = (start.getY() - end.getY()) * -1;
        this.lockon = lockon instanceof Player ? (-lockon.getId() - 1) : (lockon.getId() + 1);
        this.graphic = graphic;
        this.startHeight = startHeight;
        this.endHeight = endHeight;
        this.startDelay = startDelay;
        int duration = startDelay + speed + (5 * start.getDistance(end));
        if (roundTicks) {
            this.duration = ClientFrameTickConversion.ticksToFrames(ClientFrameTickConversion.framesToTicks(duration));
        } else {
            this.duration = duration;
        }
        this.angle = 50;
        this.arc = arc;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public int getLockon() {
        return lockon;
    }

    public int getGraphic() {
        return graphic;
    }

    public int getStartHeight() {
        return startHeight;
    }

    public int getEndHeight() {
        return endHeight;
    }

    public int getStartDelay() {
        return startDelay;
    }

    public int getDurationFrames() {
        return duration;
    }

    public int getDurationTicks() {
        return ClientFrameTickConversion.framesToTicks(duration);
    }

    public int getAngle() {
        return angle;
    }

    public int getArc() {
        return arc;
    }
}
