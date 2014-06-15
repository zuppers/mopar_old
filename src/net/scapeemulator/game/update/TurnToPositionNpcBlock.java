package net.scapeemulator.game.update;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.msg.impl.NpcUpdateMessage;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public class TurnToPositionNpcBlock extends NpcBlock {

    private final Position position;

    public TurnToPositionNpcBlock(NPC npc) {
        super(0x200);
        this.position = npc.getTurnToPosition();
    }

    @Override
    public void encode(NpcUpdateMessage message, GameFrameBuilder builder) {
        // The measure is in half tiles, you must add a half tile because the player is located on the middle of a tile
        builder.put(DataType.SHORT, DataTransformation.ADD, position.getX() * 2 + 1);
        builder.put(DataType.SHORT, position.getY() * 2 + 1);
    }
}
