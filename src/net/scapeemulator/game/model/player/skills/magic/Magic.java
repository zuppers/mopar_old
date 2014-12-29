package net.scapeemulator.game.model.player.skills.magic;

import net.scapeemulator.game.dispatcher.button.ButtonDispatcher;

/**
 * @author David Insley
 */
public class Magic {

    public static final int NORMAL_SPELLBOOK_ID = 192;
    public static final int ANCIENT_SPELLBOOK_ID = 193;
    public static final int LUNAR_SPELLBOOK_ID = 430;

    public static void initialize() {
        ButtonDispatcher.getInstance().bind(new SpellbookHandler(NORMAL_SPELLBOOK_ID, ANCIENT_SPELLBOOK_ID, LUNAR_SPELLBOOK_ID));
        ButtonDispatcher.getInstance().bind(new AutoCastInterfaceHandler());
        Spellbook.loadBooks();
    }

}
