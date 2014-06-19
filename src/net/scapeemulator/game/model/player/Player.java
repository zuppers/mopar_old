package net.scapeemulator.game.model.player;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.util.ArrayList;
import java.util.List;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.definition.NPCDefinitions;
import net.scapeemulator.game.model.grounditem.GroundItemList;
import net.scapeemulator.game.model.grounditem.GroundItemList.Type;
import net.scapeemulator.game.model.grounditem.GroundItemSynchronizer;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.mob.combat.AttackType;
import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.model.object.GroundObjectSynchronizer;
import net.scapeemulator.game.model.player.action.PlayerDeathAction;
import net.scapeemulator.game.model.player.appearance.Appearance;
import net.scapeemulator.game.model.player.inventory.Inventory;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.SkillAppearanceListener;
import net.scapeemulator.game.model.player.skills.SkillMessageListener;
import net.scapeemulator.game.model.player.skills.SkillSet;
import net.scapeemulator.game.model.player.skills.magic.Spellbook;
import net.scapeemulator.game.msg.Message;
import net.scapeemulator.game.msg.impl.ChatMessage;
import net.scapeemulator.game.msg.impl.EnergyMessage;
import net.scapeemulator.game.msg.impl.LogoutMessage;
import net.scapeemulator.game.msg.impl.PlayerMenuOptionMessage;
import net.scapeemulator.game.msg.impl.ServerMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceTextMessage;
import net.scapeemulator.game.net.game.GameSession;
import net.scapeemulator.game.util.StringUtils;
import net.scapeemulator.util.Base37Utils;

public final class Player extends Mob {

    private static final Position[] HOME_LOCATIONS = { new Position(3222, 3222) };

    private static int appearanceTicketCounter = 0;
    private static Animation DEATH_ANIMATION = new Animation(7185, 50);

    private static int nextAppearanceTicket() {
        if (++appearanceTicketCounter == 0) {
            appearanceTicketCounter = 1;
        }

        return appearanceTicketCounter;
    }

    private int databaseId;
    private String username;
    private String displayName;
    private long longUsername;
    private String password;
    private int rights = 0;
    private GameSession session;
    private boolean regionChanging;
    private boolean updateModelLists;
    private boolean blockActions;
    private Position lastKnownRegion;
    private final World world = World.getWorld();
    private final List<Player> localPlayers = new ArrayList<>();
    private final List<NPC> localNpcs = new ArrayList<>();
    private Appearance appearance = new Appearance(this);
    private int energy = 100;
    private final SkillSet skillSet = new SkillSet();
    private final InventorySet inventorySet = new InventorySet(this);
    private ChatMessage chatMessage;
    private final Friends friends = new Friends(this);
    private final ScriptInput scriptInput = new ScriptInput(this);
    private final PlayerSettings settings = new PlayerSettings(this);
    private final InterfaceSet interfaceSet = new InterfaceSet(this);
    private final GrandExchangeHandler grandExchangeHandler = new GrandExchangeHandler(this);
    private final ShopHandler shopHandler = new ShopHandler(this);
    private final StateSet stateSet = new StateSet(this);
    private Spellbook spellbook = Spellbook.NORMAL_SPELLBOOK;
    private final AccessSet accessSet = new AccessSet(this);
    private final EquipmentBonuses equipmentBonuses = new EquipmentBonuses();
    private final GroundItemList groundItems = new GroundItemList(Type.LOCAL);
    private final GroundItemSynchronizer groundItemSync = new GroundItemSynchronizer(this);
    private final GroundObjectSynchronizer groundObjSync = new GroundObjectSynchronizer(this);
    private int[] appearanceTickets = new int[World.MAX_PLAYERS];
    private int appearanceTicket = nextAppearanceTicket();
    private final PlayerOption[] options = new PlayerOption[10];
    private int homeId;
    private int pnpc = -1;

    // TODO remove
    private Position min;
    private Position max;

    public void setMin() {
        min = position;
        sendMessage("Bottom left bounds set to " + min);
    }

    public void setMax() {
        max = position;
        sendMessage("Top right bounds set to " + max);
    }

    public void setSpawnPos(int id) {
        sendMessage("Spawn for " + NPCDefinitions.forId(id).getName() + " sent to console.");
        System.out.println("INSERT INTO `npcspawns`(`type`, `x`, `y`, `height`, `roam`, `min_x`, `min_y`, `max_x`, `max_y`) " + "VALUES (" + id + "," + position.getX() + "," + position.getY() + ","
                + position.getHeight() + "," + 1 + "," + min.getX() + "," + min.getY() + "," + max.getX() + "," + max.getY() + ");");
    }

    // TODO end remove

    public Player() {
        init();
    }

