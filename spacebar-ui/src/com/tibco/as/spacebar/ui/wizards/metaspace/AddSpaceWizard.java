package com.tibco.as.spacebar.ui.wizards.metaspace;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

import com.tibco.as.spacebar.ui.model.Space;
import com.tibco.as.spacebar.ui.model.Spaces;
import com.tibco.as.spacebar.ui.wizards.AbstractWizard;

import com.tibco.as.space.Metaspace;

public class AddSpaceWizard extends AbstractWizard implements ModifyListener {

	private Spaces spaces;
	private AddSpaceWizardPage page;

	public AddSpaceWizard(Spaces spaces) {
		super("AddSpace", "Add Space Failed", "Space could not be added.");
		this.spaces = spaces;
	}

	@Override
	public void addPages() {
		page = new AddSpaceWizardPage(spaces.getMetaspace().getMetaspaceName(),
				this);
		page.setDescription("Add an existing space");
		addPage(page);
	}

	@Override
	public boolean canFinish() {
		return page.getSpaceName() != null && page.getSpaceName().length() > 0;
	}

	@Override
	protected void finish(IProgressMonitor monitor) throws Exception {
		Metaspace ms = spaces.getMetaspace().getConnection().getMetaspace();
		String spaceName = page.getSpaceName();
		spaces.addChild(new Space(spaces, ms.getSpaceDef(spaceName)));
	}

	@Override
	public void modifyText(ModifyEvent e) {
		getContainer().updateButtons();
	}
}
