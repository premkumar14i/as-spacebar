package com.tibco.as.spacebar.ui.handlers.metaspace;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.tibco.as.spacebar.ui.wizards.metaspace.CreateMetaspaceWizard;

public class Create extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		WizardDialog dialog = new WizardDialog(HandlerUtil
				.getActiveWorkbenchWindow(event).getShell(),
				new CreateMetaspaceWizard());
		dialog.open();
		return null;
	}

}
