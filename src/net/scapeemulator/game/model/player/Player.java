package net.scapeemulator.game.model.player;

import static net.scapeemulator.game.model.player.skills.prayer.Prayer.HEAL;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.util.ArrayList;
import java.util.List;

import net.scapeemulator.cache.def.NPCDefinition;
import net.scapeemulator.game.GameServer;
import net.scapeemulator.game.io.JdbcSerializer;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.definition.NPCDefinitions;
import net.scapeemulator.game.model.grounditem.GroundItemSynchronizer;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.mob.combat.AttackType;
import net.scapeemulator.game.model.mob.combat.CombatBonuses;
import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.model.object.GroundObjectSynchronizer;
import net.scapeemulator.game.model.player.PlayerVariables.Variable;
import net.scapeemulator.game.model.player.action.PlayerDeathAction;
import net.scapeemulator.game.model.player.appearance.Appearance;
import net.scapeemulator.game.model.player.bank.BankSession;
import net.scapeemulator.game.model.player.bank.BankSettings;
import net.scapeemulator.game.model.player.interfaces.AccessSet;
import net.scapeemulator.game.model.player.interfaces.InterfaceSet;
import net.scapeemulator.game.model.player.inventory.Inventory;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.SkillAppearanceListener;
import net.scapeemulator.game.model.player.skills.SkillMessageListener;
import net.scapeemulator.game.model.player.skills.SkillSet;
import net.scapeemulator.game.model.player.skills.construction.House;
import net.scapeemulator.game.model.player.skills.farming.PlayerFarms;
import net.scapeemulator.game.model.player.skills.firemaking.Firemaking;
import net.scapeemulator.game.model.player.skills.fishing.FishingTool;
import net.scapeemulator.game.model.player.skills.magic.Rune;
import net.scapeemulator.game.model.player.skills.magic.Spellbook;
import net.scapeemulator.game.model.player.skills.mining.Pickaxe;
import net.scapeemulator.game.model.player.skills.prayer.Prayers;
import net.scapeemulator.game.model.player.skills.ranged.Arrow;
import net.scapeemulator.game.model.player.skills.ranged.Bow;
import net.scapeemulator.game.model.player.skills.woodcutting.Hatchet;
import net.scapeemulator.game.model.player.trade.TradeSession;
import net.scapeemulator.game.msg.Message;
import net.scapeemulator.game.msg.impl.ChatMessage;
import net.scapeemulator.game.msg.impl.EnergyMessage;
import net.scapeemulator.game.msg.impl.LogoutMessage;
import net.scapeemulator.game.msg.impl.PlayerMenuOptionMessage;
import net.scapeemulator.game.msg.impl.ServerMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceTextMessage;
import net.scapeemulator.game.net.game.GameSession;
import net.scapeemulator.game.task.Task;
import net.scapeemulator.game.util.StringUtils;
import net.scapeemulator.util.Base37Utils;

public final class Player extends Mob {

    private static final Position[] HOME_LOCATIONS = {new Position(3222, 3222)};

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
    private Position lastKnownRegion = position;
    private final List<Player> localPlayers = new ArrayList<>();
    private final List<NPC> localNpcs = new ArrayList<>();
    private Appearance appearance = new Appearance(this);
    private ForcedMovement forcedMove;
    private int energy = 100;
    private Player wantToTrade;
    private TradeSession tradeSession;
    private final House house = new House(this);
    private House inHouse;
    private final PlayerFarms farms = new PlayerFarms(this);
    private final SkillSet skillSet = new SkillSet();
    private BankSession bankSession;
    private final BankSettings bankSettings = new BankSettings();
    private final InventorySet inventorySet = new InventorySet(this);
    private ChatMessage chatMessage;
    private RegionPalette constructedRegion;
    private SceneRebuiltListener sceneRebuiltListener;
    private final PlayerTimers timers = new PlayerTimers();
    private final Friends friends = new Friends(this);
    private final Prayers prayers = new Prayers(this);
    private final ScriptInput scriptInput = new ScriptInput(this);
    private final PlayerSettings settings = new PlayerSettings(this);
    private final PlayerVariables variables = new PlayerVariables();
    private final InterfaceSet interfaceSet = new InterfaceSet(this);
    private final GrandExchangeHandler grandExchangeHandler = new GrandExchangeHandler(this);
    private final ShopHandler shopHandler = new ShopHandler(this);
    private final StateSet stateSet = new StateSet(this);
    private Spellbook spellbook = Spellbook.NORMAL_SPELLBOOK;
    private final AccessSet accessSet = new AccessSet(this);
    private final GroundItemSynchronizer groundItemSync = new GroundItemSynchronizer(this);
    private final GroundObjectSynchronizer groundObjSync = new GroundObjectSynchronizer(this);
    private int[] appearanceTickets = new int[World.MAX_PLAYERS];
    private int appearanceTicket = nextAppearanceTicket();
    private final PlayerOption[] options = new PlayerOption[10];
    private int pnpc = -1;

