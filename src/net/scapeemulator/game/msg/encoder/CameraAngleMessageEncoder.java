package net.scapeemulator.game.msg.encoder;

import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;

import net.scapeemulator.game.msg.MessageEncoder;
import net.scapeemulator.game.msg.impl.CameraAngleMessage;
import net.scapeemulator.game.net.game.DataOrder;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameBuilder;

/**
 * @author David Insley
 */
public final class CameraAngleMessageEncoder extends MessageEncoder<CameraAngleMessage> {

    public CameraAngleMessageEncoder() {
        super(CameraAngleMessage.class);
    }

    @Override
    public GameFrame encode(ByteBufAllocator alloc, CameraAngleMessage message) throws IOException {
        GameFrameBuilder builder = new GameFrameBuilder(alloc, 187);
        builder.put(DataType.SHORT, DataOrder.LITTLE, message.getPitch());
        builder.put(DataType.SHORT, -1);
        builder.put(DataType.SHORT, message.getYaw());
        return builder.toGameFrame();
    }

}
