package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.player.RegionPalette;
import net.scapeemulator.game.msg.Message;

public final class RegionConstructMessage extends Message {

    private final Position position;
    private final RegionPalette palette;

    public RegionConstructMessage(Position position, RegionPalette palette) {
        this.position = position;
        this.palette = palette;
    }

    public Position getPosition() {
        return position;
    }

    public RegionPalette getPalette() {
        return palette;
    }

}
