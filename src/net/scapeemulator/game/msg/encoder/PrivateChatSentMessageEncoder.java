package net.scapeemulator.game.msg.encoder;

import io.netty.buffer.ByteBufAllocator;

import net.scapeemulator.game.msg.MessageEncoder;
import net.scapeemulator.game.msg.impl.PrivateChatSentMessage;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrame.Type;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class PrivateChatSentMessageEncoder extends MessageEncoder<PrivateChatSentMessage> {

    public PrivateChatSentMessageEncoder() {
        super(PrivateChatSentMessage.class);
    }

    @Override
    public GameFrame encode(ByteBufAllocator alloc, PrivateChatSentMessage message) {
        GameFrameBuilder builder = new GameFrameBuilder(alloc, 71, Type.VARIABLE_BYTE);
        builder.put(DataType.LONG, message.getName());
        //builder.put(DataType.BYTE, message.getPacked().length);
        builder.putBytes(message.getPacked());
        return builder.toGameFrame();
    }
}
