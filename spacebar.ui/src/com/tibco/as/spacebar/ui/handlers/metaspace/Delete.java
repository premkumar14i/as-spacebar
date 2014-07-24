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

public class Delete extends AbstractMetaspaceHandler {

	@Override
	protected void handle(ExecutionEvent event, final Metaspace metaspace) {
		Shell shell = HandlerUtil.getActiveShell(event);
		MessageDialog dialog = new MessageDialog(shell,
				"Confirm Metaspace Delete", null, NLS.bind(
						"Are you sure you want to delete metaspace ''{0}''?",
						metaspace), MessageDialog.QUESTION,
				new String[] { IDialogConstants.YES_LABEL,
						IDialogConstants.NO_LABEL }, 0); // yes is the default
		int code = dialog.open();
		if (code == MessageDialog.OK) {
			new UIJob(shell.getDisplay(), "DeleteMetaspace") {

				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					monitor.beginTask(
							NLS.bind("Deleting metaspace ''{0}''", metaspace),
							2);
					SpaceBarPlugin activator = SpaceBarPlugin.getDefault();
					activator.getMetaspaces().removeChild(metaspace);
					monitor.worked(1);
					activator.delete(metaspace);
					monitor.worked(1);
					monitor.done();
					return Status.OK_STATUS;
				}
			}.schedule();
		}
	}
}
