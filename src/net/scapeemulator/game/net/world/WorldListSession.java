package net.scapeemulator.game.net.world;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import net.scapeemulator.game.GameServer;
import net.scapeemulator.game.net.Session;

public final class WorldListSession extends Session {

    private static final Country[] COUNTRIES = { new Country(Country.FLAG_USA, "USA") };

    public WorldListSession(GameServer server, Channel channel) {
        super(server, channel);
    }

    @Override
    public void messageReceived(Object message) {
        World[] worlds = { new World(1, World.FLAG_MEMBERS, 0, "MoparScape Server", "game.moparscape.org"), new World(2, 0, 0, "Local", "127.0.0.1") };
        int[] players = { net.scapeemulator.game.model.World.getWorld().getPlayers().getSize(), 0 };
        channel.write(new WorldListMessage(0xDEADBEEF, COUNTRIES, worlds, players)).addListener(ChannelFutureListener.CLOSE);
    }

}
