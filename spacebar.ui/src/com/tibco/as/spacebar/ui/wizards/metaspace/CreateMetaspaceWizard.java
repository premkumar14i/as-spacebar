package com.tibco.as.spacebar.ui.wizards.metaspace;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.model.Metaspace;
import com.tibco.as.spacebar.ui.wizards.AbstractWizard;

public class CreateMetaspaceWizard extends AbstractWizard implements INewWizard {

	private Metaspace metaspace;

	public CreateMetaspaceWizard() {
		super("CreateMetaspace", "Metaspace Creation Error",
				"Could not create metaspace");
	}

	public void addPages() {
		metaspace = new Metaspace();
		metaspace.setMetaspaces(SpaceBarPlugin.getDefault().getMetaspaces());
		addPage(new CreateMetaspaceWizardPage(metaspace));
	}

	@Override
	protected void finish(IProgressMonitor monitor) throws Exception {
		monitor.beginTask(NLS.bind("Creating metaspace ''{0}''", metaspace), 2);
		SpaceBarPlugin activator = SpaceBarPlugin.getDefault();
		activator.getMetaspaces().addChild(metaspace);
		monitor.worked(1);
		activator.saveMetaspaces();
		monitor.worked(1);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

}
