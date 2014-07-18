package net.scapeemulator.game.model.area;

import net.scapeemulator.game.model.Position;

/**
 * Area wrapping for Position
 * 
 * @author David Insley
 */
public final class PositionArea extends Area {

    private final Position position;

    public PositionArea(Position position) {
        this.position = position;
    }

    @Override
    public boolean withinArea(int x, int y, int padding) {
        return x >= (position.getX() - padding) && x <= (position.getX() + padding) && y >= (position.getY() - padding) && y <= (position.getY() + padding);
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
