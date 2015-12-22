package net.scapeemulator.game.model.player.skills.construction.furniture;

import java.util.List;

import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.skills.construction.room.RoomPlaced;

public interface Furniture {
      
    Requirements getRequirements();

    MaterialRequirement material(int index);
    
    int getLevel();

    double getXp();

    int getObjectId(RoomPlaced room);

    int getItemId();
    
    List<Item> getReturnedItems();
}