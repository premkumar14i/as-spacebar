package com.tibco.as.spacebar.ui.handlers.metaspace;

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
import com.tibco.as.spacebar.ui.model.Metaspace;
import com.tibco.as.spacebar.ui.model.Connection;

public class Suspend extends AbstractMetaspaceHandler {

	@Override
	protected void handle(ExecutionEvent event, Metaspace metaspace) {
		Shell shell = HandlerUtil.getActiveShell(event);
		final Connection connection = metaspace.getConnection();
		final String metaspaceName = connection.getMetaspaceName();
		MessageDialog dialog = new MessageDialog(shell,
				"Confirm Metaspace Suspend", null, NLS.bind(
						"Are you sure you want to suspend metaspace ''{0}''?",
						metaspaceName), MessageDialog.QUESTION,
				new String[] { IDialogConstants.YES_LABEL,
						IDialogConstants.NO_LABEL }, 0); // yes is the default
		int code = dialog.open();
		if (code == MessageDialog.OK) {
			new UIJob(shell.getDisplay(), "SuspendMetaspace") {

				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					try {
						monitor.beginTask(NLS.bind(
								"Suspending metaspace ''{0}''", metaspaceName),
								1);
						connection.getMetaspace().suspend();
						monitor.worked(1);
					} catch (Exception e) {
						return new Status(IStatus.ERROR, SpaceBarPlugin.ID_PLUGIN,
								NLS.bind("Could not suspend metaspace ''{0}''",
										metaspaceName), e);
					} finally {
						monitor.done();
					}
					return Status.OK_STATUS;
				}
			}.schedule();
		}
	}

}
