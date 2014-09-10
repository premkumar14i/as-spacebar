package com.tibco.as.spacebar.ui.wizards.transfer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.transfer.MetaspaceTransferJob;

import com.tibco.as.io.IMetaspaceTransfer;
import com.tibco.as.io.AbstractTransfer;

public abstract class AbstractTransferWizard<S, T, U> extends Wizard implements
		IWorkbenchWizard {

	private IStructuredSelection selection;
	private String windowTitle;
	private Image image;
	private AbstractTransferWizardPage mainPage;
	private AbstractTransfer defaultTransfer;

	public AbstractTransferWizard(String sectionName, String windowTitle,
			Image image) {
		IDialogSettings workbenchSettings = SpaceBarPlugin.getDefault()
				.getDialogSettings();
		IDialogSettings section = workbenchSettings.getSection(sectionName);
		if (section == null) {
			section = workbenchSettings.addNewSection(sectionName);
		}
		setDialogSettings(section);
		this.windowTitle = windowTitle;
		this.image = image;
	}

	protected IStructuredSelection getSelection() {
		return selection;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		List<S> selectedResources = computeSelectedResources(selection);
		if (!selectedResources.isEmpty()) {
			this.selection = new StructuredSelection(selectedResources);
		}
		if (selection.isEmpty() && workbench.getActiveWorkbenchWindow() != null) {
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				IEditorPart editor = page.getActiveEditor();
				if (editor != null) {
					IEditorInput input = editor.getEditorInput();
					Object resource = input.getAdapter(getType());
					if (resource != null) {
						this.selection = new StructuredSelection(resource);
					}
				}
			}
		}
		setWindowTitle(windowTitle);
		setDefaultPageImageDescriptor(SpaceBarPlugin.getDefault()
				.getImageDescriptor(image));
		setNeedsProgressMonitor(true);
	}

	@SuppressWarnings("unchecked")
	private List<S> computeSelectedResources(IStructuredSelection selection) {
		List<S> resources = null;
		Iterator<?> iterator = selection.iterator();
		while (iterator.hasNext()) {
			Object next = (Object) iterator.next();
			S resource = null;
			Class<S> type = getType();
			if (type.isAssignableFrom(next.getClass())) {
				resource = (S) next;
			} else if (next instanceof IAdaptable) {
				resource = (S) ((IAdaptable) next).getAdapter(type);
			}
			if (resource != null) {
				if (resources == null) {
					resources = new ArrayList<S>(selection.size());
				}
				resources.add(resource);
			}
		}
		if (resources == null) {
			return Collections.emptyList();
		}
		return resources;
	}

	protected abstract Class<S> getType();

	protected AbstractTransferWizardPage getMainPage() {
		return mainPage;
	}

	@Override
	public void addPages() {
		super.addPages();
		mainPage = getMainPage(selection);
		addPage(mainPage);
		defaultTransfer = createTransfer();
		IWizardPage configurationPage = getConfigurationPage(defaultTransfer);
		if (configurationPage != null) {
			addPage(configurationPage);
		}
		addTransferOptionsPage(defaultTransfer);
	}

	protected IWizardPage getConfigurationPage(AbstractTransfer transfer) {
		return null;
	}

	protected abstract AbstractTransfer createTransfer();

	protected abstract AbstractTransferWizardPage getMainPage(
			IStructuredSelection selection);

	@Override
	public boolean performFinish() {
		Collection<IMetaspaceTransfer> transfers = getTransfers(defaultTransfer);
		if (transfers == null) {
			return false;
		}
		for (IMetaspaceTransfer transfer : transfers) {
			new MetaspaceTransferJob(getJobName(), getTaskName(transfer),
					transfer, getErrorMessage(transfer)).schedule();
		}
		mainPage.finish();
		return true;
	}

	protected abstract Collection<IMetaspaceTransfer> getTransfers(
			AbstractTransfer defaultTransfer);

	protected abstract String getJobName();

	protected abstract String getTaskName(IMetaspaceTransfer transfer);

	protected abstract String getErrorMessage(IMetaspaceTransfer transfer);

	protected abstract void addTransferOptionsPage(AbstractTransfer transfer);

}
