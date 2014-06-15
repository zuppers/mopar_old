package net.scapeemulator.game.model.npc;

import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.mob.combat.AttackStyle;
import net.scapeemulator.game.model.mob.combat.CombatHandler;
import net.scapeemulator.game.model.mob.combat.DelayedMagicHit;
import net.scapeemulator.game.model.player.skills.magic.CombatSpell;

public class NPCCombatHandler extends CombatHandler<NPC> {

	public NPCCombatHandler(NPC npc) {
		super(npc);
		attackStyle = AttackStyle.ACCURATE;
		// autoCast = npc.getDefinition().getAutoCast();
	}
	
	@Override
	public boolean canAttack(Mob target) {
		return mob.alive() && target.alive();
	}
	
	@Override
	public boolean attack() {
		if (combatDelay > 0) {
			return false;
		}
		int damage = 1 + (int) (Math.random() * getMaxHit());
		damage = damage > target.getCurrentHitpoints() ? target.getCurrentHitpoints() : damage;
		if (nextSpell != null) {
			CombatSpell cs = (CombatSpell) nextSpell;
			cs.cast(mob, target);
			mob.playAnimation(new Animation(mob.getDefinition().getAttackEmote()));
			World.getWorld().getTaskScheduler().schedule(new DelayedMagicHit(mob, target, cs.getExplosionGraphic(), damage));
			nextSpell = autoCast;
		} else {
			mob.playAnimation(new Animation(mob.getDefinition().getAttackEmote()));
			hitTarget(damage);
		}
		combatDelay = mob.getDefinition().getAttackDelay();
		return true;
	}

	@Override
	public Animation getDefendAnimation() {
		return new Animation(mob.getDefinition().getDefendEmote());
	}

	@Override
	public int getAttackRange() {
		return mob.getDefinition().getAttackRange();
	}

	@Override
	public boolean shouldRetaliate() {
		return target == null && noRetaliate < 1;
	}

	@Override
	public int getMaxHit() {
		return mob.getDefinition().getMaxHit();
	}

}
