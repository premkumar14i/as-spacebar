package com.tibco.as.spacebar.rcp;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.keys.IBindingService;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	private IContributionItem showViewItem;
	private IContributionItem fOpenWindowsItem;
	private IWorkbenchWindow window;
	private IContributionItem newWizardItem;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
		window = configurer.getWindowConfigurer().getWindow();
	}

	@Override
	protected void makeActions(final IWorkbenchWindow window) {

		register(ActionFactory.NEW_WIZARD_DROP_DOWN.create(window));

		register(ActionFactory.SAVE_AS.create(window));
		register(ActionFactory.CLOSE.create(window));
		register(ActionFactory.CLOSE_ALL.create(window));
		register(ActionFactory.PRINT.create(window));
		register(ActionFactory.QUIT.create(window));

		register(ActionFactory.CUT.create(window));
		register(ActionFactory.COPY.create(window));
		register(ActionFactory.PASTE.create(window));
		register(ActionFactory.DELETE.create(window));
		register(ActionFactory.SELECT_ALL.create(window));

		register(ActionFactory.IMPORT.create(window));
		register(ActionFactory.EXPORT.create(window));

		register(ActionFactory.PROPERTIES.create(window));

		register(ActionFactory.PREFERENCES.create(window));

		showViewItem = ContributionItemFactory.VIEWS_SHORTLIST.create(window);

		register(ActionFactory.OPEN_NEW_WINDOW.create(window));
		getAction(ActionFactory.OPEN_NEW_WINDOW.getId()).setText("&New Window");
		fOpenWindowsItem = ContributionItemFactory.OPEN_WINDOWS.create(window);

		register(ActionFactory.ABOUT.create(window));
		getAction(ActionFactory.ABOUT.getId()).setText("&About SpaceBar");

		register(ActionFactory.LOCK_TOOL_BAR.create(window));

		newWizardItem = ContributionItemFactory.NEW_WIZARD_SHORTLIST
				.create(window);
	}

	protected void fillMenuBar(IMenuManager menuBar) {
		createFileMenu(menuBar);
		createEditMenu(menuBar);
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		createWindowMenu(menuBar);
		createHelpMenu(menuBar);
	}

	private void createFileMenu(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager("&File",
				IWorkbenchActionConstants.M_FILE);
		menuBar.add(fileMenu);

		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.NEW_EXT));
		MenuManager newMenuManager = new MenuManager("New", "new");
		newMenuManager.add(newWizardItem);

		fileMenu.add(newMenuManager);

		fileMenu.add(new Separator());

		fileMenu.add(getAction(ActionFactory.CLOSE.getId()));
		fileMenu.add(getAction(ActionFactory.CLOSE_ALL.getId()));
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.CLOSE_EXT));
		fileMenu.add(new Separator());
		fileMenu.add(getAction(ActionFactory.SAVE_AS.getId()));
		fileMenu.add(new Separator());
		fileMenu.add(getAction(ActionFactory.PRINT.getId()));

		fileMenu.add(new Separator());
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));

		fileMenu.add(new Separator());
		fileMenu.add(getAction(ActionFactory.IMPORT.getId()));
		fileMenu.add(getAction(ActionFactory.EXPORT.getId()));
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
		fileMenu.add(new Separator());

		if (Application.IS_LINUX) {
			fileMenu.add(getPropertiesItem());
			fileMenu.add(new Separator());
		}

		fileMenu.add(getAction(ActionFactory.QUIT.getId()));
	}

	private IContributionItem getPropertiesItem() {
		return getItem(ActionFactory.PROPERTIES.getId(),
				ActionFactory.PROPERTIES.getCommandId(), null, null,
				"P&roperties", "Properties", null);
	}

	private void createEditMenu(IMenuManager menuBar) {
		MenuManager editMenu = new MenuManager("&Edit",
				IWorkbenchActionConstants.M_EDIT);
		editMenu.add(getAction(ActionFactory.COPY.getId())); // Dummy action
		menuBar.add(editMenu);

		editMenu.setRemoveAllWhenShown(true);
		editMenu.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager editMenu) {
				editMenu.add(new GroupMarker(
						IWorkbenchActionConstants.EDIT_START));
				editMenu.add(new Separator());

				editMenu.add(new GroupMarker(IWorkbenchActionConstants.UNDO_EXT));
				editMenu.add(new Separator());

				editMenu.add(getAction(ActionFactory.CUT.getId()));
				editMenu.add(getAction(ActionFactory.COPY.getId()));
				editMenu.add(getAction(ActionFactory.PASTE.getId()));
				editMenu.add(new Separator());
				editMenu.add(getAction(ActionFactory.DELETE.getId()));
				editMenu.add(getAction(ActionFactory.SELECT_ALL.getId()));

				editMenu.add(new Separator());

				editMenu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_END));
				editMenu.add(new Separator());

				if (Application.IS_LINUX) {
					IAction preferences = getAction(ActionFactory.PREFERENCES
							.getId());
					preferences
							.setImageDescriptor(getDescriptor("preferences.gif")); //$NON-NLS-1$
					editMenu.add(preferences);
				} else {
					editMenu.add(getPropertiesItem());
				}
			}
		});
	}

	private static ImageDescriptor getDescriptor(String key) {
		ImageRegistry registry = SpaceBarPlugin.getDefault().getImageRegistry();
		ImageDescriptor descriptor = registry.getDescriptor(key);
		if (descriptor == null) {
			descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(
					"spacebar-rcp", "icons/" + key);
			registry.put(key, descriptor);
		}
		return descriptor;
	}

	private void createWindowMenu(IMenuManager menuBar) {
		MenuManager windowMenu = new MenuManager("&Window",
				IWorkbenchActionConstants.M_WINDOW);
		menuBar.add(windowMenu);

		MenuManager showViewMenuManager = new MenuManager("Show &View");
		showViewMenuManager.add(showViewItem);
		windowMenu.add(showViewMenuManager);

		IAction openNewWindowAction = getAction(ActionFactory.OPEN_NEW_WINDOW
				.getId());
		openNewWindowAction.setImageDescriptor(SpaceBarPlugin.getDefault()
				.getImageDescriptor(Image.NEW_WINDOW));
		windowMenu.add(openNewWindowAction);

		windowMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		windowMenu.add(fOpenWindowsItem);
		if (!Application.IS_LINUX) {
			windowMenu.add(new Separator());
			IAction preferences = getAction(ActionFactory.PREFERENCES.getId());
			preferences.setImageDescriptor(getDescriptor("preferences.gif")); //$NON-NLS-1$
			windowMenu.add(preferences);
			if (Application.IS_MAC) {
				IContributionItem item = windowMenu
						.find(ActionFactory.PREFERENCES.getId());
				if (item != null)
					item.setVisible(false);
			}
		}
	}

	private void createHelpMenu(IMenuManager menuBar) {
		MenuManager helpMenu = new MenuManager("&Help",
				IWorkbenchActionConstants.M_HELP);
		menuBar.add(helpMenu);

		helpMenu.add(new GroupMarker(IWorkbenchActionConstants.HELP_START));

		helpMenu.add(new Separator());
		helpMenu.add(new Action("&Show Key Bindings") {
			@Override
			public void run() {
				IWorkbench workbench = PlatformUI.getWorkbench();
				IBindingService bindingService = (IBindingService) workbench
						.getService(IBindingService.class);
				bindingService.openKeyAssistDialog();
			}
		});

		helpMenu.add(new Separator());

		helpMenu.add(new GroupMarker(IWorkbenchActionConstants.HELP_END));
		helpMenu.add(new Separator());

		helpMenu.add(getAction(ActionFactory.ABOUT.getId()));
		if (Application.IS_MAC) {
			IContributionItem item = helpMenu.find(ActionFactory.ABOUT.getId());
			if (item != null)
				item.setVisible(false);
		}
	}

	private IContributionItem getItem(String actionId, String commandId,
			String image, String disabledImage, String label, String tooltip,
			String helpContextId) {
		ISharedImages sharedImages = window.getWorkbench().getSharedImages();

		CommandContributionItemParameter commandParm = new CommandContributionItemParameter(
				window, actionId, commandId, null,
				sharedImages.getImageDescriptor(image),
				sharedImages.getImageDescriptor(disabledImage), null, label,
				null, tooltip, CommandContributionItem.STYLE_PUSH, null, false);
		return new CommandContributionItem(commandParm);
	}

	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		super.fillCoolBar(coolBar);
		ToolBarManager toolbar = new ToolBarManager(SWT.FLAT);
		toolbar.add(getAction(ActionFactory.NEW_WIZARD_DROP_DOWN.getId()));
		coolBar.add(toolbar);
	}
}
