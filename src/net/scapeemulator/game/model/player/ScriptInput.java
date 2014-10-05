package net.scapeemulator.game.model.player;

import net.scapeemulator.game.msg.impl.ScriptMessage;

/**
 * @author David Insley
 */
public final class ScriptInput {

    private final Player player;

    private ScriptInputListener listener;

    public ScriptInput(Player player) {
        this.player = player;
    }

    public void showIntegerScriptInput(ScriptInputListener listener) {
        showIntegerScriptInput("Enter amount:", listener);
    }

    public void showIntegerScriptInput(String prompt, ScriptInputListener listener) {
        player.send(new ScriptMessage(108, "s", prompt));
        this.listener = listener;
    }
    
    public void showUsernameScriptInput(String prompt, ScriptInputListener listener) {
        player.send(new ScriptMessage(109, "s", prompt));
        this.listener = listener;
    }
    
    public ScriptInputListener getListener() {
        return listener;
    }

    public void reset() {
        // TODO reset when moving at all, not just walking
        listener = null;
    }

}
