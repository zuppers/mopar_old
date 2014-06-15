package net.scapeemulator.game.model.npc;

import java.util.ArrayList;

import net.scapeemulator.cache.def.NPCDefinition;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.area.Area;
import net.scapeemulator.game.model.definition.NPCDefinitions;
import net.scapeemulator.game.model.grounditem.GroundItemList;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.npc.action.NPCDeathAction;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;

public abstract class NPC extends Mob {

	private Position spawnPosition;
	private int type;
	private int currentHp;
	private Area bounds;
	private NPCDefinition definition;
	private NPCDropTable drops;

	public NPC(int type) {
		this.type = type;
		init();
	}

	private void init() {
		definition = NPCDefinitions.forId(type);
		combatHandler = new NPCCombatHandler(this);
		size = definition.getSize();
		healToFull();
		drops = new NPCDropTable();
	}

	public abstract void tick();

	public void setPosition(Position position) {
		if (spawnPosition == null) {
			spawnPosition = position;
		}
		super.setPosition(position);
	}

	public void setBounds(Area bounds) {
		this.bounds = bounds;
	}

	public Area getBounds() {
		return bounds;
	}

	public int getType() {
		return type;
	}

	public NPCDefinition getDefinition() {
		return definition;
	}

	public Position getSpawnPosition() {
		return spawnPosition;
	}

	protected void reduceHp(int amount) {
		currentHp -= amount;
	}

	public void healToFull() {
		currentHp = definition.getBaseHitpoints();
	}

	public void drop(Mob receiver) {
		GroundItemList groundItemList = receiver instanceof Player ? ((Player) receiver).getGroundItems() : World.getWorld().getGroundItems();
		ArrayList<Item> items = drops.getRandomDrops();
		for (Item item : items) {
			groundItemList.add(item.getId(), item.getAmount(), position);
		}
	}

	protected void onDeath() {
		reset();
		startAction(new NPCDeathAction(this));
	}

	@Override
	public int getCurrentHitpoints() {
		return currentHp;
	}

	@Override
	public boolean isRunning() {
		return false;
	}
}
