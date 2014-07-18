package net.scapeemulator.game.io;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.scapeemulator.cache.def.NPCDefinition;
import net.scapeemulator.game.io.jdbc.AppearanceTable;
import net.scapeemulator.game.io.jdbc.FriendsTable;
import net.scapeemulator.game.io.jdbc.GrandExchangeOfferTable;
import net.scapeemulator.game.io.jdbc.GrandExchangeTable;
import net.scapeemulator.game.io.jdbc.ItemsTable;
import net.scapeemulator.game.io.jdbc.PlayersTable;
import net.scapeemulator.game.io.jdbc.SettingsTable;
import net.scapeemulator.game.io.jdbc.SkillsTable;
import net.scapeemulator.game.io.jdbc.Table;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.area.QuadArea;
import net.scapeemulator.game.model.definition.NPCDefinitions;
import net.scapeemulator.game.model.grandexchange.GEOffer;
import net.scapeemulator.game.model.grandexchange.GrandExchange;
import net.scapeemulator.game.model.mob.combat.AttackType;
import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.model.npc.drops.NPCDropTable;
import net.scapeemulator.game.model.npc.drops.TableType;
import net.scapeemulator.game.model.npc.stateful.impl.NormalNPC;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.ShopHandler;
import net.scapeemulator.game.model.player.inventory.Inventory;
import net.scapeemulator.game.model.shop.Shop;
import net.scapeemulator.game.net.login.LoginResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JdbcSerializer extends Serializer implements Closeable {

    private static final Logger logger = LoggerFactory.getLogger(JdbcSerializer.class);

    private final Connection connection;
    private final PreparedStatement loginStatement;
    private final PreparedStatement registerStatement;
    private final Table<Player>[] playerTables;
    private final Table<GrandExchange> geTable;
    private final Table<GEOffer> geOfferTable;

    @SuppressWarnings("unchecked")
    public JdbcSerializer(String url, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(false);
        loginStatement = connection.prepareStatement("SELECT id, password FROM players WHERE username = ?;");
        registerStatement = connection.prepareStatement("INSERT INTO players (ip, username, password, rights, x, y, height) VALUES (?, ?, ?, 0, 3222, 3222, 0);");

        playerTables = new Table[] { new PlayersTable(connection), new FriendsTable(connection), new SettingsTable(connection), new AppearanceTable(connection), new SkillsTable(connection),
                new ItemsTable(connection, "inventory") {
                    @Override
                    public Inventory getInventory(Player player) {
                        return player.getInventory();
                    }
                },

                new ItemsTable(connection, "equipment") {
                    @Override
                    public Inventory getInventory(Player player) {
                        return player.getEquipment();
                    }
                }, };
        geTable = new GrandExchangeTable(connection);
        geOfferTable = new GrandExchangeOfferTable(connection);
        try {
            geTable.load(World.getWorld().getGrandExchange());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public SerializeResult loadPlayer(String username, String password) {
        try {
            loginStatement.setString(1, username);
            try (ResultSet set = loginStatement.executeQuery()) {
                if (set.first()) {
                    int id = set.getInt("id");
                    String hashedPassword = set.getString("password");
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        Player player = new Player();
                        player.setDatabaseId(id);
                        player.setPassword(password); /* can't use hashed one in PlayerTable */
                        for (Table<Player> table : playerTables)
                            table.load(player);

                        return new SerializeResult(LoginResponse.STATUS_OK, player);
                    }
                }

                return new SerializeResult(LoginResponse.STATUS_INVALID_PASSWORD);
            }
        } catch (SQLException | IOException ex) {
            logger.warn("Loading player " + username + " failed.", ex);
            return new SerializeResult(LoginResponse.STATUS_COULD_NOT_COMPLETE);
        }
    }

    @Override
    public void savePlayer(Player player) {
        try {
            for (Table<Player> table : playerTables)
                table.save(player);

            connection.commit();
        } catch (SQLException | IOException ex) {
            try {
                connection.rollback();
            } catch (SQLException innerEx) {
                /* ignore rollback failure, not much we can do */
            }

            logger.warn("Saving player " + player.getDisplayName() + " failed.", ex);
        }
    }

    @Override
    public boolean usernameAvailable(String username) {
        try {
            loginStatement.setString(1, username);
            try (ResultSet set = loginStatement.executeQuery()) {
                if (set.first()) {
                    return false;
                }
                return true;
            }
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public boolean register(String ip, String username, String password) {
        try {
            registerStatement.setString(1, ip);
            registerStatement.setString(2, username);
            registerStatement.setString(3, BCrypt.hashpw(password, BCrypt.gensalt()));
            registerStatement.execute();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            return false;
        }

    }

    public void saveGrandExchange(GrandExchange ge) {
        try {
            geTable.save(ge);
            connection.commit();
        } catch (SQLException | IOException ex) {
            try {
                connection.rollback();
            } catch (SQLException innerEx) {
            }
        }
    }

    public void saveGrandExchangeOffer(GEOffer offer) {
        try {
            geOfferTable.save(offer);
            connection.commit();
        } catch (SQLException | IOException ex) {
            try {
                ex.printStackTrace();
                connection.rollback();
            } catch (SQLException innerEx) {
            }
        }
    }

    public void removeGrandExchangeOffer(GEOffer offer) {
        try {
            ((GrandExchangeOfferTable) geOfferTable).removeOffer(offer);
            connection.commit();
        } catch (SQLException ex) {
            try {
                ex.printStackTrace();
                connection.rollback();
            } catch (SQLException innerEx) {
            }
        }
    }

    public void loadGrandExchange(GrandExchange ge) {
        try {
            geTable.load(ge);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadNPCSpawns() {
        System.out.print("Loading NPC spawns... ");
        int count = 0;
        try (ResultSet set = connection.prepareStatement("SELECT * FROM npcspawns").executeQuery()) {
            while (set.next()) {
                int type = set.getInt("type");
                NPC npc = new NormalNPC(type);
                int x = set.getInt("x");
                int y = set.getInt("y");
                int height = set.getInt("height");
                npc.setPosition(new Position(x, y, height));
                if (set.getInt("roam") == 1) {
                    int x0 = set.getInt("min_x");
                    int y0 = set.getInt("min_y");
                    int x1 = set.getInt("max_x");
                    int y1 = set.getInt("max_y");
                    npc.setWalkingBounds(new QuadArea(x0, y0, x1, y1));
                }
                count++;
                World.getWorld().addNpc(npc);
            }
            System.out.println("complete! Loaded " + count);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadNPCDefinitions() {
        System.out.print("Loading NPC definitions... ");
        int count = 0;
        try (ResultSet set = connection.prepareStatement("SELECT * FROM npcdefs").executeQuery()) {
            while (set.next()) {
                int type = set.getInt("type");
                NPCDefinition def = NPCDefinitions.forId(type);
                def.setExamine(set.getString("examine"));
                def.setAttackable(set.getBoolean("attackable"));
                if (def.isAttackable()) {
                    def.setAggressiveRange(set.getInt("aggressive_range"));
                    def.setBaseHitpoints(set.getInt("base_hp"));
                    def.setRespawnTime(set.getInt("respawn_ticks"));
                    def.setAttackRange(set.getInt("attack_range"));
                    def.setAttackDelay(set.getInt("attack_delay"));
                    def.setAttackType(AttackType.valueOf(set.getString("attack_type")));
                    String weakness = set.getString("weakness");
                    if (weakness != null) {
                        def.setWeakness(AttackType.valueOf(weakness));
                    }
                    def.setMaxHit(set.getInt("max_hit"));
                    def.setAttackEmote(set.getInt("attack_emote"));
                    def.setDefendEmote(set.getInt("defend_emote"));
                    def.setDeathEmote(set.getInt("death_emote"));
                    // def.setAutoCast();
                }
                count++;
            }
            System.out.println("complete! Loaded " + count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadNPCDrops() {
        System.out.print("Loading NPC drops... ");
        int count = 0;
        try (ResultSet set = connection.prepareStatement("SELECT * FROM npcdrops").executeQuery()) {
            while (set.next()) {
                int npcType = set.getInt("type");
                NPCDropTable table = NPCDropTable.DROP_TABLES[npcType];
                if (table == null) {
                    table = new NPCDropTable();
                    NPCDropTable.DROP_TABLES[npcType] = table;
                    count++;
                }
                TableType type = TableType.valueOf(set.getString("table_type"));
                double chance = set.getDouble("chance");
                if (set.wasNull()) {
                    table.addTable(type);
                } else {
                    table.addTable(type, chance);
                }
                String[] drops = set.getString("drops").split(" ");
                for (String drop : drops) {
                    String[] dropData = drop.split(":");
                    int itemId = Integer.parseInt(dropData[0]);
                    if (dropData.length == 1) {
                        table.addItem(type, itemId);
                    } else {
                        table.addItem(type, itemId, dropData[1]);
                    }
                }
            }
            System.out.println("complete! Loaded " + count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadShops() {
        System.out.print("Loading shops... ");
        int count = 0;
        try (ResultSet set = connection.prepareStatement("SELECT * FROM shopdefs").executeQuery()) {
            while (set.next()) {
                int id = set.getInt("id");
                String name = set.getString("name");
                int shopId = set.getInt("shop_id");
                String[] itemIds = set.getString("stock_ids").split(",");
                int[] stockIds = new int[itemIds.length];
                for (int i = 0; i < itemIds.length; i++) {
                    stockIds[i] = Integer.parseInt(itemIds[i]);
                }
                if (set.getBoolean("is_gen")) {
                    ShopHandler.shops.put(name, new Shop(id, name, shopId, false, true, stockIds));
                } else {
                    ShopHandler.shops.put(name, new Shop(id, name, shopId, false, false, stockIds));
                }
                count++;
            }
            System.out.println("complete! Loaded " + count);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

}
