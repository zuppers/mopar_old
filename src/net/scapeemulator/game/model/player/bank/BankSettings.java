package net.scapeemulator.game.model.player.bank;

import static net.scapeemulator.game.model.player.bank.BankTab.TAB_ALL;
import static net.scapeemulator.game.model.player.bank.BankTab.TAB_NINE;
import static net.scapeemulator.game.model.player.bank.BankTab.TAB_TWO;

public class BankSettings {

    private BankTab openTab = TAB_ALL;
    private int[] tabStarts;
    private int lastX;

    public BankSettings() {
        tabStarts = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        lastX = 1;
    }

    public BankTab getOpenTab() {
        return openTab;
    }

    public void setOpenTab(BankTab openTab) {
        this.openTab = openTab;
    }

    public void setTabStarts(int[] tabStarts) {
        if (tabStarts.length != this.tabStarts.length) {
            throw new IllegalArgumentException("illegal amount of tab start ids for bank settings");
        }
        this.tabStarts = tabStarts;
    }

    public void setLastX(int lastX) {
        this.lastX = lastX;
    }

    public int getLastX() {
        return lastX;
    }

    BankTab getTab(int index) {
        for (int i = 1; i < tabStarts.length; i++) {
            if (tabStarts[i] > index) {
                return BankTab.forIndex(i - 1);
            }
        }
        return TAB_ALL;
    }

    void incrementTabStarts(BankTab from) {
        if (from == TAB_ALL) {
            return;
        }
        for (int i = from.index() + 1; i < tabStarts.length; i++) {
            tabStarts[i]++;
        }
    }

    void decrementTabStarts(BankTab from) {
        if (from == TAB_ALL) {
            return;
        }
        for (int i = from.index() + 1; i < tabStarts.length; i++) {
            tabStarts[i]--;
        }
        if (getTabSize(from) == 0) {
            for (int i = from.index(); i < TAB_NINE.index(); i++) {
                tabStarts[i] = tabStarts[i + 1];
            }
            tabStarts[TAB_NINE.index()] = tabStarts[TAB_ALL.index()];
        }
    }

    BankTab createTab() {
        for (int tab = TAB_TWO.index(); tab < TAB_ALL.index(); tab++) {
            if (tabStarts[tab] == tabStarts[TAB_ALL.index()]) {
                tabStarts[tab] = (tab == TAB_TWO.index()) ? 0 : tabStarts[tab - 1] + getTabSize(BankTab.forIndex(tab - 1));
                return BankTab.forIndex(tab);
            }
        }
        return null;
    }

    int getTabStart(BankTab tab) {
        return tabStarts[tab.index()];
    }

    int getTabSize(BankTab tab) {
        if (tab == TAB_ALL) {
            throw new IndexOutOfBoundsException("Cannot get tab size for main tab");
        }
        return tabStarts[tab.index() + 1] - tabStarts[tab.index()];
    }

}
