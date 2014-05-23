package com.tibco.as.spacebar.ui.wizards.space;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.tibco.as.spacebar.ui.model.Connection;
import com.tibco.as.spacebar.ui.model.Space;
import com.tibco.as.spacebar.ui.model.Spaces;
import com.tibco.as.spacebar.ui.wizards.AbstractWizard;
import com.tibco.as.spacebar.ui.wizards.space.def.SpaceDefWizardPage;
import com.tibco.as.spacebar.ui.wizards.space.distribution.DistributionWizardPage;
import com.tibco.as.spacebar.ui.wizards.space.field.FieldsWizardPage;
import com.tibco.as.spacebar.ui.wizards.space.index.IndexesWizardPage;
import com.tibco.as.spacebar.ui.wizards.space.key.KeyWizardPage;

import com.tibco.as.space.SpaceDef;
import com.tibco.as.utils.ASUtils;

public class CreateSpaceWizard extends AbstractWizard implements INewWizard {

	private Space space;

	public CreateSpaceWizard() {
		super("CreateSpace", "Space Creation Error", "Could not create space");
	}

	@Override
	public void addPages() {
		addPage(new SpaceDefWizardPage(space));
		addPage(new FieldsWizardPage(space.getFields()));
		addPage(new KeyWizardPage(space));
		if (ASUtils.hasMethod(SpaceDef.class, "setDistributionFields")) {
			addPage(new DistributionWizardPage(space));
		}
		addPage(new IndexesWizardPage(space.getIndexes()));
	}

	@Override
	protected void finish(IProgressMonitor monitor) throws Exception {
		monitor.beginTask("Defining space", 1);
		Connection connection = space.getParent().getMetaspace()
				.getConnection();
		SpaceDef spaceDef = space.getSpaceDef();
		connection.getMetaspace().defineSpace(spaceDef);
		monitor.worked(1);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		Object element = selection.getFirstElement();
		if (element instanceof Spaces) {
			Spaces spaces = (Spaces) element;
			space = new Space(spaces);
		}
	}
}
