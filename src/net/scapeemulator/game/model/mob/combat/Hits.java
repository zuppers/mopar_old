package net.scapeemulator.game.model.mob.combat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import net.scapeemulator.game.GameServer;
import net.scapeemulator.game.model.mob.Mob;

public class Hits {

    private final Mob mob;
    private final Queue<Hit> hitQueue = new LinkedList<>();
    private final Map<Mob, Hit> history = new HashMap<>();
    private final Hit[] hits = new Hit[2];
    private int historyTimer;

    public Hits(Mob mob) {
        this.mob = mob;
    }

    public boolean updated(int index) {
        return hits[index - 1] != null;
    }

    public void tick() {
        for (int i = 0; i < hits.length; i++) {
            if (hits[i] == null && hitQueue.size() > 0) {
                Hit hit = hitQueue.poll();
                hit.setTimestamp(GameServer.getInstance().getTickTimestamp());
                addToHistory(hit);
                hits[i] = hit;
            }
        }
        if (historyTimer > 0) {
            if (--historyTimer == 0) {
                history.clear();
            }
        }
    }

    public void addToHistory(Hit hit) {
        if (hit.source == mob) {
            // Don't add self damage, doesn't matter
            return;
        }
        if (history.containsKey(hit.source)) {
            history.get(hit.source).damage += hit.damage;
        } else {
            history.put(hit.source, new Hit(hit.source, hit.type, hit.damage));
        }

        history.get(hit.source).tickTimestamp = GameServer.getInstance().getTickTimestamp();

    }

    public Mob getMostDamageDealt() {
        Hit mostDamage = null;
        for (Hit hit : history.values()) {
            if (mostDamage == null || hit.damage > mostDamage.damage) {
                mostDamage = hit;
            }
        }
        if (mostDamage != null) {
            return mostDamage.source;
        }
        return null;
    }

    public void reset() {
        hits[0] = null;
        hits[1] = null;
    }

    public void clearQueue() {
        hitQueue.clear();
    }

    public void addHit(Mob source, HitType type, int damage) {
        Hit hit = new Hit(source, type, damage);
        hitQueue.add(hit);
        historyTimer = 50;
    }

    public int getDamage(int index) {
        return hits[index - 1].damage;
    }

    public int getType(int index) {
        return hits[index - 1].type.ordinal();
    }

    public Hit getMostRecentCombatHit() {
        Hit recent = null;
        for (Hit h : history.values()) {
            // Don't want poison/disease to count as active combat
            if (h.type == HitType.ZERO || h.type == HitType.NORMAL) {
                if (recent == null || h.tickTimestamp > recent.tickTimestamp) {
                    recent = h;
                }
            }
        }
        return recent;
    }

    public class Hit implements Comparable<Hit> {

        private HitType type;
        private int damage;
        private final Mob source;
        private int tickTimestamp;

        public Hit(Mob source, HitType type, int damage) {
            this.source = source;
            this.type = type;
            this.damage = damage;
        }

        public Mob getSource() {
            return source;
        }

        public int getTimestamp() {
            return tickTimestamp;
        }

        private void setTimestamp(int timestamp) {
            tickTimestamp = timestamp;
        }

        @Override
        public int compareTo(Hit compare) {
            return damage - compare.damage;
        }

    }

}
