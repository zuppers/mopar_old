package net.scapeemulator.game.msg.decoder.player;

import java.io.IOException;

import net.scapeemulator.game.model.ExtendedOption;
import net.scapeemulator.game.msg.MessageDecoder;
import net.scapeemulator.game.msg.impl.player.PlayerOptionMessage;
import net.scapeemulator.game.net.game.DataOrder;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameReader;

/**
 * @author David Insley
 */
public final class PlayerOptionThreeMessageDecoder extends MessageDecoder<PlayerOptionMessage> {

    public PlayerOptionThreeMessageDecoder() {
        super(71);
    }

    @Override
    public PlayerOptionMessage decode(GameFrame frame) throws IOException {
        GameFrameReader reader = new GameFrameReader(frame);
        int selectedId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
        return new PlayerOptionMessage(selectedId, ExtendedOption.THREE);
    }
}