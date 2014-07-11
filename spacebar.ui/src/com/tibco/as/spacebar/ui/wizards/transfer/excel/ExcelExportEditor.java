package com.tibco.as.spacebar.ui.wizards.transfer.excel;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.tibco.as.spacebar.ui.Messages;

public class ExcelExportEditor extends Composite {

	private DataBindingContext m_bindingContext;
	private com.tibco.as.io.file.excel.ExcelExport excelExport = new com.tibco.as.io.file.excel.ExcelExport();
	private Button headerButton;

	public ExcelExportEditor(Composite parent, int style,
			com.tibco.as.io.file.excel.ExcelExport newExcelExport) {
		this(parent, style);
		setExcelExport(newExcelExport);
	}

	public ExcelExportEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		headerButton = new Button(this, SWT.CHECK | SWT.LEFT);
		headerButton.setText(Messages.Excel_Export_Header);
		headerButton.setToolTipText(Messages.Excel_Export_Header_Tooltip);
		GridDataFactory.defaultsFor(headerButton).span(2, 1)
				.applyTo(headerButton);

		if (excelExport != null) {
			m_bindingContext = initDataBindings();
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private DataBindingContext initDataBindings() {
		IObservableValue headerObserveWidget = SWTObservables
				.observeSelection(headerButton);
		IObservableValue headerObserveValue = PojoObservables.observeValue(
				excelExport, "header");
		//
		DataBindingContext bindingContext = new DataBindingContext();
		//
		bindingContext.bindValue(headerObserveWidget, headerObserveValue, null,
				null);
		//
		return bindingContext;
	}

	public com.tibco.as.io.file.excel.ExcelExport getExcelExport() {
		return excelExport;
	}

	public void setExcelExport(
			com.tibco.as.io.file.excel.ExcelExport newExcelExport) {
		setExcelExport(newExcelExport, true);
	}

	public void setExcelExport(
			com.tibco.as.io.file.excel.ExcelExport newExcelExport,
			boolean update) {
		excelExport = newExcelExport;
		if (update) {
			if (m_bindingContext != null) {
				m_bindingContext.dispose();
				m_bindingContext = null;
			}
			if (excelExport != null) {
				m_bindingContext = initDataBindings();
			}
		}
	}

}
