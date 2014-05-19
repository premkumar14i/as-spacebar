package com.tibco.as.spacebar.ui.editor.action;

import org.eclipse.ui.IExportWizard;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.wizards.transfer.excel.ExcelExportWizard;

public class ExcelExportAction extends ExportAction {

	public ExcelExportAction() {
		super("&Export To Excel", "Export table to Excel file", Image.EXCEL);
	}

	@Override
	protected IExportWizard getExportWizard() {
		return new ExcelExportWizard();
	}

}
