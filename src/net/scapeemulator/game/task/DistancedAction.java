package net.scapeemulator.game.task;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.area.Area;
import net.scapeemulator.game.model.area.PositionArea;
import net.scapeemulator.game.model.mob.Mob;

/**
 * An @{link Action} which fires when a distance requirement is met.
 * 
 * @author Blake
 * @author Graham
 */
public abstract class DistancedAction<T extends Mob> extends Action<T> {

    /**
     * The position to distance check with.
     */
    protected final Area bounds;

    /**
     * The minimum distance before the action fires.
     */
    private final int distance;

    /**
     * The delay once the threshold is reached.
     */
    private final int delay;

    /**
     * A flag indicating if this action fires immediately after the threshold is reached.
     */
    private final boolean immediate;

    /**
     * A flag indicating if this action should wait until the mob has stopped moving completely before firing.
     */
    private boolean waitForNotWalking;

    /**
     * A flag indicating if the distance has been reached yet.
     */
    private boolean reached = false;

    public DistancedAction(int delay, boolean immediate, T character, Position position, int distance) {
        this(delay, immediate, character, new PositionArea(position), distance, false);
    }

    public DistancedAction(int delay, boolean immediate, T character, Position position, int distance, boolean waitForStop) {
        this(delay, immediate, character, new PositionArea(position), distance, waitForStop);
    }

    /**
     * Creates a new DistancedAction.
     * 
     * @param delay The delay between executions once the distance threshold is reached.
     * @param immediate Whether or not this action fires immediately after the distance threshold is reached.
     * @param character The character.
     * @param position The position.
     * @param distance The distance.
     */
    public DistancedAction(int delay, boolean immediate, T character, Area bounds, int distance) {
        this(delay, immediate, character, bounds, distance, false);
    }

    /**
     * Creates a new DistancedAction.
     * 
     * @param delay The delay between executions once the distance threshold is reached.
     * @param immediate Whether or not this action fires immediately after the distance threshold is reached.
     * @param character The character.
     * @param position The position.
     * @param distance The distance.
     * @param waitForStop If true, executeAction() will not be called until the mob has completely stopped moving
     */
    public DistancedAction(int delay, boolean immediate, T character, Area bounds, int distance, boolean waitForStop) {
        super(character, 1, true);
        this.bounds = bounds;
        this.distance = distance;
        this.delay = delay;
        this.immediate = immediate;
    }

    @Override
    public void execute() {
        if (reached) {
            // some actions (e.g. agility) will cause the player to move away again
            // so we don't check once the player got close enough once
            executeAction();
        } else if (bounds.withinArea(mob.getPosition().getX(), mob.getPosition().getY(), distance, false)) {
            if (waitForNotWalking && !mob.notWalking()) {
                return;
            }
            reached = true;
            setDelay(delay);
            if (immediate) {
                executeAction();
            }
        } else if (mob.getWalkingQueue().isEmpty()) {
            cantReach();
            stop();
        }
    }

    public void waitForEmptyQueue() {
        waitForNotWalking = true;
    }

    /**
     * Executes the actual action. Called when the distance requirement is met.
     */
    public abstract void executeAction();

    /**
     * Called when the mobs walking queue is empty if they haven't yet reached the destination.
     */
    public abstract void cantReach();

}