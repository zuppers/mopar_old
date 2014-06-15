package net.scapeemulator.game.msg.decoder;

import java.io.IOException;

import net.scapeemulator.game.msg.MessageDecoder;
import net.scapeemulator.game.msg.impl.PrivacySettingsUpdateMessage;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameReader;

public class PrivacySettingsUpdateMessageDecoder extends MessageDecoder<PrivacySettingsUpdateMessage> {

	public PrivacySettingsUpdateMessageDecoder() {
		super(157);
	}

	@Override
	public PrivacySettingsUpdateMessage decode(GameFrame frame) throws IOException {
		GameFrameReader reader = new GameFrameReader(frame);
		int publicChat = (int) reader.getUnsigned(DataType.BYTE);
		int privateChat = (int) reader.getUnsigned(DataType.BYTE);
		int trade = (int) reader.getUnsigned(DataType.BYTE);
		return new PrivacySettingsUpdateMessage(publicChat, privateChat, trade);
	} 

}
