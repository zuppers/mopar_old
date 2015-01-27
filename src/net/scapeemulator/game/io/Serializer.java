package net.scapeemulator.game.io;

import net.scapeemulator.game.content.grandexchange.GEOffer;
import net.scapeemulator.game.content.grandexchange.GrandExchange;
import net.scapeemulator.game.model.player.Player;

public abstract class Serializer {

    public static class SerializeResult {

        private final int status;
        private final Player player;

        public SerializeResult(int status) {
            this(status, null);
        }

        public SerializeResult(int status, Player player) {
            this.status = status;
            this.player = player;
        }

        public int getStatus() {
            return status;
        }

        public Player getPlayer() {
            return player;
        }

    }

    public abstract SerializeResult loadPlayer(String username, String password);

    public abstract void savePlayer(Player player);

    public abstract boolean usernameAvailable(String username);

    public abstract boolean register(String username, String password, String ip);

    public abstract void loadGrandExchange(GrandExchange ge);

    public abstract void saveGrandExchange(GrandExchange ge);

    public abstract void saveGrandExchangeOffer(GEOffer offer);

    public abstract void removeGrandExchangeOffer(GEOffer offer);

    public abstract void loadNPCDefinitions();

    public abstract void loadNPCDrops();

    public abstract void loadNPCSpawns();

    public abstract void loadShops();

}
