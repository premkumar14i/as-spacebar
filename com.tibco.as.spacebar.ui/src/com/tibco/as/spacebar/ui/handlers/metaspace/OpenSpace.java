package com.tibco.as.spacebar.ui.handlers.metaspace;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.tibco.as.spacebar.ui.model.Spaces;
import com.tibco.as.spacebar.ui.wizards.metaspace.AddSpaceWizard;

public class OpenSpace extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
		if (currentSelection instanceof IStructuredSelection) {
			Object selected = ((IStructuredSelection) currentSelection)
					.getFirstElement();
			if (selected instanceof Spaces) {
				handle(event, (Spaces) selected);
			}
		}
		return null;
	}

	protected void handle(ExecutionEvent event, Spaces spaces) {
		WizardDialog dialog = new WizardDialog(
				HandlerUtil.getActiveShell(event), new AddSpaceWizard(spaces));
		dialog.open();
	}

}
