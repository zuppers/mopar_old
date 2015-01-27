package net.scapeemulator.game.io.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import net.scapeemulator.game.content.grandexchange.GEOffer;
import net.scapeemulator.game.content.grandexchange.GrandExchange;
import net.scapeemulator.game.content.grandexchange.OfferType;

public final class GrandExchangeTable extends Table<GrandExchange> {

	private final PreparedStatement loadStatement;
	private final PreparedStatement saveStatement;

	public GrandExchangeTable(Connection connection) throws SQLException {
		this.loadStatement = connection.prepareStatement("SELECT * FROM grandexchange");
		this.saveStatement = connection.prepareStatement("REPLACE INTO grandexchange (player_id, slot, timestamp, type, item, price, offer_amount, total_coins, amount_complete, unclaimed_coins, unclaimed_items, aborted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
	}

	@Override
	public void load(GrandExchange ge) throws SQLException, IOException {
		System.out.print("Loading Grand Exchange offers... ");
		int count = 0;
		try (ResultSet set = loadStatement.executeQuery()) {
			while (set.next()) {
				int playerId = set.getInt("player_id");
				int slot = set.getInt("slot");
				Timestamp timestamp = set.getTimestamp("timestamp");
				OfferType type = OfferType.values()[set.getInt("type")];
				int item = set.getInt("item");
				int price = set.getInt("price");
				int offerAmount = set.getInt("offer_amount");
				int totalCoins = set.getInt("total_coins");
				int amountComplete = set.getInt("amount_complete");
				int unclaimedCoins = set.getInt("unclaimed_coins");
				int unclaimedItems = set.getInt("unclaimed_items");
				boolean cancelled = set.getInt("aborted") == 1;
				GEOffer offer = new GEOffer(playerId, slot, type, item, price, offerAmount, timestamp);
				offer.setTotalCoins(totalCoins);
				offer.setAmountComplete(amountComplete);
				offer.setUnclaimedCoins(unclaimedCoins);
				offer.setUnclaimedItems(unclaimedItems);
				offer.setAborted(cancelled);
				ge.addOffer(offer);
				count++;
			}
			System.out.println("complete! Loaded " + count + " offers.");

		}

	}

	@Override
	public void save(GrandExchange ge) throws SQLException {
		System.out.print("Saving Grand Exchange offers... ");
		int count = 0;
		for (HashMap<Integer, TreeSet<GEOffer>> offerMap : ge.getOfferMaps()) {
			for (TreeSet<GEOffer> offerSet : offerMap.values()) {
				Iterator<GEOffer> offers = offerSet.iterator();
				while (offers.hasNext()) {
					GEOffer offer = offers.next();
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
					saveStatement.addBatch();
					count++;
				}
			}
		}
		saveStatement.executeBatch();
		System.out.println("complete! Saved " + count + " offers.");

	}

}
