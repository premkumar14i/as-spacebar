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

public class Resume extends AbstractSpaceHandler {

	@Override
	protected void handle(ExecutionEvent event, final Space space) {
		Shell shell = HandlerUtil.getActiveShell(event);
		MessageDialog dialog = new MessageDialog(shell, "Confirm Space Resume",
				null, NLS.bind(
						"Are you sure you want to resume space ''{0}''?",
						space.getName()), MessageDialog.QUESTION,
				new String[] { IDialogConstants.YES_LABEL,
						IDialogConstants.NO_LABEL }, 0); // yes is the default
		int code = dialog.open();
		if (code == MessageDialog.OK) {
			new UIJob(shell.getDisplay(), "ResumeSpace") {

				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					try {
						monitor.beginTask(
								NLS.bind("Resuming space ''{0}''",
										space.getName()), 1);
						space.getParent().getMetaspace().getConnection()
								.getMetaspace().resumeSpace(space.getName());
						monitor.worked(1);
					} catch (Exception e) {
						return new Status(IStatus.ERROR,
								SpaceBarPlugin.ID_PLUGIN, NLS.bind(
										"Could not resume space ''{0}''",
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
