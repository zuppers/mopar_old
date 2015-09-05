package net.scapeemulator.game.msg.encoder;

import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;

import net.scapeemulator.game.msg.MessageEncoder;
import net.scapeemulator.game.msg.impl.SetPositionMessage;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class SetPositionMessageEncoder extends MessageEncoder<SetPositionMessage> {

    public SetPositionMessageEncoder() {
        super(SetPositionMessage.class);
    }

    @Override
    public GameFrame encode(ByteBufAllocator alloc, SetPositionMessage message) throws IOException {
        int height = 0;
        int flag = 1; 
        GameFrameBuilder builder = new GameFrameBuilder(alloc, 13);
        builder.put(DataType.BYTE, DataTransformation.SUBTRACT, message.getX());
        builder.put(DataType.BYTE, DataTransformation.ADD, height << 1 | flag);
        builder.put(DataType.BYTE, message.getY());
        return builder.toGameFrame();
    }
}
