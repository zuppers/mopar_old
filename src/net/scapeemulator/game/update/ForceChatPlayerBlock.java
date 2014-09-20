package net.scapeemulator.game.update;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.impl.PlayerUpdateMessage;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class ForceChatPlayerBlock extends PlayerBlock {

    private final String chat;

    public ForceChatPlayerBlock(Player player) {
        super(0x20);
        chat = player.getForcedChat();
    }

    @Override
    public void encode(PlayerUpdateMessage message, GameFrameBuilder builder) {
        builder.putString(chat);
    }

}
