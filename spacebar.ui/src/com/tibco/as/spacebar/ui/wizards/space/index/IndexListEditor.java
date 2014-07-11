package com.tibco.as.spacebar.ui.wizards.space.index;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.tibco.as.space.IndexDef.IndexType;
import com.tibco.as.spacebar.ui.model.Index;
import com.tibco.as.spacebar.ui.model.Indexes;
import com.tibco.as.spacebar.ui.wizards.space.ListContentProvider;

public class IndexListEditor extends Composite {

	private static final String COLUMN_NAME = "Name";
	private static final String COLUMN_TYPE = "Type";
	private static final String COLUMN_FIELDS = "Fields";
	private TableViewer fTableViewer;
	private Button fAddButton;
	private Button fEditButton;
	private Button fRemoveButton;
	private Button fRevertButton;
	private Map<Index, Index> backup;
	private Indexes indexes;

	public IndexListEditor(Composite parent, int style, Indexes indexes) {
		super(parent, style);
		this.indexes = indexes;
		this.backup = new HashMap<Index, Index>();
		for (Index index : indexes.getIndexes()) {
			backup.put(index, index.clone());
		}
		setLayout(newGridLayout(2, false));
		Composite tableComposite = new Composite(this, SWT.NONE);
		GridData data = new GridData(GridData.FILL_BOTH);
		tableComposite.setLayoutData(data);

		TableColumnLayout columnLayout = new TableColumnLayout();
		tableComposite.setLayout(columnLayout);
		Table table = new Table(tableComposite, SWT.BORDER | SWT.MULTI
				| SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		GC gc = new GC(getShell());
		gc.setFont(JFaceResources.getDialogFont());

		TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText(COLUMN_NAME);
		int minWidth = computeMinimumColumnWidth(gc, COLUMN_NAME);
		columnLayout.setColumnData(column1, new ColumnWeightData(30, minWidth));

		TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText(COLUMN_TYPE);
		minWidth = computeMinimumColumnWidth(gc, "DATETIME");
		columnLayout.setColumnData(column2, new ColumnWeightData(30, minWidth));

		TableColumn column3 = new TableColumn(table, SWT.NONE);
		column3.setAlignment(SWT.CENTER);
		column3.setText(COLUMN_FIELDS);
		minWidth = computeMinimumColumnWidth(gc, COLUMN_FIELDS);
		columnLayout.setColumnData(column3, new ColumnWeightData(60, minWidth,
				false));

		gc.dispose();

		fTableViewer = new TableViewer(table);
		fTableViewer.setLabelProvider(new IndexLabelProvider());
		fTableViewer.setContentProvider(new ListContentProvider());

		fTableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent e) {
				edit();
			}
		});

		fTableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent e) {
						updateButtons();
					}
				});

		Composite buttons = new Composite(this, SWT.NONE);
		buttons.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		buttons.setLayout(initialize(new GridLayout()));

		fAddButton = new Button(buttons, SWT.PUSH);
		fAddButton.setText("&New...");
		fAddButton.setLayoutData(getButtonGridData(fAddButton));
		fAddButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				add();
			}
		});

		fEditButton = new Button(buttons, SWT.PUSH);
		fEditButton.setText("&Edit...");
		fEditButton.setLayoutData(getButtonGridData(fEditButton));
		fEditButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				edit();
			}
		});

		fRemoveButton = new Button(buttons, SWT.PUSH);
		fRemoveButton.setText("&Remove");
		fRemoveButton.setLayoutData(getButtonGridData(fRemoveButton));
		fRemoveButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				remove();
			}
		});
		createSeparator(buttons);
		fRevertButton = new Button(buttons, SWT.PUSH);
		fRevertButton.setText("Re&vert");
		fRevertButton.setLayoutData(getButtonGridData(fRevertButton));
		fRevertButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				revert();
			}
		});
		createSeparator(buttons);
		fTableViewer.setInput(indexes.getChildren());
		updateButtons();
		Dialog.applyDialogFont(this);
	}

	private GridLayout newGridLayout(int numColumns,
			boolean makeColumnsEqualWidth) {
		return initialize(new GridLayout(numColumns, makeColumnsEqualWidth));
	}

	private GridLayout initialize(GridLayout layout) {
		layout.marginLeft = 0;
		layout.marginWidth = 0;
		return layout;
	}

	private void revert() {
		IStructuredSelection selection = (IStructuredSelection) fTableViewer
				.getSelection();
		Iterator<?> elements = selection.iterator();
		while (elements.hasNext()) {
			Index index = (Index) elements.next();
			Index original = backup.get(index);
			if (original != null) {
				original.copyTo(index);
			}
		}
		updateButtons();
		fTableViewer.refresh();
	}

	private void remove() {
		IStructuredSelection selection = (IStructuredSelection) fTableViewer
				.getSelection();
		for (Object element : selection.toArray()) {
			indexes.removeChild((Index) element);
		}

		fTableViewer.refresh();
	}

	/**
	 * Updates the buttons.
	 */
	protected void updateButtons() {
		IStructuredSelection selection = (IStructuredSelection) fTableViewer
				.getSelection();
		int selectionCount = selection.size();
		int itemCount = fTableViewer.getTable().getItemCount();
		boolean canRevert = false;
		for (Iterator<?> it = selection.iterator(); it.hasNext();) {
			Index index = (Index) it.next();
			if (isRevertable(index)) {
				canRevert = true;
				break;
			}
		}

		fEditButton.setEnabled(selectionCount == 1);
		fRemoveButton.setEnabled(selectionCount > 0
				&& selectionCount <= itemCount);
		fRevertButton.setEnabled(canRevert);
	}

	private boolean isRevertable(Index index) {
		return backup.containsKey(index);
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

	/**
	 * Return the grid data for the button.
	 * 
	 * @param button
	 *            the button
	 * @return the grid data
	 */
	private static GridData getButtonGridData(Button button) {
		return new GridData(GridData.FILL_HORIZONTAL);
	}

	private int computeMinimumColumnWidth(GC gc, String string) {
		return gc.stringExtent(string).x + 4; // pad 4 to accommodate table
												// header trimmings
	}

	private void add() {
		Index index = new Index(indexes, "");
		index.setType(IndexType.HASH);
		if (editIndex(null, index)) {
			indexes.addChild(index);
			fTableViewer.refresh();
			fTableViewer.setSelection(new StructuredSelection(index));
		}
	}

	/**
	 * Creates the edit dialog. Subclasses may override this method to provide a
	 * custom dialog.
	 * 
	 */
	protected boolean editIndex(Index original, Index edited) {
		EditIndexWizard wizard = new EditIndexWizard(original, edited);
		return new WizardDialog(getShell(), wizard).open() == Window.OK;
	}

	private void edit() {
		IStructuredSelection selection = (IStructuredSelection) fTableViewer
				.getSelection();

		Object[] objects = selection.toArray();
		if ((objects == null) || (objects.length != 1))
			return;

		Index index = (Index) selection.getFirstElement();
		Index editedIndex = index.clone();
		if (editIndex(index, editedIndex)) {
			editedIndex.copyTo(index);
			fTableViewer.refresh(index);
			updateButtons();
			fTableViewer.setSelection(new StructuredSelection(index));
		}
	}

	// public static void main(String[] args) {
	// Display display = new Display();
	// Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
	// public void run() {
	// Space space = new Space(new Spaces(new Metaspace(
	// new Metaspaces())));
	// space.setSpaceDef(SpaceDef.create("space1"));
	// SpaceFields fields = space.getFields();
	// Field field1 = new Field(fields);
	// field1.setName("Field1");
	// field1.setType(FieldType.STRING);
	// field1.setNullable(true);
	// Field field2 = new Field(fields);
	// field2.setName("Field2");
	// field2.setType(FieldType.STRING);
	// field2.setNullable(true);
	// Field field3 = new Field(fields);
	// field3.setName("Field3");
	// field3.setType(FieldType.STRING);
	// field3.setNullable(true);
	// space.getFields().addChild(field1);
	// space.getFields().addChild(field2);
	// space.getFields().addChild(field3);
	// Indexes indexes = new Indexes(space);
	// for (int index = 0; index < 10; index++) {
	// Index indexElement = new Index(indexes, "Index"
	// + (index + 1));
	// indexElement.setType(IndexType.values()[index
	// % IndexType.values().length]);
	// indexElement.addChild(field1);
	// indexElement.addChild(field2);
	// indexes.addChild(indexElement);
	// }
	// Display display = Display.getDefault();
	// Shell shell = new Shell(display, SWT.SHELL_TRIM);
	// shell.setLayout(new GridLayout());
	// shell.setSize(1024, 400);
	// shell.setText("Indexes");
	// IndexListEditor editor = new IndexListEditor(shell, SWT.NONE,
	// indexes);
	// GridDataFactory.fillDefaults().grab(true, true).applyTo(editor);
	// shell.open();
	// while (!shell.isDisposed()) {
	// if (!display.readAndDispatch()) {
	// display.sleep();
	// }
	// }
	// shell.dispose();
	// display.dispose();
	// }
	// });
	// }
}
