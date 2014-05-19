package com.tibco.as.spacebar.ui.wizards.metaspace;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.tibco.as.spacebar.ui.wizards.AbstractWizardPage;

public class AddSpaceWizardPage extends AbstractWizardPage {

	private Text spaceNameText;
	private AddSpaceWizard wizard;

	/**
	 * Create the wizard.
	 * 
	 * @param space
	 */
	public AddSpaceWizardPage(String metaspaceName, AddSpaceWizard wizard) {
		super("addSpaceWizardPage", "Add Space", NLS.bind(
				"Add existing space to metaspace ''{0}''", metaspaceName));
		this.wizard = wizard;
	}

	@Override
	protected Control createControl(ScrolledComposite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		new Label(container, SWT.NONE).setText("Space name:");
		spaceNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		spaceNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		spaceNameText.addModifyListener(wizard);
		return container;
	}

	public String getSpaceName() {
		return spaceNameText.getText();
	}

}
