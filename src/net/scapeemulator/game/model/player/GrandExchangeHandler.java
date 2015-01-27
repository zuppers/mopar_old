package net.scapeemulator.game.model.player;

import net.scapeemulator.game.content.grandexchange.GEOffer;
import net.scapeemulator.game.content.grandexchange.OfferType;
import net.scapeemulator.game.model.ExtendedOption;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.msg.impl.GrandExchangeUpdateMessage;
import net.scapeemulator.game.msg.impl.ScriptMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceAccessMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceItemsMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceOpenMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceVisibleMessage;

public class GrandExchangeHandler extends ScriptInputListenerAdapter {

    private Player player;
    private GEOffer[] playerOffers;

    private int activeItemId;
    private int price;
    private int amount;
    private int activeSlot;
    private OfferType type;
    private boolean searchOpen;
    private int customInputId;

    GrandExchangeHandler(Player player) {
        this.player = player;
    }

    public void init() {
        playerOffers = World.getWorld().getGrandExchange().getPlayerOffers(player.getDatabaseId());
        for (GEOffer offer : playerOffers) {
            if (offer != null) {
                player.send(new GrandExchangeUpdateMessage(offer));
            }
        }
    }

    public void handleOfferInventoryClick(int childId, int dynamicId) {
        if (player.getInterfaceSet().getInventory().getCurrentId() != 107 || childId != 18 || dynamicId < 0 || dynamicId > 27) {
            return;
        }
        Item item = player.getInventory().get(dynamicId);
        if (item == null) {
            return;
        }
        activeItemId = item.getDefinition().getUnnotedItemId();
        setItemExamine("ID " + activeItemId + ": " + ItemDefinitions.forId(activeItemId).getName());
        player.getStateSet().setState(1109, activeItemId);
    }

    public void handleMainInterface(int childId, ExtendedOption option) {
        if (player.getInterfaceSet().getWindow().getCurrentId() != 105) {
            return;
        }
        switch (childId) {
        case 18:
        case 34:
            if (option == ExtendedOption.ONE) {
                openOfferInterface((childId - 18) / 16);
            } else {
                abortOffer((childId - 18) / 16);
            }
            break;
        case 50:
        case 69:
        case 88:
        case 107:
            if (option == ExtendedOption.ONE) {
                openOfferInterface(((childId - 50) / 19) + 2);
            } else {
                abortOffer(((childId - 50) / 19) + 2);
            }
            break;
        case 30:
        case 31:
        case 46:
        case 47:
            newOfferInterface((childId - 30) / 16, OfferType.values()[(childId - 30) % 16]);
            break;
        case 62:
        case 63:
        case 81:
        case 82:
        case 100:
        case 101:
        case 119:
        case 120:
            newOfferInterface(((childId - 62) / 19) + 2, OfferType.values()[(childId - 62) % 19]);
            break;
        case 127:
            showInterface();
            break;
        case 157:
            modifyAmount(-1, false);
            break;
        case 159:
            modifyAmount(1, false);
            break;
        case 162:
        case 164:
        case 166:
            modifyAmount((int) Math.pow(10, (childId - 162) / 2), type == OfferType.SELL);
            break;
        case 168: // Buy 1000, Sell all
            if (type == OfferType.BUY) {
                modifyAmount(1000, false);
            } else {
                modifyAmount(player.getInventory().getAmountNotedAndUnnoted(activeItemId), true);
            }
            break;
        case 170: // custom amount
        case 185: // custom price
            customInputId = childId;
            player.getScriptInput().showIntegerScriptInput("Enter " + (customInputId == 170 ? "amount" : "price") + ":", this);
            break;
        case 171:
            modifyPrice(-1);
            break;
        case 173:
            modifyPrice(1);
            break;
        case 177: // Market lowest
            break;
        case 180: // Market mid
            break;
        case 183: // Market highest
            break;
        case 190:
            submitOffer();
            break;
        case 194:
            openSearch();
            break;
        case 203:
            abortOffer(activeSlot);
            break;
        case 209:
            if (option == ExtendedOption.ONE) {
                collect(true, true);
            } else {
                collect(true, false);
            }
            break;
        case 211:
            collect(false, false);
            break;
        }
    }

    public void submitOffer() {
        if (activeItemId < 1 || amount < 1 || price < 1) {
            showInterface();
            return;
        }
        if (type == OfferType.BUY) {
            int totalPrice = amount * price;
            if (player.getInventory().getAmount(995) < totalPrice) {
                player.sendMessage("You do not have enough coins to create this offer.");
                return;
            }
            player.getInventory().remove(new Item(995, totalPrice));
        } else {
            Item toRemove = new Item(activeItemId, amount);
            if (player.getInventory().getAmountNotedAndUnnoted(toRemove.getId()) < toRemove.getAmount()) {
                player.sendMessage("You do not have that many items to offer.");
                return;
            }
            Item removed = player.getInventory().remove(toRemove);
            if (removed == null || !removed.equals(toRemove)) {
                if (toRemove.getDefinition().isStackable()) {
                    System.out.println("This should never happen! Please report. ID: " + toRemove.getId() + " " + player.getDisplayName());
                    return;
                }
                toRemove = new Item(toRemove.getDefinition().getNotedItemId(), removed != null ? toRemove.getAmount() - removed.getAmount() : toRemove.getAmount());
                removed = player.getInventory().remove(toRemove);
                if (!removed.equals(toRemove)) {
                    return;
                }
            }
        }
        playerOffers[activeSlot] = World.getWorld().getGrandExchange().placeOffer(player.getDatabaseId(), activeSlot, type, activeItemId, price, amount, System.currentTimeMillis());
        player.send(new GrandExchangeUpdateMessage(playerOffers[activeSlot]));
        showInterface();
    }

