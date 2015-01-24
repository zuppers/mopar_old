package net.scapeemulator.game.model.mob;

import net.scapeemulator.game.model.Entity;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.SpotAnimation;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.area.Area;
import net.scapeemulator.game.model.area.PositionArea;
import net.scapeemulator.game.model.area.QuadArea;
import net.scapeemulator.game.model.mob.action.CombatAction;
import net.scapeemulator.game.model.mob.combat.CombatHandler;
import net.scapeemulator.game.model.mob.combat.HitType;
import net.scapeemulator.game.model.mob.combat.Hits;
import net.scapeemulator.game.model.pathfinding.Path;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.action.PlayerDeathAction;
import net.scapeemulator.game.model.player.skills.prayer.HeadIcon;
import net.scapeemulator.game.task.Action;

/**
 * This represents an in-game Mob. An Entity with (mainly) {@link Animation}, {@link CombatHandler}
 * and {@link WalkingQueue}.
 */
public abstract class Mob extends Entity {

    private static final Animation CANCEL_ANIMATION = new Animation(-1);

    protected int id;
    protected boolean teleporting;
    protected boolean clipped = true;
    protected final WalkingQueue walkingQueue = new WalkingQueue(this);
    protected Direction firstDirection = Direction.NONE;
    protected Direction secondDirection = Direction.NONE;
    protected Direction mostRecentDirection = Direction.SOUTH;
    protected int size = 1;
    protected Animation animation;
    protected SpotAnimation spotAnimation;
    protected Position turnToPosition;
    protected Hits hits = new Hits(this);
    protected String forcedChat;
    protected Mob currentTarget;
    protected int turnToTargetId = -1;
    protected int frozen;
    protected HeadIcon headIcon = HeadIcon.NONE;
    protected Action<? extends Mob> action;
    protected boolean hidden;
    protected CombatHandler<? extends Mob> combatHandler;

    /**
     * Gets the id of this {@link Mob}.
     * 
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Reset the id of this {@link Mob} to 0.
     */
    public void resetId() {
        this.id = 0;
    }

    /**
     * Set the id of this {@link Mob} to id.
     * 
     * @param id The id to set it to.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set the size of this {@link Mob} to size.
     * 
     * @param size The size to set it to.
     */
    public void setSize(int size) {
        if (size > 0) {
            this.size = size;
        }
    }

    /**
     * Gets the size of this {@link Mob}.
     * 
     * @return The size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets whether this {@link Mob} is active or not.
     * 
     * @return Whether the {@link getId()} isn't 0.
     */
    public boolean isActive() {
        return id != 0;
    }

    /**
     * Start action. When different from the current Action, the currentAction is stopped and action
     * is scheduled.
     * 
     * @param action The {@link Action} to start.
     * @see stopAction()
     */
    public void startAction(Action<?> action) {
        if (this.action != null) {
            if (this.action instanceof PlayerDeathAction) {
                System.out.println("SHOULDN'T BE ABLE TO DO THAT!!! " + action.toString());
            }
            if (this.action.equals(action)) {
                return;
            }

            stopAction();
        }
        this.action = action;
        World.getWorld().getTaskScheduler().schedule(action);
    }

    /**
     * Stop the current {@link Action}.
     * 
     * @see Action#stop()
     */
    public void stopAction() {
        if (action != null) {
            Action<? extends Mob> oldAction = action;
            action = null;
            oldAction.stop();
        }
    }

    /**
     * Gets whether this {@link Mob} is teleporting.
     * 
     * @return Whether this is teleporting.
     * @see teleport(Position)
     */
    public boolean isTeleporting() {
        return teleporting;
    }

    /**
     * Gets whether or not this Mob has forced overhead chat to send to all players during the
     * update process.
     * 
     * @return if there is forced chat to be sent in the update blocks
     */
    public boolean isChatForced() {
        return forcedChat != null;
    }

    public void setForcedChat(String forcedChat) {
        this.forcedChat = forcedChat;
    }

    /**
     * Gets the forced overhead chat of this Mob for the update blocks.
     * 
     * @return the chat text
     */
    public String getForcedChat() {
        return forcedChat;
    }

    /**
     * Teleport this {@link Mob} to the specified Position. This implies {@link getPosition()} is
     * equal to position, {@link isTeleporting()} == true and the {@link WalkingQueue} will be
     * reset.
     * 
     * @param position The {@link Position} to teleport to.
     * @see WalkingQueue#reset()
     */
    public void teleport(Position position) {
        this.position = position;
        this.teleporting = true;
        this.walkingQueue.reset();
    }

