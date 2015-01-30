package com.bahj.smelt.plugin.builtin.basegui.tabs;

import javax.swing.JComponent;

import com.bahj.smelt.plugin.builtin.basegui.tabs.event.GUITabEvent;
import com.bahj.smelt.util.event.AbstractEventGenerator;

public class DefaultGUITab extends AbstractEventGenerator<GUITabEvent> implements GUITab {

    private GUITabKey key;
    private String title;
    private JComponent contents;

    public DefaultGUITab(GUITabKey key, String title, JComponent contents) {
        super();
        this.key = key;
        this.title = title;
        this.contents = contents;
    }

    @Override
    public void eventOccurred(GUITabEvent event) {
        fireEvent(event);
    }

    @Override
    public GUITabKey getKey() {
        return key;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public JComponent getContents() {
        return contents;
    }
}
