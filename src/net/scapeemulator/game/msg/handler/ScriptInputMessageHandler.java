package net.scapeemulator.game.msg.handler;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.ScriptInputMessage;

@SuppressWarnings("rawtypes")
public final class ScriptInputMessageHandler extends MessageHandler<ScriptInputMessage> {

    @Override
    public void handle(Player player, ScriptInputMessage message) {
        if (player.getScriptInput().getListener() == null) {
            System.out.println("Unhandled script input: " + message.getType());
            return;
        }
        if (message.getType() == Integer.class) {
            int value = (int) message.getValue();
            if (value < 1) {
                return;
            }
            player.getScriptInput().getListener().intInputReceived(value);
        } else if (message.getType() == Long.class) {
            long value = (long) message.getValue();
            if (value < 1) {
                return;
            }
            player.getScriptInput().getListener().usernameInputReceived(value);
        }
    }

}
