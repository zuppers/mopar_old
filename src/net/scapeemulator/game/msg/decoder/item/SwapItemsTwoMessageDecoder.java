package net.scapeemulator.game.msg.decoder.item;

import java.io.IOException;

import net.scapeemulator.game.msg.MessageDecoder;
import net.scapeemulator.game.msg.impl.SwapItemsMessage;
import net.scapeemulator.game.net.game.DataOrder;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameReader;

public final class SwapItemsTwoMessageDecoder extends MessageDecoder<SwapItemsMessage> {

	public SwapItemsTwoMessageDecoder() {
		super(79);
	}

	@Override
	public SwapItemsMessage decode(GameFrame frame) throws IOException {
		GameFrameReader reader = new GameFrameReader(frame);
		int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int childId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int destination = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int interface2 = (int) reader.getUnsigned(DataType.SHORT);
		int child2 = (int) reader.getUnsigned(DataType.SHORT);
		int source = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		return new SwapItemsMessage(interfaceId, childId, source, destination, child2);
	}

}
