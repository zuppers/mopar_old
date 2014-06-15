package net.scapeemulator.game.msg.decoder.npc;

import java.io.IOException;
import net.scapeemulator.game.msg.MessageDecoder;
import net.scapeemulator.game.msg.impl.npc.NPCInteractMessage;
import net.scapeemulator.game.net.game.DataOrder;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameReader;

public final class NPCInteractMessageDecoder extends MessageDecoder<NPCInteractMessage> {
    
    public NPCInteractMessageDecoder() {
        super(3);
    }

    @Override
    public NPCInteractMessage decode(GameFrame frame) throws IOException {
        GameFrameReader reader = new GameFrameReader(frame);
        int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
        return new NPCInteractMessage(id);
    }
}