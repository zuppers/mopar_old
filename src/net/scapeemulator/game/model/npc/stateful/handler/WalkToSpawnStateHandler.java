package net.scapeemulator.game.model.npc.stateful.handler;

import net.scapeemulator.game.model.npc.stateful.StateHandler;
import net.scapeemulator.game.model.npc.stateful.impl.NormalNPC;
import net.scapeemulator.game.model.pathfinding.AStarPathFinder;
import net.scapeemulator.game.model.pathfinding.Path;

public final class WalkToSpawnStateHandler extends StateHandler<NormalNPC> {

	private static final AStarPathFinder pf = new AStarPathFinder();

	@Override
	public void handle(NormalNPC npc) {

		npc.getWalkingQueue().reset();
		npc.getCombatHandler().reset();
		npc.resetTurnToTarget();
		npc.stopAction();
		npc.getCombatHandler().setNoRetaliate(10);
		Path p = pf.find(npc, npc.getSpawnPosition());
		if (p != null) {
			npc.getWalkingQueue().addFirstStep(p.poll());
			while (!p.isEmpty()) {
				npc.getWalkingQueue().addStep(p.poll());
			}
		}
	}
}
