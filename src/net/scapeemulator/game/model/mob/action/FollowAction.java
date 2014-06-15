package net.scapeemulator.game.model.mob.action;

import java.util.Deque;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.mob.Direction;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.pathfinding.DumbPathFinder;
import net.scapeemulator.game.model.pathfinding.Path;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.task.Action;

public class FollowAction extends Action<Mob> {

	protected final Mob target;
	private Path path = new Path();
	protected int distance;
	private boolean behind;

	public FollowAction(Mob mob, Mob target, boolean behind) {
		this(mob, target, behind, 0);
		if (!behind)
			throw new IllegalArgumentException("Must state a max distance if not following behind.");
	}

	public FollowAction(Mob mob, Mob target, boolean behind, int distance) {
		super(mob, 1, true);
		this.behind = behind;
		this.distance = distance;
		this.target = target;
		mob.turnToTarget(target);
		mob.getWalkingQueue().reset();
	}

	@Override
	public void execute() {
		if (target.isTeleporting() || !target.getPosition().isWithinDistance(mob.getPosition())) {
			stop();
			return;
		}
		Position targetPosition = target.getPosition();
		if (behind) {
			Deque<Position> recentPoints = target.getWalkingQueue().getRecentPoints();

			if (!recentPoints.isEmpty()) {
				targetPosition = recentPoints.peekLast();
			}
		}
		if(mob.getPosition().equals(target.getPosition())) {
			Position p = Direction.getNearbyTraversableTiles(mob.getPosition(), mob.getSize()).get(0);
			mob.getWalkingQueue().addPoint(p);
			return;
		}
		if (mob.getPosition().equals(targetPosition) || (withinDistance(mob, target, distance) && !behind)) {
			return;
		}
		path = DumbPathFinder.find(mob.getPosition(), targetPosition, mob.getSize(), 2);
		if (path == null || path.isEmpty()) {
			return;
		}
		mob.getWalkingQueue().addPoint(path.poll());
		if (path.isEmpty() || (withinDistance(path.peek(), mob, target, distance) && !behind)) {
			return;
		}
		if (mob instanceof Player) { // ily sinisoul
			if (((Player) mob).getSettings().isRunning()) {
				mob.getWalkingQueue().addPoint(path.poll());
			}
		}
	}
	
	public static boolean withinDistance(Position posTest, Mob mob1, Mob mob2, int distance) {
		if(distance <= 2 && !Direction.isTraversable(posTest, Direction.between(posTest, mob2.getPosition()), mob1.getSize())){
			return false;
		}
		int x = posTest.getX();
		int y = posTest.getY();
		int targetX = mob2.getPosition().getX();
		int targetY = mob2.getPosition().getY();
		boolean xValid = false;
		boolean yValid = false;
		if (x > targetX) {
			xValid = x - (targetX + (mob2.getSize() - 1)) <= distance;
		} else {
			xValid = targetX - (x + (mob1.getSize() - 1)) <= distance;
		}
		if (y > targetY) {
			yValid = y - (targetY + (mob2.getSize() - 1)) <= distance;
		} else {
			yValid = targetY - (y + (mob1.getSize() - 1)) <= distance;
		}
		if (xValid && yValid) {
			return true;
		}
		return false;
	}
	
	public static boolean withinDistance(Mob mob1, Mob mob2, int distance) {
		return withinDistance(mob1.getPosition(), mob1, mob2, distance);
	}

	protected void setDistance(int distance) {
		this.distance = distance;
	}
	
	@Override
	public void stop() {
		mob.resetTurnToTarget();
		super.stop();
	}
	
}
