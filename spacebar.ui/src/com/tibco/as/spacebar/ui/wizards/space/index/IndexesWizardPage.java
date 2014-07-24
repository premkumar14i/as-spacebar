package com.tibco.as.spacebar.ui.wizards.space.index;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Control;

import com.tibco.as.spacebar.ui.model.Indexes;
import com.tibco.as.spacebar.ui.wizards.AbstractWizardPage;

public class IndexesWizardPage extends AbstractWizardPage implements
		IListChangeListener {

	private Indexes indexes;
	private IObservableList observe;

	public IndexesWizardPage(Indexes indexes) {
		super("spaceIndexesWizardPage", "Space Indexes", "Enter space indexes");
		this.indexes = indexes;
		observe = BeanProperties.list("children").observe(indexes);
		observe.addListChangeListener(this);
	}

	@Override
	public void dispose() {
		observe.removeListChangeListener(this);
		super.dispose();
	}

	@Override
	protected Control createControl(ScrolledComposite parent) {
		return new IndexListEditor(parent, SWT.NONE, indexes);
	}

	@Override
	public void handleListChange(ListChangeEvent event) {
		getContainer().updateButtons();
	}

}
