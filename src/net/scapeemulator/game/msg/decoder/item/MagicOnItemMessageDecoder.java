package net.scapeemulator.game.msg.decoder.item;

import java.io.IOException;

import net.scapeemulator.game.msg.MessageDecoder;
import net.scapeemulator.game.msg.impl.item.MagicOnItemMessage;
import net.scapeemulator.game.net.game.DataOrder;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameReader;

/**
 * @author David Insley
 */
public final class MagicOnItemMessageDecoder extends MessageDecoder<MagicOnItemMessage> {

    public MagicOnItemMessageDecoder() {
        super(253);
    }

    @Override
    public MagicOnItemMessage decode(GameFrame frame) throws IOException {
        GameFrameReader reader = new GameFrameReader(frame);
        int spellId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
        int tabId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
        int slot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
        reader.getUnsigned(DataType.INT, DataOrder.LITTLE);
        int itemId = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
        reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
        return new MagicOnItemMessage(tabId, spellId, slot, itemId);
    }
}