    /**
     * Gets the {@link WalkingQueue} used by this {@link Mob}.
     * 
     * @return The {@link WalkingQueue}.
     */
    public WalkingQueue getWalkingQueue() {
        return walkingQueue;
    }

    /**
     * Gets the first {@link Direction} of this {@link Mob}.
     * 
     * @return The first {@link Direction}.
     */
    public Direction getFirstDirection() {
        return firstDirection;
    }

    /**
     * Gets the second {@link Direction} of this {@link Mob}.
     * 
     * @return The second {@link Direction}.
     */
    public Direction getSecondDirection() {
        return secondDirection;
    }

    /**
     * Gets whether this {@link Mob} isn't walking. Determined by {@link getFirstDirection()} and
     * {@link getSecondDirection()} both being {@link Direction#NONE}.
     * 
     * @return Whether this isn't walking.
     */
    public boolean notWalking() {
        return firstDirection == Direction.NONE && secondDirection == Direction.NONE;
    }

    /**
     * Get the most recent {@link Direction} of this {@link Mob}.
     * 
     * @return The previous non {@link Direction#NONE}, {@link getSecondDirection} or
     *         {@link getFirstDirection}
     */
    public Direction getMostRecentDirection() {
        return mostRecentDirection;
    }

    /**
     * Set the {@link getFirstDirection} and {@link getSecondDirection} to firstDirection and
     * secondDirection. {@link getMostRecentDirection()} will be set to the current non
     * {@link Direction#NONE}, {@link getSecondDirection} or {@link getFirstDirection}.
     * 
     * @param firstDirection The {@link Direction} to set the {@link getFirstDirection} to.
     * @param secondDirection The {@link Direction} to set the {@link getSecondDirection} to.
     */
    public void setDirections(Direction firstDirection, Direction secondDirection) {
        this.firstDirection = firstDirection;
        this.secondDirection = secondDirection;

        if (secondDirection != Direction.NONE) {
            mostRecentDirection = secondDirection;
        } else if (firstDirection != Direction.NONE) {
            mostRecentDirection = firstDirection;
        }
    }

    /**
     * Gets the current {@link Action} of this {@link Mob}.
     * 
     * @return The current {@link Action}. Returns null if there is currently no {@link Action}.
     * @see startAction(Action)
     * @see stopAction()
     */
    public Action<? extends Mob> getAction() {
        return action;
    }

    public HeadIcon getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(HeadIcon headIcon) {
        this.headIcon = headIcon;
    }

    /**
     * Gets the current {@link Animation} of this {@link Mob}.
     * 
     * @return The current {@link Animation}. Returns null if there is currently no
     *         {@link Animation}.
     * @see playAnimation(Animation)
     * @see isAnimationUpdated()
     */
    public Animation getAnimation() {
        return animation;
    }

    /**
     * Gets the current {@link SpotAnimation} of this {@link Mob}.
     * 
     * @return The current {@link SpotAnimation}. Returns null if there is currently no
     *         {@link SpotAnimation}.
     * @see playSpotAnimation(SpotAnimation}
     * @see isSpotAnimationUpdated()
     */
    public SpotAnimation getSpotAnimation() {
        return spotAnimation;
    }

    /**
     * Gets whether the current {@link Animation} is updated.
     * 
     * @return Whether {@link getAnimation()} isn't null.
     */
    public boolean isAnimationUpdated() {
        return animation != null;
    }

    /**
     * Gets whether the current {@link SpotAnimation} is updated.
     * 
     * @return Whether {@link getSpotAnimation()} isn't null.
     */
    public boolean isSpotAnimationUpdated() {
        return spotAnimation != null;
    }

    /**
     * Play the provided {@link Animation}. {@link getAnimation()} will be equal to animation.
     * 
     * @param animation The {@link Animation} to play.
     */
    public void playAnimation(Animation animation) {
        this.animation = animation;
    }

    /**
     * Cancels the {@link getAnimation()}. {@link getAnimation()} will be equal to
     * {@link CANCEL_ANIMATION}.
     */
    public void cancelAnimation() {
        animation = CANCEL_ANIMATION;
    }

