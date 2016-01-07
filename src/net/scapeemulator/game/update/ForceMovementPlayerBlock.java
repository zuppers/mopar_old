package net.scapeemulator.game.update;

import net.scapeemulator.game.model.player.ForcedMovement;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.impl.PlayerUpdateMessage;
import net.scapeemulator.game.net.game.DataOrder;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class ForceMovementPlayerBlock extends PlayerBlock {

    private final ForcedMovement movement;

    public ForceMovementPlayerBlock(Player player) {
        super(0x400);
        this.movement = player.getForcedMove();
    }

    @Override
    public void encode(PlayerUpdateMessage message, GameFrameBuilder builder) {
        builder.put(DataType.BYTE, DataTransformation.NEGATE, movement.getFirstPosition().getLocalX(message.getLastKnownRegion().getRegionX()));
        builder.put(DataType.BYTE, movement.getFirstPosition().getLocalY(message.getLastKnownRegion().getRegionY()));
        builder.put(DataType.BYTE, DataTransformation.ADD, movement.getSecondPosition().getLocalX(message.getLastKnownRegion().getRegionX()));
        builder.put(DataType.BYTE, movement.getSecondPosition().getLocalY(message.getLastKnownRegion().getRegionY()));
        builder.put(DataType.SHORT, DataOrder.LITTLE, movement.getMidDuration());
        builder.put(DataType.SHORT, DataOrder.LITTLE, movement.getDuration());
        builder.put(DataType.BYTE, DataTransformation.NEGATE, movement.getDirection());
    }

}
