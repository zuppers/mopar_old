package net.scapeemulator.game.msg.encoder;

import java.util.HashSet;
import java.util.Set;

import io.netty.buffer.ByteBufAllocator;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.msg.MessageEncoder;
import net.scapeemulator.game.msg.impl.RegionConstructMessage;
import net.scapeemulator.game.net.game.DataOrder;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrame.Type;
import net.scapeemulator.game.net.game.GameFrameBuilder;
import net.scapeemulator.game.util.LandscapeKeyTable;

public final class RegionConstructMessageEncoder extends MessageEncoder<RegionConstructMessage> {

    private final LandscapeKeyTable keyTable;

    public RegionConstructMessageEncoder(LandscapeKeyTable keyTable) {
        super(RegionConstructMessage.class);
        this.keyTable = keyTable;
    }

    @Override
    public GameFrame encode(ByteBufAllocator alloc, RegionConstructMessage message) {
        GameFrameBuilder builder = new GameFrameBuilder(alloc, 214, Type.VARIABLE_SHORT);

        Position position = message.getPosition();
        builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, position.getLocalX());
        builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, position.getRegionX());
        builder.put(DataType.BYTE, DataTransformation.SUBTRACT, position.getHeight());
        builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, position.getLocalY());
        // builder.switchToBitAccess();
        for (int height = 0; height < 4; height++) {
            for (int x = 0; x < 13; x++) {
                for (int y = 0; y < 13; y++) {
                    // System.out.print(x + ", " + y + ", " + height + ": ");
                    int hash = message.getPalette().getHash(height, x, y);
                    if (hash != -1) {
                        // builder.putBit(1);
                        // builder.putBits(26, hash);
                        builder.put(DataType.BYTE, 1);
                        builder.put(DataType.INT, hash);

                    } else {
                        // builder.putBit(0);
                        builder.put(DataType.BYTE, 0);

                    }
                }
            }
        }

        // builder.switchToByteAccess();

        Set<Integer> sentKeys = new HashSet<>();

        for (int height = 0; height < 4; height++) {
            for (int x = 0; x < 13; x++) {
                for (int y = 0; y < 13; y++) {
                    int hash = message.getPalette().getHash(height, x, y);
                    if (hash == -1) {
                        continue;
                    }
                    int hashX = (hash >> 14 & 1023) / 8;
                    int hashY = (hash >> 3 & 1023) / 8;
                    int region = hashY + (hashX << 8);
                    if (!sentKeys.add(region)) {
                        continue;
                    }

                    int[] keys = keyTable.getKeys(hashX, hashY);
                    for (int i = 0; i < 4; i++) {
                        builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, keys[i]);
                    }

                }
            }
        }

        builder.put(DataType.SHORT, position.getRegionY());

        return builder.toGameFrame();
    }

}
