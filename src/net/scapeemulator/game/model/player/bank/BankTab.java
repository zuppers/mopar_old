package net.scapeemulator.game.model.player.bank;

/**
 * @author David Insley
 */
public enum BankTab {
    
    TAB_ALL(8, 1, 41),
    TAB_TWO(0, 2, 39),
    TAB_THREE(1, 3, 37),
    TAB_FOUR(2, 4, 35),
    TAB_FIVE(3, 5, 33),
    TAB_SIX(4, 6, 31),
    TAB_SEVEN(5, 7, 29),
    TAB_EIGHT(6, 8, 27),
    TAB_NINE(7, 9, 25);

    private final int index;
    private final int tabId;
    private final int childId;

    private BankTab(int index, int tabId, int childId) {
        this.index = index;
        this.tabId = tabId;
        this.childId = childId;
    }

    public int index() {
        return index;
    }
    
    public int getTabId() {
        return tabId;
    }

    public int getChildId() {
        return childId;
    }

    public static BankTab forIndex(int index) {
        for (BankTab tab : values()) {
            if (tab.index == index) {
                return tab;
            }
        }
        return null;
    }
    
    public static BankTab forChildId(int childId) {
        for (BankTab tab : values()) {
            if (tab.childId == childId) {
                return tab;
            }
        }
        return null;
    }
}
