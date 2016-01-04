package net.scapeemulator.game.dialogue;

import net.scapeemulator.game.model.player.Player;

/**
 * @author Hadyn Richard
 */
public abstract class Dialogue<T> {

    public DialogueContext<T> displayTo(Player player) {
        DialogueContext<T> context = new DialogueContext<>(player, this);
        player.getInterfaceSet().getChatbox().setListener(new DialogueContextListener(context));
        return context;
    }

    public abstract void initialize(DialogueContext<T> ctx);

    public abstract void handleOption(DialogueContext<T> ctx, DialogueOption opt);

}
