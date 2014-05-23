package com.tibco.as.spacebar.ui.wizards.transfer.excel;

import org.eclipse.swt.widgets.Listener;

import com.tibco.as.io.file.excel.ExcelExporter;

import com.tibco.as.spacebar.ui.wizards.transfer.AbstractImportWizardPage;

public class ExcelImportMainPage extends AbstractImportWizardPage implements
		Listener {

	private final static String[] EXTENSIONS = { ExcelExporter.EXTENSION_XLS,
			ExcelExporter.EXTENSION_XLSX };

	public ExcelImportMainPage() {
		super("ExcelImportMainPage", "Import spaces from Excel files",
				EXTENSIONS);
		setDescription("Select the Excel files to import.");
	}

}
