package net.scapeemulator.game.msg.decoder;

import java.io.IOException;

import net.scapeemulator.game.msg.MessageDecoder;
import net.scapeemulator.game.msg.impl.PrivateChatSentMessage;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameReader;

public final class PrivateChatSentMessageDecoder extends MessageDecoder<PrivateChatSentMessage> {

	public PrivateChatSentMessageDecoder() {
		super(201);
	}

	@Override
	public PrivateChatSentMessage decode(GameFrame frame) throws IOException {
		GameFrameReader reader = new GameFrameReader(frame);
		int size = reader.getLength() - 8;
		long name = reader.getUnsigned(DataType.LONG);

		byte[] packed = new byte[size];
		reader.getBytes(packed);
		return new PrivateChatSentMessage(name, packed);
	}

}
