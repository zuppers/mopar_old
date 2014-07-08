package net.scapeemulator.game.model.player.skills.magic;

import net.scapeemulator.game.model.area.Area;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.task.Action;

/**
 * @author David Insley
 */
public class TeleportSpell extends Spell {

	private final TeleportType type;
	private final Area bounds;
	private final int height;

	public TeleportSpell(TeleportType type, double xp, Area bounds, int height) {
		super(SpellType.TELEPORT, xp, type.startAnimation, type.startGraphic);
		this.type = type;
		this.bounds = bounds;
		this.height = height;
	}

	public void cast(Player player) {
		if (player.frozen()) {
			player.sendMessage("You cannot teleport while frozen!");
		}
		if (!requirements.hasRequirementsDisplayOne(player)) {
			return;
		}
		player.startAction(new TeleportAction(player));
	}

	private class TeleportAction extends Action<Player> {

		private boolean started;

		private TeleportAction(Player player) {
			super(player, type.delay, true);
		}

		public void execute() {
			if (!started) {
				mob.getWalkingQueue().reset();
				mob.playAnimation(animation);
				mob.playSpotAnimation(graphic);
				mob.setActionsBlocked(true);
				requirements.fulfillAll(mob);
				started = true;
			} else {
				mob.teleport(bounds.randomPosition(height));
				mob.playAnimation(type.endAnimation);
				mob.playSpotAnimation(type.endGraphic);
				mob.setActionsBlocked(false);
				stop();
			}
		}
	}
}
