package com.tibco.as.spacebar.ui.wizards.transfer.csv;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.tibco.as.spacebar.ui.Messages;

import com.tibco.as.convert.ConverterFactory.Blob;

public class FormatsEditor extends Composite {

	private DataBindingContext m_bindingContext;
	private com.tibco.as.spacebar.ui.wizards.transfer.csv.Formats conversion = new com.tibco.as.spacebar.ui.wizards.transfer.csv.Formats();
	private ComboViewer blobFormat;
	private Text booleanFormat;
	private Text dateFormat;
	private Text numberFormat;

	public FormatsEditor(Composite parent, int style,
			com.tibco.as.spacebar.ui.wizards.transfer.csv.Formats conversion) {
		this(parent, style);
		setCSVConversion(conversion);
	}

	public FormatsEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		Label blobLabel = new Label(this, SWT.NONE);
		blobLabel.setText(Messages.Formats_Blob);
		blobLabel.setToolTipText(Messages.Formats_Blob_Tooltip);

		blobFormat = new ComboViewer(this, SWT.READ_ONLY);
		blobFormat.getCombo().setToolTipText(
				Messages.Formats_Blob_Tooltip);
		blobFormat.getCombo().setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		blobFormat.setContentProvider(ArrayContentProvider.getInstance());
		blobFormat.setInput(Blob.values());

		Label booleanLabel = new Label(this, SWT.NONE);
		booleanLabel.setText(Messages.Formats_Boolean);
		booleanLabel.setToolTipText(Messages.Formats_Boolean_Tooltip);

		booleanFormat = new Text(this, SWT.BORDER | SWT.SINGLE);
		booleanFormat.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		booleanFormat
				.setToolTipText(Messages.Formats_Boolean_Tooltip);

		Label dateLabel = new Label(this, SWT.NONE);
		dateLabel.setText(Messages.Formats_DateTime);
		dateLabel.setToolTipText(Messages.Formats_DateTime_Tooltip);

		dateFormat = new Text(this, SWT.BORDER | SWT.SINGLE);
		dateFormat
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		dateFormat.setToolTipText(Messages.Formats_DateTime_Tooltip);

		Label numberLabel = new Label(this, SWT.NONE);
		numberLabel.setText(Messages.Formats_Number);
		numberLabel.setToolTipText(Messages.Formats_Number_Tooltip);

		numberFormat = new Text(this, SWT.BORDER | SWT.SINGLE);
		numberFormat.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		numberFormat.setToolTipText(Messages.Formats_Number_Tooltip);

		if (conversion != null) {
			m_bindingContext = initDataBindings();
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private DataBindingContext initDataBindings() {
		IObservableValue blobFormatObserveWidget = ViewersObservables
				.observeSingleSelection(blobFormat);
		IObservableValue blobFormatObserveValue = PojoObservables.observeValue(
				conversion, "blobFormat");
		IObservableValue booleanFormatObserveWidget = SWTObservables
				.observeText(booleanFormat, SWT.Modify);
		IObservableValue booleanFormatObserveValue = PojoObservables
				.observeValue(conversion, "booleanFormat");
		IObservableValue dateFormatObserveWidget = SWTObservables.observeText(
				dateFormat, SWT.Modify);
		IObservableValue dateFormatObserveValue = PojoObservables.observeValue(
				conversion, "dateFormat");
		IObservableValue numberFormatObserveWidget = SWTObservables
				.observeText(numberFormat, SWT.Modify);
		IObservableValue numberFormatObserveValue = PojoObservables
				.observeValue(conversion, "numberFormat");

		//
		DataBindingContext bindingContext = new DataBindingContext();
		//
		bindingContext.bindValue(blobFormatObserveWidget,
				blobFormatObserveValue, null, null);
		bindingContext.bindValue(booleanFormatObserveWidget,
				booleanFormatObserveValue, null, null);
		bindingContext.bindValue(dateFormatObserveWidget,
				dateFormatObserveValue, null, null);
		bindingContext.bindValue(numberFormatObserveWidget,
				numberFormatObserveValue, null, null);
		//
		return bindingContext;
	}

	public com.tibco.as.spacebar.ui.wizards.transfer.csv.Formats getCSVConversion() {
		return conversion;
	}

	public void setCSVConversion(
			com.tibco.as.spacebar.ui.wizards.transfer.csv.Formats newCSVConversion) {
		setCSVConversion(newCSVConversion, true);
	}

	public void setCSVConversion(
			com.tibco.as.spacebar.ui.wizards.transfer.csv.Formats newCSVConversion,
			boolean update) {
		conversion = newCSVConversion;
		if (update) {
			if (m_bindingContext != null) {
				m_bindingContext.dispose();
				m_bindingContext = null;
			}
			if (conversion != null) {
				m_bindingContext = initDataBindings();
			}
		}
	}

}
