package net.scapeemulator.game.model.player.skills.construction.furniture;

/**
 * @author David Insley
 */
public enum FurnitureInterface {

    NORMAL(394, 3, 91, 106, 110),
    EXTENDED(396, 7, 97, 132, 140);

    private static final int HEIGHT = 4;

    private final int windowId;
    private final int containerId;
    private final int size;
    private final int textOffset;
    private final int levelOffset;
    private final int width;

    private FurnitureInterface(int windowId, int size, int textOffset, int containerId, int levelOffset) {
        this.windowId = windowId;
        this.size = size;
        this.textOffset = textOffset;
        this.containerId = containerId;
        this.levelOffset = levelOffset;
        width = (size / HEIGHT) + 1;
    }

    public int getItemIndex(int index) {
        return (index * width) % size;
    }

    public int getFurnitureIndex(int index) {
        return (index % width == 0) ? (index / width) : HEIGHT + (index / width);
    }

    public int getWindowId() {
        return windowId;
    }

    public int getContainerId() {
        return containerId;
    }

    public int getSize() {
        return size;
    }

    public int getTextOffset() {
        return textOffset;
    }

    public int getLevelOffset() {
        return levelOffset;
    }
}
