package net.scapeemulator.game.model.npc;

import java.util.List;

import net.scapeemulator.cache.def.NPCDefinition;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.area.Area;
import net.scapeemulator.game.model.definition.NPCDefinitions;
import net.scapeemulator.game.model.grounditem.GroundItemList;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.npc.action.NPCDeathAction;
import net.scapeemulator.game.model.npc.drops.NPCDropTable;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;

public abstract class NPC extends Mob {

    private Position spawnPosition;
    private int type;
    private int currentHp;
    private Area walkingBounds;
    private NPCDefinition definition;

    public NPC(int type) {
        this.type = type;
        init();
    }

    private void init() {
        definition = NPCDefinitions.forId(type);
        combatHandler = new NPCCombatHandler(this);
        size = definition.getSize();
        healToFull();
    }

    public abstract void tick();

    public void setPosition(Position position) {
        if (spawnPosition == null) {
            spawnPosition = position;
        }
        super.setPosition(position);
    }

    public void setWalkingBounds(Area walkingBounds) {
        this.walkingBounds = walkingBounds;
    }

    public Area getWalkingBounds() {
        return walkingBounds;
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

    public int getHealthRegen() {
        return 2;
    }
    
    protected void reduceHp(int amount) {
        currentHp -= amount;
    }

    public void heal(int amount) {
        currentHp += amount;
        if(currentHp > getMaximumHitpoints()) {
            currentHp = getMaximumHitpoints();
        }
    }
    
    public void healToFull() {
        currentHp = definition.getBaseHitpoints();
    }

    public void drop(Mob receiver) {
        if(NPCDropTable.DROP_TABLES[type] == null) {
            return;
        }
        GroundItemList groundItemList = receiver instanceof Player ? ((Player) receiver).getGroundItems() : World.getWorld().getGroundItems();
        List<Item> items = NPCDropTable.DROP_TABLES[type].getRandomDrops();
        for (Item item : items) {
            groundItemList.add(item.getId(), item.getAmount(), position);
        }
    }

    protected void onDeath() {
        reset();
        startAction(new NPCDeathAction(this));
    }
    
    @Override
    public int getMaximumHitpoints() {
        return definition.getBaseHitpoints();
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
