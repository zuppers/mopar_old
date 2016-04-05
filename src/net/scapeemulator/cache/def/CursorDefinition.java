package net.scapeemulator.cache.def;


import java.nio.ByteBuffer;

/**
 * @author David Insley
 */
public final class CursorDefinition {

    private int hotspotX;
    private int hotspotY;
    private int textureId;

    public static CursorDefinition decode(ByteBuffer buffer) {
        CursorDefinition def = new CursorDefinition();
        while (true) {
            int opcode = buffer.get() & 0xFF;
            if (opcode == 0)
                break;
            if (opcode == 1) {
                def.textureId = buffer.getShort() & 0xFFFF;
            } else if (opcode == 2) {
                def.hotspotX = buffer.get() & 0xFF;
                def.hotspotY = buffer.get() & 0xFF;
            }
        }
        return def;
    }
    
    public int getHotspotX() {
        return hotspotX;
    }

    public int getHotspotY() {
        return hotspotY;
    }

    public int getTextureId() {
        return textureId;
    }
    
}
