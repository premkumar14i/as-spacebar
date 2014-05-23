package com.tibco.as.spacebar.ui.preferences;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

public class ClipboardPreferencePage extends TabbedPreferencePage {

	@Override
	protected void addTabFields(TabFolder folder) {
		createExport(createTab(folder, "Export"));
	}

	private Composite createExport(Composite parent) {
		Composite composite = createTabItemComposite(parent);
		addStringField(Preferences.EXPORT_CLIPBOARD_SEPARATOR, "&Delimiter:",
				composite, "Cell separator character");
		return composite;
	}

}
