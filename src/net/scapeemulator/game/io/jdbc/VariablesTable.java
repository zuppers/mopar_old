package net.scapeemulator.game.io.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map.Entry;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.PlayerVariables;
import net.scapeemulator.game.model.player.PlayerVariables.Variable;

public final class VariablesTable extends Table<Player> {

    private final PreparedStatement loadStatement;
    private final PreparedStatement saveStatement;

    public VariablesTable(Connection connection) throws SQLException {
        this.loadStatement = connection.prepareStatement("SELECT * FROM variables WHERE player_id = ?;");
        this.saveStatement = connection.prepareStatement("INSERT INTO variables (player_id, variable, value) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE value = VALUES(value);");
    }

    @Override
    public void load(Player player) throws SQLException, IOException {
        loadStatement.setInt(1, player.getDatabaseId());

        PlayerVariables vars = player.getVariables();
        try (ResultSet set = loadStatement.executeQuery()) {
            while (set.next()) {
                String varS = set.getString("variable");
                int value = set.getInt("value");
                try {
                    Variable var = Variable.valueOf(varS);
                    vars.setVar(var, value);
                } catch (IllegalArgumentException e) {
                    throw new IOException("unknown variable: " + varS);
                }
            }
        }
    }

    @Override
    public void save(Player player) throws SQLException, IOException {
        saveStatement.setInt(1, player.getDatabaseId());
        PlayerVariables vars = player.getVariables();
        Iterator<Entry<Variable, Integer>> it = vars.getVariables().entrySet().iterator();
        while (it.hasNext()) {
            Entry<Variable, Integer> var = it.next();
            if (var.getKey().shouldPersist()) {
                saveStatement.setString(2, var.getKey().name());
                saveStatement.setInt(3, var.getValue());
                saveStatement.addBatch();
            }
        }
        saveStatement.executeBatch();
    }

}
