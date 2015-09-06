package net.scapeemulator.game.dialogue;

import net.scapeemulator.game.model.player.Player;

/**
 * @author Hadyn Richard
 */
public abstract class Dialogue {

    public DialogueContext displayTo(Player player) {
        DialogueContext context = new DialogueContext(player, this);
        player.getInterfaceSet().getChatbox().setListener(new DialogueContextListener(context));
        return context;
    }

    public abstract void initialize(DialogueContext ctx);

    public abstract void handleOption(DialogueContext ctx, DialogueOption opt);

}
