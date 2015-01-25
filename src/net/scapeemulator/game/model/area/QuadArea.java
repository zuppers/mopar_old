package net.scapeemulator.game.model.area;

import net.scapeemulator.game.model.Position;

public final class QuadArea extends Area {

    private final int minX, minY, maxX, maxY;

    public QuadArea(Position left, Position right) {
        this(left.getX(), left.getY(), right.getX(), right.getY());
    }

    public QuadArea(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    @Override
    public boolean withinArea(int x, int y, int padding, boolean corners) {
        if (!corners && padding != 0) {
            int deltaX = Math.abs(minX - x);
            int deltaY = Math.abs(minY - y);
            if (deltaX == padding && deltaY == padding) {
                return false;
            }
            deltaX = Math.abs(x - minX);
            deltaY = Math.abs(y - minY);
            if (deltaX == padding && deltaY == padding) {
                return false;
            }
        }
        return x >= (minX - padding) && x <= (maxX + padding) && y >= (minY - padding) && y <= (maxY + padding);
    }

    @Override
    public Position center() {
        return new Position(minX + ((maxX - minX) / 2), minY + ((maxY - minY) / 2));
    }

    @Override
    public Position randomPosition(int height) {
        int x = minX + (int) (Math.random() * (maxX - minX + 1));
        int y = minY + (int) (Math.random() * (maxY - minY + 1));
        return new Position(x, y, height);
    }

    @Override
    public String toString() {
        return "QuadArea[(" + minX + ", " + minY + "), (" + maxX + ", " + maxY + ")]";
    }

}