    /**
     * Play the provided {@link SpotAnimation}. {@link getAnimation()} will be equal to animation.
     * 
     * @param spotAnimation The {@link SpotAnimation} to play.
     */
    public void playSpotAnimation(SpotAnimation spotAnimation) {
        this.spotAnimation = spotAnimation;
    }

    /**
     * Turn to the provided turnToPosition. {@link getTurnToPosition()} will be equal to
     * turnToPosition.
     * 
     * @param turnToPosition The {@link Position} to turn to.
     */
    public void turnToPosition(Position turnToPosition) {
        this.turnToPosition = turnToPosition;
    }

    /**
     * Gets whether the {@link getTurnToPosition()} is updated.
     * 
     * @return Whether {@link getTurnToPosition()} isn't null.
     */
    public boolean isTurnToPositionUpdated() {
        return turnToPosition != null;
    }

    /**
     * Gets the {@link Position} to turn to.
     * 
     * @return The {@link Position} to turn to.
     * @see turnToPosition(Position)
     */
    public Position getTurnToPosition() {
        return turnToPosition;
    }

    /**
     * Turn to the specified target. {@link getTurnToTarget()} = target <b>and</b> {@link
     * getTurnToTargetId()} = {@link getTargetId(Mob)}
     * 
     * @param target The {@link Mob} to turn to.
     */
    public void turnToTarget(Mob target) {
        if (currentTarget == target) {
            return;
        }
        turnToTargetId = getTargetId(target);
        currentTarget = target;
    }

    /**
     * Gets the {@link Mob} target to turn to.
     * 
     * @return The target to turn to.
     * @see getTurnToTargetId()
     */
    public Mob getTurnToTarget() {
        return currentTarget;
    }

    /**
     * Reset the target to turn to. {@link getTurnToTargetId()} = 65535 <b>and</b> {@link
     * getTurnToTarget()} = null
     */
    public void resetTurnToTarget() {
        turnToTargetId = 65535;
        currentTarget = null;
    }

    /**
     * Gets whether there is a target to turn to.
     * 
     * @return Whether {@link getTurnToTarget()} isn't equal to null.
     */
    public boolean isTurnToTargetSet() {
        return currentTarget != null;
    }

    /**
     * Gets whether the first hit is updated.
     * 
     * @return The result of {@code getHits().updated(1)}
     * @see Hits#updated(int)
     */
    public boolean isHitOneUpdated() {
        return hits.updated(1);
    }

    /**
     * Gets whether the second hit is updated.
     * 
     * @return The result of {@code getHits().updated(2)}
     * @see Hits#updated(int)
     */
    public boolean isHitTwoUpdated() {
        return hits.updated(2);
    }

    /**
     * Gets the instance of our {@link Hits} on this {@link Mob}.
     * 
     * @return The hits.
     */
    public Hits getHits() {
        return hits;
    }

    /**
     * Set this {@link Mob}'s hidden status to the argument.
     * 
     * @param hidden Whether the {@link Mob} should be hidden.
     * @see isHidden()
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * Gets whether this {@link Mob} is hidden.
     * 
     * @return Whether this {@link Mob} is hidden.
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Gets whether this {@link Mob} is frozen.
     * 
     * @return Whether this {@link Mob} appears to be frozen.
     */
    public boolean frozen() {
        return frozen > 0;
    }

    /**
     * Gets whether the {@link getTurnToTargetId()} is updated.
     * 
     * @return Whether {@link getTurnToTargetId()} isn't -1.
     */
    public boolean isTurnToTargetUpdated() {
        return turnToTargetId != -1;
    }

    /**
     * Gets the id of the target to turn to.
     * 
     * @return The id of the target.
     * @see getTurnToTarget()
     */
    public int getTurnToTargetId() {
        return turnToTargetId;
    }

    /**
     * Walk the {@link Path} specified. The {@link getWalkingQueue()} will be
     * {@link WalkingQueue#reset()} and contain {@link Path#getPoints()}.
     * 
     * @param path The {@link Path} to walk.
     */
    public void walkPath(Path path) {
        if (!path.isEmpty()) {
            walkingQueue.reset();
            for (Position point : path.getPoints()) {
                walkingQueue.addPoint(point);
            }
        }
    }

