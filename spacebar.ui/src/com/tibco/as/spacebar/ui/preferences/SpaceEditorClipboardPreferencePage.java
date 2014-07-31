package com.tibco.as.spacebar.ui.preferences;

import org.eclipse.swt.widgets.Composite;

public class SpaceEditorClipboardPreferencePage extends AbstractPreferencePage {

	@Override
	protected void createFieldEditors(Composite parent) {
		addBooleanField(Preferences.SPACE_EDITOR_CLIPBOARD_HEADER, "&Header",
				parent, "Include header with field names");
		addStringField(Preferences.SPACE_EDITOR_CLIPBOARD_SEPARATOR,
				"&Delimiter:", parent, "Cell separator character");
	}

}