    public void showInterface() {
        resetItemStats();
        player.getStateSet().setState(563, 4194304);
        player.getStateSet().setState(1112, -1);
        player.getStateSet().setState(1113, -1);
        player.setInterfaceText(105, 14, "Item Exchange");
        player.getInterfaceSet().openWindow(105);
        player.send(new InterfaceAccessMessage(105, 209, -1, -1, 6));
        player.send(new InterfaceAccessMessage(105, 211, -1, -1, 6));
    }

    public void abortOffer(int slot) {
        GEOffer offer = playerOffers[slot];
        offer.abort();
        if (playerOffers[slot].finished()) {
            removeOffer(slot);
        } else {
            player.send(new GrandExchangeUpdateMessage(offer));
            sendUnclaimedSlots();
        }
    }

    public void removeOffer(int slot) {
        player.send(new GrandExchangeUpdateMessage(GEOffer.clearSlot(slot)));
        World.getWorld().getGrandExchange().removeOffer(playerOffers[slot]);
        playerOffers[slot] = null;
    }

    public void openOfferInterface(int slot) {
        activeSlot = slot;
        activeItemId = playerOffers[activeSlot].getItemId();
        setItemExamine("ID " + activeItemId + ": " + ItemDefinitions.forId(activeItemId).getName());
        sendUnclaimedSlots();
        player.getStateSet().setState(1112, slot);
    }

    public void collect(boolean itemSlot, boolean noted) {
        GEOffer offer = playerOffers[activeSlot];
        if (itemSlot) {
            if (noted) {
                Item remaining = player.getInventory().add(new Item(ItemDefinitions.forId(offer.getItemId()).getNotedItemId(), offer.getUnclaimedItems()));
                if (remaining != null) {
                    offer.setUnclaimedItems(remaining.getAmount());
                } else {
                    offer.setUnclaimedItems(0);
                }
            } else {
                Item remaining = player.getInventory().add(new Item(offer.getItemId(), offer.getUnclaimedItems()));
                if (remaining != null) {
                    offer.setUnclaimedItems(remaining.getAmount());
                } else {
                    offer.setUnclaimedItems(0);
                }
            }
        } else {
            Item remaining = player.getInventory().add(new Item(995, offer.getUnclaimedCoins()));
            if (remaining != null) {
                offer.setUnclaimedCoins(remaining.getAmount());
            } else {
                offer.setUnclaimedCoins(0);
            }
        }
        if (offer.finished()) {
            removeOffer(activeSlot);
            showInterface();
        } else {
            sendUnclaimedSlots();
        }
    }

    public void sendUnclaimedSlots() {
        GEOffer offer = playerOffers[activeSlot];
        Item[] unclaimed = { new Item(offer.getUnclaimedItems() > 0 ? offer.getItemId() : -1, offer.getUnclaimedItems()), new Item(offer.getUnclaimedCoins() > 0 ? 995 : -1, offer.getUnclaimedCoins()) };
        player.send(new InterfaceItemsMessage(523 + activeSlot, unclaimed));
    }

    public void newOfferInterface(int slot, OfferType type) {
        resetItemStats();
        this.type = type;
        activeSlot = slot;
        player.getStateSet().setState(1112, slot);
        player.getStateSet().setState(1113, type.ordinal());
        if (type == OfferType.BUY) {
            openSearch();
        } else { // TODO fix
            player.getInterfaceSet().openInventory(107);
            player.send(new ScriptMessage(149, "IviiiIsssss", "", "", "", "", "Offer", -1, 0, 7, 4, 93, 107 << 16 | 18));
            // player.send(new InterfaceItemsMessage(107, 18, 93, player.getInventory().toArray()));
            player.send(new InterfaceVisibleMessage(107, 0, false));
            player.send(new InterfaceAccessMessage(107, 18, 0, 27, 1026));
        }
    }

    public void setItemExamine(String s) {
        player.setInterfaceText(105, 142, s);
    }

    public void modifyPrice(int value) {
        price += value;
        if (price < 1) {
            price = 1;
        }
        player.getStateSet().setState(1111, price);
    }

    public void setPrice(int value) {
        if (value < 1) {
            return;
        }
        price = value;
        player.getStateSet().setState(1111, price);
    }

    public void modifyAmount(int value, boolean set) {
        if (!set) {
            amount += value;
        } else {
            amount = value;
        }
        if (amount < 1) {
            amount = 1;
        }
        player.getStateSet().setState(1110, amount);
    }

    public void setAmount(int value) {
        if (value < 1) {
            return;
        }
        amount = value;
        player.getStateSet().setState(1110, amount);
    }

    public void openSearch() {
        player.send(new InterfaceOpenMessage(752, 6, 389, 6));
        player.send(new ScriptMessage(570, "s", "Grand Exchange Item Search"));
        searchOpen = true;
    }

    public void searchComplete(int itemId) {
        if (!searchOpen) {
            return;
        }
        activeItemId = itemId;
        player.getStateSet().setState(1109, activeItemId);
        setItemExamine("ID " + activeItemId + ": " + ItemDefinitions.forId(activeItemId).getName());
        searchOpen = false;
    }

    public void resetItemStats() {
        activeItemId = -1;
        price = 1;
        amount = 1;
        player.getStateSet().setState(1109, activeItemId);
        player.getStateSet().setState(1110, amount);
        player.getStateSet().setState(1111, price);
        player.getStateSet().setState(1114, 1);
        player.getStateSet().setState(1115, 1);
        player.getStateSet().setState(1116, Integer.MAX_VALUE);
    }

    @Override
    public void intInputReceived(int value) {
        if (customInputId == 170) {
            setAmount(value);
        } else if (customInputId == 185) {
            setPrice(value);
        }
        customInputId = 0;
    }

}
