package net.scapeemulator.game.io;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import net.scapeemulator.cache.def.NPCDefinition;
import net.scapeemulator.game.cache.MapLoader;
import net.scapeemulator.game.content.grandexchange.GEOffer;
import net.scapeemulator.game.content.grandexchange.GrandExchange;
import net.scapeemulator.game.content.shop.Shop;
import net.scapeemulator.game.io.jdbc.AppearanceTable;
import net.scapeemulator.game.io.jdbc.FriendsTable;
import net.scapeemulator.game.io.jdbc.GrandExchangeOfferTable;
import net.scapeemulator.game.io.jdbc.GrandExchangeTable;
import net.scapeemulator.game.io.jdbc.ItemsTable;
import net.scapeemulator.game.io.jdbc.PlayersTable;
import net.scapeemulator.game.io.jdbc.SettingsTable;
import net.scapeemulator.game.io.jdbc.SkillsTable;
import net.scapeemulator.game.io.jdbc.Table;
import net.scapeemulator.game.io.jdbc.VariablesTable;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.area.QuadArea;
import net.scapeemulator.game.model.definition.NPCDefinitions;
import net.scapeemulator.game.model.mob.Direction;
import net.scapeemulator.game.model.mob.combat.AttackType;
import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.model.npc.drops.DropTable;
import net.scapeemulator.game.model.npc.drops.TableType;
import net.scapeemulator.game.model.npc.stateful.impl.NormalNPC;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.ShopHandler;
import net.scapeemulator.game.model.player.inventory.Inventory;
import net.scapeemulator.game.net.login.LoginResponse;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.Statement;

public final class JdbcSerializer extends Serializer implements Closeable {

    private static final Logger logger = LoggerFactory.getLogger(JdbcSerializer.class);

    private final Connection connection;
    private final PreparedStatement loginStatement;
    private final PreparedStatement registerStatement;
    private final PreparedStatement saveSpawnStatement;
    private final Table<Player>[] playerTables;
    private final Table<GrandExchange> geTable;
    private final Table<GEOffer> geOfferTable;

    @SuppressWarnings("unchecked")
    public JdbcSerializer(String url, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(false);
        loginStatement = connection.prepareStatement("SELECT id, password FROM players WHERE username = ?;");
        saveSpawnStatement = connection.prepareStatement("INSERT INTO npcspawns (type, x, y, height, roam, min_x, min_y, max_x, max_y) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
        registerStatement = connection.prepareStatement("INSERT INTO players (ip, username, password, rights, x, y, height) VALUES (?, ?, ?, 0, 3222, 3222, 0);", Statement.RETURN_GENERATED_KEYS);

        playerTables = new Table[] { new PlayersTable(connection), new FriendsTable(connection), new SettingsTable(connection), new VariablesTable(connection), new AppearanceTable(connection),
                new SkillsTable(connection), new ItemsTable(connection, "inventory") {
                    @Override
                    public Inventory getInventory(Player player) {
                        return player.getInventory();
                    }
                }, new ItemsTable(connection, "equipment") {
                    @Override
                    public Inventory getInventory(Player player) {
                        return player.getEquipment();
                    }
                }, new ItemsTable(connection, "bank") {
                    @Override
                    public Inventory getInventory(Player player) {
                        return player.getBank();
                    }
                } };
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
            for (Table<Player> table : playerTables) {
                table.save(player);
            }

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
            ResultSet result = registerStatement.getGeneratedKeys();
            result.next();
            int dbId = result.getInt(1);
            logger.info("Registered `" + username + "` dbId: " + dbId);
            connection.commit();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
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
        if (MapLoader.LOAD_NPCS) {
            return;
        }
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
                npc.setDirections(Direction.valueOf(set.getString("direction")), Direction.NONE);
                count++;
                World.getWorld().addNpc(npc);
            }
            System.out.println("complete! Loaded " + count);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveNPCSpawn(int npcType, Position spawnPosition, Position min, Position max) {
        try {
            saveSpawnStatement.setInt(1, npcType);
            saveSpawnStatement.setInt(2, spawnPosition.getX());
            saveSpawnStatement.setInt(3, spawnPosition.getY());
            saveSpawnStatement.setInt(4, spawnPosition.getHeight());

            if (min != null && max != null) {
                saveSpawnStatement.setInt(5, 1);
                saveSpawnStatement.setInt(6, min.getX());
                saveSpawnStatement.setInt(7, min.getY());
                saveSpawnStatement.setInt(8, max.getX());
                saveSpawnStatement.setInt(9, max.getY());
            } else {
                saveSpawnStatement.setInt(5, 0);
                saveSpawnStatement.setNull(6, Types.SMALLINT);
                saveSpawnStatement.setNull(7, Types.SMALLINT);
                saveSpawnStatement.setNull(8, Types.SMALLINT);
                saveSpawnStatement.setNull(9, Types.SMALLINT);
            }
            saveSpawnStatement.execute();
            connection.commit();
        } catch (Exception e) {
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
        System.out.println("Loading NPC drops... ");
        Map<String, DropTable> tables = new HashMap<>();
        try {
            ResultSet set = connection.prepareStatement("SELECT * FROM npc_drops").executeQuery();
            while (set.next()) {
                String tableName = set.getString("def");
                DropTable table = tables.get(tableName);
                if (table == null) {
                    table = new DropTable();
                    tables.put(tableName, table);
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
            int count = 0;
            set = connection.prepareStatement("SELECT * FROM npc_drop_defs").executeQuery();
            while (set.next()) {
                String tableName = set.getString("name");
                DropTable table = tables.get(tableName);
                if (table == null) {
                    System.out.println("No drops found for table " + tableName + "!");
                    continue;
                }
                String[] npcTypes = set.getString("types").trim().split(",");
                int[] npcIds = new int[npcTypes.length];
                for (int i = 0; i < npcTypes.length; i++) {
                    try {
                        npcIds[i] = Integer.parseInt(npcTypes[i]);
                        count++;
                    } catch (NumberFormatException e) {
                        System.out.println("Number format exception for NPC type " + npcTypes[i] + " in drop def " + tableName);
                    }
                }
            }
            System.out.println("Complete! Loaded drops for " + count + " NPC types.");
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
                ShopHandler.shops.put(name, new Shop(id, name, shopId, set.getBoolean("is_gen"), stockIds));
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