    // TODO remove
    private Position min;
    private Position max;

    public Player() {
        init();
    }

    public boolean actionsBlocked() {
        return blockActions || !alive();
    }

    private String addPlus(int bonus) {
        return bonus > 0 ? "+" + bonus : "" + bonus;
    }

    public void appearanceUpdated() {
        appearanceTicket = nextAppearanceTicket();
    }

    public void calculateEquipmentBonuses() {
        for (AttackType type : AttackType.values()) {
            int typeAttackBonus = 0;
            int typeDefenceBonus = 0;
            for (Item equipped : getEquipment().toArray()) {
                if (equipped == null) {
                    continue;
                }
                typeAttackBonus += equipped.getEquipmentDefinition().getBonuses().getAttackBonus(type);
                typeDefenceBonus += equipped.getEquipmentDefinition().getBonuses().getDefenceBonus(type);
            }
            combatBonuses.setAttackBonus(type, typeAttackBonus);
            combatBonuses.setDefenceBonus(type, typeDefenceBonus);
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
        combatBonuses.setPrayerBonus(prayerBonus);
        combatBonuses.setStrengthBonus(strengthBonus);

        Item weapon = getEquipment().get(Equipment.WEAPON);
        Item ammo = getEquipment().get(Equipment.AMMO);

        int rangeBonus = weapon == null ? 0 : weapon.getEquipmentDefinition().getBonuses().getRangeStrengthBonus();

        if (rangeBonus == 0 && ammo != null) {
            rangeBonus = ammo.getEquipmentDefinition().getBonuses().getRangeStrengthBonus();
        }

        combatBonuses.setRangeStrengthBonus(rangeBonus);

        if (getInterfaceSet().getWindow().getCurrentId() == 667) {
            sendEquipmentBonuses();
        }
    }

    public void endBankSession() {
        bankSession = null;
    }

    public AccessSet getAccessSet() {
        return accessSet;
    }

    public Appearance getAppearance() {
        return appearance;
    }

    public int getAppearanceTicket() {
        return appearanceTicket;
    }

    public int[] getAppearanceTickets() {
        return appearanceTickets;
    }

    public Inventory getBank() {
        return inventorySet.getBank();
    }

    public BankSession getBankSession() {
        return bankSession;
    }

    public BankSettings getBankSettings() {
        return bankSettings;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    @Override
    public int getCurrentHitpoints() {
        return skillSet.getCurrentLevel(Skill.HITPOINTS);
    }

    public int getDatabaseId() {
        return databaseId;
    }

    public Animation getDeathAnimation() {
        return DEATH_ANIMATION;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getEnergy() {
        return energy;
    }

    public Inventory getEquipment() {
        return inventorySet.getEquipment();
    }

    public Friends getFriends() {
        return friends;
    }

    public PlayerTimers getTimers() {
        return timers;
    }

    public GrandExchangeHandler getGrandExchangeHandler() {
        return grandExchangeHandler;
    }

    @Override
    public int getHealthRegen() {
        int regen = prayers.prayerActive(HEAL) ? 2 : 1;
        // Regen brace
        if (getEquipment().get(Equipment.HANDS) != null) {
            regen += getEquipment().get(Equipment.HANDS).getId() == 11133 ? 1 : 0;
        }
        return regen;
    }

    public Position getHomeLocation() {
        return HOME_LOCATIONS[variables.getVar(Variable.HOME_LOCATION)];
    }

    public InterfaceSet getInterfaceSet() {
        return interfaceSet;
    }

    public Inventory getInventory() {
        return inventorySet.getInventory();
    }

    public InventorySet getInventorySet() {
        return inventorySet;
    }

    public Position getLastKnownRegion() {
        return lastKnownRegion;
    }

    public List<NPC> getLocalNpcs() {
        return localNpcs;
    }

    public List<Player> getLocalPlayers() {
        return localPlayers;
    }

    public long getLongUsername() {
        return longUsername;
    }

    @Override
    public int getMaximumHitpoints() {
        return skillSet.getLevel(Skill.HITPOINTS);
    }

    public PlayerOption getOption(int id) {
        return options[id];
    }

    public String getPassword() {
        return password;
    }

    public PlayerCombatHandler getPlayerCombatHandler() {
        return (PlayerCombatHandler) combatHandler;
    }

    public int getPNPC() {
        return pnpc;
    }

    public int getPrayerPoints() {
        return skillSet.getCurrentLevel(Skill.PRAYER);
    }

    public Prayers getPrayers() {
        return prayers;
    }

    public int getRights() {
        return rights;
    }

    public ScriptInput getScriptInput() {
        return scriptInput;
    }

    public GameSession getSession() {
        return session;
    }

    public PlayerSettings getSettings() {
        return settings;
    }

    public PlayerVariables getVariables() {
        return variables;
    }

    public ShopHandler getShopHandler() {
        return shopHandler;
    }

    public House getHouse() {
        return house;
    }

    public House getInHouse() {
        return inHouse;
    }

    public void setInHouse(House inHouse) {
        this.inHouse = inHouse;
    }

    public PlayerFarms getFarms() {
        return farms;
    }

    public SkillSet getSkillSet() {
        return skillSet;
    }

    public Spellbook getSpellbook() {
        return spellbook;
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

    public StateSet getStateSet() {
        return stateSet;
    }

    public int getTotalWeight() {
        return getInventory().getWeight() + getEquipment().getWeight();
    }

    public TradeSession getTradeSession() {
        return tradeSession;
    }

    public boolean getUpdateModelLists() {
        return updateModelLists;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void heal(int amount) {
        int temp = getCurrentHitpoints() + amount;
        temp = temp > getMaximumHitpoints() ? getMaximumHitpoints() : temp;
        skillSet.setCurrentLevel(Skill.HITPOINTS, temp);
    }

    private void init() {
        combatBonuses = new CombatBonuses();
        combatHandler = new PlayerCombatHandler(this);
        skillSet.addListener(new SkillMessageListener(this));
        skillSet.addListener(new SkillAppearanceListener(this));
        World.getWorld().getGroundObjects().addListener(groundObjSync);
        World.getWorld().getGroundItems().addListener(groundItemSync);

        /* Initialize all the player options */
        for (int i = 0; i < options.length; i++) {
            options[i] = new PlayerOption();
        }
    }

    public void forceMovement(ForcedMovement forcedMove) {
        this.forcedMove = forcedMove;
        blockActions = true;
        walkingQueue.reset();
        World.getWorld().getTaskScheduler().schedule(new Task(forcedMove.getDurationTicks(), false) {
            @Override
            public void execute() {
                stop();
            }

            public void stop() {
                teleport(forcedMove.getSecondPosition());
                blockActions = false;
                super.stop();
            }
        });
    }

    public ForcedMovement getForcedMove() {
        return forcedMove;
    }

    public boolean isForceMovementUpdated() {
        return forcedMove != null;
    }

    public boolean isChatUpdated() {
        return chatMessage != null;
    }

    public boolean isRegionChanging() {
        return regionChanging;
    }

    @Override
    public boolean isRunning() {
        return settings.isRunning();
    }

    /**
     * Forces the player to logout. Only call from logout button.
     */
    public void logout() {
        ChannelFuture future = send(new LogoutMessage());
        if (future != null) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    protected void onDeath() {
        reset();
        startAction(new PlayerDeathAction(this));
    }

    @Override
    public void reduceHp(int amount) {
        skillSet.setCurrentLevel(Skill.HITPOINTS, getCurrentHitpoints() - amount);
    }

    public void reducePrayerPoints(int amount) {
        skillSet.setCurrentLevel(Skill.PRAYER, getPrayerPoints() - amount);
    }

    public void refreshGroundItems() {
        groundItemSync.purge();
        World.getWorld().getGroundItems().fireEvents(groundItemSync);
    }

    public void refreshGroundObjects() {
        groundObjSync.purge();
        World.getWorld().getGroundObjects().fireEvents(groundObjSync);
    }

    public GroundObjectSynchronizer getGroundObjectSynchronizer() {
        return groundObjSync;
    }

    public void refreshOptions() {
        for (int i = 0; i < options.length; i++) {
            PlayerOption option = options[i];
            send(new PlayerMenuOptionMessage(i + 1, option.atTop(), option.getText()));
        }
    }

    @Override
    public void reset() {
        super.reset();
        updateModelLists = false;
        regionChanging = false;
        chatMessage = null;
        constructedRegion = null;
        forcedMove = null;
    }

    public SceneRebuiltListener getSceneRebuiltListener() {
        return sceneRebuiltListener;
    }

    public void setSceneRebuiltListener(SceneRebuiltListener sceneRebuiltListener) {
        this.sceneRebuiltListener = sceneRebuiltListener;
    }

    public ChannelFuture send(Message message) {
        if (session != null) {
            return session.send(message);
        } else {
            return null;
        }
    }

    public void sendEquipmentBonuses() {
        setInterfaceText(667, 32, "0 kg");
        setInterfaceText(667, 36, "Stab: " + addPlus(combatBonuses.getAttackBonus(AttackType.STAB)));
        setInterfaceText(667, 37, "Slash: " + addPlus(combatBonuses.getAttackBonus(AttackType.SLASH)));
        setInterfaceText(667, 38, "Crush: " + addPlus(combatBonuses.getAttackBonus(AttackType.CRUSH)));
        setInterfaceText(667, 39, "Magic: " + addPlus(combatBonuses.getAttackBonus(AttackType.MAGIC)));
        setInterfaceText(667, 40, "Ranged: " + addPlus(combatBonuses.getAttackBonus(AttackType.RANGE)));
        setInterfaceText(667, 41, "Stab: " + addPlus(combatBonuses.getDefenceBonus(AttackType.STAB)));
        setInterfaceText(667, 42, "Slash: " + addPlus(combatBonuses.getDefenceBonus(AttackType.SLASH)));
        setInterfaceText(667, 43, "Crush: " + addPlus(combatBonuses.getDefenceBonus(AttackType.CRUSH)));
        setInterfaceText(667, 44, "Magic: " + addPlus(combatBonuses.getDefenceBonus(AttackType.MAGIC)));
        setInterfaceText(667, 45, "Range: " + addPlus(combatBonuses.getDefenceBonus(AttackType.RANGE)));
        setInterfaceText(667, 46, "Summoning: 0");
        setInterfaceText(667, 48, "Strength: " + addPlus(combatBonuses.getStrengthBonus()));
        setInterfaceText(667, 49, "Prayer: " + addPlus(combatBonuses.getPrayerBonus()));
    }

    public void sendMessage(String text) {
        send(new ServerMessage(text));
    }

    public void setActionsBlocked(boolean blockActions) {
        this.blockActions = blockActions;
    }

    public void setAppearance(Appearance appearance) {
        this.appearance = appearance;
        appearanceUpdated();
    }

    public void setChatMessage(ChatMessage message) {
        this.chatMessage = message;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
        this.send(new EnergyMessage(energy));
    }

    @Override
    public void setHidden(boolean hidden) {
        super.setHidden(hidden);
        appearanceUpdated();
    }

    public void setInterfaceText(int widgetId, int componentId, String text) {
        send(new InterfaceTextMessage(widgetId, componentId, text));
    }

    public void setLastKnownRegion(Position lastKnownRegion) {
        this.lastKnownRegion = lastKnownRegion;
        World.getWorld().getGroundObjects().removeListener(groundObjSync);

        GameServer.getInstance().getMapLoader().load(lastKnownRegion.getX() / 64, lastKnownRegion.getY() / 64);

        World.getWorld().getGroundObjects().addListener(groundObjSync);
        this.regionChanging = true;
    }

    public void resetPos() {
        min = null;
        max = null;
        sendMessage("Min/max reset.");
    }

    public void setMax() {
        max = position;
        sendMessage("Top right bounds set to " + max);
    }

    public void setMin() {
        min = position;
        sendMessage("Bottom left bounds set to " + min);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPNPC(int pnpc) {
        this.pnpc = pnpc;
        if (pnpc == -1) {
            size = 0;
        } else {
            size = NPCDefinitions.forId(pnpc).getSize();
        }
        appearanceUpdated();
    }

    public void setRights(int rights) {
        this.rights = rights;
    }

    public void setSession(GameSession session) {
        this.session = session;
    }

    public void setSpawnPos(int id) {

            NPCDefinition def = NPCDefinitions.forId(id);
            if (def.getExamine() == null) {
                sendMessage("Creating default NPC definition for " + def.getName());
                def.setExamine(def.getName() + (def.getCombatLevel() > 0 ? (" (lvl:" + def.getCombatLevel() + ")") : ""));
                ((JdbcSerializer) GameServer.getInstance().getSerializer()).saveDefaultNPCDef(id, def.getName() + (def.getCombatLevel() > 0 ? (" (lvl:" + def.getCombatLevel() + ")") : ""));
            }
            if((min != null && max != null) && (position.getX() < min.getX() || position.getY() < min.getY() || position.getX() > max.getX() || position.getY() > max.getY())) {
                sendMessage("Spawn position out of bounds!");
                return;
            }
            ((JdbcSerializer) GameServer.getInstance().getSerializer()).saveNPCSpawn(id, position, min, max);
            sendMessage("Spawn for " + def.getName() + " sent to SQL.");        
    }

    public void setSpellbook(Spellbook spellbook) {
        this.spellbook = spellbook;
    }

    public void setTradeRequest(Player other) {
        wantToTrade = other;
    }

    public void setTradeSession(TradeSession tradeSession) {
        this.tradeSession = tradeSession;
    }

    public void setUpdateModelLists(boolean updateModelLists) {
        this.updateModelLists = updateModelLists;
    }

    public void setUsername(String username) {
        this.username = username.replaceAll(" ", "_").toLowerCase();
        displayName = StringUtils.capitalize(username);
        longUsername = Base37Utils.encodeBase37(username);
    }

    public void startBankSession() {
        bankSession = new BankSession(this);
        bankSession.init();
    }

    @Override
    public void teleport(Position position) {
        interfaceSet.resetAll();
        super.teleport(position);
    }

    public void setClipped(boolean clipped) {
        this.clipped = clipped;
    }

    public void toggleClipping() {
        clipped = !clipped;
    }

    public void unregister() {
        World.getWorld().getGroundItems().removeListener(groundItemSync);
        World.getWorld().getGroundObjects().removeListener(groundObjSync);
        friends.logout();
        stopAction();
    }

    public boolean wantsToTrade(Player other) {
        return other == wantToTrade;
    }

    public void setConstructedRegion(RegionPalette region) {
        constructedRegion = region;
    }

    public RegionPalette getConstructedRegion() {
        return constructedRegion;
    }

    public void onLogin() {
        grandExchangeHandler.init();
        calculateEquipmentBonuses();
        friends.init();
        if (variables.getVar(Variable.FIRST_LOGIN) == 1) {
            variables.setVar(Variable.FIRST_LOGIN, 0);
            sendMessage("Welcome, thank you for joining MoparScape!");
            World.getWorld().sendGlobalMessage(getDisplayName() + " has joined the server! Welcome!");

            // Starter items
            getInventory().add(new Item(995, 5000)); // Coins
            getInventory().add(new Item(1171)); // Wooden shield
            getInventory().add(new Item(1277)); // Bronze sword
            getInventory().add(new Item(2309)); // Bread

            getInventory().add(new Item(FishingTool.SMALL_NET.getToolId()));
            getInventory().add(new Item(Hatchet.BRONZE.getItemId()));
            getInventory().add(new Item(Pickaxe.BRONZE.getItemId()));
            getInventory().add(new Item(Firemaking.TINDERBOX));

            getInventory().add(new Item(Bow.SHORTBOW.getBowId()));
            getInventory().add(new Item(Arrow.BRONZE.getArrowId(), 100));

            getInventory().add(new Item(Rune.MIND.getItemId(), 500));
            getInventory().add(new Item(Rune.AIR.getItemId(), 500));
            getInventory().add(new Item(Rune.WATER.getItemId(), 250));
            getInventory().add(new Item(Rune.EARTH.getItemId(), 250));
            getInventory().add(new Item(Rune.FIRE.getItemId(), 250));
        } else {
            sendMessage("Welcome to MoparScape.");
            World.getWorld().sendGlobalMessage(getDisplayName() + " has logged in.");
        }
        System.out.println("Player logged in: " + getDisplayName());
    }
}
