package com.bahj.smelt.wizards;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "com.bahj.smelt.wizards.messages"; //$NON-NLS-1$
    public static String NewProjectWizard_Description;
    public static String NewProjectWizard_Name;
    public static String NewProjectWizard_Title;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
