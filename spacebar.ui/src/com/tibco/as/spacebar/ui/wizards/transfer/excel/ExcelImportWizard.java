package com.tibco.as.spacebar.ui.wizards.transfer.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.preferences.Preferences;
import com.tibco.as.spacebar.ui.wizards.transfer.AbstractImportWizard;

import com.tibco.as.io.IMetaspaceTransfer;
import com.tibco.as.io.IOUtils;
import com.tibco.as.io.AbstractImport;
import com.tibco.as.io.AbstractTransfer;
import com.tibco.as.excel.ExcelImport;
import com.tibco.as.excel.ExcelImporter;
import com.tibco.as.space.Metaspace;

public class ExcelImportWizard extends AbstractImportWizard<Object[]> {

	public ExcelImportWizard() {
		super("ExcelImportWizard", Image.WIZBAN_EXCEL);
	}

	@Override
	protected AbstractTransfer createTransfer() {
		return new ExcelImport();
	}

	@Override
	protected ExcelImportMainPage getMainPage(IStructuredSelection selection) {
		return new ExcelImportMainPage();
	}

	@Override
	protected IWizardPage getConfigurationPage(AbstractTransfer transfer) {
		ExcelImport excelImport = (ExcelImport) transfer;
		Preferences.configureExcelImport(excelImport);
		return new ExcelImportConfigurationPage(excelImport);
	}

	@Override
	protected Collection<IMetaspaceTransfer> getImporters(List<File> files,
			AbstractImport defaultImport) {
		Collection<IMetaspaceTransfer> importers = new ArrayList<IMetaspaceTransfer>();
		Map<String, Metaspace> metaspaces = getConnectedMetaspaces();
		if (metaspaces.isEmpty()) {
			SpaceBarPlugin
					.errorDialog("Import Error",
							"No connected metaspace, connect to a metaspace before importing.");
		} else {
			for (File file : files) {
				String metaspaceName = IOUtils.getBaseName(file.getName());
				Metaspace metaspace = metaspaces.containsKey(metaspaceName) ? metaspaces
						.get(metaspaceName) : metaspaces.values().iterator()
						.next();
				ExcelImporter importer = new ExcelImporter(metaspace, file);
				importer.setDefaultTransfer(defaultImport);
				importers.add(importer);
			}
		}
		return importers;
	}
}