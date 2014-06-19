package net.scapeemulator.game.model.mob;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

import net.scapeemulator.game.model.Position;

/**
 * This {@link WalkingQueue} is associated with a certain {@link Mob}. 
 * This should handle the {@link Deque} for the walking of the associated
 * {@link Mob}. There is a {@link Deque} to hold the positions to walk to (head
 * to tail), as well as a {@link Deque} holding the most recent {@link Position}
 * points walked to.
 *
 * @author ?
 */
public final class WalkingQueue {

    /* Maximum amount of points in recentPoints. */
    private final static int AMOUNT_POINTS = 100;

    private final Mob mob;
    private final Deque<Position> points = new ArrayDeque<>();
    private boolean runningQueue = false;
    private boolean minimapFlagReset = false;
    private final Deque<Position> recentPoints = new LinkedList<>();

    /**
     * Create a {@link WalkingQueue} associated with a certain {@link Mob}.
     *
     * @param mob The {@link Mob} to associate this with.
     */
    public WalkingQueue(Mob mob) {
        this.mob = mob;
    }

    /**
     * Reset the {@link WalkingQueue}. This will clear all points, set the
     * mini-map Flag to be reset and {@link isRunningQueue()} to false.
     */
    public void reset() {
        points.clear();
        runningQueue = false;
        minimapFlagReset = true;
    }

    /**
     * Add a position to the back of the {@link Deque}. This point may or may
     * not be used to walk to, in the future.
     *
     * @param position The {@link Position}.
     */
    public void addPoint(Position position) {
        points.add(position);
    }

    /**
     * Add the first step from {@link Mob#getPosition()} to position. This
     * clears all current {@link Position} points on the {@link Deque}.
     * {@link getRunningQueue()} will be set to false. Steps will be added as
     * defined by {@code addStepImpl(position, mob.getPosition())}.
     *
     * @param position The {@link Position} to walk to.
     * @see addStepImpl(Position, Position)
     */
    public void addFirstStep(Position position) {
        points.clear();
        runningQueue = false;
        addStepImpl(position, mob.getPosition());
    }

    /**
     * Add steps from the last {@link Position} we walk to, to position. Adds
     * steps to the {@link Deque} from the last element on the {@link Deque} to
     * position.
     *
     * @param position The {@link Position} to walk to.
     * @see addStepImpl(Position, Position)
     */
    public void addStep(Position position) {
        addStepImpl(position, points.peekLast());
    }

    /**
     * Peek which {@link Position} is next, to walk to. Peeks which
     * {@link Position} is first in the {@link Deque}.
     *
     * @return The first element on the {@link Deque}. Returns null when there
     * is no first element.
     */
    public Position peek() {
        return points.peekFirst();
    }

    /**
     * Add All {@link Position} points from last to position, to the
     * {@link Deque}.
     *
     * @param position The {@link Position} to walk to.
     * @param last The {@link Position} to walk from.
     */
    private void addStepImpl(Position position, Position last) {
        int deltaX = position.getX() - last.getX();
        int deltaY = position.getY() - last.getY();

        int max = Math.max(Math.abs(deltaX), Math.abs(deltaY));

        /* Bug fix? */
        if (max < 1) {
            max = 1;
        }

        for (int i = 0; i < max; i++) {
            if (deltaX < 0) {
                deltaX++;
            } else if (deltaX > 0) {
                deltaX--;
            }

            if (deltaY < 0) {
                deltaY++;
            } else if (deltaY > 0) {
                deltaY--;
            }

            points.add(new Position(position.getX() - deltaX, position.getY() - deltaY, position.getHeight()));
        }
    }

    /**
     * Perform the tick for the {@link WalkingQueue}. 1) The associated
     * {@link Mob} will change position to the first {@link Position} A, on the
     * {@link Deque}. The old point will be added to the "most recent" points.
     * {@link Mob#getFirstDirection()} will be adjusted as well. 2) If
     * {@link Mob#isRunning()} or {@link isRunningQueue()}, do the same for the
     * next point and adjust {@link Mob#getSecondDirection()}. If direction to A
     * is not traversable, performs a {@link reset()} instead of everything else
     * described.
     */
    public void tick() {
        Position position = mob.getPosition();

        Direction firstDirection = Direction.NONE;
        Direction secondDirection = Direction.NONE;

        Position next = points.poll();

        if (next != null) {
            Direction direction = Direction.between(position, next);
            boolean traversable = Direction.isTraversable(position, direction, mob.getSize());
            if (traversable) {
                firstDirection = direction;
                addRecentPoint(position);
                position = next;
                if (runningQueue || mob.isRunning()) {
                    next = points.poll();
                    if (next != null) {
                        direction = Direction.between(position, next);
                        traversable = Direction.isTraversable(position, direction, mob.getSize());
                        if (traversable) {
                            secondDirection = direction;
                            addRecentPoint(position);
                            position = next;
                        }
                    }
                }
            }

            if (!traversable) {
                reset();
            }
        }

        mob.setDirections(firstDirection, secondDirection);
        mob.setPosition(position);
    }

    /**
     * Add position to the tail of {@link getRecentPoints()}. When
     * {@link getRecentPoints()} has reached its maximum size,
     * {@link AMOUNT_POINTS} the head will be removed.
     *
     * @param position The {@link Position} to add to the most recent points
     * {@link Deque}.
     */
    public void addRecentPoint(Position position) {
        if (recentPoints.size() >= AMOUNT_POINTS) {
            recentPoints.poll();
        }
        recentPoints.addLast(position);
    }

    /**
     * Gets a {@link Deque} listing all recent {@link Position} points. The
     * elements at the tail of this will be the most recent, head least.
     *
     * @return The {@link Deque} of last recent points.
     */
    public Deque<Position> getRecentPoints() {
        return recentPoints;
    }

    /**
     * Gets whether we have no {@link Position} to walk to.
     * @return Checks whether the {@link Deque} holding the "walk to" points is
     * empty.
     */
    public boolean isEmpty() {
        return points.isEmpty();
    }

    /**
     * Gets whether this {@link WalkingQueue} is "running".
     * @return Whether it is "running".
     */
    public boolean isRunningQueue() {
        return runningQueue;
    }

    /**
     * Set whether this {@link WalkingQueue} is "running". If it is, walking
     * will be twice as fast = "running".
     *
     * @param runningQueue Whether it is "running".
     * @see tick()
     */
    public void setRunningQueue(boolean runningQueue) {
        this.runningQueue = runningQueue;
    }

    /**
     * Gets whether the mini-map flag should be reset.
     * @return Whether mini-map flag should be reset.
     */
    public boolean isMinimapFlagReset() {
        return minimapFlagReset;
    }

    /**
     * Set whether the mini-map flag should be reset.
     * {@link isMinimapFlagReset()} will be equal to minimapFlagReset.
     *
     * @param minimapFlagReset Whether the mini-map flag should be reset.
     */
    public void setMinimapFlagReset(boolean minimapFlagReset) {
        this.minimapFlagReset = minimapFlagReset;
    }
}
