package com.bahj.smelt.wizard;

import org.eclipse.osgi.util.NLS;

public class NewWizardMessages extends NLS {
    private static final String BUNDLE_NAME = "com.bahj.smelt.wizard.messages"; //$NON-NLS-1$
    public static String NewProjectWizard_PageDescription;
    public static String NewProjectWizard_PageName;
    public static String NewProjectWizard_WizardName;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, NewWizardMessages.class);
    }

    private NewWizardMessages() {
    }
}
