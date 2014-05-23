package com.tibco.as.spacebar.ui.wizards.space.index;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.tibco.as.spacebar.ui.SWTFactory;
import com.tibco.as.spacebar.ui.model.Element;
import com.tibco.as.spacebar.ui.model.Field;
import com.tibco.as.spacebar.ui.model.Index;
import com.tibco.as.spacebar.ui.model.SpaceFields;
import com.tibco.as.spacebar.ui.wizards.space.AbstractEditElementWizardPage;
import com.tibco.as.spacebar.ui.wizards.space.DualList;

public class EditIndexWizardPage extends AbstractEditElementWizardPage<Index>
		implements IListChangeListener {

	private DualList<Element> dualList;
	private IObservableList observe;

	public EditIndexWizardPage(Index original, Index edited) {
		super("EditIndexWizardPage", original, edited);
	}

	@Override
	protected Control getControl(Composite parent, Index edited) {
		Composite composite = SWTFactory.createComposite(parent, 1, 1,
				GridData.FILL_BOTH);
		Index index = getEdited();
		IndexEditor editor = new IndexEditor(composite, SWT.NONE, index);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(editor);
		SpaceFields fields = index.getParent().getParent().getFields();
		dualList = new DualList<Element>(composite, SWT.NONE, Element.class,
				"name", fields.getChildren());
		observe = BeanProperties.list("children").observe(fields);
		observe.addListChangeListener(dualList);
		dualList.setSelection(index.getChildren());
		dualList.getSelection().addListChangeListener(this);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(dualList);
		return composite;
	}

	@Override
	public void dispose() {
		observe.removeListChangeListener(dualList);
		super.dispose();
	}

	@Override
	protected String getEmptyNameMessage(Index element) {
		return "Index name cannot be empty";
	}

	@Override
	protected String getExistsMessage(Index element) {
		return "An index with that name already exists";
	}

	@Override
	public void handleListChange(ListChangeEvent event) {
		Index index = getEdited();
		for (ListDiffEntry entry : event.diff.getDifferences()) {
			Field field = (Field) entry.getElement();
			if (entry.isAddition()) {
				index.addChild(field);
			} else {
				index.removeChild(field.getName());
			}
		}
		validate();
	}

	@Override
	protected void validate() {
		if (getEdited().getChildren().isEmpty()) {
			setErrorMessage("No field selected");
			setPageComplete(false);
		} else {
			super.validate();
		}
	}

}