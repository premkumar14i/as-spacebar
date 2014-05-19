package com.tibco.as.spacebar.ui.wizards.space.index;

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

import com.tibco.as.spacebar.ui.model.Index;

import com.tibco.as.space.IndexDef.IndexType;

public class IndexEditor extends Composite {

	private DataBindingContext m_bindingContext;
	private Index index;
	private Text nameText;
	private ComboViewer typeCombo;

	public IndexEditor(Composite parent, int style, Index newIndex) {
		this(parent, style);
		setIndex(newIndex);
	}

	public IndexEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		new Label(this, SWT.NONE).setText("Name:");

		nameText = new Text(this, SWT.BORDER | SWT.SINGLE);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		new Label(this, SWT.NONE).setText("Type:");

		typeCombo = new ComboViewer(this, SWT.READ_ONLY);
		typeCombo.setContentProvider(ArrayContentProvider.getInstance());
		typeCombo.setInput(IndexType.values());
		typeCombo.getCombo().setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, true, false));

		if (index != null) {
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
		IObservableValue nameObserveValue = PojoObservables.observeValue(index, "name");
		IObservableValue typeObserveWidget = ViewersObservables
				.observeSingleSelection(typeCombo);
		IObservableValue typeObserveValue = PojoObservables.observeValue(index,
				"type");
		//
		DataBindingContext bindingContext = new DataBindingContext();
		//
		bindingContext.bindValue(nameObserveWidget, nameObserveValue, null,
				null);
		bindingContext.bindValue(typeObserveWidget, typeObserveValue, null,
				null);
		//
		return bindingContext;
	}

	public Index getIndex() {
		return index;
	}

	public void setIndex(Index newIndex) {
		setIndex(newIndex, true);
	}

	public void setIndex(Index newIndex, boolean update) {
		index = newIndex;
		if (update) {
			if (m_bindingContext != null) {
				m_bindingContext.dispose();
				m_bindingContext = null;
			}
			if (index != null) {
				m_bindingContext = initDataBindings();
			}
		}
	}

}
