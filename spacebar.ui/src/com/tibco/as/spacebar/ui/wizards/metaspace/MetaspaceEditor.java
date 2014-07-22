package com.tibco.as.spacebar.ui.wizards.metaspace;

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

import com.tibco.as.spacebar.ui.model.Metaspace;

public class MetaspaceEditor extends Composite {

	private DataBindingContext m_bindingContext;
	private Metaspace metaspace;
	private Text nameText;
	private Text metaspaceText;
	private Text memberText;
	private Text discoveryText;
	private Button remoteButton;
	private Text listenText;
	private Text timeoutText;
	private Button autoconnectButton;

	public MetaspaceEditor(Composite parent, int style, Metaspace metaspace) {
		this(parent, style);
		setMetaspace(metaspace);
	}

	private MetaspaceEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		new Label(this, SWT.NONE).setText("Name:");

		nameText = new Text(this, SWT.BORDER | SWT.SINGLE);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		new Label(this, SWT.NONE).setText("Metaspace:");

		metaspaceText = new Text(this, SWT.BORDER | SWT.SINGLE);
		metaspaceText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		new Label(this, SWT.NONE).setText("Member:");

		memberText = new Text(this, SWT.BORDER | SWT.SINGLE);
		memberText
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		new Label(this, SWT.NONE).setText("Discovery:");

		discoveryText = new Text(this, SWT.BORDER | SWT.SINGLE);
		discoveryText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		remoteButton = new Button(this, SWT.CHECK | SWT.LEFT);
		GridDataFactory.defaultsFor(remoteButton).span(2, 1)
				.applyTo(remoteButton);
		remoteButton.setText("Remote");

		new Label(this, SWT.NONE).setText("Listen:");

		listenText = new Text(this, SWT.BORDER | SWT.SINGLE);
		listenText
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Label timeoutLabel = new Label(this, SWT.NONE);
		timeoutLabel.setText("Timeout:");

		timeoutText = new Text(this, SWT.BORDER | SWT.SINGLE);
		timeoutText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		autoconnectButton = new Button(this, SWT.CHECK | SWT.LEFT);
		autoconnectButton.setText("Autoconnect");
		GridDataFactory.defaultsFor(remoteButton).span(2, 1)
				.applyTo(autoconnectButton);

		if (metaspace != null) {
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
		IObservableValue nameObserveValue = PojoObservables.observeValue(
				metaspace, "name");
		IObservableValue metaspaceObserveWidget = SWTObservables.observeText(
				metaspaceText, SWT.Modify);
		IObservableValue metaspaceObserveValue = PojoObservables.observeValue(
				metaspace, "metaspaceName");
		IObservableValue memberObserveWidget = SWTObservables.observeText(
				memberText, SWT.Modify);
		IObservableValue memberObserveValue = PojoObservables.observeValue(
				metaspace, "memberName");
		IObservableValue discoveryObserveWidget = SWTObservables.observeText(
				discoveryText, SWT.Modify);
		IObservableValue discoveryObserveValue = PojoObservables.observeValue(
				metaspace, "discovery");
		IObservableValue remoteObserveWidget = SWTObservables
				.observeSelection(remoteButton);
		IObservableValue remoteObserveValue = PojoObservables.observeValue(
				metaspace, "remote");
		IObservableValue listenObserveWidget = SWTObservables.observeText(
				listenText, SWT.Modify);
		IObservableValue listenObserveValue = PojoObservables.observeValue(
				metaspace, "listen");
		IObservableValue timeoutObserveWidget = SWTObservables.observeText(
				timeoutText, SWT.Modify);
		IObservableValue timeoutObserveValue = PojoObservables.observeValue(
				metaspace, "timeout");
		IObservableValue autoconnectObserveWidget = SWTObservables
				.observeSelection(autoconnectButton);
		IObservableValue autoconnectObserveValue = PojoObservables
				.observeValue(metaspace, "autoconnect");
		//
		DataBindingContext bindingContext = new DataBindingContext();
		//
		bindingContext.bindValue(nameObserveWidget, nameObserveValue, null,
				null);
		bindingContext.bindValue(metaspaceObserveWidget, metaspaceObserveValue,
				null, null);
		bindingContext.bindValue(memberObserveWidget, memberObserveValue, null,
				null);
		bindingContext.bindValue(discoveryObserveWidget, discoveryObserveValue,
				null, null);
		bindingContext.bindValue(remoteObserveWidget, remoteObserveValue, null,
				null);
		bindingContext.bindValue(listenObserveWidget, listenObserveValue, null,
				null);
		bindingContext.bindValue(timeoutObserveWidget, timeoutObserveValue,
				null, null);
		bindingContext.bindValue(autoconnectObserveWidget,
				autoconnectObserveValue, null, null);
		//
		return bindingContext;
	}

	public Metaspace getMetaspace() {
		return metaspace;
	}

	public void setMetaspace(Metaspace newMetaspace) {
		setMetaspace(newMetaspace, true);
	}

	public void setMetaspace(Metaspace newMetaspace, boolean update) {
		metaspace = newMetaspace;
		if (update) {
			if (m_bindingContext != null) {
				m_bindingContext.dispose();
				m_bindingContext = null;
			}
			if (metaspace != null) {
				m_bindingContext = initDataBindings();
			}
		}
	}

}