    private void init() {
        combatHandler = new PlayerCombatHandler(this);
        skillSet.addListener(new SkillMessageListener(this));
        skillSet.addListener(new SkillAppearanceListener(this));

        world.getGroundObjects().addListener(groundObjSync);

        world.getGroundItems().addListener(groundItemSync);
        groundItems.addListener(groundItemSync);

        /* Initialize all the player options */
        for (int i = 0; i < options.length; i++) {
            options[i] = new PlayerOption();
        }
    }

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public void setUsername(String username) {
        this.username = username.replaceAll(" ", "_").toLowerCase();
        displayName = StringUtils.capitalize(username);
        longUsername = Base37Utils.encodeBase37(username);
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getLongUsername() {
        return longUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRights() {
        return rights;
    }

    public void setRights(int rights) {
        this.rights = rights;
    }

    public GameSession getSession() {
        return session;
    }

    public void setSession(GameSession session) {
        this.session = session;
    }

    public ChannelFuture send(Message message) {
        if (session != null) {
            return session.send(message);
        } else {
            return null;
        }
    }

    public void setInterfaceText(int widgetId, int componentId, String text) {
        send(new InterfaceTextMessage(widgetId, componentId, text));
    }

    public void sendMessage(String text) {
        send(new ServerMessage(text));
    }

    public boolean isRegionChanging() {
        return regionChanging;
    }

    public Position getLastKnownRegion() {
        return lastKnownRegion;
    }

    public void setLastKnownRegion(Position lastKnownRegion) {
        this.lastKnownRegion = lastKnownRegion;
        this.regionChanging = true;
    }

    public List<Player> getLocalPlayers() {
        return localPlayers;
    }

    public List<NPC> getLocalNpcs() {
        return localNpcs;
    }

    public int getAppearanceTicket() {
        return appearanceTicket;
    }

    public int[] getAppearanceTickets() {
        return appearanceTickets;
    }

    public Appearance getAppearance() {
        return appearance;
    }

    public void setAppearance(Appearance appearance) {
        this.appearance = appearance;
        appearanceUpdated();
    }

    public void appearanceUpdated() {
        appearanceTicket = nextAppearanceTicket();
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
        this.send(new EnergyMessage(energy));
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage message) {
        this.chatMessage = message;
    }

    public boolean isChatUpdated() {
        return chatMessage != null;
    }

    public InventorySet getInventorySet() {
        return inventorySet;
    }

    public Inventory getInventory() {
        return inventorySet.getInventory();
    }

    public Inventory getEquipment() {
        return inventorySet.getEquipment();
    }

    public int getStance() {
        if (pnpc > -1) {
            return NPCDefinitions.forId(pnpc).getStance();
        }
        Item weapon = inventorySet.getEquipment().get(Equipment.WEAPON);
        if (weapon != null) {
            return EquipmentDefinition.forId(weapon.getId()).getStance();
        } else {
            return 1426;
        }
    }

    public void setPNPC(int pnpc) {
        this.pnpc = pnpc;
        appearanceUpdated();
    }

    public int getPNPC() {
        return pnpc;
    }

    public PlayerSettings getSettings() {
        return settings;
    }

    public StateSet getStateSet() {
        return stateSet;
    }

    public InterfaceSet getInterfaceSet() {
        return interfaceSet;
    }

    public GrandExchangeHandler getGrandExchangeHandler() {
        return grandExchangeHandler;
    }

    public ShopHandler getShopHandler() {
        return shopHandler;
    }

    public AccessSet getAccessSet() {
        return accessSet;
    }

    public Spellbook getSpellbook() {
        return spellbook;
    }

    public void setSpellbook(Spellbook spellbook) {
        this.spellbook = spellbook;
    }

    public GroundItemList getGroundItems() {
        return groundItems;
    }

    public ScriptInput getScriptInput() {
        return scriptInput;
    }

    public void setUpdateModelLists(boolean updateModelLists) {
        this.updateModelLists = updateModelLists;
    }

    public boolean getUpdateModelLists() {
        return updateModelLists;
    }

    public void toggleClipping() {
        clipped = !clipped;
    }

    public boolean actionsBlocked() {
        return blockActions || !alive();
    }

    public void setActionsBlocked(boolean blockActions) {
        this.blockActions = blockActions;
    }

    public PlayerCombatHandler getPlayerCombatHandler() {
        return (PlayerCombatHandler) combatHandler;
    }

    public void refreshOptions() {
        for (int i = 0; i < options.length; i++) {
            PlayerOption option = options[i];
            send(new PlayerMenuOptionMessage(i + 1, option.atTop(), option.getText()));
        }
    }

    public PlayerOption getOption(int id) {
        return options[id];
    }

    public void refreshGroundObjects() {
        // TODO: Make this cleaner?
        groundObjSync.purge();
        World.getWorld().getGroundObjects().fireEvents(groundObjSync);
    }

    public void refreshGroundItems() {
        // TODO: Make this cleaner?
        groundItemSync.purge();
        World.getWorld().getGroundItems().fireEvents(groundItemSync);
        groundItems.fireEvents(groundItemSync);
    }

    public void logout() {
        // TODO: Make this cleaner?
        World.getWorld().getGroundItems().removeListener(groundItemSync);
        World.getWorld().getGroundObjects().removeListener(groundObjSync);

        // TODO this seems fragile
        ChannelFuture future = send(new LogoutMessage());
        if (future != null) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    public Friends getFriends() {
        return friends;
    }

    public SkillSet getSkillSet() {
        return skillSet;
    }

    public void calculateEquipmentBonuses() {
        for (AttackType type : AttackType.values()) {
            if (type == AttackType.AIR || type == AttackType.WATER || type == AttackType.EARTH || type == AttackType.FIRE || type == AttackType.DRAGONFIRE) {
                continue;
            }
            int typeAttackBonus = 0;
            int typeDefenceBonus = 0;
            for (Item equipped : getEquipment().toArray()) {
                if (equipped == null) {
                    continue;
                }
                typeAttackBonus += equipped.getEquipmentDefinition().getBonuses().getAttackBonus(type);
                typeDefenceBonus += equipped.getEquipmentDefinition().getBonuses().getDefenceBonus(type);
            }
            equipmentBonuses.setAttackBonus(type, typeAttackBonus);
            equipmentBonuses.setDefenceBonus(type, typeDefenceBonus);
        }
        int prayerBonus = 0;
        int strengthBonus = 0;
        for (Item equipped : getEquipment().toArray()) {
            if (equipped == null) {
                continue;
            }
            prayerBonus += equipped.getEquipmentDefinition().getBonuses().getPrayerBonus();
            strengthBonus += equipped.getEquipmentDefinition().getBonuses().getStrengthBonus();
        }
        equipmentBonuses.setPrayerBonus(prayerBonus);
        equipmentBonuses.setStrengthBonus(strengthBonus);

        int rangeBonus = getEquipment().get(Equipment.WEAPON).getEquipmentDefinition().getBonuses().getRangeStrengthBonus();
        rangeBonus = rangeBonus == 0 ? getEquipment().get(Equipment.AMMO).getEquipmentDefinition().getBonuses().getRangeStrengthBonus() : rangeBonus;
        equipmentBonuses.setRangeStrengthBonus(rangeBonus);

        if (getInterfaceSet().getWindow().getCurrentId() == 667) {
            sendEquipmentBonuses();
        }
    }

    private String addPlus(int bonus) {
        return bonus > 0 ? "+" + bonus : "" + bonus;
    }

    public void sendEquipmentBonuses() {
        setInterfaceText(667, 32, "0 kg");
        setInterfaceText(667, 36, "Stab: " + addPlus(equipmentBonuses.getAttackBonus(AttackType.STAB)));
        setInterfaceText(667, 37, "Slash: " + addPlus(equipmentBonuses.getAttackBonus(AttackType.SLASH)));
        setInterfaceText(667, 38, "Crush: " + addPlus(equipmentBonuses.getAttackBonus(AttackType.CRUSH)));
        setInterfaceText(667, 39, "Magic: " + addPlus(equipmentBonuses.getAttackBonus(AttackType.ALL_MAGIC)));
        setInterfaceText(667, 40, "Ranged: " + addPlus(equipmentBonuses.getAttackBonus(AttackType.RANGE)));
        setInterfaceText(667, 41, "Stab: " + addPlus(equipmentBonuses.getDefenceBonus(AttackType.STAB)));
        setInterfaceText(667, 42, "Slash: " + addPlus(equipmentBonuses.getDefenceBonus(AttackType.SLASH)));
        setInterfaceText(667, 43, "Crush: " + addPlus(equipmentBonuses.getDefenceBonus(AttackType.CRUSH)));
        setInterfaceText(667, 44, "Magic: " + addPlus(equipmentBonuses.getDefenceBonus(AttackType.ALL_MAGIC)));
        setInterfaceText(667, 45, "Range: " + addPlus(equipmentBonuses.getDefenceBonus(AttackType.RANGE)));
        setInterfaceText(667, 46, "Summoning: 0");
        setInterfaceText(667, 48, "Strength: " + addPlus(equipmentBonuses.getStrengthBonus()));
        setInterfaceText(667, 49, "Prayer: " + addPlus(equipmentBonuses.getPrayerBonus()));
    }

    @Override
    public void reset() {
        super.reset();
        updateModelLists = false;
        regionChanging = false;
        chatMessage = null;
    }

    public EquipmentBonuses getEquipmentBonuses() {
        return equipmentBonuses;
    }

    public int getCurrentHitpoints() {
        return skillSet.getCurrentLevel(Skill.HITPOINTS);
    }

    public void reduceHp(int amount) {
        skillSet.setCurrentLevel(Skill.HITPOINTS, getCurrentHitpoints() - amount);
        skillSet.refresh();
    }

    protected void onDeath() {
        reset();
        startAction(new PlayerDeathAction(this));
    }

    public Position getHomeLocation() {
        return HOME_LOCATIONS[homeId];
    }

    @Override
    public void setHidden(boolean hidden) {
        super.setHidden(hidden);
        appearanceUpdated();
    }

    @Override
    public boolean isRunning() {
        return settings.isRunning();
    }

    public Animation getDeathAnimation() {
        return DEATH_ANIMATION;
    }
}
