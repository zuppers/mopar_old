package net.scapeemulator.game.msg.decoder.grounditem;

import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.msg.MessageDecoder;
import net.scapeemulator.game.msg.impl.grounditem.GroundItemOptionMessage;
import net.scapeemulator.game.net.game.DataOrder;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameReader;

/**
 *
 * @author zuppers
 */
public class GroundItemOptionFourMessageDecoder extends MessageDecoder<GroundItemOptionMessage> {

    public GroundItemOptionFourMessageDecoder() {
        super(33);
    }

    @Override
    public GroundItemOptionMessage decode(GameFrame frame) {
        GameFrameReader reader = new GameFrameReader(frame);
        int id = (int) reader.getSigned(DataType.SHORT);
        int x = (int) reader.getSigned(DataType.SHORT);
        int y = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE);
        return new GroundItemOptionMessage(id, x, y, Option.FOUR);
    }

}
