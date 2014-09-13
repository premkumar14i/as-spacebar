package com.tibco.as.spacebar.ui.wizards.transfer;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
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

import com.tibco.as.io.AbstractExport;
import com.tibco.as.space.browser.BrowserDef.BrowserType;
import com.tibco.as.space.browser.BrowserDef.TimeScope;
import com.tibco.as.spacebar.ui.preferences.Preferences;

public class ExportEditor extends Composite {

	private DataBindingContext m_bindingContext;
	private AbstractExport export;
	protected ComboViewer timeScopeCombo;
	protected ComboViewer browserTypeCombo;
	protected Text timeoutText;
	protected Text prefetchText;
	protected Text queryLimitText;
	protected Text filterText;

	public ExportEditor(Composite parent, int style, AbstractExport export) {
		super(parent, style);
		this.export = export;
		setLayout(new GridLayout(2, false));

		new Label(this, SWT.NONE).setText("Browser type:");

		browserTypeCombo = new ComboViewer(this, SWT.READ_ONLY);
		browserTypeCombo.getCombo().setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		browserTypeCombo.setContentProvider(ArrayContentProvider.getInstance());
		browserTypeCombo.setInput(BrowserType.values());

		new Label(this, SWT.NONE).setText("Time scope:");

		timeScopeCombo = new ComboViewer(this, SWT.READ_ONLY);
		timeScopeCombo.getCombo().setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		timeScopeCombo.setContentProvider(ArrayContentProvider.getInstance());
		timeScopeCombo.setInput(TimeScope.values());

		new Label(this, SWT.NONE).setText("Timeout:");

		timeoutText = new Text(this, SWT.BORDER | SWT.SINGLE);
		timeoutText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		new Label(this, SWT.NONE).setText("Prefetch:");

		prefetchText = new Text(this, SWT.BORDER | SWT.SINGLE);
		prefetchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		new Label(this, SWT.NONE).setText("Query limit:");

		queryLimitText = new Text(this, SWT.BORDER | SWT.SINGLE);
		queryLimitText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		Label filterLabel = new Label(this, SWT.NONE);
		GridData filterLabelGridData = new GridData();
		filterLabelGridData.verticalIndent = 3;
		filterLabelGridData.verticalAlignment = SWT.TOP;
		filterLabel.setLayoutData(filterLabelGridData);
		filterLabel.setText("Filter:");

		filterText = new Text(this, SWT.WRAP | SWT.MULTI);
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridData.heightHint = 5 * filterText.getLineHeight();
		filterText.setLayoutData(gridData);

		if (this.export != null) {
			m_bindingContext = initDataBindings();
		}
		timeScopeUpdate();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private DataBindingContext initDataBindings() {
		IObservableValue browserTypeObserveWidget = ViewersObservables
				.observeSingleSelection(browserTypeCombo);
		IObservableValue browserTypeObserveValue = PojoObservables
				.observeValue(export, "browserType");
		browserTypeObserveValue
				.addValueChangeListener(new IValueChangeListener() {

					@Override
					public void handleValueChange(ValueChangeEvent event) {
						browseTypeUpdate();

					}
				});
		IObservableValue timeScopeObserveWidget = ViewersObservables
				.observeSingleSelection(timeScopeCombo);
		IObservableValue timeScopeObserveValue = PojoObservables.observeValue(
				export, "timeScope");
		timeScopeObserveValue
				.addValueChangeListener(new IValueChangeListener() {

					@Override
					public void handleValueChange(ValueChangeEvent event) {
						timeScopeUpdate();
					}

				});
		IObservableValue timeoutObserveWidget = SWTObservables.observeText(
				timeoutText, SWT.Modify);
		IObservableValue timeoutObserveValue = PojoObservables.observeValue(
				export, "timeout");
		IObservableValue prefetchObserveWidget = SWTObservables.observeText(
				prefetchText, SWT.Modify);
		IObservableValue prefetchObserveValue = PojoObservables.observeValue(
				export, "prefetch");
		IObservableValue queryLimitObserveWidget = SWTObservables.observeText(
				queryLimitText, SWT.Modify);
		IObservableValue queryLimitObserveValue = PojoObservables.observeValue(
				export, "queryLimit");
		IObservableValue filterObserveWidget = SWTObservables.observeText(
				filterText, SWT.Modify);
		IObservableValue filterObserveValue = PojoObservables.observeValue(
				export, "filter");
		//
		DataBindingContext bindingContext = new DataBindingContext();
		//
		bindingContext.bindValue(browserTypeObserveWidget,
				browserTypeObserveValue, null, null);
		bindingContext.bindValue(timeScopeObserveWidget, timeScopeObserveValue,
				null, null);
		bindingContext.bindValue(timeoutObserveWidget, timeoutObserveValue,
				null, null);
		bindingContext.bindValue(prefetchObserveWidget, prefetchObserveValue,
				null, null);
		bindingContext.bindValue(queryLimitObserveWidget,
				queryLimitObserveValue, null, null);
		bindingContext.bindValue(filterObserveWidget, filterObserveValue, null,
				null);
		//
		return bindingContext;
	}

	protected void browseTypeUpdate() {
		BrowserType browseType = export.getBrowserType();
		if (browseType == BrowserType.TAKE) {
			timeScopeCombo.getCombo().setEnabled(false);
		} else {
			timeScopeCombo.getCombo().setEnabled(true);
		}
	}

	protected void timeScopeUpdate() {
		TimeScope timeScope = export.getTimeScope();
		if (timeScope == null) {
			return;
		}
		String name = timeScope.name();
		if (Preferences.TIMESCOPE_ALL.equals(name)) {
			timeoutText.setEnabled(true);
			queryLimitText.setEnabled(true);
			prefetchText.setEnabled(true);
		} else if (Preferences.TIMESCOPE_NEW.equals(name)) {
			timeoutText.setEnabled(true);
			queryLimitText.setEnabled(false);
			prefetchText.setEnabled(false);
		} else if (Preferences.TIMESCOPE_CURRENT.equals(name)) {
			prefetchText.setEnabled(true);
			timeoutText.setEnabled(false);
			queryLimitText.setEnabled(false);
		} else if (Preferences.TIMESCOPE_SNAPSHOT.equals(name)) {
			prefetchText.setEnabled(true);
			queryLimitText.setEnabled(true);
			timeoutText.setEnabled(false);
		}
	}

	public AbstractExport getExport() {
		return export;
	}

	public void setExportBean(AbstractExport newExportBean) {
		setExportBean(newExportBean, true);
	}

	public void setExportBean(AbstractExport newExportBean, boolean update) {
		export = newExportBean;
		if (update) {
			if (m_bindingContext != null) {
				m_bindingContext.dispose();
				m_bindingContext = null;
			}
			if (export != null) {
				m_bindingContext = initDataBindings();
			}
		}
	}

}
