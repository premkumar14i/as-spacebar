package com.tibco.as.spacebar.ui.wizards.transfer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IImportWizard;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.model.Connection;
import com.tibco.as.spacebar.ui.model.Metaspace;
import com.tibco.as.spacebar.ui.wizards.transfer.AbstractImportWizardPage.ImportFileSystemElement;

import com.tibco.as.io.IMetaspaceTransfer;
import com.tibco.as.io.AbstractImport;
import com.tibco.as.io.AbstractTransfer;
import com.tibco.as.space.Tuple;

public abstract class AbstractImportWizard<T> extends
		AbstractTransferWizard<File, T, Tuple> implements IImportWizard {

	/**
	 * Creates a wizard for importing resources into the workspace from the file
	 * system.
	 */
	public AbstractImportWizard(String sectionName, Image image) {
		super(sectionName, "Import", image);
	}

	@Override
	protected Class<File> getType() {
		return File.class;
	}

	@Override
	protected Collection<IMetaspaceTransfer> getTransfers(AbstractTransfer transfer) {
		List<File> files = new ArrayList<File>();
		AbstractImportWizardPage mainPage = (AbstractImportWizardPage) getMainPage();
		for (Object resource : mainPage.getSelectedResources()) {
			files.add((File) ((ImportFileSystemElement) resource)
					.getFileSystemObject());
		}
		return getImporters(files, (AbstractImport) transfer);
	}

	@Override
	protected void addTransferOptionsPage(AbstractTransfer transfer) {
		addPage(new ImportOptionsPage((AbstractImport) transfer));
	}

	protected abstract Collection<IMetaspaceTransfer> getImporters(
			List<File> files, AbstractImport defaultImport);

	@Override
	protected String getJobName() {
		return "Import";
	}

	@Override
	protected String getTaskName(IMetaspaceTransfer transfer) {
		return NLS.bind("Importing to metaspace ''{0}''", transfer
				.getMetaspace().getName());
	}

	@Override
	protected String getErrorMessage(IMetaspaceTransfer transfer) {
		return "Could not import";
	}

	protected Map<String, com.tibco.as.space.Metaspace> getConnectedMetaspaces() {
		Map<String, com.tibco.as.space.Metaspace> metaspaces = new LinkedHashMap<String, com.tibco.as.space.Metaspace>();
		for (Metaspace metaspace : SpaceBarPlugin.getDefault()
				.getMetaspaces().getConnectedMetaspaces()) {
			Connection connection = metaspace.getConnection();
			com.tibco.as.space.Metaspace ms = connection.getMetaspace();
			metaspaces.put(ms.getName(), ms);
		}
		return metaspaces;
	}
}
