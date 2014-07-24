package com.tibco.as.spacebar.ui.handlers.space;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.progress.UIJob;

import com.tibco.as.space.Metaspace;
import com.tibco.as.space.SpaceDef;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.model.Index;
import com.tibco.as.spacebar.ui.model.Space;

public class DropIndex extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
		if (currentSelection instanceof IStructuredSelection) {
			Object selected = ((IStructuredSelection) currentSelection)
					.getFirstElement();
			if (selected instanceof Index) {
				handle(event, (Index) selected);
			}
		}
		return null;
	}

	protected void handle(ExecutionEvent event, final Index index) {
		Shell shell = HandlerUtil.getActiveShell(event);
		MessageDialog dialog = new MessageDialog(shell, "Confirm Index Drop",
				null, NLS.bind("Are you sure you want to drop index ''{0}''?",
						index), MessageDialog.QUESTION,
				new String[] { IDialogConstants.YES_LABEL,
						IDialogConstants.NO_LABEL }, 0); // yes is the default
		int code = dialog.open();
		if (code == MessageDialog.OK) {
			new UIJob(shell.getDisplay(), "DropIndex") {

				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					try {
						monitor.beginTask(
								NLS.bind("Dropping index ''{0}''", index), 1);
						Space space = index.getParent().getParent();
						Metaspace metaspace = space.getParent().getParent()
								.getConnection().getMetaspace();
						SpaceDef spaceDef = metaspace.getSpaceDef(space
								.getName());
						spaceDef.removeIndexDef(index.getName());
						metaspace.alterSpace(spaceDef);
						monitor.worked(1);
					} catch (Exception e) {
						return SpaceBarPlugin.createStatus(e,
								"Could not drop index ''{0}''", index);
					} finally {
						monitor.done();
					}
					return Status.OK_STATUS;
				}
			}.schedule();
		}
	}

}
