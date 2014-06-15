package net.scapeemulator.game.model.player.action;

import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.task.Action;

public final class PlayerDeathAction extends Action<Player> {

	private boolean started;

	public PlayerDeathAction(Player player) {
		super(player, 8, true);
	}

	@Override
	public void execute() {
		if (!started) {
			mob.getWalkingQueue().reset();
			mob.getCombatHandler().reset();
			mob.playAnimation(mob.getDeathAnimation());
			started = true;
		} else {
			mob.cancelAnimation();
			Mob killer = mob.getHits().getMostDamageDealt();

			// TODO if(shouldDropItems()) {
			mob.getInventory().dropAll(killer);
			mob.getEquipment().dropAll(killer);
			mob.getPlayerCombatHandler().weaponChanged();
			// }

			mob.getSkillSet().restoreStats();
			mob.teleport(mob.getHomeLocation());
			mob.sendMessage("Oh dear, you have died!");
			stop();
			return;
		}
	}

}
