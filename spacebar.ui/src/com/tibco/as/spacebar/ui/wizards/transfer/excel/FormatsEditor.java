package com.tibco.as.spacebar.ui.wizards.transfer.excel;

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

import com.tibco.as.convert.ConverterFactory.Blob;

import com.tibco.as.spacebar.ui.Messages;

public class FormatsEditor extends Composite {

	private DataBindingContext m_bindingContext;
	private com.tibco.as.spacebar.ui.wizards.transfer.excel.Formats conversion = new com.tibco.as.spacebar.ui.wizards.transfer.excel.Formats();
	private ComboViewer blobFormat;
	private Text dateFormat;

	public FormatsEditor(Composite parent, int style,
			com.tibco.as.spacebar.ui.wizards.transfer.excel.Formats conversion) {
		this(parent, style);
		setExcelConversion(conversion);
	}

	public FormatsEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		Label blobLabel = new Label(this, SWT.NONE);
		blobLabel.setText(Messages.Formats_Blob);
		blobLabel.setToolTipText(Messages.Formats_Blob_Tooltip);

		blobFormat = new ComboViewer(this, SWT.READ_ONLY);
		blobFormat.getCombo().setToolTipText(Messages.Formats_Blob_Tooltip);
		blobFormat.getCombo().setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		blobFormat.setContentProvider(ArrayContentProvider.getInstance());
		blobFormat.setInput(Blob.values());

		Label dateFormatLabel = new Label(this, SWT.NONE);
		dateFormatLabel.setText(Messages.Formats_DateTime);
		dateFormatLabel
				.setToolTipText(Messages.Formats_DateTime_Tooltip);

		dateFormat = new Text(this, SWT.BORDER | SWT.SINGLE);
		dateFormat
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

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
		IObservableValue dateFormatObserveWidget = SWTObservables.observeText(
				dateFormat, SWT.Modify);
		IObservableValue dateFormatObserveValue = PojoObservables.observeValue(
				conversion, "dateFormat");
		//
		DataBindingContext bindingContext = new DataBindingContext();
		//
		bindingContext.bindValue(blobFormatObserveWidget,
				blobFormatObserveValue, null, null);
		bindingContext.bindValue(dateFormatObserveWidget,
				dateFormatObserveValue, null, null);
		//
		return bindingContext;
	}

	public com.tibco.as.spacebar.ui.wizards.transfer.excel.Formats getExcelConversion() {
		return conversion;
	}

	public void setExcelConversion(
			com.tibco.as.spacebar.ui.wizards.transfer.excel.Formats newExcelConversion) {
		setExcelConversion(newExcelConversion, true);
	}

	public void setExcelConversion(
			com.tibco.as.spacebar.ui.wizards.transfer.excel.Formats newExcelConversion,
			boolean update) {
		conversion = newExcelConversion;
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
