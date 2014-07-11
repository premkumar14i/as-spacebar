package com.tibco.as.spacebar.ui.wizards.space.browse;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.util.NLS;

import com.tibco.as.spacebar.ui.model.Space;
import com.tibco.as.spacebar.ui.wizards.AbstractWizard;

import com.tibco.as.io.Export;

/**
 * This wizard's role is to open a space editor.
 */
public class BrowseSpaceWizard extends AbstractWizard {

	private BrowseSpaceWizardPage page;

	/**
	 * Constructor for NewMetaspaceWizard.
	 * 
	 * @throws ASException
	 */
	public BrowseSpaceWizard(Space space, Export export) {
		super("BrowseSpace", "Browse Error", NLS.bind(
				"Could not browse space ''{0}''", space));
		this.page = new BrowseSpaceWizardPage(space, export);
	}

	/**
	 * Adding the page to the wizard.
	 */
	@Override
	public void addPages() {
		addPage(page);
	}

	@Override
	protected void finish(IProgressMonitor monitor) {
		// do nothing
	}

	public Export getExport() {
		return page.getExport();
	}

}