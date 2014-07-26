package com.tibco.as.spacebar.ui.wizards.space.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.tibco.as.spacebar.ui.model.Field;
import com.tibco.as.spacebar.ui.wizards.space.AbstractEditElementWizardPage;

public class AddFieldWizardPage extends AbstractEditElementWizardPage<Field> {

	public AddFieldWizardPage(Field field) {
		super("AddFieldWizardPage", null, field);
	}

	@Override
	protected Control getControl(Composite parent, Field edited) {
		FieldEditor fieldEditor = new FieldEditor(parent, SWT.NONE, edited);
		fieldEditor.getNullableButton().setEnabled(false);
		return fieldEditor;
	}

	@Override
	protected String getEmptyNameMessage(Field element) {
		return "Field name cannot be empty";
	}

	@Override
	protected String getExistsMessage(Field element) {
		return "A field with that name already exists";
	}

}