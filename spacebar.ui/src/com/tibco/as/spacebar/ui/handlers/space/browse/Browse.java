package com.tibco.as.spacebar.ui.handlers.space.browse;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import com.tibco.as.spacebar.ui.editor.SpaceEditorInput;
import com.tibco.as.spacebar.ui.preferences.Preferences;
import com.tibco.as.spacebar.ui.wizards.space.browse.BrowseSpaceWizard;

public class Browse extends AbstractBrowseHandler {

	@Override
	protected void openEditor(IWorkbenchPage page, SpaceEditorInput input)
			throws PartInitException {
		BrowseSpaceWizard wizard = new BrowseSpaceWizard(input.getSpace(),
				input.getExport());
		WizardDialog dialog = new WizardDialog(page.getWorkbenchWindow()
				.getShell(), wizard);
		if (dialog.open() == Window.OK) {
			super.openEditor(page, input);
		}
	}

	@Override
	protected String getTimeScope() {
		return Preferences
				.getString(Preferences.SPACE_EDITOR_BROWSE_TIME_SCOPE);
	}
}
