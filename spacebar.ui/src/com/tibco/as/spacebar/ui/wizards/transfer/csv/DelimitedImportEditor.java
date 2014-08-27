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

public class DelimitedImportEditor extends Composite {

	private DataBindingContext m_bindingContext;
	private com.tibco.as.file.text.delimited.DelimitedImport delimitedImport = new com.tibco.as.file.text.delimited.DelimitedImport();
	private Text escapeText;
	private Button headerButton;
	private Button ignoreLeadingWhiteSpaceButton;
	private Text quotecharText;
	private Text separatorText;
	private Button strictQuotesButton;

	public DelimitedImportEditor(
			Composite parent,
			int style,
			com.tibco.as.file.text.delimited.DelimitedImport newDelimitedImport) {
		this(parent, style);
		setDelimitedImport(newDelimitedImport);
	}

	public DelimitedImportEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		headerButton = new Button(this, SWT.CHECK | SWT.LEFT);
		headerButton.setText(Messages.Delimited_Import_Header);
		headerButton.setToolTipText(Messages.Delimited_Import_Header_Tooltip);
		GridDataFactory.defaultsFor(headerButton).span(2, 1)
				.applyTo(headerButton);

		strictQuotesButton = new Button(this, SWT.CHECK | SWT.LEFT);
		strictQuotesButton.setText(Messages.Delimited_Import_StrictQuotes);
		strictQuotesButton.setToolTipText(Messages.Delimited_Import_StrictQuotes_Tooltip);
		GridDataFactory.defaultsFor(headerButton).span(2, 1)
				.applyTo(strictQuotesButton);

		ignoreLeadingWhiteSpaceButton = new Button(this, SWT.CHECK | SWT.LEFT);
		ignoreLeadingWhiteSpaceButton.setText(Messages.Delimited_Import_IgnoreLeadingWhiteSpace);
		ignoreLeadingWhiteSpaceButton.setToolTipText(Messages.Delimited_Import_IgnoreLeadingWhiteSpace_Tooltip);
		GridDataFactory.defaultsFor(headerButton).span(2, 1)
				.applyTo(ignoreLeadingWhiteSpaceButton);

		Label separatorLabel = new Label(this, SWT.NONE);
		separatorLabel.setText(Messages.Delimited_Import_Separator);
		separatorLabel.setToolTipText(Messages.Delimited_Import_Separator_Tooltip);

		separatorText = new Text(this, SWT.BORDER | SWT.SINGLE);
		separatorText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		separatorText.setToolTipText(Messages.Delimited_Import_Separator_Tooltip);

		Label quoteLabel = new Label(this, SWT.NONE);
		quoteLabel.setText(Messages.Delimited_Import_Quote);
		quoteLabel.setToolTipText(Messages.Delimited_Import_Quote_Tooltip);

		quotecharText = new Text(this, SWT.BORDER | SWT.SINGLE);
		quotecharText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		quotecharText.setToolTipText(Messages.Delimited_Import_Quote_Tooltip);

		Label escapeLabel = new Label(this, SWT.NONE);
		escapeLabel.setText(Messages.Delimited_Import_Escape);
		escapeLabel.setToolTipText(Messages.Delimited_Import_Escape_Tooltip);

		escapeText = new Text(this, SWT.BORDER | SWT.SINGLE);
		escapeText
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		escapeText.setToolTipText(Messages.Delimited_Import_Escape_Tooltip);

		if (delimitedImport != null) {
			m_bindingContext = initDataBindings();
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private DataBindingContext initDataBindings() {
		IObservableValue escapeObserveWidget = SWTObservables.observeText(
				escapeText, SWT.Modify);
		IObservableValue escapeObserveValue = PojoObservables.observeValue(
				delimitedImport, "escapeChar");
		IObservableValue headerObserveWidget = SWTObservables
				.observeSelection(headerButton);
		IObservableValue headerObserveValue = PojoObservables.observeValue(
				delimitedImport, "header");
		IObservableValue ignoreLeadingWhiteSpaceObserveWidget = SWTObservables
				.observeSelection(ignoreLeadingWhiteSpaceButton);
		IObservableValue ignoreLeadingWhiteSpaceObserveValue = PojoObservables
				.observeValue(delimitedImport, "ignoreLeadingWhiteSpace");
		IObservableValue quotecharObserveWidget = SWTObservables.observeText(
				quotecharText, SWT.Modify);
		IObservableValue quotecharObserveValue = PojoObservables.observeValue(
				delimitedImport, "quoteChar");
		IObservableValue separatorObserveWidget = SWTObservables.observeText(
				separatorText, SWT.Modify);
		IObservableValue separatorObserveValue = PojoObservables.observeValue(
				delimitedImport, "separator");
		IObservableValue strictQuotesObserveWidget = SWTObservables
				.observeSelection(strictQuotesButton);
		IObservableValue strictQuotesObserveValue = PojoObservables
				.observeValue(delimitedImport, "strictQuotes");
		//
		DataBindingContext bindingContext = new DataBindingContext();
		//
		bindingContext.bindValue(escapeObserveWidget, escapeObserveValue, null,
				null);
		bindingContext.bindValue(headerObserveWidget, headerObserveValue, null,
				null);
		bindingContext.bindValue(ignoreLeadingWhiteSpaceObserveWidget,
				ignoreLeadingWhiteSpaceObserveValue, null, null);
		bindingContext.bindValue(quotecharObserveWidget, quotecharObserveValue,
				null, null);
		bindingContext.bindValue(separatorObserveWidget, separatorObserveValue,
				null, null);
		bindingContext.bindValue(strictQuotesObserveWidget,
				strictQuotesObserveValue, null, null);
		//
		return bindingContext;
	}

	public com.tibco.as.file.text.delimited.DelimitedImport getDelimitedImport() {
		return delimitedImport;
	}

	public void setDelimitedImport(
			com.tibco.as.file.text.delimited.DelimitedImport newDelimitedImport) {
		setDelimitedImport(newDelimitedImport, true);
	}

	public void setDelimitedImport(
			com.tibco.as.file.text.delimited.DelimitedImport newDelimitedImport,
			boolean update) {
		delimitedImport = newDelimitedImport;
		if (update) {
			if (m_bindingContext != null) {
				m_bindingContext.dispose();
				m_bindingContext = null;
			}
			if (delimitedImport != null) {
				m_bindingContext = initDataBindings();
			}
		}
	}

}
