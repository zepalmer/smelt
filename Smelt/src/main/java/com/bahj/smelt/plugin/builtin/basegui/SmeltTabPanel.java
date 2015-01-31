package com.bahj.smelt.plugin.builtin.basegui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.bahj.smelt.plugin.builtin.basegui.tabs.GUITab;
import com.bahj.smelt.plugin.builtin.basegui.tabs.GUITabKey;
import com.bahj.smelt.plugin.builtin.basegui.tabs.event.GUITabClosedEvent;
import com.bahj.smelt.plugin.builtin.basegui.tabs.event.GUITabClosingEvent;

public class SmeltTabPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private List<GUITab> tabs;
    private JTabbedPane tabbedPane;

    public SmeltTabPanel() {
        this.setMinimumSize(new Dimension(400, 300));

        this.tabs = new ArrayList<>();

        this.tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        this.setLayout(new BorderLayout());
        this.add(this.tabbedPane, BorderLayout.CENTER);

    }

    /**
     * Ensures that a tab is present in the GUI. If a tab with the same key already exists, it is focused. Otherwise, a
     * new tab is added.
     * 
     * @param tab
     *            The tab to ensure.
     */
    public void ensureTab(GUITab tab) {
        for (int tabIndex = 0; tabIndex < this.tabs.size(); tabIndex++) {
            GUITab otherTab = this.tabs.get(tabIndex);
            if (otherTab.getKey().equals(tab.getKey())) {
                this.tabbedPane.setSelectedIndex(tabIndex);
                return;
            }
        }

        // Create a new tab containing the provided contents.
        this.tabbedPane.insertTab(tab.getTitle(), null, tab.getContents(), null, this.tabbedPane.getTabCount());

        // Change the header for the tab so that it has a closing button.
        this.tabbedPane.setTabComponentAt(this.tabbedPane.getTabCount() - 1, new TabComponent(tab));
    }

    /**
     * Closes a tab in this tab pane by its key. If no such tab exists, this does nothing.
     * 
     * @param key
     *            The key to use.
     * @param vetoable
     *            <code>true</code> if event recipients should be able to cancel this close; <code>false</code> if they
     *            should not.
     */
    public void closeTab(GUITabKey key, boolean vetoable) {
        for (int tabIndex = 0; tabIndex < this.tabs.size(); tabIndex++) {
            GUITab tab = this.tabs.get(tabIndex);
            if (tab.getKey().equals(key)) {
                GUITabClosingEvent closingEvent = new GUITabClosingEvent(tab, vetoable);
                tab.eventOccurred(closingEvent);
                if (!closingEvent.isVetoed()) {
                    this.tabs.remove(tabIndex);
                    this.tabbedPane.removeTabAt(tabIndex);
                    GUITabClosedEvent closedEvent = new GUITabClosedEvent(tab);
                    tab.eventOccurred(closedEvent);
                }
                return;
            }
        }
    }

    private class TabComponent extends JComponent {
        private static final long serialVersionUID = 1L;

        private GUITab tab;

        public TabComponent(GUITab tab) {
            super();
            this.tab = tab;

            JButton closeButton = new JButton();

            this.setLayout(new BorderLayout());
            this.add(new JLabel(tab.getTitle()), BorderLayout.CENTER);
            this.add(closeButton, BorderLayout.EAST);

            // TODO: icon instead
            closeButton.setText("x");

            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    closeTab(TabComponent.this.tab.getKey(), true);
                }
            });
        }
    }
}
