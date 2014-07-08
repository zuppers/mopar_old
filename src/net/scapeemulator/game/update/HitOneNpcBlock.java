package net.scapeemulator.game.update;

import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.msg.impl.NpcUpdateMessage;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class HitOneNpcBlock extends NpcBlock {

	private final int type;
	private final int damage;
	private final int hpRatio;

	public HitOneNpcBlock(NPC npc) {
		super(0x40);
		hpRatio = npc.getCurrentHitpoints() * 255 / npc.getMaximumHitpoints();
		damage = npc.getHits().getDamage(1);
		type = npc.getHits().getType(1);
	}

	@Override
	public void encode(NpcUpdateMessage message, GameFrameBuilder builder) {
		builder.put(DataType.BYTE, damage);
		builder.put(DataType.BYTE, DataTransformation.NEGATE, type);
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, hpRatio);
	}

}
