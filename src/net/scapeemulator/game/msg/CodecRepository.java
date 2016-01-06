package net.scapeemulator.game.msg;

import java.util.HashMap;
import java.util.Map;

import net.scapeemulator.game.model.ExtendedOption;
import net.scapeemulator.game.msg.decoder.CameraAngleMessageDecoder;
import net.scapeemulator.game.msg.decoder.ChatMessageDecoder;
import net.scapeemulator.game.msg.decoder.ClickMessageDecoder;
import net.scapeemulator.game.msg.decoder.CommandMessageDecoder;
import net.scapeemulator.game.msg.decoder.DisplayMessageDecoder;
import net.scapeemulator.game.msg.decoder.FlagsMessageDecoder;
import net.scapeemulator.game.msg.decoder.FocusMessageDecoder;
import net.scapeemulator.game.msg.decoder.FriendListOperationMessageDecoder;
import net.scapeemulator.game.msg.decoder.GrandExchangeSearchMessageDecoder;
import net.scapeemulator.game.msg.decoder.IdleLogoutMessageDecoder;
import net.scapeemulator.game.msg.decoder.IntegerScriptInputMessageDecoder;
import net.scapeemulator.game.msg.decoder.PingMessageDecoder;
import net.scapeemulator.game.msg.decoder.PrivacySettingsUpdateMessageDecoder;
import net.scapeemulator.game.msg.decoder.PrivateChatSentMessageDecoder;
import net.scapeemulator.game.msg.decoder.RegionChangedMessageDecoder;
import net.scapeemulator.game.msg.decoder.SceneRebuiltMessageDecoder;
import net.scapeemulator.game.msg.decoder.SequenceNumberMessageDecoder;
import net.scapeemulator.game.msg.decoder.UsernameScriptInputMessageDecoder;
import net.scapeemulator.game.msg.decoder.WalkMessageDecoder;
import net.scapeemulator.game.msg.decoder.button.ButtonOptionMessageDecoder;
import net.scapeemulator.game.msg.decoder.button.OldButtonMessageDecoder;
import net.scapeemulator.game.msg.decoder.grounditem.GroundItemOptionThreeMessageDecoder;
import net.scapeemulator.game.msg.decoder.grounditem.GroundItemOptionFourMessageDecoder;
import net.scapeemulator.game.msg.decoder.inter.InterfaceClosedMessageDecoder;
import net.scapeemulator.game.msg.decoder.inter.InterfaceInputMessageDecoder;
import net.scapeemulator.game.msg.decoder.item.ItemDropMessageDecoder;
import net.scapeemulator.game.msg.decoder.item.ItemExamineMessageDecoder;
import net.scapeemulator.game.msg.decoder.item.ItemOnGroundItemMessageDecoder;
import net.scapeemulator.game.msg.decoder.item.ItemOnItemMessageDecoder;
import net.scapeemulator.game.msg.decoder.item.ItemOnNPCMessageDecoder;
import net.scapeemulator.game.msg.decoder.item.ItemOnObjectMessageDecoder;
import net.scapeemulator.game.msg.decoder.item.ItemOptionFourMessageDecoder;
import net.scapeemulator.game.msg.decoder.item.ItemOptionOneMessageDecoder;
import net.scapeemulator.game.msg.decoder.item.ItemOptionTwoMessageDecoder;
import net.scapeemulator.game.msg.decoder.item.MagicOnItemMessageDecoder;
import net.scapeemulator.game.msg.decoder.item.RemoveItemMessageDecoder;
import net.scapeemulator.game.msg.decoder.item.SwapItemsMessageDecoder;
import net.scapeemulator.game.msg.decoder.item.SwapItemsTwoMessageDecoder;
import net.scapeemulator.game.msg.decoder.npc.MagicOnNPCMessageDecoder;
import net.scapeemulator.game.msg.decoder.npc.NPCExamineMessageDecoder;
import net.scapeemulator.game.msg.decoder.npc.NPCOptionTwoMessageDecoder;
import net.scapeemulator.game.msg.decoder.npc.NPCOptionOneMessageDecoder;
import net.scapeemulator.game.msg.decoder.npc.NPCOptionThreeMessageDecoder;
import net.scapeemulator.game.msg.decoder.object.ObjectExamineMessageDecoder;
import net.scapeemulator.game.msg.decoder.object.ObjectOptionFourMessageDecoder;
import net.scapeemulator.game.msg.decoder.object.ObjectOptionOneMessageDecoder;
import net.scapeemulator.game.msg.decoder.object.ObjectOptionThreeMessageDecoder;
import net.scapeemulator.game.msg.decoder.object.ObjectOptionTwoMessageDecoder;
import net.scapeemulator.game.msg.decoder.object.ObjectOptionFiveMessageDecoder;
import net.scapeemulator.game.msg.decoder.player.PlayerOptionFourMessageDecoder;
import net.scapeemulator.game.msg.decoder.player.PlayerOptionThreeMessageDecoder;
import net.scapeemulator.game.msg.encoder.CameraAngleMessageEncoder;
import net.scapeemulator.game.msg.encoder.CameraFaceMessageEncoder;
import net.scapeemulator.game.msg.encoder.CameraMoveMessageEncoder;
import net.scapeemulator.game.msg.encoder.CameraResetMessageEncoder;
import net.scapeemulator.game.msg.encoder.ConfigMessageEncoder;
import net.scapeemulator.game.msg.encoder.CreateProjectileMessageEncoder;
import net.scapeemulator.game.msg.encoder.EnergyMessageEncoder;
import net.scapeemulator.game.msg.encoder.FriendStatusMessageEncoder;
import net.scapeemulator.game.msg.encoder.FriendsListStatusMessageEncoder;
import net.scapeemulator.game.msg.encoder.GrandExchangeUpdateMessageEncoder;
import net.scapeemulator.game.msg.encoder.GroundItemCreateMessageEncoder;
import net.scapeemulator.game.msg.encoder.GroundItemRemoveMessageEncoder;
import net.scapeemulator.game.msg.encoder.GroundItemUpdateMessageEncoder;
import net.scapeemulator.game.msg.encoder.GroundObjectAnimateMessageEncoder;
import net.scapeemulator.game.msg.encoder.GroundObjectRemoveMessageEncoder;
import net.scapeemulator.game.msg.encoder.GroundObjectUpdateMessageEncoder;
import net.scapeemulator.game.msg.encoder.RegionConstructMessageEncoder;
import net.scapeemulator.game.msg.encoder.RenameTileActionMessageEncoder;
import net.scapeemulator.game.msg.encoder.InterfaceAccessMessageEncoder;
import net.scapeemulator.game.msg.encoder.InterfaceAnimationMessageEncoder;
import net.scapeemulator.game.msg.encoder.InterfaceCloseMessageEncoder;
import net.scapeemulator.game.msg.encoder.InterfaceItemMessageEncoder;
import net.scapeemulator.game.msg.encoder.InterfaceItemsMessageEncoder;
import net.scapeemulator.game.msg.encoder.InterfaceNPCHeadMessageEncoder;
import net.scapeemulator.game.msg.encoder.InterfaceOpenMessageEncoder;
import net.scapeemulator.game.msg.encoder.InterfacePlayerHeadMessageEncoder;
import net.scapeemulator.game.msg.encoder.InterfaceResetItemsMessageEncoder;
import net.scapeemulator.game.msg.encoder.InterfaceRootMessageEncoder;
import net.scapeemulator.game.msg.encoder.InterfaceSlottedItemsMessageEncoder;
import net.scapeemulator.game.msg.encoder.InterfaceTextMessageEncoder;
import net.scapeemulator.game.msg.encoder.InterfaceVisibleMessageEncoder;
import net.scapeemulator.game.msg.encoder.LogoutMessageEncoder;
import net.scapeemulator.game.msg.encoder.NpcUpdateMessageEncoder;
import net.scapeemulator.game.msg.encoder.PlacementCoordsMessageEncoder;
import net.scapeemulator.game.msg.encoder.PlayerOptionMessageEncoder;
import net.scapeemulator.game.msg.encoder.PlayerUpdateMessageEncoder;
import net.scapeemulator.game.msg.encoder.PrivacySettingsUpdateMessageEncoder;
import net.scapeemulator.game.msg.encoder.PrivateChatReceivedMessageEncoder;
import net.scapeemulator.game.msg.encoder.PrivateChatSentMessageEncoder;
import net.scapeemulator.game.msg.encoder.RegionChangeMessageEncoder;
import net.scapeemulator.game.msg.encoder.ResetMinimapFlagMessageEncoder;
import net.scapeemulator.game.msg.encoder.ScriptIntMessageEncoder;
import net.scapeemulator.game.msg.encoder.ScriptMessageEncoder;
import net.scapeemulator.game.msg.encoder.ScriptStringMessageEncoder;
import net.scapeemulator.game.msg.encoder.SendIgnoresMessageEncoder;
import net.scapeemulator.game.msg.encoder.ServerMessageEncoder;
import net.scapeemulator.game.msg.encoder.SetPositionMessageEncoder;
import net.scapeemulator.game.msg.encoder.SkillMessageEncoder;
import net.scapeemulator.game.msg.encoder.MinimapUpdateMessageEncoder;
import net.scapeemulator.game.msg.encoder.VarbitMessageEncoder;
import net.scapeemulator.game.util.LandscapeKeyTable;

