package net.scapeemulator.game.io.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.player.Player;

public final class PlayersTable extends Table<Player> {

    private final PreparedStatement loadStatement;
    private final PreparedStatement saveStatement;

    public PlayersTable(Connection connection) throws SQLException {
        this.loadStatement = connection.prepareStatement("SELECT * FROM players WHERE id = ?");
        this.saveStatement = connection.prepareStatement("UPDATE players SET x = ?, y = ?, height = ? WHERE id = ?;");
    }

    @Override
    public void load(Player player) throws SQLException, IOException {
        loadStatement.setInt(1, player.getDatabaseId());

        try (ResultSet set = loadStatement.executeQuery()) {
            if (!set.first())
                throw new IOException();

            player.setUsername(set.getString("username"));
            player.setRights(set.getInt("rights"));

            int x = set.getInt("x");
            int y = set.getInt("y");
            int height = set.getInt("height");
            player.setPosition(new Position(x, y, height));
        }
    }

    @Override
    public void save(Player player) throws SQLException {
        saveStatement.setInt(4, player.getDatabaseId());
        Position position = player.getPosition();
        saveStatement.setInt(1, position.getX());
        saveStatement.setInt(2, position.getY());
        saveStatement.setInt(3, position.getHeight());

        saveStatement.execute();
    }

}
