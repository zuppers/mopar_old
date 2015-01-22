package net.scapeemulator.game.msg.decoder.item;

import java.io.IOException;

import net.scapeemulator.game.msg.MessageDecoder;
import net.scapeemulator.game.msg.impl.item.ItemOnNPCMessage;
import net.scapeemulator.game.net.game.DataOrder;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameReader;

/**
 * @author zuppers
 */
public final class ItemOnNPCMessageDecoder extends MessageDecoder<ItemOnNPCMessage> {

    public ItemOnNPCMessageDecoder() {
        super(115);
    }

    @Override
    public ItemOnNPCMessage decode(GameFrame frame) throws IOException {
        GameFrameReader reader = new GameFrameReader(frame);
        int widgetHash = (int) reader.getUnsigned(DataType.INT, DataOrder.INVERSED_MIDDLE);
        int slot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
        int npcIndex = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
        int itemId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
        return new ItemOnNPCMessage(itemId, slot, npcIndex, widgetHash);
    }

}
