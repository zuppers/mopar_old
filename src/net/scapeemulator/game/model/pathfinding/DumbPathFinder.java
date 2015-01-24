package net.scapeemulator.game.model.pathfinding;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.mob.Direction;

public class DumbPathFinder {

    public static Path find(Position position, Position dest, int size, int max, boolean inside) {
        Path path = new Path();
        Position p = position;
        for (int i = 0; i < max; i++) {
            p = bestDummyPosition(p, dest, size);
            if (p != null) {
                if (!inside && p.equals(dest)) {
                    Position cur;
                    if (path.isEmpty()) {
                        cur = position;
                    } else {
                        cur = path.getPoints().getLast();
                    }
                    switch (Direction.between(cur, dest)) {
                    case NONE:
                    case EAST:
                    case NORTH:
                    case SOUTH:
                    case WEST:
                        break;
                    case NORTH_EAST:
                        if (Direction.isTraversable(cur, Direction.NORTH, size)) {
                            path.addLast(new Position(cur.getX(), cur.getY() + 1, cur.getHeight()));
                        } else if (Direction.isTraversable(cur, Direction.EAST, size)) {
                            path.addLast(new Position(cur.getX() + 1, cur.getY(), cur.getHeight()));
                        }
                        break;
                    case NORTH_WEST:
                        if (Direction.isTraversable(cur, Direction.NORTH, size)) {
                            path.addLast(new Position(cur.getX(), cur.getY() + 1, cur.getHeight()));
                        } else if (Direction.isTraversable(cur, Direction.WEST, size)) {
                            path.addLast(new Position(cur.getX() - 1, cur.getY(), cur.getHeight()));
                        }
                        break;
                    case SOUTH_EAST:
                        if (Direction.isTraversable(cur, Direction.SOUTH, size)) {
                            path.addLast(new Position(cur.getX(), cur.getY() - 1, cur.getHeight()));
                        } else if (Direction.isTraversable(cur, Direction.EAST, size)) {
                            path.addLast(new Position(cur.getX() + 1, cur.getY(), cur.getHeight()));
                        }
                        break;
                    case SOUTH_WEST:
                        if (Direction.isTraversable(cur, Direction.SOUTH, size)) {
                            path.addLast(new Position(cur.getX(), cur.getY() - 1, cur.getHeight()));
                        } else if (Direction.isTraversable(cur, Direction.WEST, size)) {
                            path.addLast(new Position(cur.getX() - 1, cur.getY(), cur.getHeight()));
                        }
                        break;
                    }
                    return path;
                }
                path.addLast(p);
            } else {
                return path;
            }
        }
        return path;
    }

    public static Position bestDummyPosition(Position cur, Position next, int size) {
        if (cur.equals(next)) {
            return null;
        }
        int deltaX = next.getX() - cur.getX();
        int deltaY = next.getY() - cur.getY();
        switch (Direction.between(cur, next)) {
        case NONE:
            return null;
        case NORTH:
            return new Position(cur.getX(), cur.getY() + 1, cur.getHeight());
        case SOUTH:
            return new Position(cur.getX(), cur.getY() - 1, cur.getHeight());
        case EAST:
            return new Position(cur.getX() + 1, cur.getY(), cur.getHeight());
        case WEST:
            return new Position(cur.getX() - 1, cur.getY(), cur.getHeight());
        case NORTH_EAST:
            if (Direction.isTraversable(cur, Direction.NORTH_EAST, size)) {
                return new Position(cur.getX() + 1, cur.getY() + 1, cur.getHeight());
            }
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (Direction.isTraversable(cur, Direction.EAST, size)) {
                    return new Position(cur.getX() + 1, cur.getY(), cur.getHeight());
                } else {
                    return new Position(cur.getX(), cur.getY() + 1, cur.getHeight());
                }
            } else {
                if (Direction.isTraversable(cur, Direction.NORTH, size)) {
                    return new Position(cur.getX(), cur.getY() + 1, cur.getHeight());
                } else {
                    return new Position(cur.getX() + 1, cur.getY(), cur.getHeight());
                }
            }
        case NORTH_WEST:
            if (Direction.isTraversable(cur, Direction.NORTH_WEST, size)) {
                return new Position(cur.getX() - 1, cur.getY() + 1, cur.getHeight());
            }
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (Direction.isTraversable(cur, Direction.WEST, size)) {
                    return new Position(cur.getX() - 1, cur.getY(), cur.getHeight());
                } else {
                    return new Position(cur.getX(), cur.getY() + 1, cur.getHeight());
                }
            } else {
                if (Direction.isTraversable(cur, Direction.NORTH, size)) {
                    return new Position(cur.getX(), cur.getY() + 1, cur.getHeight());
                } else {
                    return new Position(cur.getX() - 1, cur.getY(), cur.getHeight());
                }
            }
        case SOUTH_EAST:
            if (Direction.isTraversable(cur, Direction.SOUTH_EAST, size)) {
                return new Position(cur.getX() + 1, cur.getY() - 1, cur.getHeight());
            }
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (Direction.isTraversable(cur, Direction.EAST, size)) {
                    return new Position(cur.getX() + 1, cur.getY(), cur.getHeight());
                } else {
                    return new Position(cur.getX(), cur.getY() - 1, cur.getHeight());
                }
            } else {
                if (Direction.isTraversable(cur, Direction.SOUTH, size)) {
                    return new Position(cur.getX(), cur.getY() - 1, cur.getHeight());
                } else {
                    return new Position(cur.getX() + 1, cur.getY(), cur.getHeight());
                }
            }
        case SOUTH_WEST:
            if (Direction.isTraversable(cur, Direction.SOUTH_WEST, size)) {
                return new Position(cur.getX() - 1, cur.getY() - 1, cur.getHeight());
            }
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (Direction.isTraversable(cur, Direction.WEST, size)) {
                    return new Position(cur.getX() - 1, cur.getY(), cur.getHeight());
                } else {
                    return new Position(cur.getX(), cur.getY() - 1, cur.getHeight());
                }
            } else {
                if (Direction.isTraversable(cur, Direction.SOUTH, size)) {
                    return new Position(cur.getX(), cur.getY() - 1, cur.getHeight());
                } else {
                    return new Position(cur.getX() - 1, cur.getY(), cur.getHeight());
                }
            }
        }
        return null;
    }

}
