package net.scapeemulator.game.msg.encoder;

import io.netty.buffer.ByteBufAllocator;
import net.scapeemulator.game.msg.MessageEncoder;
import net.scapeemulator.game.msg.impl.FriendStatusMessage;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrame.Type;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class FriendStatusMessageEncoder extends MessageEncoder<FriendStatusMessage> {

	public FriendStatusMessageEncoder() {
		super(FriendStatusMessage.class);
	}

	@Override
	public GameFrame encode(ByteBufAllocator alloc, FriendStatusMessage message) {
		GameFrameBuilder builder = new GameFrameBuilder(alloc, 62, Type.VARIABLE_BYTE);
		builder.put(DataType.LONG, message.getFriend());
		builder.put(DataType.SHORT, message.getWorld());
		builder.put(DataType.BYTE, 0); // TODO clan rank
		if(message.getWorld() > 0) {
			builder.putString("Online"); // TODO check world
		}
		return builder.toGameFrame();
	}

}
