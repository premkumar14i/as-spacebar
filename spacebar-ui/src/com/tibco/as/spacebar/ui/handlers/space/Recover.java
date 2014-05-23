package com.tibco.as.spacebar.ui.handlers.space;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.progress.UIJob;

import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.model.Space;

public class Recover extends AbstractSpaceHandler {

	@Override
	protected void handle(ExecutionEvent event, final Space space) {
		Shell shell = HandlerUtil.getActiveShell(event);
		MessageDialog dialog = new MessageDialog(shell,
				"Confirm Space Recover", null, NLS.bind(
						"Are you sure you want to recover space ''{0}''?",
						space.getName()), MessageDialog.QUESTION,
				new String[] { IDialogConstants.YES_LABEL,
						IDialogConstants.NO_LABEL }, 0); // yes is the default
		int code = dialog.open();
		if (code == MessageDialog.OK) {
			new UIJob(shell.getDisplay(), "RecoverSpace") {

				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					try {
						monitor.beginTask(
								NLS.bind("Recovering space ''{0}''",
										space.getName()), 1);
						space.getParent().getMetaspace().getConnection()
								.getMetaspace().recoverSpace(space.getName()); // TODO
																				// add
																				// RecoveryOptions
						monitor.worked(1);
					} catch (Exception e) {
						return new Status(IStatus.ERROR,
								SpaceBarPlugin.ID_PLUGIN, NLS.bind(
										"Could not recover space ''{0}''",
										space.getName()), e);
					} finally {
						monitor.done();
					}
					return Status.OK_STATUS;
				}
			}.schedule();
		}
	}

}
