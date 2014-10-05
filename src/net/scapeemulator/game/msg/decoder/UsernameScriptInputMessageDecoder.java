package net.scapeemulator.game.msg.decoder;

import java.io.IOException;

import net.scapeemulator.game.msg.MessageDecoder;
import net.scapeemulator.game.msg.impl.ScriptInputMessage;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameReader;

/**
 * @author David Insley
 */
public final class UsernameScriptInputMessageDecoder extends MessageDecoder<ScriptInputMessage<Long>> {

    public UsernameScriptInputMessageDecoder() {
        super(244);
    }

    @Override
    public ScriptInputMessage<Long> decode(GameFrame frame) throws IOException {
        GameFrameReader reader = new GameFrameReader(frame);
        long value = reader.getUnsigned(DataType.LONG);
        return new ScriptInputMessage<>(value);
    }
}
