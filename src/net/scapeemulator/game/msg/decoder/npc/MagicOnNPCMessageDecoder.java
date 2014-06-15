package net.scapeemulator.game.msg.decoder.npc;

import java.io.IOException;
import net.scapeemulator.game.msg.MessageDecoder;
import net.scapeemulator.game.msg.impl.npc.MagicOnNPCMessage;
import net.scapeemulator.game.net.game.DataOrder;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameReader;

public final class MagicOnNPCMessageDecoder extends MessageDecoder<MagicOnNPCMessage> {
    
    public MagicOnNPCMessageDecoder() {
        super(239);
    }

    @Override
    public MagicOnNPCMessage decode(GameFrame frame) throws IOException {
        GameFrameReader reader = new GameFrameReader(frame);
        int childId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
        int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
        int junk = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
        int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
        return new MagicOnNPCMessage(interfaceId, childId, id);
    }
}