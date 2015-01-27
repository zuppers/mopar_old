package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.content.grandexchange.GEOffer;
import net.scapeemulator.game.msg.Message;

public final class GrandExchangeUpdateMessage extends Message {

	private final GEOffer offer;

	public GrandExchangeUpdateMessage(GEOffer offer) {
		this.offer = offer;
	}

	public GEOffer getOffer() {
		return offer;
	}

}
