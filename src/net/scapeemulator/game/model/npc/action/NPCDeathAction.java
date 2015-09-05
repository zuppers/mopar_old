package net.scapeemulator.game.model.npc.action;

import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.mob.combat.MobKillListeners;
import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.task.Action;

public final class NPCDeathAction extends Action<NPC> {

	private enum State {
		START, END, RESPAWN
	}

	private State state = State.START;

	public NPCDeathAction(NPC npc) {
		super(npc, 4, true);
	}

	@Override
	public void execute() {
		switch (state) {
		case START:
			mob.getWalkingQueue().reset();
			mob.getCombatHandler().reset();
			mob.playAnimation(new Animation(mob.getDefinition().getDeathEmote()));
			state = State.END;
			return;
		case END:
			mob.setHidden(true);
			mob.drop(mob.getHits().getMostDamageDealt());
			MobKillListeners.mobKilled(mob, mob.getHits().getMostDamageDealt());
			setDelay(mob.getDefinition().getRespawnTime());
			state = State.RESPAWN;
			return;
		case RESPAWN:
			mob.cancelAnimation();
			mob.healToFull();
			mob.teleport(mob.getSpawnPosition());
			mob.setHidden(false);
			stop();
		}
	}

}
