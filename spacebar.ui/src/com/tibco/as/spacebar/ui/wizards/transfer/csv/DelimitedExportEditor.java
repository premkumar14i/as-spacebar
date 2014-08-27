package com.tibco.as.spacebar.ui.wizards.transfer.csv;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.tibco.as.spacebar.ui.Messages;

public class DelimitedExportEditor extends Composite {

	private DataBindingContext m_bindingContext;
	private com.tibco.as.file.text.delimited.DelimitedExport delimitedExport = new com.tibco.as.file.text.delimited.DelimitedExport();
	private Text escapeCharText;
	private Button headerButton;
	private Text quoteCharText;
	private Text separatorText;

	public DelimitedExportEditor(
			Composite parent,
			int style,
			com.tibco.as.file.text.delimited.DelimitedExport newDelimitedExport) {
		this(parent, style);
		setDelimitedExport(newDelimitedExport);
	}

	public DelimitedExportEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		headerButton = new Button(this, SWT.CHECK | SWT.LEFT);
		headerButton.setText(Messages.Delimited_Export_Header);
		headerButton.setToolTipText(Messages.Delimited_Export_Header_Tooltip);
		GridDataFactory.defaultsFor(headerButton).span(2, 1)
				.applyTo(headerButton);

		Label separatorLabel = new Label(this, SWT.NONE);
		separatorLabel.setText(Messages.Delimited_Export_Separator);
		separatorLabel
				.setToolTipText(Messages.Delimited_Export_Separator_Tooltip);

		separatorText = new Text(this, SWT.BORDER | SWT.SINGLE);
		separatorText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		separatorText
				.setToolTipText(Messages.Delimited_Export_Separator_Tooltip);

		Label quoteLabel = new Label(this, SWT.NONE);
		quoteLabel.setText(Messages.Delimited_Export_Quote);
		quoteLabel.setToolTipText(Messages.Delimited_Export_Quote_Tooltip);

		quoteCharText = new Text(this, SWT.BORDER | SWT.SINGLE);
		quoteCharText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		quoteCharText.setToolTipText(Messages.Delimited_Export_Quote_Tooltip);

		Label escapeLabel = new Label(this, SWT.NONE);
		escapeLabel.setText(Messages.Delimited_Export_Escape);
		escapeLabel.setToolTipText(Messages.Delimited_Export_Escape_Tooltip);

		escapeCharText = new Text(this, SWT.BORDER | SWT.SINGLE);
		escapeCharText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		escapeCharText.setToolTipText(Messages.Delimited_Export_Escape_Tooltip);

		if (delimitedExport != null) {
			m_bindingContext = initDataBindings();
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private DataBindingContext initDataBindings() {
		IObservableValue escapeCharObserveWidget = SWTObservables.observeText(
				escapeCharText, SWT.Modify);
		IObservableValue escapeCharObserveValue = PojoObservables.observeValue(
				delimitedExport, "escapeChar");
		IObservableValue headerObserveWidget = SWTObservables
				.observeSelection(headerButton);
		IObservableValue headerObserveValue = PojoObservables.observeValue(
				delimitedExport, "header");
		IObservableValue quoteCharObserveWidget = SWTObservables.observeText(
				quoteCharText, SWT.Modify);
		IObservableValue quoteCharObserveValue = PojoObservables.observeValue(
				delimitedExport, "quoteChar");
		IObservableValue separatorObserveWidget = SWTObservables.observeText(
				separatorText, SWT.Modify);
		IObservableValue separatorObserveValue = PojoObservables.observeValue(
				delimitedExport, "separator");
		//
		DataBindingContext bindingContext = new DataBindingContext();
		//
		bindingContext.bindValue(escapeCharObserveWidget,
				escapeCharObserveValue, null, null);
		bindingContext.bindValue(headerObserveWidget, headerObserveValue, null,
				null);
		bindingContext.bindValue(quoteCharObserveWidget, quoteCharObserveValue,
				null, null);
		bindingContext.bindValue(separatorObserveWidget, separatorObserveValue,
				null, null);
		//
		return bindingContext;
	}

	public com.tibco.as.file.text.delimited.DelimitedExport getDelimitedExport() {
		return delimitedExport;
	}

	public void setDelimitedExport(
			com.tibco.as.file.text.delimited.DelimitedExport newDelimitedExport) {
		setDelimitedExport(newDelimitedExport, true);
	}

	public void setDelimitedExport(
			com.tibco.as.file.text.delimited.DelimitedExport newDelimitedExport,
			boolean update) {
		delimitedExport = newDelimitedExport;
		if (update) {
			if (m_bindingContext != null) {
				m_bindingContext.dispose();
				m_bindingContext = null;
			}
			if (delimitedExport != null) {
				m_bindingContext = initDataBindings();
			}
		}
	}

}
