package com.tibco.as.spacebar.ui.wizards.space.field;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.tibco.as.space.FieldDef.FieldType;
import com.tibco.as.util.Utils;

public class FieldEditor extends Composite {

	private DataBindingContext m_bindingContext;
	private com.tibco.as.spacebar.ui.model.Field field;
	private Text nameText;
	private Button nullableButton;
	private Button encryptedButton;
	private ComboViewer typeCombo;

	public FieldEditor(Composite parent, int style,
			com.tibco.as.spacebar.ui.model.Field newField) {
		this(parent, style);
		setField(newField);
	}

	public FieldEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		new Label(this, SWT.NONE).setText("Name:");

		nameText = new Text(this, SWT.BORDER | SWT.SINGLE);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		new Label(this, SWT.NONE).setText("Type:");

		typeCombo = new ComboViewer(this, SWT.READ_ONLY);
		typeCombo.getCombo().setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, true, false));
		typeCombo.setContentProvider(ArrayContentProvider.getInstance());
		typeCombo.setInput(FieldType.values());

		nullableButton = new Button(this, SWT.CHECK | SWT.LEFT);
		nullableButton.setText("Nullable");
		GridDataFactory.defaultsFor(nullableButton).span(2, 1)
				.applyTo(nullableButton);
		GridData nullableGridData = new GridData(SWT.FILL, SWT.CENTER, true,
				false);
		nullableGridData.horizontalSpan = 2;
		nullableButton.setLayoutData(nullableGridData);

		encryptedButton = new Button(this, SWT.CHECK | SWT.LEFT);
		encryptedButton.setText("Encrypted");
		GridDataFactory.defaultsFor(encryptedButton).span(2, 1)
				.applyTo(encryptedButton);
		encryptedButton.setVisible(Utils.hasFieldDefMethod("setEncrypted"));
		if (field != null) {
			m_bindingContext = initDataBindings();
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private DataBindingContext initDataBindings() {
		IObservableValue nameObserveWidget = SWTObservables.observeText(
				nameText, SWT.Modify);
		IObservableValue nameObserveValue = PojoObservables.observeValue(field,
				"name");
		IObservableValue nullableObserveWidget = SWTObservables
				.observeSelection(nullableButton);
		IObservableValue nullableObserveValue = PojoObservables.observeValue(
				field, "nullable");
		IObservableValue encryptedObserveWidget = SWTObservables
				.observeSelection(encryptedButton);
		IObservableValue encryptedObserveValue = PojoObservables.observeValue(
				field, "encrypted");
		IObservableValue typeObserveWidget = ViewersObservables
				.observeSingleSelection(typeCombo);
		IObservableValue typeObserveValue = PojoObservables.observeValue(field,
				"type");
		//
		DataBindingContext bindingContext = new DataBindingContext();
		//
		bindingContext.bindValue(nameObserveWidget, nameObserveValue, null,
				null);
		bindingContext.bindValue(nullableObserveWidget, nullableObserveValue,
				null, null);
		bindingContext.bindValue(encryptedObserveWidget, encryptedObserveValue,
				null, null);
		bindingContext.bindValue(typeObserveWidget, typeObserveValue, null,
				null);
		//
		return bindingContext;
	}

	public com.tibco.as.spacebar.ui.model.Field getField() {
		return field;
	}

	public void setField(com.tibco.as.spacebar.ui.model.Field newField) {
		setField(newField, true);
	}

	public void setField(com.tibco.as.spacebar.ui.model.Field newField,
			boolean update) {
		field = newField;
		if (update) {
			if (m_bindingContext != null) {
				m_bindingContext.dispose();
				m_bindingContext = null;
			}
			if (field != null) {
				m_bindingContext = initDataBindings();
			}
		}
	}

}
