package net.scapeemulator.game.msg.impl.object;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.msg.Message;

/**
 * @author Hadyn Richard
 */
public final class GroundObjectAnimateMessage extends Message {

    private final Position position;
    private final int animationId, type, rotation;

    public GroundObjectAnimateMessage(Position position, int animationId, int type, int rotation) {
        this.position = position;
        this.animationId = animationId;
        this.type = type;
        this.rotation = rotation;
    }

    public Position getPosition() {
        return position;
    }

    public int getAnimationId() {
        return animationId;
    }

    public int infoHash() {
        return type << 2 | (rotation & 0x3);
    }
}
