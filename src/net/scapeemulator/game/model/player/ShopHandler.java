package net.scapeemulator.game.model.player;

import java.util.HashMap;

import net.scapeemulator.cache.def.ItemDefinition;
import net.scapeemulator.game.content.shop.Shop;
import net.scapeemulator.game.content.shop.ShopInterfaceHandler;
import net.scapeemulator.game.content.shop.StockType;
import net.scapeemulator.game.dispatcher.button.ButtonDispatcher;
import net.scapeemulator.game.model.ExtendedOption;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.player.interfaces.ComponentListener;
import net.scapeemulator.game.model.player.interfaces.InterfaceSet.Component;
import net.scapeemulator.game.msg.impl.ScriptMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceAccessMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceItemsMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceVisibleMessage;
import net.scapeemulator.game.util.math.BasicMath;

public class ShopHandler extends ComponentListener {

    static {
        ButtonDispatcher.getInstance().bind(new ShopInterfaceHandler());
    }

    public final static HashMap<String, Shop> shops = new HashMap<String, Shop>();
    private final static int[] amounts = { 1, 5, 10, 50 };

    private final Player player;
    private Shop activeShop;
    private StockType activeStock;

    public ShopHandler(Player player) {
        this.player = player;
    }

    public void sell(int itemId, int amount) {
        if (activeShop == null) {
            return;
        }
        int unnotedId = ItemDefinitions.forId(itemId).getUnnotedItemId();
        if (!activeShop.acceptsItem(unnotedId)) {
            player.sendMessage("You cannot sell that item to this shop.");
            return;
        }

        int itemValue = ItemDefinitions.forId(unnotedId).getLowAlchemyValue();

        // Compare the amount against the amount the player actually has
        int playerAmount = player.getInventory().getAmount(itemId);
        amount = amount > playerAmount ? playerAmount : amount;

        // Compare the amount against the number of coins the player can hold
        int maxAmount = (Integer.MAX_VALUE - player.getInventory().getAmount(995)) / itemValue;
        amount = amount > maxAmount ? maxAmount : amount;

        if (amount < 1) {
            player.sendMessage("You cannot hold any more coins!");
            return;
        }

        // Remove the items from the players inventory
        Item toSell = new Item(itemId, amount);
        Item removed = player.getInventory().remove(toSell);

        // Triple check that everything worked
        if (removed == null || !removed.equals(toSell)) {
            return;
        }

        // Give the player coins
        player.getInventory().add(new Item(995, amount * itemValue));

        // Update the shop
        if (activeShop.add(unnotedId, toSell.getAmount()) > 0) {
            updateShopGlobally();
        }
    }

    public void buy(int index, int amount) {
        if (activeShop == null) {
            return;
        }

        Item item = activeShop.getItemAtIndex(activeStock, index);
        if (item == null) {
            return;
        }
        int itemId = item.getId();

        // ---- Start amount check ----//
        ItemDefinition def = ItemDefinitions.forId(itemId);
        int cost = def.getValue() < 1 ? 1 : def.getValue();
        int assets = player.getInventory().getAmount(995);
        boolean coinsGone = false;
        if (cost * amount > assets || cost * amount < 1) {
            coinsGone = assets % cost == 0;
            amount = assets / cost;
            if (amount < 1) {
                player.sendMessage("You do not have enough coins to buy that.");
                return;
            }
        }
        int freeSlots = player.getInventory().freeSlots() + (coinsGone ? 1 : 0);
        if (!def.isStackable()) {
            amount = amount > freeSlots ? freeSlots : amount;
        } else {
            if (player.getInventory().contains(itemId)) {
                amount -= BasicMath.integerOverflow(player.getInventory().getAmount(itemId), amount);
            } else {
                if (freeSlots < 1) {
                    amount = 0;
                }
            }
        }
        if (amount < 1) {
            player.sendMessage("You do not have any inventory space to buy that.");
            return;
        }
        // ---- End amount check ----//
        if (!activeShop.contains(activeStock, itemId)) {
            return;
        }

        amount = activeShop.remove(activeStock, itemId, amount);
        if (amount > 0 && activeStock != StockType.MAIN) {
            updateShopGlobally();
        }
        player.getInventory().remove(new Item(995, cost * amount));
        player.getInventory().add(new Item(itemId, amount));
    }

    public void handleInput(int childId, final int dynamicId, ExtendedOption option) {
        if (activeShop == null) {
            return;
        }
        switch (childId) {
        case 7:
            player.getInterfaceSet().closeWindow();
            break;
        case 23:
        case 24:
            if (activeShop.getItemAtIndex(activeStock, dynamicId) == null) {
                return;
            }
            ItemDefinition def = activeShop.getItemAtIndex(activeStock, dynamicId).getDefinition();
            switch (option) {
            case ONE:
                int value = def.getValue() < 1 ? 1 : def.getValue();
                player.sendMessage(def.getName() + " costs " + value + " coin" + (value == 1 ? "" : "s") + ".");
                break;
            case TWO:
            case THREE:
            case FOUR:
                buy(dynamicId, amounts[option.toInteger() - 1]);
                break;
            case FIVE:
                player.getScriptInput().showIntegerScriptInput(new ScriptInputListener() {
                    @Override
                    public void intInputReceived(int value) {
                        buy(dynamicId, value);
                    }

                    @Override
                    public void usernameInputReceived(long value) {
                    }
                });
                break;
            case SIX:
                player.sendMessage(def.getExamine());
                break;
            default:
                return;
            }
            break;
        case 25:
            openMainStock();
            break;
        case 26:
            openPlayerStock();
            break;
        }
    }

