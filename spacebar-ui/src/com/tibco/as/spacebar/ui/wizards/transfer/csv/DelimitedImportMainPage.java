package com.tibco.as.spacebar.ui.wizards.transfer.csv;

import org.eclipse.swt.widgets.Listener;

import com.tibco.as.spacebar.ui.wizards.transfer.AbstractImportWizardPage;

public class DelimitedImportMainPage extends AbstractImportWizardPage implements
		Listener {

	private final static String[] EXTENSIONS = { "", "csv", "txt" };

	public DelimitedImportMainPage() {
		super("DelimitedImportMainPage", "Import spaces from CSV files",
				EXTENSIONS);
		setDescription("Select the CSV files to import.");
	}

}
