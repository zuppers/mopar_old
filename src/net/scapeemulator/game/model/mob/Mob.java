package net.scapeemulator.game.model.mob;

import net.scapeemulator.game.model.Entity;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.SpotAnimation;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.mob.action.CombatAction;
import net.scapeemulator.game.model.mob.combat.CombatHandler;
import net.scapeemulator.game.model.mob.combat.HitType;
import net.scapeemulator.game.model.mob.combat.Hits;
import net.scapeemulator.game.model.pathfinding.Path;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.action.PlayerDeathAction;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.SkillSet;
import net.scapeemulator.game.task.Action;

public abstract class Mob extends Entity {

    private static final Animation CANCEL_ANIMATION = new Animation(-1);

    protected int id;
    protected boolean teleporting;
    protected final WalkingQueue walkingQueue = new WalkingQueue(this);
    protected Direction firstDirection = Direction.NONE;
    protected Direction secondDirection = Direction.NONE;
    protected Direction mostRecentDirection = Direction.SOUTH;
    protected int size;
    protected Animation animation;
    protected SpotAnimation spotAnimation;
    protected Position turnToPosition;
    protected Hits hits = new Hits(this);
    protected Mob currentTarget;
    protected int turnToTargetId = -1;
    protected int frozen;
    protected Action<?> action;
    protected boolean hidden;
    protected CombatHandler<?> combatHandler;
    
    public Mob() {
        size = 1;
    }

    public int getId() {
        return id;
    }

    public void resetId() {
        this.id = 0;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public boolean isActive() {
        return id != 0;
    }

    public void startAction(Action<?> action) {
        if (this.action != null) {
        	if(this.action instanceof PlayerDeathAction) {
        		System.out.println("SHOULDNT BE ABLE TO DO THAT!!! " + action.toString());
        	}
            if (this.action.equals(action)) {
                return;
            }

            stopAction();
        }
        this.action = action;
        World.getWorld().getTaskScheduler().schedule(action);
    }

    public void stopAction() {
        if (action != null) {
            Action<?> oldAction = action;
            action = null;
            oldAction.stop();
        }
    }

    public boolean isTeleporting() {
        return teleporting;
    }

    public void teleport(Position position) {
        this.position = position;
        this.teleporting = true;
        this.walkingQueue.reset();
    }

    public WalkingQueue getWalkingQueue() {
        return walkingQueue;
    }

    public Direction getFirstDirection() {
        return firstDirection;
    }

    public Direction getSecondDirection() {
        return secondDirection;
    }

    public boolean notWalking() {
    	return firstDirection == Direction.NONE && secondDirection == Direction.NONE; 
    }

    public Direction getMostRecentDirection() {
        return mostRecentDirection;
    }

    public void setDirections(Direction firstDirection, Direction secondDirection) {
        this.firstDirection = firstDirection;
        this.secondDirection = secondDirection;

        if (secondDirection != Direction.NONE) {
            mostRecentDirection = secondDirection;
        } else if (firstDirection != Direction.NONE) {
            mostRecentDirection = firstDirection;
        }
    }
    
    public Action<?> getAction() {
    	return action;
    }
    
    public Animation getAnimation() {
        return animation;
    }

    public SpotAnimation getSpotAnimation() {
        return spotAnimation;
    }

    public boolean isAnimationUpdated() {
        return animation != null;
    }

    public boolean isSpotAnimationUpdated() {
        return spotAnimation != null;
    }

    public void playAnimation(Animation animation) {
        this.animation = animation;
    }

    public void cancelAnimation() {
        animation = CANCEL_ANIMATION;
    }

    public void playSpotAnimation(SpotAnimation spotAnimation) {
        this.spotAnimation = spotAnimation;
    }

    public void turnToPosition(Position turnToPosition) {
        this.turnToPosition = turnToPosition;
    }

    public boolean isTurnToPositionUpdated() {
        return turnToPosition != null;
    }

    public Position getTurnToPosition() {
        return turnToPosition;
    }

    public void turnToTarget(Mob target) {
    	if(currentTarget == target) {
    		return;
    	}
        turnToTargetId = getTargetId(target);
        currentTarget = target;
    }

    public Mob getTurnToTarget() {
        return currentTarget;
    }

    public void resetTurnToTarget() {
        turnToTargetId = 65535;
        currentTarget = null;
    }

    public boolean isTurnToTargetSet() {
        return currentTarget != null;
    }

    public boolean isHitOneUpdated() {
    	return hits.updated(1);
    }
    
    public boolean isHitTwoUpdated() {
    	return hits.updated(2);
    }
    
    public Hits getHits() {
    	return hits;
    }
    
    public void setHidden(boolean hidden) {
    	this.hidden = hidden;
    }
    
    public boolean isHidden() {
    	return hidden;
    }
    
    public boolean frozen() {
    	return frozen > 0;
    }
    
    public boolean isTurnToTargetUpdated() {
        return turnToTargetId != -1;
    }

    public int getTurnToTargetId() {
        return turnToTargetId;
    }

    public void walkPath(Path path) {
        if (!path.isEmpty()) {
            walkingQueue.reset();
            for (Position point : path.getPoints()) {
                walkingQueue.addPoint(point);
            }
        }
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (!(object instanceof Mob)) {
            return false;
        }

        Mob otherMob = (Mob) object;
        return getTargetId(otherMob) == getTargetId(this);
    }
    
    public boolean canTraverse(Direction direction) {
    	return Direction.isTraversable(position, direction, size);
    }
    
    public abstract int getCurrentHitpoints();
    protected abstract void reduceHp(int amount);
    protected abstract void onDeath();
    
    public boolean alive() {
    	return getCurrentHitpoints() > 0;
    }
    
    public void processHit(Mob source, int damage) {
    	if(!alive()) {
    		System.out.println("Processing hit while dead?");
    	}
    	if(damage > getCurrentHitpoints()) {
    		System.out.println("Damage greater than current HP");
    	}
		if(combatHandler.shouldRetaliate()) {
			startAction(new CombatAction(this, source));
		}
    	hits.addHit(source, damage > 0 ? HitType.NORMAL : HitType.ZERO, damage);
    	reduceHp(damage);
    	if(!alive()) {
    		onDeath();
    	}
    }
    
    public void reset() {
        animation = null;
        spotAnimation = null;
        turnToPosition = null;
        teleporting = false;
        turnToTargetId = -1;
        hits.reset();
        walkingQueue.setMinimapFlagReset(false);
    }

    private static int getTargetId(Mob mob) {
        int val = mob.getId();
        if (mob instanceof Player) {
            val += 0x8000;
        }
        return val;
    }

    public CombatHandler<?> getCombatHandler() {
    	return combatHandler;
    }
    
    public abstract boolean isRunning();
}
