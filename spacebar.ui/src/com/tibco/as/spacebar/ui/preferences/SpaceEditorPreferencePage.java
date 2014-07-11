package com.tibco.as.spacebar.ui.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;

public class SpaceEditorPreferencePage extends TabbedPreferencePage {

	private static final String DISPLAY_TIMESCOPE_ALL = "All";
	private static final String DISPLAY_TIMESCOPE_NEW = "New";
	private static final String DISPLAY_TIMESCOPE_CURRENT = "Current";
	private static final String DISPLAY_TIMESCOPE_SNAPSHOT = "Snapshot";

	public static final String[][] TIME_SCOPES = {
			{ DISPLAY_TIMESCOPE_ALL, Preferences.TIMESCOPE_ALL },
			{ DISPLAY_TIMESCOPE_NEW, Preferences.TIMESCOPE_NEW },
			{ DISPLAY_TIMESCOPE_CURRENT, Preferences.TIMESCOPE_CURRENT },
			{ DISPLAY_TIMESCOPE_SNAPSHOT, Preferences.TIMESCOPE_SNAPSHOT } };

	@Override
	protected void addFields(Composite parent) {
		addIntegerField(Preferences.SPACE_EDITOR_BROWSE_LIMIT, "&Limit:",
				parent, "Maximum number of entries to fetch and display");
		addComboField(Preferences.SPACE_EDITOR_BROWSE_TIME_SCOPE,
				"Default &time scope:", TIME_SCOPES, parent,
				"Default browse time scope");
	}

	@Override
	protected Composite createTabItemComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);
		return composite;
	}

	@Override
	protected void addTabFields(TabFolder folder) {
		createAll(createTab(folder, DISPLAY_TIMESCOPE_ALL));
		createNew(createTab(folder, DISPLAY_TIMESCOPE_NEW));
		createCurrent(createTab(folder, DISPLAY_TIMESCOPE_CURRENT));
		createSnapshot(createTab(folder, DISPLAY_TIMESCOPE_SNAPSHOT));
	}

	private Control createAll(Composite parent) {
		Composite composite = createTabItemComposite(parent);
		addPrefetchField(Preferences.TIMESCOPE_ALL, composite);
		addTimeoutField(Preferences.TIMESCOPE_ALL, composite);
		addQueryLimitField(Preferences.TIMESCOPE_ALL, composite);
		return composite;
	}

	private Control createNew(Composite parent) {
		Composite composite = createTabItemComposite(parent);
		addTimeoutField(Preferences.TIMESCOPE_NEW, composite);
		return composite;
	}

	private Control createCurrent(Composite parent) {
		Composite composite = createTabItemComposite(parent);
		addPrefetchField(Preferences.TIMESCOPE_CURRENT, composite);
		return composite;
	}

	private Control createSnapshot(Composite parent) {
		Composite composite = createTabItemComposite(parent);
		addPrefetchField(Preferences.TIMESCOPE_SNAPSHOT, composite);
		addQueryLimitField(Preferences.TIMESCOPE_SNAPSHOT, composite);
		return composite;
	}

	private FieldEditor addTimeoutField(String timeScope, Composite parent) {
		return addField(Preferences.SPACE_EDITOR_BROWSE_TIMEOUT, timeScope,
				"Default &timeout:", parent,
				"Default timeout value associated with the browser");
	}

	private FieldEditor addQueryLimitField(String timeScope, Composite parent) {
		return addField(Preferences.SPACE_EDITOR_BROWSE_QUERY_LIMIT, timeScope,
				"Default &query limit:", parent,
				"Defaut query limit associated with the browser");
	}

	private FieldEditor addPrefetchField(String timeScope, Composite parent) {
		return addField(Preferences.SPACE_EDITOR_BROWSE_PREFETCH, timeScope,
				"Default &prefetch:", parent,
				"Default number of entries to prefetch from the space");
	}

	private IntegerFieldEditor addField(String preferenceName,
			String timeScope, String label, Composite parent, String toolTip) {
		return addIntegerField(
				Preferences.getPreferenceName(preferenceName, timeScope),
				label, parent, toolTip, -1, Integer.MAX_VALUE);
	}

}