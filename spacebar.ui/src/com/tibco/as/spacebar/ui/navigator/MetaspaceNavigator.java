package com.tibco.as.spacebar.ui.navigator;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
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

import com.tibco.as.io.Export;
import com.tibco.as.spacebar.ui.ConnectJob;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.editor.SpaceEditorInput;
import com.tibco.as.spacebar.ui.model.IElement;
import com.tibco.as.spacebar.ui.model.Metaspace;
import com.tibco.as.spacebar.ui.model.Metaspaces;
import com.tibco.as.spacebar.ui.model.Space;
import com.tibco.as.spacebar.ui.model.Spaces;
import com.tibco.as.spacebar.ui.preferences.Preferences;

public class MetaspaceNavigator extends CommonNavigator {

	@Override
	protected Metaspaces getInitialInput() {
		return SpaceBarPlugin.getDefault().getMetaspaces();
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		CommonViewer treeViewer = getCommonViewer();
		Metaspaces model = (Metaspaces) treeViewer.getInput();
		IObservableList observable = BeanProperties.list("children").observe(
				model);
		observable.addListChangeListener(new IListChangeListener() {

			@Override
			public void handleListChange(ListChangeEvent event) {
				for (ListDiffEntry entry : event.diff.getDifferences()) {
					if (entry.isAddition()) {
						observeMetaspace((Metaspace) entry.getElement());
					}
				}
			}

		});
		for (IElement element : model.getChildren()) {
			// treeViewer.setSelection(new
			// StructuredSelection(metaspaces.get(0)));
			observeMetaspace((Metaspace) element);
		}
	}

	private void observeMetaspace(final Metaspace metaspace) {
		BeanProperties.value("state").observe(metaspace)
				.addValueChangeListener(new IValueChangeListener() {

					@Override
					public void handleValueChange(ValueChangeEvent event) {
						getCommonViewer().update(metaspace,
								new String[] { "state" });
					}
				});
		BeanProperties.value("name").observe(metaspace)
				.addValueChangeListener(new IValueChangeListener() {

					@Override
					public void handleValueChange(ValueChangeEvent event) {
						getCommonViewer().update(metaspace,
								new String[] { "name" });
					}
				});
		IObservableList observable = BeanProperties.list("children").observe(
				metaspace);
		observable.addListChangeListener(new IListChangeListener() {

			@Override
			public void handleListChange(ListChangeEvent event) {
				for (ListDiffEntry entry : event.diff.getDifferences()) {
					if (entry.isAddition()) {
						if (entry.getElement() instanceof Spaces) {
							Spaces spaces = (Spaces) entry.getElement();
							reveal(spaces);
							IObservableList childObservable = BeanProperties
									.list("children").observe(spaces);
							childObservable
									.addListChangeListener(new IListChangeListener() {

										@Override
										public void handleListChange(
												ListChangeEvent event) {
											for (ListDiffEntry entry : event.diff
													.getDifferences()) {
												if (entry.isAddition()) {
													reveal(entry.getElement());
												}
											}
										}
									});
							for (IElement element : spaces.getChildren()) {
								reveal(element);
							}
						}
					}
				}
			}
		});
		Spaces spaces = metaspace.getSpaces();
		if (spaces != null) {
			reveal(spaces);
			for (IElement space : spaces.getChildren()) {
				reveal(space);
			}
		}
	}

	private void reveal(final Object element) {
		getCommonViewer().getTree().getDisplay().asyncExec(new Runnable() {
			public void run() {
				CommonViewer viewer = getCommonViewer();
				if (viewer.getTree().isDisposed()) {
					return;
				}
				viewer.reveal(element);
				// viewer.setExpandedState(element, true);
				// viewer.refresh(element, false);
			}
		});
	}

	@Override
	protected void handleDoubleClick(DoubleClickEvent event) {
		Object element = ((IStructuredSelection) event.getSelection())
				.getFirstElement();
		if (element instanceof Metaspace) {
			Metaspace metaspace = (Metaspace) element;
			if (metaspace.isDisconnected()) {
				new ConnectJob(metaspace).schedule();
			}
		} else {
			if (element instanceof Space) {
				Space space = (Space) element;
				Export export = Preferences.getSpaceEditorExport(Preferences
						.getSpaceEditorBrowseTimeScope());
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
