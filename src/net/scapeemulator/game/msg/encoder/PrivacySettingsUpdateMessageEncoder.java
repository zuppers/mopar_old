package net.scapeemulator.game.msg.encoder;

import io.netty.buffer.ByteBufAllocator;
import java.io.IOException;
import net.scapeemulator.game.msg.MessageEncoder;
import net.scapeemulator.game.msg.impl.PrivacySettingsUpdateMessage;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameBuilder;

public final class PrivacySettingsUpdateMessageEncoder extends MessageEncoder<PrivacySettingsUpdateMessage> {

	public PrivacySettingsUpdateMessageEncoder() {
		super(PrivacySettingsUpdateMessage.class);
	}

	@Override
	public GameFrame encode(ByteBufAllocator alloc, PrivacySettingsUpdateMessage message) throws IOException {
		GameFrameBuilder builder = new GameFrameBuilder(alloc, 232);
		builder.put(DataType.BYTE, message.getPublicChatSetting());
		builder.put(DataType.BYTE, message.getPrivateChatSetting());
		builder.put(DataType.BYTE, message.getTradeRequestsSetting());
		return builder.toGameFrame();
	}

}
