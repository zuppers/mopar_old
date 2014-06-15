package net.scapeemulator.game.msg.encoder;

import io.netty.buffer.ByteBufAllocator;
import net.scapeemulator.game.msg.MessageEncoder;
import net.scapeemulator.game.msg.impl.inter.InterfaceItemMessage;
import net.scapeemulator.game.net.game.DataOrder;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class InterfaceItemMessageEncoder extends MessageEncoder<InterfaceItemMessage> {

	public InterfaceItemMessageEncoder() {
		super(InterfaceItemMessage.class);
	}

	@Override
	public GameFrame encode(ByteBufAllocator alloc, InterfaceItemMessage message) {
		GameFrameBuilder builder = new GameFrameBuilder(alloc, 50);
		builder.put(DataType.INT, message.getSize());
		builder.put(DataType.SHORT, DataOrder.LITTLE, message.getId());
		builder.put(DataType.SHORT, DataOrder.LITTLE, message.getChild());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getItem());
		builder.put(DataType.SHORT, DataOrder.LITTLE, 0);
		return builder.toGameFrame();
	}

}
