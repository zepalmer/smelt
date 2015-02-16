package com.bahj.smelt.util.swing;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * A Swing {@link Action} which simply accepts a consumer to execute when the action is triggered.
 * 
 * @author Zachary Palmer
 */
public class BasicAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    private Consumer<ActionEvent> consumer;

    public BasicAction(Consumer<ActionEvent> Consumer) {
        super();
        this.consumer = Consumer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.consumer.accept(e);
    }
}
