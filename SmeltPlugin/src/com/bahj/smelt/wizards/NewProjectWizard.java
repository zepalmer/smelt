package com.bahj.smelt.wizards;

import java.net.URI;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import com.bahj.smelt.projects.SmeltProjectSupport;

public class NewProjectWizard extends Wizard implements INewWizard, IExecutableExtension {

    private static final String WIZARD_NAME = Messages.NewProjectWizard_Name;
    private static final String WIZARD_TITLE = Messages.NewProjectWizard_Title;
    private static final String WIZARD_DESCRIPTION = Messages.NewProjectWizard_Description;

    private WizardNewProjectCreationPage pageOne;
    private IConfigurationElement configurationElement;

    public NewProjectWizard() {
        setWindowTitle(WIZARD_NAME);
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
            throws CoreException {
        this.configurationElement = config;        
    }

    @Override
    public boolean performFinish() {
        String name = pageOne.getProjectName();
        URI location;
        if (!pageOne.useDefaults()) {
            location = pageOne.getLocationURI();
        } else {
            location = null;
        }

        SmeltProjectSupport.createProject(name, location);
        BasicNewProjectResourceWizard.updatePerspective(configurationElement);

        return true;
    }

    @Override
    public void addPages() {
        super.addPages();

        pageOne = new WizardNewProjectCreationPage(WIZARD_NAME);
        pageOne.setTitle(WIZARD_TITLE);
        pageOne.setDescription(WIZARD_DESCRIPTION);

        addPage(pageOne);
    }
}
