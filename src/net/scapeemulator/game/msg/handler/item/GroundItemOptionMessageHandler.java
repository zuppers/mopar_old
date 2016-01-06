package net.scapeemulator.game.msg.handler.item;

import net.scapeemulator.game.dispatcher.grounditem.GroundItemDispatcher;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.grounditem.GroundItemOptionMessage;

/**
 * @author Hadyn Richard
 */
public final class GroundItemOptionMessageHandler extends MessageHandler<GroundItemOptionMessage> {

    private final GroundItemDispatcher dispatcher;

    public GroundItemOptionMessageHandler(GroundItemDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void handle(Player player, GroundItemOptionMessage message) {
        dispatcher.handle(player, message.getId(), new Position(message.getX(), message.getY(), player.getPosition().getHeight()), message.getOption());
    }
}
