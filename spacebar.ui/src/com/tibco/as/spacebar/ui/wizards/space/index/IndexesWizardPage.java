package com.tibco.as.spacebar.ui.wizards.space.index;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Control;

import com.tibco.as.spacebar.ui.model.Indexes;
import com.tibco.as.spacebar.ui.wizards.AbstractWizardPage;

public class IndexesWizardPage extends AbstractWizardPage {

	private Indexes indexes;

	public IndexesWizardPage(Indexes indexes) {
		super("spaceIndexesWizardPage", "Space Indexes", "Enter space indexes");
		this.indexes = indexes;
	}

	@Override
	protected Control createControl(ScrolledComposite parent) {
		return new IndexListEditor(parent, SWT.NONE, indexes);
	}

}
