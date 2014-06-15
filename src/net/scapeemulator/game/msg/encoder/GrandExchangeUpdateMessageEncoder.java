package net.scapeemulator.game.msg.encoder;

import io.netty.buffer.ByteBufAllocator;
import java.io.IOException;
import net.scapeemulator.game.msg.MessageEncoder;
import net.scapeemulator.game.msg.impl.GrandExchangeUpdateMessage;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class GrandExchangeUpdateMessageEncoder extends MessageEncoder<GrandExchangeUpdateMessage> {

	public GrandExchangeUpdateMessageEncoder() {
		super(GrandExchangeUpdateMessage.class);
	}

	@Override
	public GameFrame encode(ByteBufAllocator alloc, GrandExchangeUpdateMessage message) throws IOException {
		GameFrameBuilder builder = new GameFrameBuilder(alloc, 116);
		builder.put(DataType.BYTE, message.getOffer().getSlot());
		builder.put(DataType.BYTE, message.getOffer().getBarId());
		builder.put(DataType.SHORT, message.getOffer().getItemId());
		builder.put(DataType.INT, message.getOffer().getPrice());
		builder.put(DataType.INT, message.getOffer().getOfferAmount());
		builder.put(DataType.INT, message.getOffer().getAmountComplete());
		builder.put(DataType.INT, message.getOffer().getTotalCoins());
		return builder.toGameFrame();
	}

}
