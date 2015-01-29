package com.bahj.smelt.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

/**
 * The wizard for creating a new Smelt project.
 */
public class NewProjectWizard extends Wizard implements INewWizard {
    private static final String PAGE_DESCRIPTION = NewWizardMessages.NewProjectWizard_PageDescription;
    private static final String PAGE_NAME = NewWizardMessages.NewProjectWizard_PageName;
    private static final String WIZARD_NAME = NewWizardMessages.NewProjectWizard_WizardName;
    
    private WizardNewProjectCreationPage pageOne;

    public NewProjectWizard() {
        setWindowTitle(WIZARD_NAME);
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean performFinish() {
        return true;
    }

    @Override
    public void addPages() {
        super.addPages();
        
        // Instantiate the pages used by this wizard.
        pageOne = new WizardNewProjectCreationPage(WIZARD_NAME);
        pageOne.setTitle(PAGE_NAME);
        pageOne.setDescription(PAGE_DESCRIPTION);
        
        addPage(pageOne);
    }
}
