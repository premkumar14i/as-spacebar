package com.tibco.as.spacebar.ui.navigator;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;

import com.tibco.as.spacebar.ui.ConnectJob;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.editor.SpaceEditorExport;
import com.tibco.as.spacebar.ui.editor.SpaceEditorInput;
import com.tibco.as.spacebar.ui.model.IElement;
import com.tibco.as.spacebar.ui.model.IModelListener;
import com.tibco.as.spacebar.ui.model.Metaspace;
import com.tibco.as.spacebar.ui.model.Metaspaces;
import com.tibco.as.spacebar.ui.model.Space;
import com.tibco.as.spacebar.ui.model.Spaces;
import com.tibco.as.spacebar.ui.preferences.Preferences;

public class MetaspaceNavigator extends CommonNavigator implements
		IModelListener {

	@Override
	protected Metaspaces getInitialInput() {
		return SpaceBarPlugin.getDefault().getMetaspaces();
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		CommonViewer treeViewer = getCommonViewer();
		Metaspaces metaspaces = (Metaspaces) treeViewer.getInput();
		metaspaces.addListener(this);
		for (Metaspace metaspace : metaspaces.getChildren()) {
			Spaces spaces = metaspace.getSpaces();
			if (spaces != null) {
				reveal(spaces);
				for (IElement space : spaces.getChildren()) {
					reveal(space);
				}
			}
		}
	}

	@Override
	public void added(IElement element) {
		reveal(element);
	}

	@Override
	public void removed(final IElement element) {
		getCommonViewer().getTree().getDisplay().asyncExec(new Runnable() {
			public void run() {
				CommonViewer viewer = getCommonViewer();
				if (viewer.getTree().isDisposed()) {
					return;
				}
				viewer.remove(element);
				// viewer.refresh(element.getParent());
			}
		});
	}

	@Override
	public void changed(final IElement element, final String propertyName,
			Object oldValue, Object newValue) {
		getCommonViewer().getTree().getDisplay().asyncExec(new Runnable() {
			public void run() {
				CommonViewer viewer = getCommonViewer();
				if (viewer.getTree().isDisposed()) {
					return;
				}
				viewer.update(element, new String[] { propertyName });
			}
		});
	}

	private void reveal(final IElement element) {
		getCommonViewer().getTree().getDisplay().asyncExec(new Runnable() {
			public void run() {
				CommonViewer viewer = getCommonViewer();
				if (viewer.getTree().isDisposed()) {
					return;
				}
				viewer.reveal(element);
				viewer.setExpandedState(element, true);
				viewer.refresh(element.getParent(), false);
			}
		});
	}

	@Override
	protected void handleDoubleClick(DoubleClickEvent event) {
		Object element = ((IStructuredSelection) event.getSelection())
				.getFirstElement();
		if (element instanceof Metaspace) {
			Metaspace metaspace = (Metaspace) element;
			if (!metaspace.isConnected()) {
				new ConnectJob(metaspace).schedule();
			}
		} else {
			if (element instanceof Space) {
				Space space = (Space) element;
				SpaceEditorExport export = Preferences.getSpaceEditorExport(Preferences
						.getString(Preferences.SPACE_EDITOR_BROWSE_TIME_SCOPE));
				SpaceEditorInput input = new SpaceEditorInput(space, export);
				IWorkbenchPartSite site = getSite();
				final IWorkbenchPage page = site.getWorkbenchWindow()
						.getActivePage();
				getCommonViewer().addSelectionChangedListener(
						new ISelectionChangedListener() {

							@Override
							public void selectionChanged(
									SelectionChangedEvent event) {
								page.activate(MetaspaceNavigator.this);
							}
						});
				try {
					page.openEditor(input, input.getEditorId());
				} catch (PartInitException e) {
					SpaceBarPlugin.errorDialog(site.getShell(),
							"Could not open editor", e);
				}
				return;
			}
		}
		super.handleDoubleClick(event);
	}

}
