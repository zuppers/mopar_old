package net.scapeemulator.game.update;

import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.msg.impl.NpcUpdateMessage;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class HitTwoNpcBlock extends NpcBlock {

	private final int type;
	private final int damage;

	public HitTwoNpcBlock(NPC npc) {
		super(0x2);
		this.type = npc.getHits().getType(2);
		this.damage = npc.getHits().getDamage(2);
	}

	@Override
	public void encode(NpcUpdateMessage message, GameFrameBuilder builder) {
		builder.put(DataType.BYTE, DataTransformation.NEGATE, damage);
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, type);
	}

}