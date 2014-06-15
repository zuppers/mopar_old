package net.scapeemulator.game.msg.decoder;

import java.io.IOException;

import net.scapeemulator.game.msg.MessageDecoder;
import net.scapeemulator.game.msg.impl.IntegerScriptInputMessage;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameReader;

/**
 * Written by David Insley
 */
public final class IntegerScriptInputMessageDecoder extends MessageDecoder<IntegerScriptInputMessage> {
    
    public IntegerScriptInputMessageDecoder() {
        super(23);
    }

    @Override
    public IntegerScriptInputMessage decode(GameFrame frame) throws IOException {
        GameFrameReader reader = new GameFrameReader(frame);
        int value = (int) reader.getUnsigned(DataType.INT);
        return new IntegerScriptInputMessage(value);
    }
}
