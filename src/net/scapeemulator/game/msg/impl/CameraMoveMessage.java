package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.msg.Message;

public final class CameraMoveMessage extends Message {

    private final int x;
    private final int y;
    private final int height;
    private final int constantSpeed;
    private final int variableSpeed;

    /**
     * Creates a message which, when sent, moves the camera to the specified coordinates and height
     * at the specified speeds. Important to note is that this moves the camera <i>to</i> that
     * location, it does not cause the camera to look <i>at</i> that location.
     * 
     * @param x the x coordinate within the current region to move the camera to
     * @param y the y coordinate within the current region to move the camera to
     * @param height the new camera height, around 1100 is average if the camera is straight down
     * @param constantSpeed the base speed the camera will move
     * @param variableSpeed the variable speed that increases the camera speed through the move,
     *            value over 100 makes the move instant, value of 0 keeps the speed constant
     */
    public CameraMoveMessage(int x, int y, int height, int constantSpeed, int variableSpeed) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.constantSpeed = constantSpeed;
        this.variableSpeed = variableSpeed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getConstantSpeed() {
        return constantSpeed;
    }

    public int getVariableSpeed() {
        return variableSpeed;
    }
}
