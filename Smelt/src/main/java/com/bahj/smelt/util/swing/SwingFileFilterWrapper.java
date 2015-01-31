package com.bahj.smelt.util.swing;

import java.io.File;
import java.io.FileFilter;

/**
 * A Swing {@link javax.swing.filechooser.FileFilter} which adapts a backing {@link java.io.FileFilter} to do its work.
 * 
 * @author Zachary Palmer
 */
public class SwingFileFilterWrapper extends javax.swing.filechooser.FileFilter {
    private java.io.FileFilter filter;
    private String description;

    public SwingFileFilterWrapper(String description, FileFilter filter) {
        super();
        this.filter = filter;
        this.description = description;
    }

    public java.io.FileFilter getFilter() {
        return filter;
    }

    @Override
    public boolean accept(File f) {
        return this.filter.accept(f);
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
