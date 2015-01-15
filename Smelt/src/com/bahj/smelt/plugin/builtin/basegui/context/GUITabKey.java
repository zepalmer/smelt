package com.bahj.smelt.plugin.builtin.basegui.context;

/**
 * An interface used by objects meant to identify GUI tabs in the Smelt GUI.  The uniqueness of this key
 * is used to prevent the opening of duplicate tabs.
 * @author Zachary Palmer
 */
public interface GUITabKey {
    public String getTitle();
}
