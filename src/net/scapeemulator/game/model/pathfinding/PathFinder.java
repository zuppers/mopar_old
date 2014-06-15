package net.scapeemulator.game.model.pathfinding;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.mob.Mob;

public abstract class PathFinder {
    
	public Path find(Mob mob, Position dest) {
		return find(mob, dest.getX(), dest.getY());
	}
	
    public Path find(Mob mob, int destX, int destY) {

        /* Get the current position of the player */
        Position position = mob.getPosition();

        /* Get the scene base x and y coordinates */
        int baseLocalX = position.getBaseLocalX(), baseLocalY = position.getBaseLocalY();

        /* Calculate the local x and y coordinates */
        int destLocalX = destX - baseLocalX, destLocalY = destY - baseLocalY;

        return find(new Position(baseLocalX, baseLocalY, position.getHeight()), 104, 104, position.getLocalX(), position.getLocalY(), destLocalX, destLocalY, mob.getSize());
    }

    public Path find(Position position, int width, int length, int srcX, int srcY, int destX, int destY) {
        return find(position, width, length, srcX, srcY, destX, destY, 1);
    }
    
    public abstract Path find(Position position, int width, int length, int srcX, int srcY, int destX, int destY, int size);
}
