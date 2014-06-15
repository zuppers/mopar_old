package net.scapeemulator.game.msg.decoder;

import java.io.IOException;

import net.scapeemulator.game.msg.MessageDecoder;
import net.scapeemulator.game.msg.impl.FriendListOperationMessage;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrameReader;

public class FriendListOperationMessageDecoder extends MessageDecoder<FriendListOperationMessage> {

	public FriendListOperationMessageDecoder(int packetId) {
		super(packetId);
	}

	@Override
	public FriendListOperationMessage decode(GameFrame frame) throws IOException {
		GameFrameReader reader = new GameFrameReader(frame);
		long name = reader.getUnsigned(DataType.LONG);
		//reader.getUnsigned(DataType.)
		return new FriendListOperationMessage(opcode, name);
	} 

}
