package net.scapeemulator.game.model.mob.combat;

import java.util.ArrayList;
import java.util.List;

import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.model.player.Player;

public class MobKillListeners {
    private static List<MobKillListener> listeners = new ArrayList<>();

    static {
        listeners.add(new MobKillListener<NPC, Player>() {

            @Override
            public void mobKilled(NPC killer, Player killed) {
                System.out.println("NPC killed player");
            }
        });
        listeners.add(new MobKillListener<Player, NPC>() {

            @Override
            public void mobKilled(Player killer, NPC killed) {
                System.out.println("Player " + killer.getDisplayName() + " killed " + killed.getDefinition().getName());
            }
        });
    }

    public static void addListener(MobKillListener<?, ?> listener) {
        listeners.add(listener);
    }

    public static void mobKilled(Mob killed, Mob killer) {
        for (MobKillListener listener : listeners) {
            try {
                listener.mobKilled(killer, killed);
            } catch (ClassCastException cce) {
            }
        }
    }

}
