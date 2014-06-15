package net.scapeemulator.game.msg.decoder.npc;

import java.io.IOException;
import net.scapeemulator.game.msg.MessageDecoder;
import net.scapeemulator.game.msg.impl.npc.NPCExamineMessage;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameReader;

public final class NPCExamineMessageDecoder extends MessageDecoder<NPCExamineMessage> {
    
    public NPCExamineMessageDecoder() {
        super(72);
    }

    @Override
    public NPCExamineMessage decode(GameFrame frame) throws IOException {
        GameFrameReader reader = new GameFrameReader(frame);
        int id = (int) reader.getUnsigned(DataType.SHORT);
        return new NPCExamineMessage(id);
    }
}