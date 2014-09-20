package net.scapeemulator.game.msg.impl.camera;

import net.scapeemulator.game.msg.Message;

/**
 * @author David Insley
 */
public final class CameraFaceMessage extends Message {

    private final int x;
    private final int y;
    private final int unknown;
    private final int constantSpeed;
    private final int variableSpeed;

    /**
     * Creates a message which, when sent, turns to camera to face the specified coordinates with
     * given speeds.
     * 
     * @param x the x coordinate within the current region to face the camera at
     * @param y the y coordinate within the current region to face the camera at
     * @param unknown unknown, something to do with the camera angle?
     * @param constantSpeed the base speed the camera will turn
     * @param variableSpeed the variable speed that increases the camera speed through the rotation,
     *            value over 100 makes the turn instant, value of 0 keeps the speed constant
     */
    public CameraFaceMessage(int x, int y, int unknown, int constantSpeed, int variableSpeed) {
        this.x = x;
        this.y = y;
        this.unknown = unknown;
        this.constantSpeed = constantSpeed;
        this.variableSpeed = variableSpeed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getUnknown() {
        return unknown;
    }

    public int getConstantSpeed() {
        return constantSpeed;
    }

    public int getVariableSpeed() {
        return variableSpeed;
    }
}
