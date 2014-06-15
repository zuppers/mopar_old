package net.scapeemulator.game.model.pathfinding;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.mob.Direction;

public class DumbPathFinder {

	public static Path find(Position position, Position dest, int size, int max) {
		Path path = new Path();
		Position p = position;
		for (int i = 0; i < max; i++) {
			p = bestDummyPosition(p, dest, size);
			if (p != null) {
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
			return new Position(cur.getX(), cur.getY() + 1);
		case SOUTH:
			return new Position(cur.getX(), cur.getY() - 1);
		case EAST:
			return new Position(cur.getX() + 1, cur.getY());
		case WEST:
			return new Position(cur.getX() - 1, cur.getY());
		case NORTH_EAST:
			if (Direction.isTraversable(cur, Direction.NORTH_EAST, size)) {
				return new Position(cur.getX() + 1, cur.getY() + 1);
			}
			if (Math.abs(deltaX) > Math.abs(deltaY)) {
				if (Direction.isTraversable(cur, Direction.EAST, size)) {
					return new Position(cur.getX() + 1, cur.getY());
				} else {
					return new Position(cur.getX(), cur.getY() + 1);
				}
			} else {
				if (Direction.isTraversable(cur, Direction.NORTH, size)) {
					return new Position(cur.getX(), cur.getY() + 1);
				} else {
					return new Position(cur.getX() + 1, cur.getY());
				}
			}
		case NORTH_WEST:
			if (Direction.isTraversable(cur, Direction.NORTH_WEST, size)) {
				return new Position(cur.getX() - 1, cur.getY() + 1);
			}
			if (Math.abs(deltaX) > Math.abs(deltaY)) {
				if (Direction.isTraversable(cur, Direction.WEST, size)) {
					return new Position(cur.getX() - 1, cur.getY());
				} else {
					return new Position(cur.getX(), cur.getY() + 1);
				}
			} else {
				if (Direction.isTraversable(cur, Direction.NORTH, size)) {
					return new Position(cur.getX(), cur.getY() + 1);
				} else {
					return new Position(cur.getX() - 1, cur.getY());
				}
			}
		case SOUTH_EAST:
			if (Direction.isTraversable(cur, Direction.SOUTH_EAST, size)) {
				return new Position(cur.getX() + 1, cur.getY() - 1);
			}
			if (Math.abs(deltaX) > Math.abs(deltaY)) {
				if (Direction.isTraversable(cur, Direction.EAST, size)) {
					return new Position(cur.getX() + 1, cur.getY());
				} else {
					return new Position(cur.getX(), cur.getY() - 1);
				}
			} else {
				if (Direction.isTraversable(cur, Direction.SOUTH, size)) {
					return new Position(cur.getX(), cur.getY() - 1);
				} else {
					return new Position(cur.getX() + 1, cur.getY());
				}
			}
		case SOUTH_WEST:
			if (Direction.isTraversable(cur, Direction.SOUTH_WEST, size)) {
				return new Position(cur.getX() - 1, cur.getY() - 1);
			}
			if (Math.abs(deltaX) > Math.abs(deltaY)) {
				if (Direction.isTraversable(cur, Direction.WEST, size)) {
					return new Position(cur.getX() - 1, cur.getY());
				} else {
					return new Position(cur.getX(), cur.getY() - 1);
				}
			} else {
				if (Direction.isTraversable(cur, Direction.SOUTH, size)) {
					return new Position(cur.getX(), cur.getY() - 1);
				} else {
					return new Position(cur.getX() - 1, cur.getY());
				}
			}
		}
		return null;
	}
	

	
}
