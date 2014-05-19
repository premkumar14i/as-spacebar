package com.tibco.as.spacebar.ui.wizards.space.field;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Control;

import com.tibco.as.spacebar.ui.model.SpaceFields;
import com.tibco.as.spacebar.ui.wizards.AbstractWizardPage;

public class FieldsWizardPage extends AbstractWizardPage implements
		IListChangeListener {

	private SpaceFields fields;
	private IObservableList observe;

	/**
	 * Create the wizard.
	 */
	public FieldsWizardPage(SpaceFields fields) {
		super("spaceFieldsWizardPage", "Space Fields", "Enter space fields");
		this.fields = fields;
		observe = BeanProperties.list("children").observe(fields);
		observe.addListChangeListener(this);
	}

	@Override
	public void dispose() {
		observe.removeListChangeListener(this);
		super.dispose();
	}

	@Override
	protected Control createControl(ScrolledComposite parent) {
		return new FieldListEditor(parent, SWT.NONE, fields);
	}

	@Override
	public boolean canFlipToNextPage() {
		return fields.getChildren().size() > 0;
	}

	@Override
	public void handleListChange(ListChangeEvent event) {
		getContainer().updateButtons();
	}

}
