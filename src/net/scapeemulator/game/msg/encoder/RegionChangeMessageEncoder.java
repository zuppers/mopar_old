package net.scapeemulator.game.msg.encoder;

import io.netty.buffer.ByteBufAllocator;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.msg.MessageEncoder;
import net.scapeemulator.game.msg.impl.RegionChangeMessage;
import net.scapeemulator.game.net.game.DataOrder;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrame;
import net.scapeemulator.game.net.game.GameFrame.Type;
import net.scapeemulator.game.net.game.GameFrameBuilder;
import net.scapeemulator.game.util.LandscapeKeyTable;

public final class RegionChangeMessageEncoder extends MessageEncoder<RegionChangeMessage> {

    private final LandscapeKeyTable table;

    public RegionChangeMessageEncoder(LandscapeKeyTable table) {
        super(RegionChangeMessage.class);
        this.table = table;
    }

    @Override
    public GameFrame encode(ByteBufAllocator alloc, RegionChangeMessage message) {
        GameFrameBuilder builder = new GameFrameBuilder(alloc, 162, Type.VARIABLE_SHORT);

        Position position = message.getPosition();
        builder.put(DataType.SHORT, DataTransformation.ADD, position.getLocalX(position.getRegionX()));

        boolean force = true;
        int centralMapX = position.getRegionX() / 8;
        int centralMapY = position.getRegionY() / 8;

        if ((centralMapX == 48 || centralMapX == 49) && centralMapY == 48)
            force = false;

        if (centralMapX == 48 && centralMapY == 148)
            force = false;

        for (int mapX = ((position.getRegionX() - 6) / 8); mapX <= ((position.getRegionX() + 6) / 8); mapX++) {
            for (int mapY = ((position.getRegionY() - 6) / 8); mapY <= ((position.getRegionY() + 6) / 8); mapY++) {
                if (force || (mapY != 49 && mapY != 149 && mapY != 147 && mapX != 50 && (mapX != 49 || mapY != 47))) {
                    int[] keys = table.getKeys(mapX, mapY);
                    for (int i = 0; i < 4; i++)
                        builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, keys[i]);
                } else {
                    for (int i = 0; i < 4; i++)
                        builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, 0);
                }
            }
        }

        builder.put(DataType.BYTE, DataTransformation.SUBTRACT, position.getHeight() % 4);
        builder.put(DataType.SHORT, position.getRegionX());
        builder.put(DataType.SHORT, DataTransformation.ADD, position.getRegionY());
        builder.put(DataType.SHORT, DataTransformation.ADD, position.getLocalY(position.getRegionY()));
        return builder.toGameFrame();
    }

}
