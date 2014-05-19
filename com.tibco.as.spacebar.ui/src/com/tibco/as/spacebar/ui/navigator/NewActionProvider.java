package com.tibco.as.spacebar.ui.navigator;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.eclipse.ui.navigator.WizardActionGroup;

public class NewActionProvider extends CommonActionProvider {

	private static final String NEW_MENU_NAME = "common.new.menu";

	private ActionFactory.IWorkbenchAction newAction;

	private WizardActionGroup newWizardActionGroup;

	private boolean contribute = false;

	@Override
	public void init(ICommonActionExtensionSite site) {
		if (site.getViewSite() instanceof ICommonViewerWorkbenchSite) {
			IWorkbenchWindow window = ((ICommonViewerWorkbenchSite) site
					.getViewSite()).getWorkbenchWindow();
			newAction = ActionFactory.NEW.create(window);
			newWizardActionGroup = new WizardActionGroup(window, PlatformUI
					.getWorkbench().getNewWizardRegistry(),
					WizardActionGroup.TYPE_NEW, site.getContentService());
			contribute = true;
		}
	}

	@Override
	public void fillContextMenu(IMenuManager menu) {
		IMenuManager newSubmenu = new MenuManager("New", NEW_MENU_NAME);
		if (!contribute) {
			return;
		}
		newWizardActionGroup.setContext(getContext());
		newWizardActionGroup.fillContextMenu(newSubmenu);
		menu.insertAfter(ICommonMenuConstants.GROUP_NEW, newSubmenu);
	}

	@Override
	public void dispose() {
		if (newAction != null) {
			newAction.dispose();
			newAction = null;
		}
		super.dispose();
	}

}
