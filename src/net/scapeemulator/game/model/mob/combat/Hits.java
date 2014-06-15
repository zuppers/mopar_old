package net.scapeemulator.game.model.mob.combat;

import java.util.LinkedList;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;
import net.scapeemulator.game.model.mob.Mob;

public class Hits {

	private final Mob mob;
	private final Queue<Hit> hitQueue = new LinkedList<Hit>();
	private final SortedSet<Hit> history = new TreeSet<Hit>();
	private final Hit[] hits = new Hit[2];
	private int lastHit;

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
				addToHistory(hit);
				hits[i] = hit;
			}
		}
		if (lastHit > 0) {
			if (--lastHit == 0) {
				history.clear();
			}
		}
	}

	public void addToHistory(Hit hit) {
		if (hit.source == mob) {
			// Don't let people damage themselves to avoid others getting the drop, ex: Dharok's
			return;
		}
		for (Hit h : history) {
			if (h.source == hit.source) {
				h.damage += hit.damage;
				return;
			}
		}
		history.add(hit);
	}

	public Mob getMostDamageDealt() {
		return history.last().source;
	}

	public void reset() {
		hits[0] = null;
		hits[1] = null;
	}

	public void clearQueue() {
		hitQueue.clear();
	}

	public void addHit(Mob source, HitType type, int damage) {
		hitQueue.add(new Hit(source, type, damage));
		lastHit = 50;
	}

	public int getDamage(int index) {
		return hits[index - 1].damage;
	}

	public int getType(int index) {
		return hits[index - 1].type.ordinal();
	}

	private class Hit implements Comparable<Hit> {

		private HitType type;
		private int damage;
		private final Mob source;

		public Hit(Mob source, HitType type, int damage) {
			this.source = source;
			this.type = type;
			this.damage = damage;
		}

		@Override
		public int compareTo(Hit compare) {
			return damage - compare.damage;
		}

	}

}
