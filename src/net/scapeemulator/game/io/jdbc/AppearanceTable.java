package net.scapeemulator.game.io.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.appearance.Appearance;
import net.scapeemulator.game.model.player.appearance.Appearance.Feature;
import net.scapeemulator.game.model.player.appearance.Gender;

public final class AppearanceTable extends Table<Player> {

	private final PreparedStatement loadStatement;
	private final PreparedStatement saveStatement;

	public AppearanceTable(Connection connection) throws SQLException {
		this.loadStatement = connection.prepareStatement("SELECT * FROM appearance WHERE player_id = ?;");
		this.saveStatement = connection.prepareStatement("INSERT INTO appearance (player_id, type, value) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE value = VALUES(value);");
	}

	@Override
	public void load(Player player) throws SQLException, IOException {
		loadStatement.setInt(1, player.getDatabaseId());

		Appearance appearance = new Appearance(player);

		try (ResultSet set = loadStatement.executeQuery()) {
			while (set.next()) {
				String type = set.getString("type");
				int value = set.getInt("value");

				switch (type) {
				case "gender":
					appearance.setGender(value == 0 ? Gender.MALE : Gender.FEMALE);
					appearance.reset();
					break;
				case "hair_style":
					appearance.setStyleIndex(Feature.HAIR, value);
					break;
				case "hair_color":
					appearance.setColorIndex(Feature.HAIR, value);
					break;
				case "facial_hair_style":
					appearance.setStyleIndex(Feature.FACIAL_HAIR, value);
					break;
				case "torso_style":
					appearance.setStyleIndex(Feature.TORSO, value);
					break;
				case "torso_color":
					appearance.setColorIndex(Feature.TORSO, value);
					break;
				case "arm_style":
					appearance.setStyleIndex(Feature.ARMS, value);
					break;
				case "wrist_style":
					appearance.setStyleIndex(Feature.WRISTS, value);
					break;
				case "leg_style":
					appearance.setStyleIndex(Feature.LEGS, value);
					break;
				case "leg_color":
					appearance.setColorIndex(Feature.LEGS, value);
					break;
				case "feet_style":
					appearance.setStyleIndex(Feature.FEET, value);
					break;
				case "feet_color":
					appearance.setColorIndex(Feature.FEET, value);
					break;
				case "skin_color":
					appearance.setColorIndex(Feature.SKIN, value);
					break;
				default:
					throw new IOException("unknown appearance type: " + type);
				}
			}
		}

		player.setAppearance(appearance);
	}

	@Override
	public void save(Player player) throws SQLException, IOException {
		saveStatement.setInt(1, player.getDatabaseId());

		Appearance appearance = player.getAppearance();

		saveStatement.setString(2, "gender");
		saveStatement.setInt(3, appearance.getGender().ordinal());
		saveStatement.addBatch();
		
		saveStatement.setString(2, "hair_style");
		saveStatement.setInt(3, appearance.getStyleIndex(Feature.HAIR));
		saveStatement.addBatch();

		saveStatement.setString(2, "hair_color");
		saveStatement.setInt(3, appearance.getColorIndex(Feature.HAIR));
		saveStatement.addBatch();

		saveStatement.setString(2, "facial_hair_style");
		saveStatement.setInt(3, appearance.getStyleIndex(Feature.FACIAL_HAIR));
		saveStatement.addBatch();

		saveStatement.setString(2, "torso_style");
		saveStatement.setInt(3, appearance.getStyleIndex(Feature.TORSO));
		saveStatement.addBatch();

		saveStatement.setString(2, "torso_color");
		saveStatement.setInt(3, appearance.getColorIndex(Feature.TORSO));
		saveStatement.addBatch();

		saveStatement.setString(2, "arm_style");
		saveStatement.setInt(3, appearance.getStyleIndex(Feature.ARMS));
		saveStatement.addBatch();

		saveStatement.setString(2, "wrist_style");
		saveStatement.setInt(3, appearance.getStyleIndex(Feature.WRISTS));
		saveStatement.addBatch();

		saveStatement.setString(2, "leg_style");
		saveStatement.setInt(3, appearance.getStyleIndex(Feature.LEGS));
		saveStatement.addBatch();

		saveStatement.setString(2, "leg_color");
		saveStatement.setInt(3, appearance.getColorIndex(Feature.LEGS));
		saveStatement.addBatch();

		saveStatement.setString(2, "feet_style");
		saveStatement.setInt(3, appearance.getStyleIndex(Feature.FEET));
		saveStatement.addBatch();

		saveStatement.setString(2, "feet_color");
		saveStatement.setInt(3, appearance.getColorIndex(Feature.FEET));
		saveStatement.addBatch();

		saveStatement.setString(2, "skin_color");
		saveStatement.setInt(3, appearance.getColorIndex(Feature.SKIN));
		saveStatement.addBatch();

		saveStatement.executeBatch();
	}
}
