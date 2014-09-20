package net.scapeemulator.game.msg.encoder;

import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;

import net.scapeemulator.game.msg.MessageEncoder;
import net.scapeemulator.game.msg.impl.RenameTileActionMessage;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrame.Type;
import net.scapeemulator.game.net.game.GameFrameBuilder;

/**
 * @author David Insley
 */
public class RenameTileActionMessageEncoder extends MessageEncoder<RenameTileActionMessage> {

    public RenameTileActionMessageEncoder() {
        super(RenameTileActionMessage.class);
    }

    @Override
    public GameFrame encode(ByteBufAllocator alloc, RenameTileActionMessage message) throws IOException {
        GameFrameBuilder builder = new GameFrameBuilder(alloc, 160, Type.VARIABLE_BYTE);
        if (message.getText() != null) {
            builder.putString(message.getText());
        }
        return builder.toGameFrame();
    }

}
