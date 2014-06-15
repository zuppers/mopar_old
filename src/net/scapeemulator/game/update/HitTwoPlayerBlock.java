package net.scapeemulator.game.update;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.impl.PlayerUpdateMessage;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class HitTwoPlayerBlock extends PlayerBlock {

	private final int type;
	private final int damage;

	public HitTwoPlayerBlock(Player player) {
		super(0x200);
		this.type = player.getHits().getType(2);
		this.damage = player.getHits().getDamage(2);
	}

	@Override
	public void encode(PlayerUpdateMessage message, GameFrameBuilder builder) {
		builder.put(DataType.BYTE, damage);
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, type);
	}

}