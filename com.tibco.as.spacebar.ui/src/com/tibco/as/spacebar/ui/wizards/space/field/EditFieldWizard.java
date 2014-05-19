package com.tibco.as.spacebar.ui.wizards.space.field;

import org.eclipse.jface.wizard.Wizard;

import com.tibco.as.spacebar.ui.model.Field;

public class EditFieldWizard extends Wizard {

	private Field original;
	private Field field;

	public EditFieldWizard(Field original, Field field) {
		this.original = original;
		this.field = field;
		setWindowTitle("Edit Field");
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(new EditFieldWizardPage(original, field));
	}

	@Override
	public boolean performFinish() {
		return true;
	}

}
