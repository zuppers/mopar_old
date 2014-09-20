package net.scapeemulator.game.msg.encoder;

import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;

import net.scapeemulator.game.msg.MessageEncoder;
import net.scapeemulator.game.msg.impl.camera.CameraMoveMessage;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class CameraMoveMessageEncoder extends MessageEncoder<CameraMoveMessage> {

    public CameraMoveMessageEncoder() {
        super(CameraMoveMessage.class);
    }

    @Override
    public GameFrame encode(ByteBufAllocator alloc, CameraMoveMessage message) throws IOException {
        GameFrameBuilder builder = new GameFrameBuilder(alloc, 154);
        builder.put(DataType.SHORT, -1);
        builder.put(DataType.BYTE, message.getX());
        builder.put(DataType.BYTE, message.getY());
        builder.put(DataType.SHORT, message.getHeight());
        builder.put(DataType.BYTE, message.getConstantSpeed());
        builder.put(DataType.BYTE, message.getVariableSpeed());
        return builder.toGameFrame();
    }

}
