package net.scapeemulator.game.update;

import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.msg.impl.NpcUpdateMessage;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class ForceChatNpcBlock extends NpcBlock {

    private final String chat;

    public ForceChatNpcBlock(NPC npc) {
        super(0x20);
        chat = npc.getForcedChat();
    }

    @Override
    public void encode(NpcUpdateMessage message, GameFrameBuilder builder) {
        builder.putString(chat);
    }

}
