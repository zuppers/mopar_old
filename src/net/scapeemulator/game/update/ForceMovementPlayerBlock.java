package net.scapeemulator.game.update;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.impl.PlayerUpdateMessage;
import net.scapeemulator.game.net.game.DataOrder;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class ForceMovementPlayerBlock extends PlayerBlock {

    public ForceMovementPlayerBlock(Player player) {
        super(0x400);
    }

    @Override
    public void encode(PlayerUpdateMessage message, GameFrameBuilder builder) {
        builder.put(DataType.BYTE, DataTransformation.NEGATE, 2);
        builder.put(DataType.BYTE, 2);
        builder.put(DataType.BYTE, DataTransformation.ADD, 2);
        builder.put(DataType.BYTE, 2);
        builder.put(DataType.SHORT, DataOrder.LITTLE, 2);
        builder.put(DataType.SHORT, DataOrder.LITTLE, 2);
        builder.put(DataType.BYTE, DataTransformation.NEGATE, 2);
    }

}
