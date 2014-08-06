package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.msg.Message;

/**
 * The CameraAngleMessage sets the pitch and yaw of the players camera, but does not lock it. The
 * yaw is the axis players manipulate with the left and right arrow keys, the pitch is the axis
 * manipulated with the up and down arrow keys.
 * 
 * @author David Insley
 */
public final class CameraAngleMessage extends Message {

    private final int pitch;
    private final int yaw;

    public CameraAngleMessage(int yaw, int pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public int getYaw() {
        return yaw;
    }

    public int getPitch() {
        return pitch;
    }

}
