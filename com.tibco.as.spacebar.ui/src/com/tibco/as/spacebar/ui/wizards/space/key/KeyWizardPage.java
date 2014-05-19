package com.tibco.as.spacebar.ui.wizards.space.key;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.tibco.as.spacebar.ui.model.Element;
import com.tibco.as.spacebar.ui.model.Field;
import com.tibco.as.spacebar.ui.model.Space;
import com.tibco.as.spacebar.ui.wizards.AbstractWizardPage;
import com.tibco.as.spacebar.ui.wizards.space.DualList;

public class KeyWizardPage extends AbstractWizardPage implements
		IListChangeListener {

	private Space space;
	private IObservableList observe;
	private DualList<Element> dualList;

	public KeyWizardPage(Space space) {
		super("spaceKeyDefWizardPage", "Space Key Definition",
				"Enter space key index type and fields");
		this.space = space;
		setPageComplete(isKeySet());
	}

	@Override
	protected Control createControl(ScrolledComposite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		KeyIndexTypeEditor indexTypeEditor = new KeyIndexTypeEditor(composite,
				SWT.NONE, space.getSpaceDef().getKeyDef());
		GridDataFactory.fillDefaults().grab(true, false)
				.applyTo(indexTypeEditor);
		dualList = new DualList<Element>(composite, SWT.NONE, Element.class,
				"name", space.getFields().getChildren());
		dualList.addSelection(space.getKeys().getChildren());
		observe = BeanProperties.list("children").observe(space.getFields());
		observe.addListChangeListener(dualList);
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
	public boolean canFlipToNextPage() {
		return isKeySet();
	}

	private boolean isKeySet() {
		return !space.getKeys().getChildren().isEmpty();
	}

	@Override
	public void handleListChange(ListChangeEvent event) {
		for (ListDiffEntry entry : event.diff.getDifferences()) {
			Field field = (Field) entry.getElement();
			if (entry.isAddition()) {
				space.getKeys().addChild(field);
			} else {
				space.getKeys().removeChild(field.getName());
			}
		}
		setPageComplete(isKeySet());
	}

}
