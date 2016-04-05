package net.scapeemulator.game.tools.npcviewer;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import net.scapeemulator.cache.def.NPCDefinition;
import net.scapeemulator.game.model.definition.NPCDefinitions;

@SuppressWarnings("serial")
public class FilteredNPCJList extends JList<NPCDefinition> {

    private DefaultListModel<NPCDefinition> model;

    public FilteredNPCJList() {
        model = new DefaultListModel<>();
        setModel(model);
        filterReset("");
    }

    public void filterReset(String newFilter) {
        model.clear();
        newFilter = format(newFilter);
        for (NPCDefinition def : NPCDefinitions.getDefinitions()) {
            if (def != null) {
                if (format(def.toString()).contains(newFilter)) {
                    model.addElement(def);
                }
            }
        }
        setModel(model);
    }

    public void filterNarrowed(String newFilter) {
        newFilter = format(newFilter);
        for (int i = 0; i < model.getSize(); i++) {
            if (!format(model.get(i).toString()).contains(newFilter)) {
                model.remove(i--);
            }
        }
        setModel(model);
    }

    private static String format(String s) {
        return s.trim().toLowerCase();
    }

}
