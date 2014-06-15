package net.scapeemulator.game.io.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.SkillSet;

public final class SkillsTable extends Table<Player> {

	private final PreparedStatement loadStatement;
	private final PreparedStatement saveStatement;

	public SkillsTable(Connection connection) throws SQLException {
		this.loadStatement = connection.prepareStatement("SELECT * FROM skills WHERE player_id = ?;");
		this.saveStatement = connection.prepareStatement("INSERT INTO skills (player_id, skill, lvl, xp) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE lvl = VALUES(lvl), xp = VALUES(xp);");
	}

	@Override
	public void load(Player player) throws SQLException, IOException {
		loadStatement.setInt(1, player.getDatabaseId());
		SkillSet skills = player.getSkillSet();

		try (ResultSet set = loadStatement.executeQuery()) {
			while (set.next()) {
				String skill = set.getString("skill");
				int lvl = set.getInt("lvl");
				double xp = set.getDouble("xp");
				for(int i = 0; i < Skill.SKILL_NAMES.length; i++) {
					if(Skill.SKILL_NAMES[i].equals(skill)) {
						skills.setCurrentLevel(i, lvl);
						skills.setExperience(i, xp);
						break;
					}
				}
			}
		}
	}

	@Override
	public void save(Player player) throws SQLException, IOException {
		saveStatement.setInt(1, player.getDatabaseId());

		SkillSet skills = player.getSkillSet();
		for (int i = 0; i < Skill.SKILL_NAMES.length; i++) {
			saveStatement.setString(2, Skill.SKILL_NAMES[i]);
			saveStatement.setInt(3, skills.getCurrentLevel(i));
			saveStatement.setDouble(4, skills.getExperience(i));
			saveStatement.addBatch();
		}
		saveStatement.executeBatch();
	}

}
