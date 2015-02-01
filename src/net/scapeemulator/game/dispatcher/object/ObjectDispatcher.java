package net.scapeemulator.game.dispatcher.object;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.scapeemulator.game.GameServer;
import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.definition.ObjectDefinitions;
import net.scapeemulator.game.model.object.GroundObjectList;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.util.HandlerContext;

/**
 * @author Hadyn Richard
 */
public final class ObjectDispatcher {

    /**
     * The mapping for all of the handler lists.
     */
    private Map<Option, List<ObjectHandler>> handlerLists = new HashMap<>();

    /**
     * Constructs a new {@link ObjectDispatcher};
     */
    public ObjectDispatcher() {
        for (Option option : Option.values()) {
            if (option.equals(Option.ALL)) {
                continue;
            }
            handlerLists.put(option, new LinkedList<ObjectHandler>());
        }
    }

    public void bind(ObjectHandler handler) {
        if (handler.getOption().equals(Option.ALL)) {
            for (Map.Entry<Option, List<ObjectHandler>> entry : handlerLists.entrySet()) {
                entry.getValue().add(handler);
            }
        } else {
            List<ObjectHandler> list = handlerLists.get(handler.getOption());
            list.add(handler);
        }
    }

    public void unbindAll() {
        for (List<?> list : handlerLists.values()) {
            list.clear();
        }
    }

    private static String getOptionName(int id, Option option) {
        String optionName = ObjectDefinitions.forId(id).getOptions()[option.toInteger()];
        return optionName == null ? "null" : optionName.toLowerCase();
    }

    public void handle(Player player, int id, Position position, Option option) {
        if (player.actionsBlocked()) {
            return;
        }
        List<ObjectHandler> handlers = handlerLists.get(option);
        if (handlers != null) {

            GroundObjectList objectList = World.getWorld().getGroundObjects();
            if (player.getInHouse() != null) {
                objectList = player.getInHouse().getObjectList();
            }
            if (World.getWorld().getTraversalMap().isLowered(position.getX(),position.getY())) {
                position = new Position(position.getX(),position.getY(),position.getHeight() + 1);
            }
            GroundObject object = objectList.get(id, position);
            if (object == null || object.isHidden()) {
                return;
            }
            System.out.println("id: " + id + ", rotation: " + object.getRotation() + " , option: " + option + ", type: " + object.getType());

            String optionName = getOptionName(id, option);
            HandlerContext context = new HandlerContext();

            for (ObjectHandler handler : handlers) {

                /* Handle the message parameters */
                handler.handle(player, object, optionName, context);

                if (context.doStop()) {
                    break;
                }
            }
        }
    }

    public static ObjectDispatcher getInstance() {
        return GameServer.getInstance().getMessageDispatcher().getObjectDispatcher();
    }
}
