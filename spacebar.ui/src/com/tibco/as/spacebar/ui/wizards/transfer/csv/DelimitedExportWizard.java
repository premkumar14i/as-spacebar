package com.tibco.as.spacebar.ui.wizards.transfer.csv;

import java.io.File;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.preferences.Preferences;
import com.tibco.as.spacebar.ui.wizards.transfer.AbstractExportWizard;
import com.tibco.as.spacebar.ui.wizards.transfer.AbstractExportWizardPage;

import com.tibco.as.io.AbstractExporter;
import com.tibco.as.io.AbstractTransfer;
import com.tibco.as.file.text.delimited.DelimitedExport;
import com.tibco.as.file.text.delimited.DelimitedExporter;
import com.tibco.as.space.Metaspace;

public class DelimitedExportWizard extends AbstractExportWizard<String[]> {

	public DelimitedExportWizard() {
		super("DelimitedExportWizard", Image.WIZBAN_CSV);
	}

	@Override
	protected DelimitedExport createTransfer() {
		DelimitedExport config = new DelimitedExport();
		Preferences.configureDelimitedExport(config);
		return config;
	}

	@Override
	protected AbstractExportWizardPage getMainPage(
			IStructuredSelection selection) {
		return new DelimitedExportMainPage(selection);
	}

	@Override
	protected IWizardPage getConfigurationPage(AbstractTransfer transfer) {
		return new DelimitedExportConfigurationPage((DelimitedExport) transfer);
	}

	@Override
	protected AbstractExporter<String[]> getExporter(Metaspace ms, File dir) {
		File directory = getDirectory(dir, ms);
		if (directory.exists()) {
			if (directory.isFile()) {
				displayErrorDialog("Export Error",
						"Target directory already exists as a file.");
				return null;
			}
			if (!isOverwrite()) {
				if (!queryYesNoQuestion("Target already exists.  Would you like to overwrite it?")) {
					return null;
				}
			}
		} else {
			if (!directory.mkdirs()) {
				displayErrorDialog("Export Error",
						"Target directory could not be created.");
				return null;
			}
		}
		return new DelimitedExporter(ms, directory);
	}

	private File getDirectory(File dir, com.tibco.as.space.Metaspace ms) {
		DelimitedExportMainPage mainPage = (DelimitedExportMainPage) getMainPage();
		if (mainPage.isCreateMetaspaceDirectories()) {
			return new File(dir, ms.getName());
		}
		return dir;
	}

}