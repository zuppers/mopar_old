package net.scapeemulator.game.net.jaggrab;

import io.netty.buffer.MessageBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public final class JaggrabDecoder extends MessageToMessageDecoder<String> {

    @Override
    public void decode(ChannelHandlerContext ctx, String str, MessageBuf<Object> out) throws Exception {
        if (!str.startsWith("JAGGRAB ")) {
            return;
        }
        out.add(new JaggrabRequest(str.substring(8)));
    }

}
