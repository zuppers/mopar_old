package net.scapeemulator.game.msg;

import java.util.HashMap;
import java.util.Map;

import net.scapeemulator.game.button.ButtonDispatcher;
import net.scapeemulator.game.command.CommandDispatcher;
import net.scapeemulator.game.grounditem.GroundItemDispatcher;
import net.scapeemulator.game.item.ItemDispatcher;
import net.scapeemulator.game.item.ItemInteractDispatcher;
import net.scapeemulator.game.item.ItemOnItemDispatcher;
import net.scapeemulator.game.item.ItemOnObjectDispatcher;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.handler.*;
import net.scapeemulator.game.msg.handler.inter.*;
import net.scapeemulator.game.msg.handler.item.*;
import net.scapeemulator.game.msg.handler.npc.MagicOnNPCMessageHandler;
import net.scapeemulator.game.msg.handler.npc.NPCExamineMessageHandler;
import net.scapeemulator.game.msg.handler.npc.NPCInteractMessageHandler;
import net.scapeemulator.game.msg.handler.npc.NPCOptionMessageHandler;
import net.scapeemulator.game.msg.handler.object.ObjectExamineMessageHandler;
import net.scapeemulator.game.msg.handler.object.ObjectOptionMessageHandler;
import net.scapeemulator.game.msg.impl.*;
import net.scapeemulator.game.msg.impl.button.ButtonOptionMessage;
import net.scapeemulator.game.msg.impl.grounditem.GroundItemOptionMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceClosedMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceInputMessage;
import net.scapeemulator.game.msg.impl.item.ItemDropMessage;
import net.scapeemulator.game.msg.impl.item.ItemExamineMessage;
import net.scapeemulator.game.msg.impl.item.ItemInteractMessage;
import net.scapeemulator.game.msg.impl.item.ItemOnItemMessage;
import net.scapeemulator.game.msg.impl.item.ItemOnObjectMessage;
import net.scapeemulator.game.msg.impl.item.ItemOptionMessage;
import net.scapeemulator.game.msg.impl.npc.MagicOnNPCMessage;
import net.scapeemulator.game.msg.impl.npc.NPCExamineMessage;
import net.scapeemulator.game.msg.impl.npc.NPCInteractMessage;
import net.scapeemulator.game.msg.impl.npc.NPCOptionMessage;
import net.scapeemulator.game.msg.impl.object.ObjectExamineMessage;
import net.scapeemulator.game.msg.impl.object.ObjectOptionMessage;
import net.scapeemulator.game.msg.impl.player.PlayerOptionMessage;
import net.scapeemulator.game.npc.NPCDispatcher;
import net.scapeemulator.game.object.ObjectDispatcher;
import net.scapeemulator.game.player.PlayerDispatcher;
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
    private final ItemInteractDispatcher itemInteractDispatcher = new ItemInteractDispatcher();
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
        bind(CameraMessage.class, new CameraMessageHandler());
        bind(FlagsMessage.class, new FlagsMessageHandler());
        bind(PrivacySettingsUpdateMessage.class, new PrivacySettingsUpdateMessageHandler());
        bind(PrivateChatSentMessage.class, new PrivateChatSentMessageHandler());
        bind(FriendListOperationMessage.class, new FriendListOperationMessageHandler());
        bind(SequenceNumberMessage.class, new SequenceNumberMessageHandler());
        bind(InterfaceClosedMessage.class, new InterfaceClosedMessageHandler());
        bind(SceneRebuiltMessage.class, new SceneRebuiltMessageHandler());
        bind(IntegerScriptInputMessage.class, new IntegerScriptInputMessageHandler());
        bind(GroundItemOptionMessage.class, new GroundItemOptionMessageHandler(groundItemDispatcher));
        bind(ItemOnItemMessage.class, new ItemOnItemMessageHandler(itemOnItemDispatcher));
        bind(ItemOnObjectMessage.class, new ItemOnObjectMessageHandler(itemOnObjectDispatcher));
        bind(ItemOptionMessage.class, new ItemOptionMessageHandler(itemDispatcher));
        bind(ItemInteractMessage.class, new ItemInteractMessageHandler(itemInteractDispatcher));
        bind(ItemExamineMessage.class, new ItemExamineMessageHandler());
        bind(ItemDropMessage.class, new ItemDropMessageHandler());
        bind(ButtonOptionMessage.class, new ButtonOptionMessageHandler(buttonDispatcher));
        bind(ObjectOptionMessage.class, new ObjectOptionMessageHandler(objectDispatcher));
        bind(ObjectExamineMessage.class, new ObjectExamineMessageHandler());
        bind(PlayerOptionMessage.class, new PlayerOptionMessageHandler(playerDispatcher));
        bind(NPCOptionMessage.class, new NPCOptionMessageHandler(npcDispatcher));
        bind(NPCInteractMessage.class, new NPCInteractMessageHandler(npcDispatcher));
        bind(MagicOnNPCMessage.class, new MagicOnNPCMessageHandler(npcDispatcher));
        bind(NPCExamineMessage.class, new NPCExamineMessageHandler());
        bind(InterfaceInputMessage.class, new InterfaceInputMessageHandler());
        //bind();
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
    
    public void purge() {
        buttonDispatcher.unbindAll();
        commandDispatcher.unbindAll();
        itemOnItemDispatcher.unbindAll();
        itemOnObjectDispatcher.unbindAll();
        itemDispatcher.unbindAll();
        objectDispatcher.unbindAll();
        playerDispatcher.unbindAll();
        npcDispatcher.unbindAll();
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
}