    /**
     * 
     * @param object The object to check for equality.
     * @return True if the {@link Mob#getTargetId()} match, else false.
     */
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof Mob)) {
            return false;
        }

        return getTargetId((Mob) object) == getTargetId(this);
    }

    /**
     * 
     * @return hashCode depending on getTargetId(this).
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + getTargetId(this);
        return hash;
    }

    /**
     * Gets whether this {@link Mob} can traverse in the specified {@link Direction}.
     * 
     * @param direction The {@link Direction} to check if traversable.
     * @return {@code Direction.isTraversable(getPosition(), direction, getSize())}
     */
    public boolean canTraverse(Direction direction) {
        return Direction.isTraversable(position, direction, size);
    }

    /**
     * Gets the current amount of hit points this {@link Mob} has.
     * 
     * @return The current amount of hit points.
     */
    public abstract int getCurrentHitpoints();

    /**
     * Gets the current amount of hit points this {@link Mob} has.
     * 
     * @return The current amount of hit points.
     */
    public abstract int getMaximumHitpoints();

    /**
     * Reduce the hit points of this {@link Mob}.
     * 
     * @param amount number of hitpoints to reduce
     */
    protected abstract void reduceHp(int amount);

    /**
     * Increase the hit points of this {@link Mob}.
     * 
     * @param amount number of hitpoints to heal
     */
    protected abstract void heal(int amount);

    /**
     * Executes what has to be done when this {@link Mob} dies.
     */
    protected abstract void onDeath();

    /**
     * Gets whether this {@link Mob} is alive.
     * 
     * @return {@code getCurrentHitpoints() > 0}
     */
    public boolean alive() {
        return getCurrentHitpoints() > 0;
    }

    /**
     * Process the hit of a certain {@link Mob} source. This includes starting possible retaliation,
     * {@link Hits#addHit(Mob, HitType, int)}, {@link reduceHp(int)} and possible {@link onDeath()}.
     * 
     * @param source The source of the damage.
     * @param damage The damage to process.
     */
    public void processHit(Mob source, int damage) {
        if (!alive()) {
            System.out.println("Processing hit while dead?");
        }
        if (damage > getCurrentHitpoints()) {
            System.out.println("Damage greater than current HP");
        }
        if (combatHandler.shouldRetaliate()) {
            startAction(new CombatAction(this, source));
        }
        hits.addHit(source, damage > 0 ? HitType.NORMAL : HitType.ZERO, damage);
        reduceHp(damage);
        if (!alive()) {
            onDeath();
        }
    }

    /**
     * Reset this {@link Mob}. {@link getAnimation()} = {@link getSpotAnimation()} = {@link
     * getTurnToPosition()} = null. <b>and</b> {@link isTeleporting()} =
     * {@link WalkingQueue#isMinimapFlagReset()} = false. <b>and</b> {@link getTurnToTargetId()} =
     * -1.
     * 
     * @see Hits#reset()
     */
    public void reset() {
        animation = null;
        spotAnimation = null;
        turnToPosition = null;
        teleporting = false;
        forcedChat = null;
        turnToTargetId = -1;
        hits.reset();
        walkingQueue.setMinimapFlagReset(false);
    }

    /**
     * Get the target id for this mob.
     * 
     * @param mob The {@link Mob} for which the target id has to be given.
     * @return The id of mob, and when it is a Player, 0x8000 is added.
     */
    private static int getTargetId(Mob mob) {
        int val = mob.getId();
        if (mob instanceof Player) {
            val += 0x8000;
        }
        return val;
    }

    /**
     * Gets the {@link CombatHandler} of this {@link Mob}.
     * 
     * @return The {@link CombatHandler}.
     */
    public CombatHandler<? extends Mob> getCombatHandler() {
        return combatHandler;
    }

    /**
     * Gets whether this {@link mob} is clipped.
     * 
     * @return whether it is clipped.
     */
    public boolean isClipped() {
        return clipped;
    }

    public Area getBounds() {
        if (size == 1) {
            return new PositionArea(position);
        }
        return new QuadArea(position.getX(), position.getY(), position.getX() + size - 1, position.getY() + size - 1);
    }

    /**
     * Gets whether this {@link Mob} is running.
     * 
     * @return Whether this {@link Mob} is running.
     */
    public abstract boolean isRunning();

    /**
     * Gets the health regen per tick of this {@link Mob}. 100 = One hitpoint per tick
     * 
     * @return the health regen value of this {@link Mob}
     */
    public abstract int getHealthRegen();
}
