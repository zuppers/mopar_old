package net.scapeemulator.game.content.grandexchange;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import net.scapeemulator.game.GameServer;
import net.scapeemulator.game.dispatcher.button.ButtonDispatcher;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.impl.GrandExchangeUpdateMessage;

public class GrandExchange {

    private final ArrayList<HashMap<Integer, TreeSet<GEOffer>>> offerMaps = new ArrayList<HashMap<Integer, TreeSet<GEOffer>>>();

    public GrandExchange() {
        offerMaps.add(new HashMap<Integer, TreeSet<GEOffer>>());
        offerMaps.add(new HashMap<Integer, TreeSet<GEOffer>>());
        ButtonDispatcher.getInstance().bind(new GEInterfaceHandler());
    }

    public GEOffer placeOffer(int playerId, int slot, OfferType type, int itemId, int price, int amt, long time) {
        GEOffer offer = new GEOffer(playerId, slot, type, itemId, price, amt, new Timestamp(time));
        HashMap<Integer, TreeSet<GEOffer>> typeOffers = offerMaps.get(Math.abs(type.ordinal() - 1));
        if (typeOffers.containsKey(itemId)) {
            Iterator<GEOffer> offers = typeOffers.get(itemId).iterator();
            while (offers.hasNext() && offer.active()) {
                GEOffer compareOffer = offers.next();
                if ((compareOffer.getPrice() > price && type == OfferType.BUY) || (compareOffer.getPrice() < price && type == OfferType.SELL)) {
                    break;
                }
                if (!compareOffer.active()) {
                    continue;
                }
                int updateAmount = compareOffer.getAmountRemaining() >= offer.getAmountRemaining() ? offer.getAmountRemaining() : compareOffer.getAmountRemaining();
                offer.update(updateAmount, compareOffer.getPrice());
                compareOffer.update(updateAmount, compareOffer.getPrice());
                GameServer.getInstance().getSerializer().saveGrandExchangeOffer(compareOffer);
                Player compareOfferOwner = World.getWorld().getPlayerByDatabaseId(compareOffer.getPlayerId());
                if (compareOfferOwner != null) {
                    compareOfferOwner.send(new GrandExchangeUpdateMessage(compareOffer));
                }
            }
        }
        addOffer(offer);
        GameServer.getInstance().getSerializer().saveGrandExchangeOffer(offer);
        return offer;
    }

    public GEOffer[] getPlayerOffers(int playerId) {
        GEOffer[] playerOffers = new GEOffer[6];
        for (HashMap<Integer, TreeSet<GEOffer>> offerMap : offerMaps) {
            for (TreeSet<GEOffer> offerSet : offerMap.values()) {
                Iterator<GEOffer> offers = offerSet.iterator();
                while (offers.hasNext()) {
                    GEOffer offer = offers.next();
                    if (offer.getPlayerId() == playerId) {
                        playerOffers[offer.getSlot()] = offer;
                    }
                }
            }
        }
        return playerOffers;
    }

    public void addOffer(GEOffer offer) {
        HashMap<Integer, TreeSet<GEOffer>> offerMap = offerMaps.get(offer.getType().ordinal());
        TreeSet<GEOffer> offerList = offerMap.get(offer.getItemId());
        if (offerList == null) {
            offerList = new TreeSet<GEOffer>();
            offerMap.put(offer.getItemId(), offerList);
        }
        offerList.add(offer);
    }

    public void removeOffer(GEOffer offer) {
        HashMap<Integer, TreeSet<GEOffer>> offerMap = offerMaps.get(offer.getType().ordinal());
        TreeSet<GEOffer> offerList = offerMap.get(offer.getItemId());
        GameServer.getInstance().getSerializer().removeGrandExchangeOffer(offer);
        offerList.remove(offer);
    }

    public ArrayList<HashMap<Integer, TreeSet<GEOffer>>> getOfferMaps() {
        return offerMaps;
    }

}
