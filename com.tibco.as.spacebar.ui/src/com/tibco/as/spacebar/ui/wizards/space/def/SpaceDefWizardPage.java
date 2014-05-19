package com.tibco.as.spacebar.ui.wizards.space.def;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.tibco.as.spacebar.ui.SWTFactory;

import com.tibco.as.space.SpaceDef;

public class SpaceDefWizardPage extends WizardPage {

	private SpaceDef spaceDef;
	private Text nameText;

	/**
	 * Create the wizard.
	 */
	public SpaceDefWizardPage(SpaceDef spaceDef) {
		super("spaceDefWizardPage");
		setTitle("Space Definition");
		setDescription("Enter space definition");
		this.spaceDef = spaceDef;
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = SWTFactory.createComposite(parent, 1, 1,
				GridData.FILL_HORIZONTAL);

		Composite nameComposite = SWTFactory.createComposite(composite, 2, 1,
				GridData.FILL_HORIZONTAL);
		new Label(nameComposite, SWT.NONE).setText("Name:");
		nameText = new Text(nameComposite, SWT.BORDER | SWT.SINGLE);
		nameText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				determinePageCompletion();
			}
		});
		GridDataFactory.defaultsFor(nameText).applyTo(nameText);

		SpaceDefEditor defEditor = new SpaceDefEditor(composite, SWT.NONE,
				spaceDef);
		GridDataFactory.defaultsFor(defEditor).applyTo(defEditor);

		setControl(composite);
		setPageComplete(false);
	}

	protected void determinePageCompletion() {
		String name = nameText.getText().trim();
		try {
			spaceDef.setName(name);
		} catch (Throwable e) {
			setErrorMessage(e.getMessage());
		}
		setPageComplete(spaceDef.getName() != null);
	}

}
