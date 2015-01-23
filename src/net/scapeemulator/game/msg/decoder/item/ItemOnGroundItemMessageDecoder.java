package net.scapeemulator.game.msg.decoder.item;

import java.io.IOException;

import net.scapeemulator.game.msg.MessageDecoder;
import net.scapeemulator.game.msg.impl.item.ItemOnGroundItemMessage;
import net.scapeemulator.game.net.game.DataOrder;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameReader;

/**
 * @author zuppers
 */
public final class ItemOnGroundItemMessageDecoder extends MessageDecoder<ItemOnGroundItemMessage> {

    public ItemOnGroundItemMessageDecoder() {
        super(101);
    }

    @Override
    public ItemOnGroundItemMessage decode(GameFrame frame) throws IOException {
        GameFrameReader reader = new GameFrameReader(frame);
        int x = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
        int slot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
        int itemId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
        int groundItemId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
        int y = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
        int widgetHash = (int) reader.getUnsigned(DataType.INT, DataOrder.INVERSED_MIDDLE);
        return new ItemOnGroundItemMessage(x, y, slot, itemId, groundItemId, widgetHash);
    }

}
