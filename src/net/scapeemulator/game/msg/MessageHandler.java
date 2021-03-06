package net.scapeemulator.game.msg;

import net.scapeemulator.game.model.player.Player;

public abstract class MessageHandler<T extends Message> {

	public abstract void handle(Player player, T message);

}
