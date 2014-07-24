package com.tibco.as.spacebar.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;

public abstract class AbstractWizardPage extends WizardPage {

	protected AbstractWizardPage(String pageName, String title,
			String description) {
		super(pageName);
		setTitle(title);
		setDescription(description);
	}

	protected AbstractWizardPage(String pageName, String title,
			String description, Image image) {
		this(pageName, title, description);
		setImageDescriptor(SpaceBarPlugin.getDefault()
				.getImageDescriptor(image));
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent,
				SWT.H_SCROLL | SWT.V_SCROLL);
		Control control = createControl(scrolledComposite);
		control.setFont(parent.getFont());
		scrolledComposite.setContent(control);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setMinSize(control.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
		setControl(scrolledComposite);
	}

	protected abstract Control createControl(ScrolledComposite parent);

}
