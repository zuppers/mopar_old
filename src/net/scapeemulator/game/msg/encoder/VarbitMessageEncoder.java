package net.scapeemulator.game.msg.encoder;

import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;

import net.scapeemulator.game.msg.MessageEncoder;
import net.scapeemulator.game.msg.impl.VarbitMessage;
import net.scapeemulator.game.net.game.DataOrder;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameBuilder;

/**
 * @author David Insley
 */
public final class VarbitMessageEncoder extends MessageEncoder<VarbitMessage> {

    public VarbitMessageEncoder() {
        super(VarbitMessage.class);
    }

    @Override
    public GameFrame encode(ByteBufAllocator alloc, VarbitMessage message) throws IOException {
        int id = message.getId();
        int value = message.getValue();

        if (value >= -128 && value <= 127) {
            GameFrameBuilder builder = new GameFrameBuilder(alloc, 37);
            builder.put(DataType.BYTE, DataTransformation.ADD, value);
            builder.put(DataType.SHORT, DataOrder.LITTLE, id);
            return builder.toGameFrame();
        } else {
            GameFrameBuilder builder = new GameFrameBuilder(alloc, 84);
            builder.put(DataType.INT, DataOrder.LITTLE, value);
            builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, id);
            return builder.toGameFrame();
        }
    }

}
