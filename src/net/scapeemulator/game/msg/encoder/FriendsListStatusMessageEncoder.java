package net.scapeemulator.game.msg.encoder;

import io.netty.buffer.ByteBufAllocator;
import net.scapeemulator.game.msg.MessageEncoder;
import net.scapeemulator.game.msg.impl.FriendsListStatusMessage;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class FriendsListStatusMessageEncoder extends MessageEncoder<FriendsListStatusMessage> {

	public FriendsListStatusMessageEncoder() {
		super(FriendsListStatusMessage.class);
	}

	@Override
	public GameFrame encode(ByteBufAllocator alloc, FriendsListStatusMessage message) {
		GameFrameBuilder builder = new GameFrameBuilder(alloc, 197);
		builder.put(DataType.BYTE, message.getStatus());
		return builder.toGameFrame();
	}

}
