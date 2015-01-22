package net.scapeemulator.game.msg;

import java.util.HashMap;
import java.util.Map;

import net.scapeemulator.game.dispatcher.button.ButtonDispatcher;
import net.scapeemulator.game.dispatcher.command.CommandDispatcher;
import net.scapeemulator.game.dispatcher.grounditem.GroundItemDispatcher;
import net.scapeemulator.game.dispatcher.item.*;
import net.scapeemulator.game.dispatcher.npc.NPCDispatcher;
import net.scapeemulator.game.dispatcher.object.ObjectDispatcher;
import net.scapeemulator.game.dispatcher.player.PlayerDispatcher;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.handler.*;
import net.scapeemulator.game.msg.handler.inter.*;
import net.scapeemulator.game.msg.handler.item.*;
import net.scapeemulator.game.msg.handler.npc.*;
import net.scapeemulator.game.msg.handler.object.ObjectExamineMessageHandler;
import net.scapeemulator.game.msg.handler.object.ObjectOptionMessageHandler;
import net.scapeemulator.game.msg.impl.*;
import net.scapeemulator.game.msg.impl.button.ButtonOptionMessage;
import net.scapeemulator.game.msg.impl.camera.CameraAngleMessage;
import net.scapeemulator.game.msg.impl.grounditem.GroundItemOptionMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceClosedMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceInputMessage;
import net.scapeemulator.game.msg.impl.item.*;
import net.scapeemulator.game.msg.impl.npc.*;
import net.scapeemulator.game.msg.impl.object.ObjectExamineMessage;
import net.scapeemulator.game.msg.impl.object.ObjectOptionMessage;
import net.scapeemulator.game.msg.impl.player.PlayerOptionMessage;
import net.scapeemulator.game.plugin.ScriptContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MessageDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(MessageDispatcher.class);

    private final Map<Class<?>, MessageHandler<?>> handlers = new HashMap<>();
    private final ButtonDispatcher buttonDispatcher = new ButtonDispatcher();
    private final CommandDispatcher commandDispatcher = new CommandDispatcher();
    private final GroundItemDispatcher groundItemDispatcher = new GroundItemDispatcher();
    private final ItemOnItemDispatcher itemOnItemDispatcher = new ItemOnItemDispatcher();
    private final ItemOnObjectDispatcher itemOnObjectDispatcher = new ItemOnObjectDispatcher();
    private final ItemDispatcher itemDispatcher = new ItemDispatcher();
    private final ItemOnNPCDispatcher itemOnNPCDispatcher = new ItemOnNPCDispatcher();
    private final ObjectDispatcher objectDispatcher = new ObjectDispatcher();
    private final PlayerDispatcher playerDispatcher = new PlayerDispatcher();
    private final NPCDispatcher npcDispatcher = new NPCDispatcher();

    public MessageDispatcher() {
        bind(PingMessage.class, new PingMessageHandler());
        bind(IdleLogoutMessage.class, new IdleLogoutMessageHandler());
        bind(WalkMessage.class, new WalkMessageHandler());
        bind(ChatMessage.class, new ChatMessageHandler());
        bind(CommandMessage.class, new CommandMessageHandler(commandDispatcher));
        bind(SwapItemsMessage.class, new SwapItemsMessageHandler());
        bind(DisplayMessage.class, new DisplayMessageHandler());
        bind(RemoveItemMessage.class, new RemoveItemMessageHandler());
        bind(RegionChangedMessage.class, new RegionChangedMessageHandler());
        bind(ClickMessage.class, new ClickMessageHandler());
        bind(FocusMessage.class, new FocusMessageHandler());
        bind(GrandExchangeSearchMessage.class, new GrandExchangeSearchMessageHandler());
        bind(CameraAngleMessage.class, new CameraAngleMessageHandler());
        bind(FlagsMessage.class, new FlagsMessageHandler());
        bind(PrivacySettingsUpdateMessage.class, new PrivacySettingsUpdateMessageHandler());
        bind(PrivateChatSentMessage.class, new PrivateChatSentMessageHandler());
        bind(FriendListOperationMessage.class, new FriendListOperationMessageHandler());
        bind(SequenceNumberMessage.class, new SequenceNumberMessageHandler());
        bind(InterfaceClosedMessage.class, new InterfaceClosedMessageHandler());
        bind(SceneRebuiltMessage.class, new SceneRebuiltMessageHandler());
        bind(ScriptInputMessage.class, new ScriptInputMessageHandler());
        bind(GroundItemOptionMessage.class, new GroundItemOptionMessageHandler(groundItemDispatcher));
        bind(ItemOnItemMessage.class, new ItemOnItemMessageHandler(itemOnItemDispatcher));
        bind(MagicOnItemMessage.class, new MagicOnItemMessageHandler());
        bind(ItemOnObjectMessage.class, new ItemOnObjectMessageHandler(itemOnObjectDispatcher));
        bind(ItemOptionMessage.class, new ItemOptionMessageHandler(itemDispatcher));
        bind(ItemOnNPCMessage.class, new ItemOnNPCMessageHandler(itemOnNPCDispatcher));
        bind(ItemExamineMessage.class, new ItemExamineMessageHandler());
        bind(ItemDropMessage.class, new ItemDropMessageHandler());
        bind(ButtonOptionMessage.class, new ButtonOptionMessageHandler(buttonDispatcher));
        bind(ObjectOptionMessage.class, new ObjectOptionMessageHandler(objectDispatcher));
        bind(ObjectExamineMessage.class, new ObjectExamineMessageHandler());
        bind(PlayerOptionMessage.class, new PlayerOptionMessageHandler(playerDispatcher));
        bind(NPCOptionMessage.class, new NPCOptionMessageHandler(npcDispatcher));
        bind(MagicOnNPCMessage.class, new MagicOnNPCMessageHandler(npcDispatcher));
        bind(NPCExamineMessage.class, new NPCExamineMessageHandler());
        bind(InterfaceInputMessage.class, new InterfaceInputMessageHandler());
    }

    public void decorateDispatchers(ScriptContext context) {
        context.decorateButtonDispatcher(buttonDispatcher);
        context.decorateCommandDispatcher(commandDispatcher);
        context.decorateItemOnItemDispatcher(itemOnItemDispatcher);
        context.decorateItemOnObjectDispatcher(itemOnObjectDispatcher);
        context.decorateItemDispatcher(itemDispatcher);
        context.decorateObjectDispatcher(objectDispatcher);
        context.decoratePlayerDispatcher(playerDispatcher);
        context.decorateNPCDispatcher(npcDispatcher);
    }

    public <T extends Message> void bind(Class<T> clazz, MessageHandler<T> handler) {
        handlers.put(clazz, handler);
    }

    @SuppressWarnings("unchecked")
    public void dispatch(Player player, Message message) {
        MessageHandler<Message> handler = (MessageHandler<Message>) handlers.get(message.getClass());
        if (handler != null) {
            try {
                handler.handle(player, message);
            } catch (Throwable t) {
                logger.warn("Error processing packet.", t);
            }
        } else {
            logger.warn("Cannot dispatch message (no handler): " + message.getClass().getName() + ".");
        }
    }

    public ButtonDispatcher getButtonDispatcher() {
        return buttonDispatcher;
    }

    public CommandDispatcher getCommandDispatcher() {
        return commandDispatcher;
    }

    public GroundItemDispatcher getGroundItemDispatcher() {
        return groundItemDispatcher;
    }

    public ItemOnItemDispatcher getItemOnItemDispatcher() {
        return itemOnItemDispatcher;
    }

    public ItemOnObjectDispatcher getItemOnObjectDispatcher() {
        return itemOnObjectDispatcher;
    }

    public ItemOnNPCDispatcher getItemOnNPCDispatcher() {
        return itemOnNPCDispatcher;
    }

    public ItemDispatcher getItemDispatcher() {
        return itemDispatcher;
    }

    public ObjectDispatcher getObjectDispatcher() {
        return objectDispatcher;
    }

    public PlayerDispatcher getPlayerDispatcher() {
        return playerDispatcher;
    }

    public NPCDispatcher getNpcDispatcher() {
        return npcDispatcher;
    }

}
