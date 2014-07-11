package com.tibco.as.spacebar.ui.wizards.space.distribution;

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

import com.tibco.as.spacebar.ui.model.Field;
import com.tibco.as.spacebar.ui.model.Space;
import com.tibco.as.spacebar.ui.wizards.AbstractWizardPage;
import com.tibco.as.spacebar.ui.wizards.space.DualList;

public class DistributionWizardPage extends AbstractWizardPage implements
		IListChangeListener {

	private Space space;
	private IObservableList observe;
	private DualList<Field> dualList;

	public DistributionWizardPage(Space space) {
		super("spaceDistributionWizardPage", "Space Distribution",
				"Select space distribution fields");
		this.space = space;
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createControl(ScrolledComposite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		dualList = new DualList<Field>(composite, SWT.NONE, Field.class,
				"name", space.getKeys().getChildren());
		dualList.addSelection(space.getDistribution().getChildren());
		observe = BeanProperties.list("children").observe(space.getKeys());
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
	public void handleListChange(ListChangeEvent event) {
		for (ListDiffEntry entry : event.diff.getDifferences()) {
			Field field = (Field) entry.getElement();
			if (entry.isAddition()) {
				space.getDistribution().addChild(field);
			} else {
				space.getDistribution().removeChild(field);
			}
		}
	}
}
