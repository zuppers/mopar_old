package net.scapeemulator.game.model.grandexchange;

public enum OfferType {
	BUY(2, 5, 5), SELL(-2, -3, -3), CLEAR(0, 0, 0);
	
	private final int orangeBar, greenBar, redBar;
	
	private OfferType(int orangeBar, int greenBar, int redBar) {
		this.orangeBar = orangeBar;
		this.greenBar = greenBar;
		this.redBar = redBar;
	}

	public int getOrangeBar() {
		return orangeBar;
	}

	public int getGreenBar() {
		return greenBar;
	}

	public int getRedBar() {
		return redBar;
	}
}
