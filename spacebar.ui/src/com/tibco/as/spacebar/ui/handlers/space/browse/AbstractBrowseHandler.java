package com.tibco.as.spacebar.ui.handlers.space.browse;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.editor.SpaceEditorExport;
import com.tibco.as.spacebar.ui.editor.SpaceEditorInput;
import com.tibco.as.spacebar.ui.handlers.space.AbstractSpaceHandler;
import com.tibco.as.spacebar.ui.model.Space;
import com.tibco.as.spacebar.ui.navigator.MetaspaceNavigator;
import com.tibco.as.spacebar.ui.preferences.Preferences;

public abstract class AbstractBrowseHandler extends AbstractSpaceHandler {

	@Override
	protected void handle(ExecutionEvent event, Space space)
			throws ExecutionException {
		final IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event)
				.getActivePage();
		final MetaspaceNavigator navigator = (MetaspaceNavigator) page
				.findView(SpaceBarPlugin.ID_METASPACES);
		navigator.getCommonViewer().addSelectionChangedListener(
				new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						page.activate(navigator);
					}
				});
		SpaceEditorExport export = Preferences.getSpaceEditorExport(getTimeScope());
		try {
			openEditor(page, new SpaceEditorInput(space, export));
		} catch (PartInitException e) {
			throw new ExecutionException("Could not open space editor", e);
		}
	}

	protected void openEditor(IWorkbenchPage page, SpaceEditorInput input)
			throws PartInitException {
		page.openEditor(input, input.getEditorId(), true);
	}

	protected abstract String getTimeScope();

}
