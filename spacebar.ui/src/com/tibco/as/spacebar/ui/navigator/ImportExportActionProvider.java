package com.tibco.as.spacebar.ui.navigator;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;

public class ImportExportActionProvider extends CommonActionProvider {

	private IWorkbenchAction exportAction;

	private IWorkbenchAction importAction;

	private boolean contribute = false;

	@Override
	public void init(ICommonActionExtensionSite site) {
		if (site.getViewSite() instanceof ICommonViewerWorkbenchSite) {
			IWorkbenchWindow window = ((ICommonViewerWorkbenchSite) site
					.getViewSite()).getWorkbenchWindow();
			importAction = ActionFactory.IMPORT.create(window);
			exportAction = ActionFactory.EXPORT.create(window);
			contribute = true;
		}
	}

	@Override
	public void fillContextMenu(IMenuManager menu) {
		if (!contribute) {
			return;
		}
		menu.appendToGroup(ICommonMenuConstants.GROUP_PORT, importAction);
		menu.appendToGroup(ICommonMenuConstants.GROUP_PORT, exportAction);

	}

	@Override
	public void dispose() {
		if (exportAction != null) {
			exportAction.dispose();
			exportAction = null;
		}
		if (importAction != null) {
			importAction.dispose();
			importAction = null;
		}
		super.dispose();
	}

}
