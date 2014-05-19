package com.tibco.as.spacebar.ui.wizards.transfer.excel;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Listener;

import com.tibco.as.spacebar.ui.wizards.transfer.AbstractExportWizardPage;

public class ExcelExportMainPage extends AbstractExportWizardPage implements
		Listener {

	/**
	 * Create an instance of this class
	 */
	protected ExcelExportMainPage(String name, IStructuredSelection selection) {
		super(name, selection);
	}

	/**
	 * Create an instance of this class.
	 * 
	 * @param selection
	 *            the selection
	 */
	public ExcelExportMainPage(IStructuredSelection selection) {
		this("ExcelExportWizardPage", selection);
		setTitle("Excel");
		setDescription("Export spaces to Excel.");
	}

	@Override
	protected String getDestinationTitle() {
		return "Export to Excel";
	}

}
