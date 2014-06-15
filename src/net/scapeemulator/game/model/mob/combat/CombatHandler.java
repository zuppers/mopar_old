package net.scapeemulator.game.model.mob.combat;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.skills.magic.CombatSpell;
import net.scapeemulator.game.model.player.skills.magic.Spell;
import net.scapeemulator.game.msg.impl.CreateProjectileMessage;
import net.scapeemulator.game.msg.impl.PlacementCoordsMessage;

public abstract class CombatHandler<T extends Mob> {

	protected final T mob;
	protected Mob target;
	protected CombatSpell autoCast;
	protected Spell nextSpell;
	protected AttackStyle attackStyle;
	protected AttackType attackType;
	protected int noRetaliate;

	/**
	 * The delay (in ticks) until the next attack can be administered.
	 */
	protected int combatDelay;

	public CombatHandler(T mob) {
		this.mob = mob;
	}

	public void tick() {
		if (combatDelay > 0) {
			combatDelay--;
		}
		if (noRetaliate > 0) {
			noRetaliate--;
		}
	}

	public void initiate(Mob target) {
		if (nextSpell == null && autoCast != null) {
			nextSpell = autoCast;
		}
		mob.turnToTarget(target);
		this.target = target;
	}

	public void hitTarget(int damage) {
		hit(target, damage);
	}
	
	public void hit(Mob other, int damage) {
		other.processHit(mob, damage);
	}
	
	public void sendProjectile(int graphic, int startHeight, int endHeight, int startSpeed, int speed) {
		Position source = mob.getPosition();
		Position destination = target.getPosition();
		for (Player p : World.getWorld().getPlayers()) {
			if (!p.getPosition().isWithinScene(mob.getPosition())) {
				continue;
			}
			int localX = source.getX() - p.getPosition().getBaseLocalX(p.getLastKnownRegion().getX() >> 3) - 3;
			int localY = source.getY() - p.getPosition().getBaseLocalY(p.getLastKnownRegion().getY() >> 3) - 2;
			p.send(new PlacementCoordsMessage(localX, localY));
			p.send(new CreateProjectileMessage(source, destination, target, graphic, startHeight, endHeight, startSpeed, speed));
		}
	}

	public void reset() {
		target = null;
		nextSpell = autoCast;
	}

	public void setNoRetaliate(int delay) {
		noRetaliate = delay;
	}
	
	public int getNoRetaliate() {
		return noRetaliate;
	}
	
	public void setNextSpell(Spell nextSpell) {
		this.nextSpell = nextSpell;
	}

	public CombatSpell getAutoCast() {
		return autoCast;
	}
	
	public void setAutoCast(CombatSpell autoCast) {
		this.autoCast = autoCast;
		nextSpell = autoCast;
	}
	
	public abstract boolean canAttack(Mob target);
	
	public abstract boolean attack();

	public abstract int getAttackRange();

	public abstract boolean shouldRetaliate();
	
	public abstract int getMaxHit();
	
	public abstract Animation getDefendAnimation();

}
