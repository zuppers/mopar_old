package net.scapeemulator.game.dispatcher.command.impl;

import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import net.scapeemulator.game.dispatcher.command.CommandHandler;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.util.StringUtils;

/**
 * Handles the <tt>::yell</tt> command.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class YellCommandHandler extends CommandHandler {

	/**
	 * An immutable {@link List} of invalid {@code String} input within a yell
	 * message.
	 */
	private static final List<String> INVALID_INPUT = Arrays.asList("@", "req:", "<", ">", "#", "`", "~");

	/**
	 * A {@link CharsetEncoder} used to determine if all characters within this
	 * yell message are valid to the standard us-ascii charset.
	 */
	private static final CharsetEncoder ENCODER = StandardCharsets.US_ASCII.newEncoder();

	/**
	 * The amount of characters, including whitespace, a yell message is allowed
	 * to contain.
	 */
	private static final int CHARACTER_THRESHOLD = 120; // XXX: should this be more or less?

	/**
	 * Constructs a new {@link YellCommandHandler} with the specified command
	 * name.
	 * 
	 * @param name This commands name.
	 */
	public YellCommandHandler(String name) {
		super(name);
	}

	@Override
	public void handle(Player player, String[] arguments) {
		String message = StringUtils.join(arguments);

		if (message.isEmpty()) {
			player.sendMessage("You cannot yell an empty message!");
			return;
		}

		if (message.length() > CHARACTER_THRESHOLD) {
			player.sendMessage("Your message exceeded the maximum character threshold and could not be sent.");
			return;
		}

		Optional<String> invalid = INVALID_INPUT.stream().filter(message::contains).findFirst();
		if (invalid.isPresent()) {
			player.sendMessage("The message you attempted to send contained an illegal character.");
			return;
		}

		if (!ENCODER.canEncode(message)) {
			player.sendMessage("The message you attempted to send contained an illegal character.");
			return;
		}

		World.getWorld().sendGlobalMessage(String.format("[%s]: %s", player.getUsername(), message));
	}

}