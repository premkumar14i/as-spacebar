package com.tibco.as.spacebar.ui.wizards.transfer.excel;

import java.io.File;

import org.apache.poi.ss.SpreadsheetVersion;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.preferences.Preferences;
import com.tibco.as.spacebar.ui.wizards.transfer.AbstractExportWizard;
import com.tibco.as.spacebar.ui.wizards.transfer.AbstractExportWizardPage;

import com.tibco.as.io.Exporter;
import com.tibco.as.io.Transfer;
import com.tibco.as.excel.ExcelExport;
import com.tibco.as.excel.ExcelExporter;
import com.tibco.as.space.Metaspace;

public class ExcelExportWizard extends AbstractExportWizard<Object[]> {

	private ExcelExportConfigurationPage configPage;

	public ExcelExportWizard() {
		super("ExcelExportWizard", Image.WIZBAN_EXCEL);
	}

	@Override
	protected Transfer createTransfer() {
		ExcelExport config = new ExcelExport();
		Preferences.configureExcelExport(config);
		return config;
	}

	@Override
	protected AbstractExportWizardPage getMainPage(
			IStructuredSelection selection) {
		return new ExcelExportMainPage(selection);
	}

	@Override
	protected IWizardPage getConfigurationPage(Transfer transfer) {
		ExcelExport config = (ExcelExport) transfer;
		String versionString = Preferences
				.getString(Preferences.EXPORT_EXCEL_VERSION);
		SpreadsheetVersion version = SpreadsheetVersion.valueOf(versionString);
		configPage = new ExcelExportConfigurationPage(version, config);
		return configPage;
	}

	@Override
	protected Exporter<Object[]> getExporter(Metaspace ms, File directory) {
		ExcelExporter exporter = new ExcelExporter(ms, directory,
				configPage.getSpreadsheetVersion());
		File file = exporter.getFile();
		if (file.exists()) {
			if (file.isDirectory()) {
				displayErrorDialog("Export Error",
						"Target file already exists as a directory.");
				return null;
			}
			if (!isOverwrite()) {
				if (!queryYesNoQuestion("Target file already exists. Overwrite?")) {
					return null;
				}
			}
			boolean deleted = file.delete();
			if (!deleted) {
				displayErrorDialog("Export Error",
						"Target file could not be deleted.");
				return null;
			}
		}
		return exporter;
	}
}