    public void handleInventoryClick(int dynamicId, ExtendedOption option) {
        if (dynamicId < 0 || dynamicId > 27) {
            return;
        }
        final Item item = player.getInventory().get(dynamicId);
        if (item == null) {
            return;
        }
        ItemDefinition def = ItemDefinitions.forId(item.getDefinition().getUnnotedItemId());
        switch (option) {
        case ONE:
            int value = (int) (def.getLowAlchemyValue());
            player.sendMessage(def.getName() + " sells for " + value + " coin" + (value == 1 ? "" : "s") + ".");
            break;
        case TWO:
        case THREE:
        case FOUR:
            sell(item.getId(), amounts[option.toInteger() - 1]);
            break;
        case FIVE:
            player.getScriptInput().showIntegerScriptInput(new ScriptInputListener() {
                @Override
                public void intInputReceived(int value) {
                    sell(item.getId(), value);
                }

                @Override
                public void usernameInputReceived(long value) {
                }
            });
            break;
        case NINE:
            player.sendMessage(def.getExamine());
            break;
        default:
            return;
        }
    }

    public void updateShopGlobally() {
        for (Player p : World.getWorld().getPlayers()) {
            if (p.getShopHandler().getActiveShop() == activeShop) {
                p.send(new InterfaceItemsMessage(31, activeShop.getStock(StockType.PLAYER)));
            }
        }
    }

    private void openPlayerStock() {
        if (activeStock == StockType.PLAYER) {
            return;
        }
        if (!activeShop.hasStock(StockType.PLAYER)) {
            player.sendMessage("This shop has no player stock.");
            return;
        }
        player.send(new InterfaceVisibleMessage(620, 23, false));
        player.send(new InterfaceVisibleMessage(620, 24, true));
        player.send(new InterfaceVisibleMessage(620, 29, false));
        player.send(new InterfaceVisibleMessage(620, 25, true));
        player.send(new InterfaceVisibleMessage(620, 27, true));
        player.send(new InterfaceVisibleMessage(620, 26, false));
        player.send(new InterfaceAccessMessage(620, 24, 0, 40, 1278));
        activeStock = StockType.PLAYER;
    }

    private void openMainStock() {
        if (activeStock == StockType.MAIN) {
            return;
        }
        player.send(new InterfaceVisibleMessage(620, 23, true));
        player.send(new InterfaceVisibleMessage(620, 24, false));
        player.send(new InterfaceVisibleMessage(620, 29, true));
        player.send(new InterfaceVisibleMessage(620, 25, false));
        player.send(new InterfaceVisibleMessage(620, 27, false));
        player.send(new InterfaceVisibleMessage(620, 26, true));
        player.send(new InterfaceAccessMessage(620, 23, 0, 40, 1278));
        activeStock = StockType.MAIN;
    }

    public void openShop(String shopName) {
        if (!shops.containsKey(shopName)) {
            throw new IllegalArgumentException("Invalid shop name \"" + shopName + "\"");
        }
        Shop shop = shops.get(shopName);
        activeShop = shop;
        player.send(new InterfaceItemsMessage(93, player.getInventory().toArray()));
        if (shop.hasStock(StockType.PLAYER)) {
            player.send(new InterfaceItemsMessage(31, activeShop.getStock(StockType.PLAYER)));
        }
        player.setInterfaceText(620, 22, shopName);
        player.send(new InterfaceVisibleMessage(620, 34, shop.hasStock(StockType.PLAYER)));
        player.getInterfaceSet().openWindow(620);
        player.getInterfaceSet().openInventory(621);
        player.getInterfaceSet().getWindow().setListener(this);
        player.send(new ScriptMessage(150, "IviiiIsssssssss", new Object[] { "", "", "", "", "Sell X", "Sell 10", "Sell 5", "Sell 1", "Value", -1, 0, 7, 4, 93, 621 << 16 }));
        player.send(new ScriptMessage(150, "IviiiIsssssssss", new Object[] { "", "", "", "", "Buy X", "Buy 10", "Buy 5", "Buy 1", "Value", -1, 0, 4, 10, 31, (620 << 16) + 24 }));
        player.send(new InterfaceAccessMessage(621, 0, 0, 27, 1278));
        player.send(new InterfaceAccessMessage(621, 34, 0, 27, 2360446));
        player.send(new ScriptMessage(25, "vg", new Object[] { shop.getShopId(), 93 }));
        openMainStock();
    }

    public void shopScript(int id) {
        player.send(new ScriptMessage(25, "vg", new Object[] { id, 93 }));
    }

    public Shop getActiveShop() {
        return activeShop;
    }

    // This is not the same type of input as button input
    @Override
    public void inputPressed(Component component, int componentId, int dynamicId) {
    }

    @Override
    public void componentClosed(Component component) {
        activeShop = null;
        activeStock = null;
    }

    @Override
    public boolean componentChanged(Component component, int oldId) {
        componentClosed(component);
        return false;
    }

}
