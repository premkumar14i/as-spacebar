package com.tibco.as.spacebar.ui.wizards.transfer.csv;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.preferences.Preferences;
import com.tibco.as.spacebar.ui.wizards.transfer.AbstractImportWizard;

import com.tibco.as.io.IMetaspaceTransfer;
import com.tibco.as.io.AbstractImport;
import com.tibco.as.io.AbstractTransfer;
import com.tibco.as.file.text.delimited.DelimitedImport;
import com.tibco.as.file.text.delimited.DelimitedImporter;
import com.tibco.as.space.Metaspace;

public class DelimitedImportWizard extends AbstractImportWizard<String[]> {

	public DelimitedImportWizard() {
		super("DelimitedImportWizard", Image.WIZBAN_CSV);
	}

	@Override
	protected DelimitedImport createTransfer() {
		DelimitedImport config = new DelimitedImport();
		Preferences.configureDelimitedImport(config);
		return config;
	}

	@Override
	protected IWizardPage getConfigurationPage(AbstractTransfer transfer) {
		return new DelimitedImportConfigurationPage((DelimitedImport) transfer);
	}

	@Override
	protected DelimitedImportMainPage getMainPage(IStructuredSelection selection) {
		return new DelimitedImportMainPage();
	}

	@Override
	protected Collection<IMetaspaceTransfer> getImporters(List<File> files,
			AbstractImport defaultImport) {
		Map<File, Collection<File>> dirs = new HashMap<File, Collection<File>>();
		for (File file : files) {
			File dir = file.getParentFile();
			if (!dirs.containsKey(dir)) {
				dirs.put(dir, new ArrayList<File>());
			}
			dirs.get(dir).add(file);
		}
		Map<String, Metaspace> metaspaces = getConnectedMetaspaces();
		if (metaspaces.isEmpty()) {
			SpaceBarPlugin
					.errorDialog("Import Error",
							"No connected metaspace, connect to a metaspace before importing.");
		}
		Collection<IMetaspaceTransfer> importers = new ArrayList<IMetaspaceTransfer>();
		for (File dir : dirs.keySet()) {
			String metaspaceName = dir.getName();
			Metaspace metaspace = metaspaces.containsKey(metaspaceName) ? metaspaces
					.get(metaspaceName) : metaspaces.values().iterator().next();
			DelimitedImporter importer = new DelimitedImporter(metaspace, dir);
			importer.setDefaultTransfer(defaultImport);
			for (File file : dirs.get(dir)) {
				importer.addImport(file.getName());
			}
			importers.add(importer);
		}
		return importers;
	}
}