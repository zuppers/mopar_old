package net.scapeemulator.game.io.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.scapeemulator.game.model.player.Player;

public final class FriendsTable extends Table<Player> {

	private final PreparedStatement loadStatement;
	private final PreparedStatement saveStatement;
	private final PreparedStatement clearStatement;
	
	public FriendsTable(Connection connection) throws SQLException {
		this.loadStatement = connection.prepareStatement("SELECT * FROM friends WHERE player_id = ?;");
		this.saveStatement = connection.prepareStatement("INSERT INTO friends (player_id, list, list_index, name) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE name = VALUES(name);");
		this.clearStatement = connection.prepareStatement("DELETE FROM friends WHERE player_id = ? and list = ? and list_index >= ?;");
	}

	@Override
	public void load(Player player) throws SQLException, IOException {
		loadStatement.setInt(1, player.getDatabaseId());

		long[] friendsA = new long[200];
		long[] ignoresA = new long[100];

		try (ResultSet set = loadStatement.executeQuery()) {
			while (set.next()) {
				String list = set.getString("list");
				int index = set.getInt("list_index");
				long name = set.getLong("name");
				switch (list) {
				case "FRIEND":
					friendsA[index] = name;
					break;
				case "IGNORE":
					ignoresA[index] = name;
					break;
				}
			}
		}
		List<Long> friends = new ArrayList<Long>();
		for (long l : friendsA) {
			if (l > 0) {
				friends.add(l);
			}
		}
		List<Long> ignores = new ArrayList<Long>();
		for (long l : ignoresA) {
			if (l > 0) {
				ignores.add(l);
			}
		}
		player.getFriends().loadedLists(friends, ignores);
	}

	@Override
	public void save(Player player) throws SQLException, IOException {
		
		List<Long> friends = player.getFriends().getFriendsList();
		clearStatement.setInt(1, player.getDatabaseId());
		clearStatement.setString(2, "FRIEND");
		clearStatement.setInt(3, friends.size());
		clearStatement.addBatch();
		if (friends.size() > 0) {
			saveStatement.setInt(1, player.getDatabaseId());
			saveStatement.setString(2, "FRIEND");
			for (int i = 0; i < friends.size(); i++) {
				saveStatement.setInt(3, i);
				saveStatement.setLong(4, friends.get(i));
				saveStatement.addBatch();
			}
			saveStatement.executeBatch();
		}
		List<Long> ignores = player.getFriends().getIgnoreList();
		clearStatement.setInt(1, player.getDatabaseId());
		clearStatement.setString(2, "IGNORE");
		clearStatement.setInt(3, ignores.size());
		clearStatement.addBatch();
		clearStatement.executeBatch();
		if (ignores.size() > 0) {
			saveStatement.setInt(1, player.getDatabaseId());
			saveStatement.setString(2, "IGNORE");
			for (int i = 0; i < friends.size(); i++) {
				saveStatement.setInt(3, i);
				saveStatement.setLong(4, ignores.get(i));
				saveStatement.addBatch();
			}
			saveStatement.executeBatch();
		}
		
	}

}
