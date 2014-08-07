package com.tibco.as.spacebar.ui.wizards.space.field;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.tibco.as.space.FieldDef.FieldType;
import com.tibco.as.spacebar.ui.model.Field;
import com.tibco.as.spacebar.ui.model.Fields;
import com.tibco.as.spacebar.ui.model.IElement;
import com.tibco.as.spacebar.ui.model.SpaceFields;
import com.tibco.as.spacebar.ui.wizards.space.ColumnConfig;
import com.tibco.as.spacebar.ui.wizards.space.ElementListEditor;

public class FieldListEditor extends ElementListEditor {

	private static final String COLUMN_NAME = "Name";
	private static final String COLUMN_TYPE = "Type";
	private static final String COLUMN_NULLABLE = "Nullable";
	private static final String COLUMN_ENCRYPTED = "Encrypted";

	public FieldListEditor(Composite parent, int style, SpaceFields fields) {
		super(parent, style, fields, new ColumnConfig(COLUMN_NAME, SWT.LEFT,
				40, null, true, new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						return ((Field) element).getName();
					}
				}), new ColumnConfig(COLUMN_TYPE, SWT.LEFT, 20, null, true,
				new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						FieldType type = ((Field) element).getType();
						if (type == null) {
							return null;
						}
						return type.name();
					}
				}), new ColumnConfig(COLUMN_NULLABLE, SWT.CENTER, 20, null,
				true, new CheckBoxLabelProvider() {

					protected boolean isChecked(Object element) {
						return ((Field) element).isNullable();
					}

				}), new ColumnConfig(COLUMN_ENCRYPTED, SWT.CENTER, 20, null,
				false, new CheckBoxLabelProvider() {

					protected boolean isChecked(Object element) {
						return ((Field) element).isEncrypted();
					}
				}));
	}

	@Override
	protected IElement newElement(IElement parentElement) {
		Field field = new Field();
		field.setFields((Fields) parentElement);
		field.setName("");
		field.setType(FieldType.STRING);
		field.setNullable(true);
		return field;
	}

	@Override
	protected boolean editElement(IElement original, IElement edited) {
		EditFieldWizard wizard = new EditFieldWizard((Field) original,
				(Field) edited);
		return new WizardDialog(getShell(), wizard).open() == Window.OK;
	}

}
