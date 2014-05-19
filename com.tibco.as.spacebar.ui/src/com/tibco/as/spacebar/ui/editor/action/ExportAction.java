package com.tibco.as.spacebar.ui.editor.action;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.editor.AbstractSpaceEditor;
import com.tibco.as.spacebar.ui.editor.snapshot.SpaceEditor;
import com.tibco.as.spacebar.ui.model.TupleSelection;

import com.tibco.as.space.Tuple;

public abstract class ExportAction extends SpaceEditorAction {

	public ExportAction(String text, String toolTip, ImageDescriptor descriptor) {
		super(text, toolTip, descriptor);
	}

	public ExportAction(String text, String toolTip, Image image) {
		super(text, toolTip, image);
	}

	@Override
	protected void runWithEvent(Event event, AbstractSpaceEditor<?> editor) {
		SpaceEditor snapshotEditor = (SpaceEditor) editor;
		List<Tuple> tuples = snapshotEditor.getSelection();
		if (tuples.isEmpty()) {
			tuples = snapshotEditor.getSortedList();
		}
		IExportWizard wizard = getExportWizard();
		TupleSelection selection = new TupleSelection(editor.getSpace(), tuples);
		IEditorSite site = editor.getEditorSite();
		IWorkbenchWindow window = site.getWorkbenchWindow();
		IWorkbench workbench = window.getWorkbench();
		wizard.init(workbench, selection);
		new WizardDialog(event.display.getActiveShell(), wizard).open();
		editor.activate();
	}

	protected abstract IExportWizard getExportWizard();

}
