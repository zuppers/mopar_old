package net.scapeemulator.game.model.npc.stateful.handler;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.area.Area;
import net.scapeemulator.game.model.mob.Direction;
import net.scapeemulator.game.model.npc.stateful.StateHandler;
import net.scapeemulator.game.model.npc.stateful.impl.NormalNPC;

/**
 * @author Hadyn Richard
 *
 */
public final class RandomWalkStateHandler extends StateHandler<NormalNPC> {

	private final static Random random = new Random();

	@Override
	public void handle(NormalNPC npc) {

		/* Get the amount of steps that the NPC will walk if possible */
		int iterations = random.nextInt(2) + 1;

		Position currentPosition = npc.getPosition();

		Set<Position> oldPositions = new HashSet<>();
		oldPositions.add(currentPosition);
		Area bounds = npc.getWalkingBounds();

		for (int i = 0; i < iterations; i++) {

			List<Position> positions = Direction.getNearbyTraversableTiles(currentPosition, npc.getSize());
			// Remove all the positions that are not within bounds or previously used 
			Iterator<Position> iterator = positions.iterator();
			while (iterator.hasNext()) {
				Position position = iterator.next();
				if (!bounds.allWithinArea(position, npc.getSize(), 0) || oldPositions.contains(position)) {
					iterator.remove();
				}
			}

			Position[] array = positions.toArray(new Position[0]);
			if (array.length > 0) {
				Position position = array[random.nextInt(array.length)];
				npc.getWalkingQueue().addPoint(position);
				oldPositions.add(position);
				currentPosition = position;
			}
		}
	}
}
