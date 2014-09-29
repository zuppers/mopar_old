package net.scapeemulator.game.msg.encoder;

import io.netty.buffer.ByteBufAllocator;
import net.scapeemulator.game.msg.MessageEncoder;
import net.scapeemulator.game.msg.impl.SendIgnoresMessage;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrame.Type;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class SendIgnoresMessageEncoder extends MessageEncoder<SendIgnoresMessage> {

	public SendIgnoresMessageEncoder() {
		super(SendIgnoresMessage.class);
	}

	@Override
	public GameFrame encode(ByteBufAllocator alloc, SendIgnoresMessage message) {
		GameFrameBuilder builder = new GameFrameBuilder(alloc, 126, Type.VARIABLE_SHORT);
		for (long name : message.getIgnores()) {
			if (name > 0) {
				builder.put(DataType.LONG, name);
			}
		}
		return builder.toGameFrame();
	}

}
