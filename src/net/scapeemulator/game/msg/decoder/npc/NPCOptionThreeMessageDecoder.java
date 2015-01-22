package net.scapeemulator.game.msg.decoder.npc;

import java.io.IOException;

import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.msg.MessageDecoder;
import net.scapeemulator.game.msg.impl.npc.NPCOptionMessage;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameReader;

/**
 * @author David Insley
 */
public final class NPCOptionThreeMessageDecoder extends MessageDecoder<NPCOptionMessage> {

    public NPCOptionThreeMessageDecoder() {
        super(148);
    }

    @Override
    public NPCOptionMessage decode(GameFrame frame) throws IOException {
        GameFrameReader reader = new GameFrameReader(frame);
        int id = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
        return new NPCOptionMessage(id, Option.THREE);
    }
}