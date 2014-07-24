package net.scapeemulator.game.model.mob.action;

import java.util.Deque;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.area.Area;
import net.scapeemulator.game.model.area.QuadArea;
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

        Area targetBounds = target.getBounds();
        if (behind) {
            Deque<Position> recentPoints = target.getWalkingQueue().getRecentPoints();

            if (!recentPoints.isEmpty()) {
                Position last = recentPoints.peekLast();
                int lastX = last.getX();
                int lastY = last.getY();
                targetBounds = new QuadArea(lastX, lastY, lastX + target.getSize() - 1, lastY + target.getSize() - 1);
            }
        }

        // Check if we are inside the mob (and not supposed to be)
        if (distance > 0 && target.getBounds().anyWithinArea(mob.getPosition(), mob.getSize(), 0)) {
            Position p = Direction.getNearbyTraversableTiles(mob.getPosition(), mob.getSize()).get(0);
            mob.getWalkingQueue().addPoint(p);
            return;
        }

        // Check if we are inside the target area (don't move)
        if (targetBounds.anyWithinArea(mob.getPosition(), mob.getSize(), distance)) {
            return;
        }

        path = DumbPathFinder.find(mob.getPosition(), targetBounds.center(), mob.getSize(), 2);
        if (path == null || path.isEmpty()) {
            return;
        }
        mob.getWalkingQueue().addPoint(path.poll());
        if (path.isEmpty()) {
            System.out.println("5");
            return;
        }
        if (mob instanceof Player) {

            if (!((Player) mob).getSettings().isRunning()) {
                return;
            }
            if ((targetBounds.anyWithinArea(path.peek(), mob.getSize(), distance == 0 ? 0 : distance - 1) && !behind)) {
                return;
            }

            mob.getWalkingQueue().addPoint(path.poll());
        }

    }

    @Override
    public void stop() {
        mob.resetTurnToTarget();
        super.stop();
    }

}
