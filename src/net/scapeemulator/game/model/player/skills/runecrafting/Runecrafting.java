package net.scapeemulator.game.model.player.skills.runecrafting;

import net.scapeemulator.game.GameServer;
import net.scapeemulator.game.model.SpotAnimation;
import net.scapeemulator.game.model.mob.Animation;

public class Runecrafting {

    public static final int RUNE_ESS = 1436;
    public static final int PURE_ESS = 7936;

    public static final Animation CRAFT_ANIMATION = new Animation(791);
    public static final SpotAnimation CRAFT_GFX = new SpotAnimation(186, 0, 100);

    public static void initialize() {
        GameServer.getInstance().getMessageDispatcher().getObjectDispatcher().bind(new AltarObjectHandler());
    }

}
