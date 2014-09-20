package net.scapeemulator.game.msg.impl.camera;

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

    /**
     * Constructs a message which, when sent, will set the players camera angle to the specified
     * pitch and yaw. The yaw must be between 0 and 2048, with 1024 setting the camera facing south.
     * The pitch must be between 128 and 383.
     * 
     * @param yaw the yaw to set the camera to
     * @param pitch the pitch to set the camera to
     */
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
