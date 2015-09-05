package net.scapeemulator.game.msg.encoder;

import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;

import net.scapeemulator.game.msg.MessageEncoder;
import net.scapeemulator.game.msg.impl.MinimapUpdateMessage;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrame.Type;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class MinimapUpdateMessageEncoder extends MessageEncoder<MinimapUpdateMessage> {

    public MinimapUpdateMessageEncoder() {
        super(MinimapUpdateMessage.class);
    }

    @Override
    public GameFrame encode(ByteBufAllocator alloc, MinimapUpdateMessage message) throws IOException {
        GameFrameBuilder builder = new GameFrameBuilder(alloc, 192, Type.FIXED);
        builder.put(DataType.BYTE, message.getValue());
        return builder.toGameFrame();
    }

}