public final class CodecRepository {

    private final MessageDecoder<?>[] inCodecs = new MessageDecoder<?>[256];
    private final Map<Class<?>, MessageEncoder<?>> outCodecs = new HashMap<>();

    public CodecRepository(LandscapeKeyTable table) {
        /* decoders */
        bind(new PingMessageDecoder());
        bind(new IdleLogoutMessageDecoder());
        bind(new OldButtonMessageDecoder());
        bind(new WalkMessageDecoder(39));
        bind(new WalkMessageDecoder(77));
        bind(new WalkMessageDecoder(215));
        bind(new ChatMessageDecoder());
        bind(new CommandMessageDecoder());
        bind(new DisplayMessageDecoder());
        bind(new RemoveItemMessageDecoder());
        bind(new RegionChangedMessageDecoder());
        bind(new ClickMessageDecoder());
        bind(new FocusMessageDecoder());
        bind(new CameraAngleMessageDecoder());
        bind(new FlagsMessageDecoder());
        bind(new SequenceNumberMessageDecoder());
        bind(new InterfaceClosedMessageDecoder());
        bind(new IntegerScriptInputMessageDecoder());
        bind(new UsernameScriptInputMessageDecoder());
        bind(new ObjectOptionOneMessageDecoder());
        bind(new ObjectOptionTwoMessageDecoder());
        bind(new ObjectOptionThreeMessageDecoder());
        bind(new ObjectOptionFourMessageDecoder());
        bind(new ObjectOptionFiveMessageDecoder());
        bind(new ObjectExamineMessageDecoder());
        bind(new SceneRebuiltMessageDecoder());
        bind(new PrivacySettingsUpdateMessageDecoder());
        bind(new FriendListOperationMessageDecoder(34));
        bind(new FriendListOperationMessageDecoder(57));
        bind(new FriendListOperationMessageDecoder(120));
        bind(new FriendListOperationMessageDecoder(213));
        bind(new PrivateChatSentMessageDecoder());
        bind(new GroundItemOptionThreeMessageDecoder());
        bind(new GroundItemOptionFourMessageDecoder());
        bind(new GrandExchangeSearchMessageDecoder());

        /* Bind all the item decoders */
        bind(new ItemOptionOneMessageDecoder());
        bind(new ItemOptionTwoMessageDecoder());
        bind(new ItemOptionFourMessageDecoder());
        bind(new ItemExamineMessageDecoder());
        bind(new ItemOnNPCMessageDecoder());
        bind(new ItemDropMessageDecoder());
        bind(new SwapItemsMessageDecoder());
        bind(new SwapItemsTwoMessageDecoder());
        bind(new ItemOnItemMessageDecoder());
        bind(new ItemOnObjectMessageDecoder());
        bind(new ItemOnGroundItemMessageDecoder());
        bind(new MagicOnItemMessageDecoder());

        /* Bind all the button option decoders */
        bind(new ButtonOptionMessageDecoder(ExtendedOption.ONE, 155));
        bind(new ButtonOptionMessageDecoder(ExtendedOption.TWO, 196));
        bind(new ButtonOptionMessageDecoder(ExtendedOption.THREE, 124));
        bind(new ButtonOptionMessageDecoder(ExtendedOption.FOUR, 199));
        bind(new ButtonOptionMessageDecoder(ExtendedOption.FIVE, 234));
        bind(new ButtonOptionMessageDecoder(ExtendedOption.SIX, 168));
        bind(new ButtonOptionMessageDecoder(ExtendedOption.SEVEN, 166));
        bind(new ButtonOptionMessageDecoder(ExtendedOption.EIGHT, 64));
        bind(new ButtonOptionMessageDecoder(ExtendedOption.NINE, 9));

        bind(new InterfaceInputMessageDecoder());

        bind(new PlayerOptionFourMessageDecoder());
        bind(new PlayerOptionThreeMessageDecoder());

        bind(new NPCOptionOneMessageDecoder());
        bind(new NPCOptionThreeMessageDecoder());
        bind(new NPCExamineMessageDecoder());
        bind(new NPCOptionTwoMessageDecoder());
        bind(new MagicOnNPCMessageDecoder());

        /* encoders */
        bind(new SetPositionMessageEncoder());
        
        bind(new RegionChangeMessageEncoder(table));
        bind(new RegionConstructMessageEncoder(table));

        bind(new InterfaceRootMessageEncoder());
        bind(new InterfaceOpenMessageEncoder());
        bind(new InterfaceCloseMessageEncoder());
        bind(new InterfaceVisibleMessageEncoder());
        bind(new InterfaceItemMessageEncoder());
        bind(new InterfaceTextMessageEncoder());
        bind(new InterfaceItemsMessageEncoder());
        bind(new InterfaceSlottedItemsMessageEncoder());
        bind(new InterfaceResetItemsMessageEncoder());
        bind(new InterfaceAccessMessageEncoder());
        bind(new InterfacePlayerHeadMessageEncoder());
        bind(new InterfaceNPCHeadMessageEncoder());
        bind(new InterfaceAnimationMessageEncoder());
        bind(new MinimapUpdateMessageEncoder());

        bind(new ServerMessageEncoder());
        bind(new RenameTileActionMessageEncoder());
        bind(new LogoutMessageEncoder());

        bind(new CameraMoveMessageEncoder());
        bind(new CameraAngleMessageEncoder());
        bind(new CameraResetMessageEncoder());
        bind(new CameraFaceMessageEncoder());

        bind(new PlayerUpdateMessageEncoder());
        bind(new PrivacySettingsUpdateMessageEncoder());
        bind(new PrivateChatSentMessageEncoder());
        bind(new PrivateChatReceivedMessageEncoder());
        bind(new SkillMessageEncoder());
        bind(new EnergyMessageEncoder());
        bind(new ResetMinimapFlagMessageEncoder());
        bind(new ConfigMessageEncoder());
        bind(new VarbitMessageEncoder());
        bind(new ScriptMessageEncoder());
        bind(new NpcUpdateMessageEncoder());

        bind(new ScriptStringMessageEncoder());
        bind(new ScriptIntMessageEncoder());

        bind(new PlacementCoordsMessageEncoder());

        bind(new GroundItemCreateMessageEncoder());
        bind(new GroundItemUpdateMessageEncoder());
        bind(new GroundItemRemoveMessageEncoder());

        bind(new GroundObjectUpdateMessageEncoder());
        bind(new GroundObjectRemoveMessageEncoder());
        bind(new GroundObjectAnimateMessageEncoder());
        bind(new PlayerOptionMessageEncoder());

        bind(new CreateProjectileMessageEncoder());
        bind(new GrandExchangeUpdateMessageEncoder());
        bind(new SendIgnoresMessageEncoder());
        bind(new FriendStatusMessageEncoder());
        bind(new FriendsListStatusMessageEncoder());
    }

    public MessageDecoder<?> get(int opcode) {
        return inCodecs[opcode];
    }

    @SuppressWarnings("unchecked")
    public <T extends Message> MessageEncoder<T> get(Class<T> clazz) {
        return (MessageEncoder<T>) outCodecs.get(clazz);
    }

    public void bind(MessageDecoder<?> decoder) {
        inCodecs[decoder.opcode] = decoder;
    }

    public void bind(MessageEncoder<?> encoder) {
        outCodecs.put(encoder.clazz, encoder);
    }
}
