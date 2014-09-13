package com.tibco.as.spacebar.ui.editor.action;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.editor.AbstractBrowser;
import com.tibco.as.spacebar.ui.editor.SpaceEditorExport;
import com.tibco.as.spacebar.ui.editor.SpaceEditorInput;
import com.tibco.as.spacebar.ui.wizards.space.browse.BrowseSpaceWizard;

public class BrowseAction extends SpaceEditorAction {

	public BrowseAction() {
		super("&Browse", "Open browse settings", Image.CONFIG);
	}

	@Override
	protected void runWithEvent(Event event, AbstractBrowser<?> editor) {
		SpaceEditorInput input = editor.getBrowserInput();
		SpaceEditorExport browse = input.getExport().clone();
		BrowseSpaceWizard wizard = new BrowseSpaceWizard(input.getSpace(),
				browse);
		Shell shell = event.display.getActiveShell();
		WizardDialog dialog = new WizardDialog(shell, wizard);
		if (dialog.open() == Window.OK) {
			input.setExport(wizard.getExport());
			try {
				editor.refresh();
			} catch (InterruptedException e) {
				SpaceBarPlugin.errorDialog("Could not refresh editor", e);
			}
		}
		editor.activate();
	}

}
