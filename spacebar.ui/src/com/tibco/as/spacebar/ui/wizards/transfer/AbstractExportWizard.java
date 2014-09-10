package com.tibco.as.spacebar.ui.wizards.transfer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IExportWizard;

import com.tibco.as.io.AbstractExport;
import com.tibco.as.io.AbstractExporter;
import com.tibco.as.io.IMetaspaceTransfer;
import com.tibco.as.io.ListInputStream;
import com.tibco.as.io.AbstractTransfer;
import com.tibco.as.space.Tuple;
import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.model.IElement;
import com.tibco.as.spacebar.ui.model.Metaspace;
import com.tibco.as.spacebar.ui.model.Space;
import com.tibco.as.spacebar.ui.model.TupleSelection;

public abstract class AbstractExportWizard<T> extends
		AbstractTransferWizard<IElement, Tuple, T> implements IExportWizard {

	public AbstractExportWizard(String sectionName, Image image) {
		super(sectionName, "Export", image);
	}

	@Override
	protected Class<IElement> getType() {
		return IElement.class;
	}

	@Override
	protected void addTransferOptionsPage(AbstractTransfer transfer) {
		if (isTupleSelection()) {
			return;
		}
		addPage(new ExportOptionsPage((AbstractExport) transfer));
	}

	protected boolean isTupleSelection() {
		return getSelection() != null
				&& getSelection() instanceof TupleSelection;
	}

	@Override
	protected Collection<IMetaspaceTransfer> getTransfers(
			AbstractTransfer defaultTransfer) {
		// Save dirty editors if possible but do not stop if not all are saved
		if (!SpaceBarPlugin.getDefault().getWorkbench().saveAllEditors(true)) {
			return null;
		}
		AbstractExportWizardPage mainPage = (AbstractExportWizardPage) getMainPage();
		File directory = new File(mainPage.getDestinationValue());
		if (directory.exists()) {
			if (directory.isFile()) {
				displayErrorDialog("Export Error",
						"Target directory already exists as a file.");
				return null;
			}
		} else {
			if (!queryYesNoQuestion("Target directory does not exist. Create it?")) {
				return null;
			}
			if (!directory.mkdirs()) {
				displayErrorDialog("Export Error",
						"Target directory could not be created.");
				return null;
			}
		}
		Collection<IMetaspaceTransfer> transfers = new ArrayList<IMetaspaceTransfer>();
		if (isTupleSelection()) {
			TupleSelection tupleSelection = (TupleSelection) getSelection();
			Space space = tupleSelection.getSpace();
			com.tibco.as.space.Metaspace ms = space.getParent().getParent()
					.getConnection().getMetaspace();
			AbstractExporter<T> exporter = getExporter(ms, directory);
			if (exporter == null) {
				return null;
			}
			exporter.setDefaultTransfer(defaultTransfer);
			exporter.addExport(space.getName());
			exporter.setInputStream(new ListInputStream<Tuple>(tupleSelection
					.getTuples()));
			transfers.add(exporter);
		} else {
			List<?> itemsToExport = mainPage.getWhiteCheckedResources();
			Map<Metaspace, Collection<Space>> metaspaces = new LinkedHashMap<Metaspace, Collection<Space>>();
			for (Object item : itemsToExport) {
				if (item instanceof Space) {
					Space space = (Space) item;
					Metaspace metaspace = space.getParent().getParent();
					putMetaspace(metaspaces, metaspace);
					metaspaces.get(metaspace).add(space);
				}
				if (item instanceof Metaspace) {
					Metaspace metaspace = (Metaspace) item;
					putMetaspace(metaspaces, metaspace);
				}
			}
			for (Metaspace metaspace : metaspaces.keySet()) {
				com.tibco.as.space.Metaspace ms = metaspace.getConnection()
						.getMetaspace();
				AbstractExporter<T> exporter = getExporter(ms, directory);
				if (exporter == null) {
					continue;
				}
				exporter.setDefaultTransfer(defaultTransfer);
				for (Space space : metaspaces.get(metaspace)) {
					exporter.addExport(space.getName());
				}
				transfers.add(exporter);
			}
		}
		return transfers;
	}

	protected abstract AbstractExporter<T> getExporter(com.tibco.as.space.Metaspace ms,
			File directory);

	private void putMetaspace(Map<Metaspace, Collection<Space>> metaspaces,
			Metaspace metaspace) {
		if (!metaspaces.containsKey(metaspace)) {
			metaspaces.put(metaspace, new ArrayList<Space>());
		}
	}

	protected void displayErrorDialog(String title, String message) {
		MessageDialog.open(MessageDialog.ERROR, getContainer().getShell(),
				title, message, SWT.SHEET);
	}

	/**
	 * Displays a Yes/No question to the user with the specified message and
	 * returns the user's response.
	 * 
	 * @param message
	 *            the question to ask
	 * @return <code>true</code> for Yes, and <code>false</code> for No
	 */
	protected boolean queryYesNoQuestion(String message) {
		MessageDialog dialog = new MessageDialog(getShell(), "Question", null,
				message, MessageDialog.NONE,
				new String[] { IDialogConstants.YES_LABEL,
						IDialogConstants.NO_LABEL }, 0) {
			protected int getShellStyle() {
				return super.getShellStyle() | SWT.SHEET;
			}
		};
		return dialog.open() == 0;
	}

	protected boolean isOverwrite() {
		return ((AbstractExportWizardPage) getMainPage()).isOverwrite();
	}

	@Override
	protected String getJobName() {
		return "Export";
	}

	@Override
	protected String getTaskName(IMetaspaceTransfer transfer) {
		return NLS.bind("Exporting metaspace ''{0}''", transfer.getMetaspace()
				.getName());
	}

	@Override
	protected String getErrorMessage(IMetaspaceTransfer transfer) {
		return "Could not export";
	}

}
