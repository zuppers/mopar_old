package net.scapeemulator.game.msg.decoder.object;

import java.io.IOException;
import net.scapeemulator.game.msg.MessageDecoder;
import net.scapeemulator.game.msg.impl.object.ObjectExamineMessage;
import net.scapeemulator.game.net.game.DataOrder;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameReader;

/**
 * Written by David Insley
 */
public final class ObjectExamineMessageDecoder extends MessageDecoder<ObjectExamineMessage> {
    
    public ObjectExamineMessageDecoder() {
        super(94);
    }

    @Override
    public ObjectExamineMessage decode(GameFrame frame) throws IOException {
        GameFrameReader reader = new GameFrameReader(frame);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
        return new ObjectExamineMessage(id);
    }
}