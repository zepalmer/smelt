package com.bahj.smelt.util.swing;

import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import com.bahj.smelt.util.FileUtils;

public class FileFilterUtils {
    public static final FileFilter SMELT_DB_FILTER = new SwingFileFilterWrapper("Smelt database", new OrFileFilter(
            new SuffixFileFilter(FileUtils.SMELT_DB_EXTENSION), DirectoryFileFilter.INSTANCE));
    public static final FileFilter SMELT_SPEC_FILTER = new SwingFileFilterWrapper("Smelt specification",
            new OrFileFilter(new SuffixFileFilter(FileUtils.SMELT_SPEC_EXTENSION), DirectoryFileFilter.INSTANCE));
}
