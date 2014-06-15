package net.scapeemulator.game.model.pathfinding;

import net.scapeemulator.game.model.Position;

public class ProjectilePathFinder {

	public static Path find(Position source, Position dest) {
		return find(source.getX(), source.getY(), dest.getX(), dest.getY());
	}
	
	public static Path find(int sourceX, int sourceY, int destX, int destY) {
		Path path = new Path();
		double curX = sourceX;
		double curY = sourceY;
		double deltaX = destX - sourceX;
		double deltaY = destY - sourceY;

		while (deltaX >= 1 || deltaY >= 1 || deltaX <= -1 || deltaY <= -1) {
			deltaX = deltaX / 2;
			deltaY = deltaY / 2;
		}

		int prevX = sourceX;
		int prevY = sourceY;

		while (true) {
			curX += deltaX;
			curY += deltaY;

			if (prevX != (int) curX || prevY != (int) curY) {
				path.addLast(new Position((int) curX, (int) curY));
				prevX = (int) curX;
				prevY = (int) curY;
			}
			
			if ((int) curX == destX && (int) curY == destY) {
				return path;
			}
		}

	}

}
