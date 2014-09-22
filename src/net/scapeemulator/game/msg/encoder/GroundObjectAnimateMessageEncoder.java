package net.scapeemulator.game.msg.encoder;

import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;

import net.scapeemulator.game.msg.MessageEncoder;
import net.scapeemulator.game.msg.impl.object.GroundObjectAnimateMessage;
import net.scapeemulator.game.net.game.DataOrder;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameBuilder;

/**
 * @author David Insley
 */
public final class GroundObjectAnimateMessageEncoder extends MessageEncoder<GroundObjectAnimateMessage> {

    public GroundObjectAnimateMessageEncoder() {
        super(GroundObjectAnimateMessage.class);
    }

    @Override
    public GameFrame encode(ByteBufAllocator alloc, GroundObjectAnimateMessage msg) throws IOException {
        GameFrameBuilder builder = new GameFrameBuilder(alloc, 20);
        builder.put(DataType.BYTE, DataTransformation.SUBTRACT, msg.getPosition().blockHash());
        builder.put(DataType.BYTE, DataTransformation.SUBTRACT, msg.infoHash());
        builder.put(DataType.SHORT, DataOrder.LITTLE, msg.getAnimationId());
        return builder.toGameFrame();
    }
}