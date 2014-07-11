package com.tibco.as.spacebar.ui.wizards.space.field;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;

import com.tibco.as.space.FieldDef.FieldType;
import com.tibco.as.spacebar.ui.SWTFactory;
import com.tibco.as.spacebar.ui.model.Field;
import com.tibco.as.spacebar.ui.model.SpaceFields;
import com.tibco.as.spacebar.ui.wizards.space.ListContentProvider;

public class FieldListEditor extends Composite {

	private static final String COLUMN_NAME = "Name";
	private static final String COLUMN_TYPE = "Type";
	private static final String COLUMN_NULLABLE = "Nullable";
	private static final String COLUMN_ENCRYPTED = "Encrypted";
	private TableViewer tableViewer;
	private Button addButton;
	private Button editButton;
	private Button removeButton;
	private Button revertButton;
	private Map<Field, Field> backup;
	private SpaceFields fields;

	public FieldListEditor(Composite parent, int style, SpaceFields fields) {
		super(parent, style);
		Dialog.applyDialogFont(this);
		this.fields = fields;
		this.backup = new HashMap<Field, Field>();
		for (Field field : fields.getChildren()) {
			backup.put(field, field.clone());
		}
		setLayout(new GridLayout(2, false));
		// setLayout(newGridLayout(2, false));
		// Composite innerParent = new Composite(this, SWT.NONE);
		// innerParent.setLayout(newGridLayout(2, false));
		// GridData gd = new GridData(GridData.FILL_BOTH);
		// gd.horizontalSpan = 2;
		// innerParent.setLayoutData(gd);
		//
		// Composite tableComposite = new Composite(innerParent, SWT.NONE);
		// GridData data = new GridData(GridData.FILL_BOTH);
		// tableComposite.setLayoutData(data);

		// TableColumnLayout columnLayout = new TableColumnLayout();
		// tableComposite.setLayout(columnLayout);
		// Table table = new Table(tableComposite, SWT.BORDER | SWT.MULTI
		// | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		//
		// table.setHeaderVisible(true);
		// table.setLinesVisible(true);
		// if (Platform.WS_GTK.equals(Platform.getWS())) {
		// // Removes gray padding around buttons embedded in the table on
		// // GTK, see bug 312240
		// table.setBackgroundMode(SWT.INHERIT_FORCE);
		// }

		tableViewer = new TableViewer(this, SWT.BORDER);
		Table table = tableViewer.getTable();
		GridData tableData = new GridData(GridData.FILL_BOTH);
		tableData.grabExcessHorizontalSpace = true;
		table.setLayoutData(tableData);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setBackgroundMode(SWT.INHERIT_DEFAULT);

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

		TableViewerColumn nameColumn = createColumn(tableViewer, COLUMN_NAME);
		nameColumn.getColumn().setWidth(300);
		nameColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Field) element).getName();
			}
		});
		TableViewerColumn typeColumn = createColumn(tableViewer, COLUMN_TYPE);
		typeColumn.getColumn().setWidth(100);
		typeColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				FieldType type = ((Field) element).getType();
				if (type == null) {
					return null;
				}
				return type.name();
			}
		});
		TableViewerColumn nullableColumn = createColumn(tableViewer,
				COLUMN_NULLABLE);
		nullableColumn.getColumn().pack();
		nullableColumn.setLabelProvider(new CheckBoxLabelProvider() {

			protected boolean isChecked(Object element) {
				return ((Field) element).isNullable();
			}

		});

		TableViewerColumn encryptedColumn = createColumn(tableViewer,
				COLUMN_ENCRYPTED);
		encryptedColumn.getColumn().pack();
		encryptedColumn.setLabelProvider(new CheckBoxLabelProvider() {

			protected boolean isChecked(Object element) {
				return ((Field) element).isEncrypted();
			}

		});

		// tableViewer.setLabelProvider(new FieldLabelProvider());
		tableViewer.setContentProvider(new ListContentProvider());

		tableViewer.setInput(fields.getChildren());

		// updateButtons();
		// innerParent.layout();
	}

	private TableViewerColumn createColumn(TableViewer viewer, String columnName) {
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.CENTER);
		column.getColumn().setText(columnName);
		return column;
	}

	private void revert() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer
				.getSelection();
		Iterator<?> elements = selection.iterator();
		while (elements.hasNext()) {
			Field field = (Field) elements.next();
			Field original = backup.get(field);
			if (original != null) {
				original.copyTo(field);
			}
		}
		updateButtons();
		tableViewer.refresh();
	}

	private void remove() {
		for (Object element : ((IStructuredSelection) tableViewer
				.getSelection()).toArray()) {
			fields.removeChild((Field) element);
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
			Field field = (Field) it.next();
			if (isRevertable(field)) {
				canRevert = true;
				break;
			}
		}

		editButton.setEnabled(selectionCount == 1);
		removeButton.setEnabled(selectionCount > 0
				&& selectionCount <= itemCount);
		revertButton.setEnabled(canRevert);
	}

	private boolean isRevertable(Field field) {
		return backup.containsKey(field);
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
		Field field = new Field(fields);
		field.setName("");
		field.setType(FieldType.STRING);
		field.setNullable(true);
		if (editField(null, field)) {
			fields.addChild(field);
			tableViewer.refresh();
			tableViewer.setSelection(new StructuredSelection(field));
		}
	}

	/**
	 * Creates the edit dialog. Subclasses may override this method to provide a
	 * custom dialog.
	 * 
	 */
	protected boolean editField(Field original, Field field) {
		EditFieldWizard wizard = new EditFieldWizard(original, field);
		return new WizardDialog(getShell(), wizard).open() == Window.OK;
	}

	private void edit() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer
				.getSelection();

		Object[] objects = selection.toArray();
		if ((objects == null) || (objects.length != 1))
			return;

		Field field = (Field) selection.getFirstElement();
		Field editedField = field.clone();
		if (editField(field, editedField)) {
			editedField.copyTo(field);
			tableViewer.refresh(field);
			updateButtons();
			tableViewer.setSelection(new StructuredSelection(field));
		}
	}

	// public static void main(String[] args) {
	// Display display = new Display();
	//
	// Shell shell = new Shell(display);
	// shell.setLayout(new GridLayout());
	// Space space = new Space(new Spaces(new Metaspace(new Metaspaces())));
	// space.setSpaceDef(SpaceDef.create());
	// SpaceFields fields = new SpaceFields(space);
	// Field field1 = new Field(fields);
	// field1.setName("field1");
	// field1.setType(FieldType.DATETIME);
	// field1.setNullable(true);
	// fields.addChild(field1);
	// Field field2 = new Field(fields);
	// field2.setName("field2");
	// field2.setType(FieldType.INTEGER);
	// field2.setEncrypted(true);
	// fields.addChild(field2);
	// new FieldListEditor(shell, SWT.NONE, fields);
	// shell.open();
	//
	// while (!shell.isDisposed()) {
	// if (!display.readAndDispatch())
	// display.sleep();
	// }
	//
	// display.dispose();
	// }
}
