package net.scapeemulator.game.io.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.scapeemulator.game.content.grandexchange.GEOffer;

public final class GrandExchangeOfferTable extends Table<GEOffer> {

	private final PreparedStatement saveStatement;
	private final PreparedStatement deleteStatement;
	
	public GrandExchangeOfferTable(Connection connection) throws SQLException {
		this.saveStatement = connection.prepareStatement("REPLACE INTO grandexchange (player_id, slot, timestamp, type, item, price, offer_amount, total_coins, amount_complete, unclaimed_coins, unclaimed_items, aborted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
		this.deleteStatement = connection.prepareStatement("DELETE FROM grandexchange WHERE player_id = ? and slot = ? LIMIT 1;");
	}

	@Override
	public void save(GEOffer offer) throws SQLException {
		saveStatement.setInt(1, offer.getPlayerId());
		saveStatement.setInt(2, offer.getSlot());
		saveStatement.setTimestamp(3, offer.getTimestamp());
		saveStatement.setInt(4, offer.getType().ordinal());
		saveStatement.setInt(5, offer.getItemId());
		saveStatement.setInt(6, offer.getPrice());
		saveStatement.setInt(7, offer.getOfferAmount());
		saveStatement.setInt(8, offer.getTotalCoins());
		saveStatement.setInt(9, offer.getAmountComplete());
		saveStatement.setInt(10, offer.getUnclaimedCoins());
		saveStatement.setInt(11, offer.getUnclaimedItems());
		saveStatement.setInt(12, offer.isAborted() ? 1 : 0);
		saveStatement.execute();
	}
	
	public void removeOffer(GEOffer offer) throws SQLException {
		deleteStatement.setInt(1, offer.getPlayerId());
		deleteStatement.setInt(2, offer.getSlot());
		deleteStatement.execute();
	}

	@Override
	public void load(GEOffer e) throws SQLException, IOException {}

}
