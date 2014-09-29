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
public final class ObjectOptionThreeMessageDecoder extends MessageDecoder<ObjectOptionMessage> {

    public ObjectOptionThreeMessageDecoder() {
        super(84);
    }

    @Override
    public ObjectOptionMessage decode(GameFrame frame) {
        GameFrameReader reader = new GameFrameReader(frame);
        int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
        int y = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
        int x = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
        return new ObjectOptionMessage(id, x, y, Option.THREE);
    }

}
