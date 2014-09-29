package net.scapeemulator.game.msg.decoder.object;

import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.msg.MessageDecoder;
import net.scapeemulator.game.msg.impl.object.ObjectOptionMessage;
import net.scapeemulator.game.net.game.DataOrder;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameReader;

/**
 * @author David Insley
 */
public final class ObjectOptionFourMessageDecoder extends MessageDecoder<ObjectOptionMessage> {

    public ObjectOptionFourMessageDecoder() {
        super(247);
    }

    @Override
    public ObjectOptionMessage decode(GameFrame frame) {
        GameFrameReader reader = new GameFrameReader(frame);
        int y = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
        int x = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
        int id = (int) reader.getUnsigned(DataType.SHORT);
        return new ObjectOptionMessage(id, x, y, Option.FOUR);
    }

}
