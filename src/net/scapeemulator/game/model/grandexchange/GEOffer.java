package net.scapeemulator.game.model.grandexchange;

import java.sql.Timestamp;

public class GEOffer implements Comparable<GEOffer> {

	private final int playerId;
	private final int slot;
	private final OfferType type;
	private final int itemId; 
	private final int price; //The price per item we are willing to buy/sell for
	private final Timestamp timestamp;
	private final int offerAmount; //The total amount of items we want to buy/sell
	private boolean aborted;
	private int totalCoins;  //The total amount of coins we have spent or received
	private int amountComplete; //The total amount of items we have bought or sold
	private int unclaimedCoins; //The curret number of coins waiting to be collected
	private int unclaimedItems; //The current number of items waiting to be collected
	
	public GEOffer(int playerId, int slot, OfferType type, int itemId, int price, int offerAmount, Timestamp timestamp) {
		this.playerId = playerId;
		this.slot = slot;
		this.type = type;
		this.itemId = itemId;
		this.offerAmount = offerAmount;
		this.price = price;
		this.timestamp = timestamp;
	}
	
	public static GEOffer clearSlot(int slot) {
		return new GEOffer(0, slot, OfferType.CLEAR, 0, 0, 0, null);
	}
	
	@Override
	public int compareTo(GEOffer compare) {
		if(price == compare.getPrice()) {
			
			//If the prices are the same, older offers have precedence
			return timestamp.compareTo(compare.getTimestamp());
		}
		
		//Make sure to reverse the price sorting if this is the buy offer list [highest price --> lowest]
		return (price - compare.getPrice()) * (type == OfferType.BUY ? -1 : 1);		
	}
	
	public void update(int amount, int tPrice) {
		if(type == OfferType.BUY) {
			int priceDifference = price - tPrice; //If we're buying and they sold for cheaper, give us money back
			unclaimedCoins += priceDifference * amount;
			unclaimedItems += amount;
		} else {
			unclaimedCoins += tPrice * amount;
		}
		totalCoins += tPrice * amount;
		amountComplete += amount;
	}
	
	public boolean active() {
		return !aborted && getAmountRemaining() > 0;
	}
	
	public int getBarId() {
		if(aborted) {
			return type.getRedBar();
		} else if(getAmountRemaining() == 0) {
			return type.getGreenBar();
		} else {
			return type.getOrangeBar();
		}
	}
	
	public void abort() {
		if(type == OfferType.BUY) {
			unclaimedCoins += getAmountRemaining() * price;
		} else {
			unclaimedItems = getAmountRemaining();
		}
		aborted = true;
	}
	
	public boolean finished() {
		return unclaimedCoins < 1 && unclaimedItems < 1 && !active();
	}
	
	@Override
	public String toString() {
		return "GEOffer: " + type.toString() + " " + amountComplete + "/" + offerAmount + " of " + itemId + " for " + price + " each. stamp: " + (timestamp.getTime() - 1388440000000L)/100 + 
				"; coins: " + unclaimedCoins + "; items: " + unclaimedItems;
	}
	
	public int getPlayerId() {
		return playerId;
	}

	public int getSlot() {
		return slot;
	}

	public OfferType getType() {
		return type;
	}

	public int getItemId() {
		return itemId;
	}

	public int getPrice() {
		return price;
	}

	public int getOfferAmount() {
		return offerAmount;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public int getTotalCoins() {
		return totalCoins;
	}
	
	public void setTotalCoins(int totalCoins) {
		this.totalCoins = totalCoins;
	}
	
	public int getAmountComplete() {
		return amountComplete;
	}
	
	public int getAmountRemaining() {
		return offerAmount - amountComplete;
	}

	public void setAmountComplete(int amountComplete) {
		this.amountComplete = amountComplete;
	}
	
	public int getUnclaimedCoins() {
		return unclaimedCoins;
	}

	public void setUnclaimedCoins(int unclaimedCoins) {
		this.unclaimedCoins = unclaimedCoins;
	}

	public int getUnclaimedItems() {
		return unclaimedItems;
	}

	public void setUnclaimedItems(int unclaimedItems) {
		this.unclaimedItems = unclaimedItems;
	}

	public boolean isAborted() {
		return aborted;
	}

	public void setAborted(boolean aborted) {
		this.aborted = aborted;
	}
	
}
