package net.scapeemulator.game.model.area;

import net.scapeemulator.game.model.Position;

/**
 * Area wrapping for standard position.
 * 
 * @author David Insley
 */
public final class PositionArea extends Area {

    private final Position position;

    public PositionArea(Position position) {
        this.position = position;
    }

    @Override
    public boolean withinArea(int x, int y, int padding, boolean corners) {
        int deltaX = Math.abs(position.getX() - x);
        int deltaY = Math.abs(position.getY() - y);
        if (deltaX == padding && deltaY == padding && !corners) {
            return false;
        }
        return deltaX <= padding && deltaY <= padding;
    }

    @Override
    public Position center() {
        return position;
    }

    @Override
    public Position randomPosition(int height) {
        return new Position(position.getX(), position.getY(), height);
    }

}
