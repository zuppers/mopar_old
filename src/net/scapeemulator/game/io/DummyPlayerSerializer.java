package net.scapeemulator.game.io;

import net.scapeemulator.game.content.grandexchange.GEOffer;
import net.scapeemulator.game.content.grandexchange.GrandExchange;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.net.login.LoginResponse;

public final class DummyPlayerSerializer extends Serializer {

    @Override
    public SerializeResult loadPlayer(String username, String password) {
        Player player = new Player();
        player.setUsername(username);
        player.setPassword(password);
        player.setRights(2);
        player.setPosition(new Position(3222, 3222));
        return new SerializeResult(LoginResponse.STATUS_OK, player);
    }

    @Override
    public void savePlayer(Player player) {
    }

    @Override
    public boolean usernameAvailable(String username) {
        return true;
    }

    @Override
    public boolean register(String username, String password, String ip) {
        return true;
    }

    @Override
    public void loadGrandExchange(GrandExchange ge) {
    }

    @Override
    public void saveGrandExchange(GrandExchange ge) {
    }

    @Override
    public void saveGrandExchangeOffer(GEOffer offer) {
    }

    @Override
    public void removeGrandExchangeOffer(GEOffer offer) {
    }

    @Override
    public void loadNPCSpawns() {
    }

    @Override
    public void loadShops() {
    }

    @Override
    public void loadNPCDefinitions() {
    }

    @Override
    public void loadNPCDrops() {
    }

}
