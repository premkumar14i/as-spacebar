package com.tibco.as.spacebar.ui.wizards.space.key;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.tibco.as.space.IndexDef.IndexType;
import com.tibco.as.space.KeyDef;

public class KeyIndexTypeEditor extends Composite {

	private DataBindingContext m_bindingContext;
	private KeyDef keys;
	private ComboViewer keyIndexTypeCombo;

	public KeyIndexTypeEditor(Composite parent, int style, KeyDef spaceDef) {
		this(parent, style);
		setKeys(spaceDef);
	}

	public KeyIndexTypeEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		new Label(this, SWT.NONE).setText("Index type:");

		keyIndexTypeCombo = new ComboViewer(this, SWT.READ_ONLY);
		keyIndexTypeCombo.getCombo().setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, true, false));
		keyIndexTypeCombo
				.setContentProvider(ArrayContentProvider.getInstance());
		keyIndexTypeCombo.setInput(IndexType.values());

		if (keys != null) {
			m_bindingContext = initDataBindings();
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private DataBindingContext initDataBindings() {
		IObservableValue keyIndexTypeObserveWidget = ViewersObservables
				.observeSingleSelection(keyIndexTypeCombo);
		IObservableValue keyIndexTypeObserveValue = PojoObservables
				.observeValue(keys, "indexType");
		//
		DataBindingContext bindingContext = new DataBindingContext();
		//
		bindingContext.bindValue(keyIndexTypeObserveWidget,
				keyIndexTypeObserveValue, null, null);
		//
		return bindingContext;
	}

	public KeyDef getKeys() {
		return keys;
	}

	public void setKeys(KeyDef newKeys) {
		setKeys(newKeys, true);
	}

	public void setKeys(KeyDef newKeys, boolean update) {
		keys = newKeys;
		if (update) {
			if (m_bindingContext != null) {
				m_bindingContext.dispose();
				m_bindingContext = null;
			}
			if (keys != null) {
				m_bindingContext = initDataBindings();
			}
		}
	}

}
