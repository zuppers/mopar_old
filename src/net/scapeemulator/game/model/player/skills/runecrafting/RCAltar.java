package net.scapeemulator.game.model.player.skills.runecrafting;

import net.scapeemulator.game.model.Position;

public enum RCAltar {
    AIR(1438, 5527, 0, 2452, pos(2983, 3290), 2478, pos(2842, 4829), 2465, RCRune.AIR),
    MIND(1448, 5529, 1, 2453, pos(2983, 3512), 2479, pos(2793, 4828), 2466, RCRune.MIND),
    WATER(1444, 5531, 2, 2454, pos(3184, 3163), 2480, pos(3493, 4832), 2467, RCRune.WATER),
    EARTH(1440, 5535, 3, 2455, pos(3304, 3476), 2481, pos(2656, 4830), 2468, RCRune.EARTH),
    FIRE(1442, 5537, 4, 2456, pos(3311, 3253), 2482, pos(2577, 4845), 2469, RCRune.FIRE),
    BODY(1446, 5533, 5, 2457, pos(3055, 3444), 2483, pos(2521, 4834), 2470, RCRune.BODY),
    COSMIC(1454, 5539, 6, 2458, pos(2407, 4379), 2484, pos(2142, 4814), 2471, RCRune.COSMIC),
    CHAOS(1452, 5543, 9, 2461, pos(3062, 3591), 2487, new Position(2275, 4847, 3), 2474, RCRune.CHAOS),
    ASTRAL(-1, -1, -1, -1, null, 17010, pos(2155, 3864), -1, RCRune.ASTRAL),
    NATURE(1462, 5541, 8, 2460, pos(2868, 3021), 2486, pos(2400, 4835), 2473, RCRune.NATURE),
    LAW(1458, 5545, 7, 2459, pos(2858, 3379), 2485, pos(2464, 4819), 2472, RCRune.LAW),
    DEATH(1456, 5547, 10, 2462, pos(1863, 4639), 2488, pos(2208, 4831), 2475, RCRune.DEATH),
    BLOOD(1450, 5549, 11, 30529, pos(3561, 9779), 30624, new Position(2467, 4889, 1), 2477, RCRune.BLOOD);

    private final RCRune rune;
    private final int altarId;
    private final int ruinsId;
    private final int talismanId;
    private final int tiaraId;
    private final int configIndex;
    private final int portalId;
    private final Position ruinsPos;
    private final Position altarPos;

    /**
     * @param talismanId the talisman item id
     * @param tiaraId the tiara item id
     * @param configIndex
     * @param ruinsId the ruins object id
     * @param ruinsPos the position in the 'real world' to teleport the player when leaving the altar
     * @param altarId the altar object id
     * @param altarPos the position to teleport the player to when entering the altar
     * @param portalId the portal object id
     * @param rune the rune type the altar can craft
     */
    private RCAltar(int talismanId, int tiaraId, int configIndex, int ruinsId, Position ruinsPos, int altarId, Position altarPos, int portalId,
            RCRune rune) {
        this.rune = rune;
        this.talismanId = talismanId;
        this.tiaraId = tiaraId;
        this.configIndex = configIndex;
        this.altarId = altarId;
        this.ruinsId = ruinsId;
        this.portalId = portalId;
        this.ruinsPos = ruinsPos;
        this.altarPos = altarPos;
    }

    public static RCAltar forAltarId(int objectId) {
        for (RCAltar altar : values()) {
            if (altar.altarId == objectId) {
                return altar;
            }
        }
        return null;
    }

    public static RCAltar forRuinsId(int objectId) {
        for (RCAltar altar : values()) {
            if (altar.ruinsId == objectId) {
                return altar;
            }
        }
        return null;
    }

    public static RCAltar forPortalId(int objectId) {
        for (RCAltar altar : values()) {
            if (altar.portalId == objectId) {
                return altar;
            }
        }
        return null;
    }

    public static RCAltar forTalismanId(int itemId) {
        if (itemId >= 0) {
            for (RCAltar t : values()) {
                if (t.talismanId == itemId) {
                    return t;
                }
            }
        }
        return null;
    }

    public static RCAltar forTiaraId(int itemId) {
        if (itemId >= 0) {
            for (RCAltar t : values()) {
                if (t.tiaraId == itemId) {
                    return t;
                }
            }
        }
        return null;
    }

    public RCRune getRune() {
        return rune;
    }

    public int getTalismanId() {
        return talismanId;
    }

    public int getTiaraId() {
        return tiaraId;
    }

    public int getRuinsId() {
        return ruinsId;
    }

    public int getConfigIndex() {
        return configIndex;
    }

    public Position getRuinsPos() {
        return ruinsPos;
    }

    public Position getAltarPos() {
        return altarPos;
    }

    private static final Position pos(int x, int y) {
        return new Position(x, y);
    }

}