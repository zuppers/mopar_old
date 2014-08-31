package net.scapeemulator.game.update;

import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.msg.impl.NpcUpdateMessage;
import net.scapeemulator.game.net.game.DataOrder;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class ChangeTypeNpcBlock extends NpcBlock {

    private final int type;

    public ChangeTypeNpcBlock(NPC npc) {
        super(0x1);
        this.type = npc.getChangingType();
    }

    @Override
    public void encode(NpcUpdateMessage message, GameFrameBuilder builder) {
        builder.put(DataType.SHORT, DataOrder.LITTLE, type);
    }

}