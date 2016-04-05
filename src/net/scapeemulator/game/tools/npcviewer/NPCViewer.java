/**
 * 
 */
package net.scapeemulator.game.tools.npcviewer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.scapeemulator.cache.Cache;
import net.scapeemulator.cache.FileStore;
import net.scapeemulator.game.model.definition.NPCDefinitions;
import net.scapeemulator.game.tools.dropeditor.FilteredNPCJList;

/**
 * @author David Insley
 */
public class NPCViewer {

    private static final String CACHE_PATH = "data/game/cache";

    private JFrame frame;
    private JTextField namefilterField;
    private JTextField cbFilterField;
    private FilteredNPCJList npcList;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Cache cache = new Cache(FileStore.open(CACHE_PATH));
                    NPCViewer app = new NPCViewer(cache);
                    app.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    NPCViewer(Cache cache) {
        try {
            NPCDefinitions.init(cache);
        } catch (Exception e) {
            System.exit(1);
        }

        init();
    }

    private void init() {
        frame = new JFrame("NPC Viewer");
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 350, 500);
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        frame.getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setLayout(new BorderLayout(5, 10));

        JPanel filtersPanel = new JPanel();
        filtersPanel.setLayout(new BorderLayout());
        namefilterField = new JTextField();
        FilterListener listener = new FilterListener();
        namefilterField.getDocument().addDocumentListener(listener);
        cbFilterField = new JTextField();
        cbFilterField.getDocument().addDocumentListener(listener);
        filtersPanel.add(namefilterField, BorderLayout.NORTH);
        filtersPanel.add(cbFilterField, BorderLayout.SOUTH);
        contentPanel.add(filtersPanel, BorderLayout.NORTH);

        npcList = new FilteredNPCJList();
        npcList.setListData(NPCDefinitions.getDefinitions());

        JScrollPane scrollPane = new JScrollPane(npcList);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private class FilterListener implements DocumentListener {

        @Override
        public void changedUpdate(DocumentEvent de) {
        }

        @Override
        public void insertUpdate(DocumentEvent de) {
            npcList.filterNarrowed(namefilterField.getText(), Integer.parseInt("0" + cbFilterField.getText()));
        }

        @Override
        public void removeUpdate(DocumentEvent arg0) {
            npcList.filterReset(namefilterField.getText(), Integer.parseInt("0" + cbFilterField.getText()));
        }
    }
}
