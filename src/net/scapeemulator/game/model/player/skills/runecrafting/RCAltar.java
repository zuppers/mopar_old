package net.scapeemulator.game.model.player.skills.runecrafting;

import net.scapeemulator.game.model.Position;

public enum RCAltar {
    AIR(RCRune.AIR, 1438, 5527, 0, 2478, 2452, 2465, null, pos(2842, 4829)),
    MIND(RCRune.MIND, 1448, 5529, 1, 2479, 2453, 2466, null, pos(2793, 4828)),
    WATER(RCRune.WATER, 1444, 5531, 2, 2480, 2454, 2467, null, pos(3493, 4832)),
    EARTH(RCRune.EARTH, 1440, 5535, 3, 2481, 2455, 2468, pos(3304, 3476), pos(2656, 4830)),
    FIRE(RCRune.FIRE, 1442, 5537, 4, 2482, 2456, 2469, null, pos(2577, 4845)),
    BODY(RCRune.BODY, 1446, 5533, 5, 2483, 2457, 2470, null, pos(2521, 4834)),
    COSMIC(RCRune.COSMIC, 1454, 5539, 6, 2484, 2458, 2471, null, pos(2142, 4814)),
    CHAOS(RCRune.CHAOS, 1452, 5543, 7, 2487, 2461, 2474, null, new Position(2275, 4847, 3)),
    ASTRAL(RCRune.ASTRAL, -1, -1, -1, 17010, -1, -1, null, pos(2155, 3864)),

    NATURE(RCRune.NATURE, 1462, 5541, 8, 2486, 2460, 2473, null, pos(2400, 4835)),
    LAW(RCRune.LAW, 1458, 5545, 9, 2485, 2459, 2472, pos(2858, 3379), pos(2464, 4819)),
    DEATH(RCRune.DEATH, 1456, 5547, 10, 2488, 2462, 2475, pos(1863, 4639), pos(2208, 4831)),
    BLOOD(RCRune.BLOOD, 1450, 5549, 11, 30624, 30529, 2477, pos(3561, 9779), new Position(2467, 4889, 1));
    // SOUL(RCRune.SOUL, 1460, 5551, -1) UNUSED

    private final RCRune rune;
    private final int altarId;
    private final int ruinsId;
    private final int talismanId;
    private final int tiaraId;
    private final int configIndex;
    private final int portalId;
    private final Position ruinsPos;
    private final Position altarPos;

    private RCAltar(RCRune rune, int talismanId, int tiaraId, int configIndex, int altarId, int ruinsId, int portalId, Position ruinsPos,
            Position altarPos) {
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