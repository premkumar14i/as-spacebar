package com.tibco.as.spacebar.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;

import com.tibco.as.spacebar.ui.SpaceBarPlugin;

public abstract class AbstractWizard extends Wizard {

	private String errorTitle;
	private String errorMessage;

	public AbstractWizard(String sectionName, String errorTitle,
			String errorMessage) {
		setNeedsProgressMonitor(true);
		SpaceBarPlugin plugin = SpaceBarPlugin.getDefault();
		IDialogSettings workbenchSettings = plugin.getDialogSettings();
		IDialogSettings section = workbenchSettings.getSection(sectionName);
		if (section == null) {
			section = workbenchSettings.addNewSection(sectionName);
		}
		setDialogSettings(section);
		this.errorTitle = errorTitle;
		this.errorMessage = errorMessage;
	}

	@Override
	public boolean performFinish() {
		IRunnableWithProgress op = getRunnable();
		try {
			getContainer().run(false, true, op);
		} catch (InvocationTargetException e) {
			handleException(e.getTargetException());
			return false;
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

	protected IRunnableWithProgress getRunnable() {
		return new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					finish(monitor);
				} catch (Exception e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}

		};
	}

	protected abstract void finish(IProgressMonitor monitor) throws Exception;

	protected void handleException(Throwable target) {
		if (target instanceof CoreException) {
			IStatus status = ((CoreException) target).getStatus();
			ErrorDialog.openError(getShell(), errorTitle, errorMessage, status);
			SpaceBarPlugin.logException(status.getMessage(),
					status.getException());
		} else {
			MessageDialog
					.openError(getShell(), errorTitle, target.getMessage());
			SpaceBarPlugin.logException(target);
		}
	}

}
