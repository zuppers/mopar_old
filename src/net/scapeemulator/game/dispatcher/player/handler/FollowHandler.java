package net.scapeemulator.game.dispatcher.player.handler;

import net.scapeemulator.game.dispatcher.player.PlayerHandler;
import net.scapeemulator.game.model.mob.action.FollowAction;
import net.scapeemulator.game.model.player.Player;

public final class FollowHandler extends PlayerHandler {

	public FollowHandler() {
		super("follow");
	}

	@Override
	public void handle(Player player, Player selectedPlayer) {
		player.startAction(new FollowAction(player, selectedPlayer, true));
	}
}