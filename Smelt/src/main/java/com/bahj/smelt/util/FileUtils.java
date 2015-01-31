package com.bahj.smelt.util;

import java.io.File;

import com.bahj.smelt.configuration.Configuration;

public class FileUtils {
    /** The file containing the serialized Smelt {@link Configuration}. */
    public static final File CONFIGURATION_FILE = FileUtils.getConfigFile("smelt.cfg");
    
    /** The file extension for Smelt specification files.  (Does not include the leading dot.) */
    public static final String SMELT_SPEC_EXTENSION = "spec.smelt";
    
    /**
     * Creates a {@link File} object representing the location of a Smelt configuration file.
     * 
     * @param baseName
     *            The base name of the file in question.
     * @return The location for the Smelt configuration file with that base name.
     */
    public static final File getConfigFile(String baseName) {
        return new File(System.getProperty("user.home") + File.separatorChar + ".smelt" + File.separatorChar + baseName);
    }
}
