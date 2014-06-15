package net.scapeemulator.game.msg.encoder;

import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;

import net.scapeemulator.game.msg.MessageEncoder;
import net.scapeemulator.game.msg.impl.ScriptMessage;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrame.Type;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class ScriptMessageEncoder extends MessageEncoder<ScriptMessage> {

	public ScriptMessageEncoder() {
		super(ScriptMessage.class);
	}

	@Override
	public GameFrame encode(ByteBufAllocator alloc, ScriptMessage message) throws IOException {
		String types = message.getTypes();
		Object[] parameters = message.getParameters();

		GameFrameBuilder builder = new GameFrameBuilder(alloc, 115, Type.VARIABLE_SHORT);
		builder.put(DataType.SHORT, 0);
		builder.putString(types);
		if (!message.blank()) {
			for (int i = types.length() - 1; i >= 0; i--) {
				if (types.charAt(i) == 's') {
					builder.putString((String) parameters[types.length() - 1 - i]);
				} else {
					builder.put(DataType.INT, ((Integer) parameters[types.length() - 1 - i]));
				}
			}
		}
		builder.put(DataType.INT, message.getId());
		return builder.toGameFrame();
	}

}
