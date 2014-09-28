package net.scapeemulator.game.msg.handler.object;

import net.scapeemulator.game.dispatcher.object.ObjectDispatcher;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.object.ObjectOptionMessage;

/**
 * @author Hadyn Richard
 */
public final class ObjectOptionMessageHandler extends MessageHandler<ObjectOptionMessage> {

    private final ObjectDispatcher dispatcher;

    public ObjectOptionMessageHandler(ObjectDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void handle(Player player, ObjectOptionMessage msg) {
        dispatcher.handle(player, msg.getId(), new Position(msg.getX(), msg.getY(), player.getPosition().getHeight()), msg.getOption());
    }
}
