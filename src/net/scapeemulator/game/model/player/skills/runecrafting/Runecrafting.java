package net.scapeemulator.game.model.player.skills.runecrafting;

import net.scapeemulator.game.dispatcher.item.ItemDispatcher;
import net.scapeemulator.game.dispatcher.item.ItemOnObjectDispatcher;
import net.scapeemulator.game.dispatcher.object.ObjectDispatcher;
import net.scapeemulator.game.model.SpotAnimation;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;

public class Runecrafting {

    public static final int RUNE_ESS = 1436;
    public static final int PURE_ESS = 7936;

    public static final Animation CRAFT_ANIMATION = new Animation(791);
    public static final SpotAnimation CRAFT_GFX = new SpotAnimation(186, 0, 100);

    public static void initialize() {
        ObjectDispatcher.getInstance().bind(new AltarObjectHandler());
        ObjectDispatcher.getInstance().bind(new RuinsObjectHandler());
        ItemDispatcher.getInstance().bind(new TalismanLocateHandler());
        for (RCAltar altar : RCAltar.values()) {
            ItemOnObjectDispatcher.getInstance().bind(new TalismanOnRuinsHandler(altar));
        }
    }

    public static void checkTiara(Player p, Item oldI, Item newI) {
        if (oldI != null) {
            RCAltar t = RCAltar.forTiaraId(oldI.getId());
            if (t != null) {
                p.getStateSet().setBitState(607 + t.getConfigIndex(), 0);
            }
        }
        if (newI != null) {
            RCAltar t = RCAltar.forTiaraId(newI.getId());
            if (t != null) {
                p.getStateSet().setBitState(607 + t.getConfigIndex(), 1);
            }
        }
    }

}
