package com.tibco.as.spacebar.ui.wizards.space.index;

import org.eclipse.jface.wizard.Wizard;

import com.tibco.as.spacebar.ui.model.Index;

public class EditIndexWizard extends Wizard {

	private Index original;
	private Index index;

	public EditIndexWizard(Index original, Index index) {
		this.original = original;
		this.index = index;
		setWindowTitle("Edit Index");
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(new EditIndexWizardPage(original, index));
	}

	@Override
	public boolean performFinish() {
		return true;
	}

}
