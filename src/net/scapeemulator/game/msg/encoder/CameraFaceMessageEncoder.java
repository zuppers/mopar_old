package net.scapeemulator.game.msg.encoder;

import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;

import net.scapeemulator.game.msg.MessageEncoder;
import net.scapeemulator.game.msg.impl.CameraFaceMessage;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class CameraFaceMessageEncoder extends MessageEncoder<CameraFaceMessage> {

    public CameraFaceMessageEncoder() {
        super(CameraFaceMessage.class);
    }

    @Override
    public GameFrame encode(ByteBufAllocator alloc, CameraFaceMessage message) throws IOException {
        GameFrameBuilder builder = new GameFrameBuilder(alloc, 125);
        builder.put(DataType.SHORT, -1);
        builder.put(DataType.BYTE, message.getX());
        builder.put(DataType.BYTE, message.getY());
        builder.put(DataType.SHORT, message.getUnknown());
        builder.put(DataType.BYTE, message.getConstantSpeed());
        builder.put(DataType.BYTE, message.getVariableSpeed());
        return builder.toGameFrame();
    }

}
