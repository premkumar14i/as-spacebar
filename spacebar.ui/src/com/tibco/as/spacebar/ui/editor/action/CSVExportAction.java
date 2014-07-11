package com.tibco.as.spacebar.ui.editor.action;

import org.eclipse.ui.IExportWizard;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.wizards.transfer.csv.DelimitedExportWizard;

public class CSVExportAction extends ExportAction {

	public CSVExportAction() {
		super("&Export To CSV", "Export table to CSV file", Image.CSV);
	}

	@Override
	protected IExportWizard getExportWizard() {
		return new DelimitedExportWizard();
	}
}
