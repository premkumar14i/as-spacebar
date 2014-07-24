package com.tibco.as.spacebar.ui.wizards.space;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import com.tibco.as.spacebar.ui.SWTFactory;
import com.tibco.as.spacebar.ui.model.IElement;

public abstract class ElementListEditor extends Composite {

	private TableViewer tableViewer;
	private Button addButton;
	private Button editButton;
	private Button removeButton;
	private Button revertButton;
	private Map<IElement, IElement> backup = new HashMap<IElement, IElement>();
	private IElement parentElement;

	public ElementListEditor(Composite parent, int style,
			IElement parentElement, ColumnConfig... configs) {
		super(parent, style);
		this.parentElement = parentElement;
		for (IElement element : parentElement.getChildren()) {
			backup.put(element, element.clone());
		}
		setLayout(new GridLayout(2, false));
		Composite tableComposite = new Composite(this, SWT.NONE);
		tableComposite.setFont(getFont());
		tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		TableColumnLayout columnLayout = new TableColumnLayout();
		tableComposite.setLayout(columnLayout);
		GridData tableData = new GridData(GridData.FILL_BOTH);
		tableData.grabExcessHorizontalSpace = true;
		tableData.horizontalSpan = 1;
		tableComposite.setLayoutData(tableData);
		tableViewer = new TableViewer(tableComposite, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setBackgroundMode(SWT.INHERIT_DEFAULT);
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent e) {
				edit();
			}
		});
		tableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent e) {
						updateButtons();
					}
				});
		tableViewer.setContentProvider(new ListContentProvider());
		for (ColumnConfig config : configs) {
			TableViewerColumn nameColumn = new TableViewerColumn(tableViewer,
					config.getStyle());
			nameColumn.getColumn().setText(config.getName());
			columnLayout.setColumnData(
					nameColumn.getColumn(),
					new ColumnWeightData(config.getWeight(), config
							.getMinimumWidth(), config.isResizable()));
			nameColumn.setLabelProvider(config.getLabelProvider());
		}

		Composite buttons = SWTFactory.createComposite(this, 1, 1,
				GridData.FILL_BOTH);
		addButton = new Button(buttons, SWT.PUSH);
		addButton.setText("&New...");
		GridDataFactory.defaultsFor(addButton).applyTo(addButton);
		addButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				add();
			}
		});
		editButton = new Button(buttons, SWT.PUSH);
		editButton.setText("&Edit...");
		GridDataFactory.defaultsFor(editButton).grab(true, false)
				.applyTo(editButton);
		editButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				edit();
			}
		});
		removeButton = new Button(buttons, SWT.PUSH);
		removeButton.setText("&Remove");
		GridDataFactory.defaultsFor(removeButton).grab(true, false)
				.applyTo(removeButton);
		removeButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				remove();
			}
		});
		createSeparator(buttons);
		revertButton = new Button(buttons, SWT.PUSH);
		revertButton.setText("Re&vert");
		GridDataFactory.defaultsFor(revertButton).grab(true, false)
				.applyTo(revertButton);
		revertButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				revert();
			}
		});
		createSeparator(buttons);
		tableViewer.setInput(parentElement.getChildren());
	}

	private void revert() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer
				.getSelection();
		Iterator<?> elements = selection.iterator();
		while (elements.hasNext()) {
			IElement element = (IElement) elements.next();
			IElement original = backup.get(element);
			if (original != null) {
				original.copyTo(element);
			}
		}
		updateButtons();
		tableViewer.refresh();
	}

	private void remove() {
		for (Object element : ((IStructuredSelection) tableViewer
				.getSelection()).toArray()) {
			parentElement.removeChild((IElement) element);
		}
		tableViewer.refresh();
	}

	/**
	 * Updates the buttons.
	 */
	protected void updateButtons() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer
				.getSelection();
		int selectionCount = selection.size();
		int itemCount = tableViewer.getTable().getItemCount();
		boolean canRevert = false;
		for (Iterator<?> it = selection.iterator(); it.hasNext();) {
			if (backup.containsKey(it.next())) {
				canRevert = true;
				break;
			}
		}

		editButton.setEnabled(selectionCount == 1);
		removeButton.setEnabled(selectionCount > 0
				&& selectionCount <= itemCount);
		revertButton.setEnabled(canRevert);
	}

	/**
	 * Creates a separator between buttons.
	 * 
	 * @param parent
	 *            the parent composite
	 * @return a separator
	 */
	private Label createSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.NONE);
		separator.setVisible(false);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.BEGINNING;
		gd.heightHint = 4;
		separator.setLayoutData(gd);
		return separator;
	}

	private void add() {
		IElement element = newElement(parentElement);
		if (editElement(null, element)) {
			parentElement.addChild(element);
			tableViewer.refresh();
			tableViewer.setSelection(new StructuredSelection(element));
		}
	}

	protected abstract IElement newElement(IElement parentElement);

	protected abstract boolean editElement(IElement original, IElement edited);

	private void edit() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer
				.getSelection();
		Object[] objects = selection.toArray();
		if ((objects == null) || (objects.length != 1)) {
			return;
		}
		IElement element = (IElement) selection.getFirstElement();
		IElement edited = element.clone();
		if (editElement(element, edited)) {
			edited.copyTo(element);
			tableViewer.refresh(element);
			updateButtons();
			tableViewer.setSelection(new StructuredSelection(element));
		}
	}

}
