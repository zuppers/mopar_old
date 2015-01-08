package net.scapeemulator.game.net.jaggrab;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public final class JaggrabEncoder extends MessageToByteEncoder<String> {

	@Override
	public void encode(ChannelHandlerContext ctx, String response, ByteBuf buf) {
		return;
	}

}
