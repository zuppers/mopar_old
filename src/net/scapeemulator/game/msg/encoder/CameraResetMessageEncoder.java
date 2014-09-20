package net.scapeemulator.game.msg.encoder;

import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;

import net.scapeemulator.game.msg.MessageEncoder;
import net.scapeemulator.game.msg.impl.camera.CameraResetMessage;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class CameraResetMessageEncoder extends MessageEncoder<CameraResetMessage> {

    public CameraResetMessageEncoder() {
        super(CameraResetMessage.class);
    }

    @Override
    public GameFrame encode(ByteBufAllocator alloc, CameraResetMessage message) throws IOException {
        GameFrameBuilder builder = new GameFrameBuilder(alloc, 24);
        builder.put(DataType.SHORT, -1);
        return builder.toGameFrame();
    }

}
