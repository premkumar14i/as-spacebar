package com.tibco.as.spacebar.ui.wizards.transfer.simulation;

import org.eclipse.swt.widgets.Listener;

import com.tibco.as.spacebar.ui.wizards.transfer.AbstractImportWizardPage;

public class SimulationImportMainPage extends AbstractImportWizardPage
		implements Listener {

	private final static String[] EXTENSIONS = { "xml" };

	public SimulationImportMainPage() {
		super("SimulationImportMainPage", "Simulate space data", EXTENSIONS);
		setDescription("Select the simulation configurations to execute.");
	}

}
