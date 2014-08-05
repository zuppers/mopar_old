package net.scapeemulator.game.msg.decoder.item;

import java.io.IOException;

import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.msg.MessageDecoder;
import net.scapeemulator.game.msg.impl.item.ItemOptionMessage;
import net.scapeemulator.game.net.game.DataOrder;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameReader;

/**
 * @author David Insley
 * @author Hayden Richard
 */
public final class ItemOptionOneMessageDecoder extends MessageDecoder<ItemOptionMessage> {

    public ItemOptionOneMessageDecoder() {
        super(156);
    }

    @Override
    public ItemOptionMessage decode(GameFrame frame) throws IOException {
        GameFrameReader reader = new GameFrameReader(frame);
        int slot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
        int itemId = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
        int inter = (int) reader.getUnsigned(DataType.INT, DataOrder.LITTLE);
        return new ItemOptionMessage(itemId, slot, inter, Option.ONE);
    }

}
