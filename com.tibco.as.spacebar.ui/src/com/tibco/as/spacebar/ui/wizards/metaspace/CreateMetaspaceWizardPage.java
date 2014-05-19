package com.tibco.as.spacebar.ui.wizards.metaspace;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.tibco.as.spacebar.ui.model.Metaspace;

public class CreateMetaspaceWizardPage extends WizardPage implements
		PropertyChangeListener {

	private Metaspace metaspace;

	/**
	 * Create the wizard.
	 */
	public CreateMetaspaceWizardPage(Metaspace metaspace) {
		super("metaspaceWizardPage");
		setTitle("Create Metaspace");
		setDescription("New metaspace connection profile");
		this.metaspace = metaspace;
	}

	@Override
	public void createControl(Composite parent) {
		MetaspaceEditor editor = new MetaspaceEditor(parent, SWT.NONE,
				metaspace);
		setControl(editor);
		metaspace.addPropertyChangeListener("name", this);
		validate();
	}

	private void validate() {
		String name = metaspace.getName();
		if (name == null || name.trim().length() == 0) {
			setErrorMessage("Profile name cannot be empty");
			setPageComplete(false);
		} else {
			setErrorMessage(null);
			setPageComplete(true);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		validate();
	}
}
