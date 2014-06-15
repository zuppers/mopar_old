package net.scapeemulator.game.msg.decoder;

import java.io.IOException;
import net.scapeemulator.game.msg.MessageDecoder;
import net.scapeemulator.game.msg.impl.GrandExchangeSearchMessage;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameReader;

public final class GrandExchangeSearchMessageDecoder extends MessageDecoder<GrandExchangeSearchMessage> {

	public GrandExchangeSearchMessageDecoder() {
		super(111);
	}

	@Override
	public GrandExchangeSearchMessage decode(GameFrame frame) throws IOException {
		GameFrameReader reader = new GameFrameReader(frame);
		int itemId = (int) reader.getUnsigned(DataType.SHORT);
		return new GrandExchangeSearchMessage(itemId);
	}

}
