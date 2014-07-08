package net.scapeemulator.game.update;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.impl.PlayerUpdateMessage;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class HitOnePlayerBlock extends PlayerBlock {

	private final int type;
	private final int damage;
	private final int hpRatio;

	public HitOnePlayerBlock(Player player) {
		super(0x1);
		hpRatio = player.getCurrentHitpoints() * 255 / player.getMaximumHitpoints();
		damage = player.getHits().getDamage(1);
		type = player.getHits().getType(1);
	}

	@Override
	public void encode(PlayerUpdateMessage message, GameFrameBuilder builder) {
		builder.put(DataType.BYTE, damage);
		builder.put(DataType.BYTE, DataTransformation.ADD, type);
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, hpRatio);
	}

}
