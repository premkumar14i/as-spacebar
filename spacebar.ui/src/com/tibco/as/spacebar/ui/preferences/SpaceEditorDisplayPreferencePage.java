package com.tibco.as.spacebar.ui.preferences;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class SpaceEditorDisplayPreferencePage extends AbstractPreferencePage {

	public static final String[][] THEMES = {
			{ "Default", Preferences.THEME_DEFAULT },
			{ "Modern", Preferences.THEME_MODERN },
			{ "Dark", Preferences.THEME_DARK } };

	@Override
	protected void createFieldEditors(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());
		GridLayout basicLayout = new GridLayout();
		basicLayout.numColumns = 2;
		composite.setLayout(basicLayout);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		composite.setLayoutData(gridData);
		addComboField(Preferences.SPACE_EDITOR_THEME, "&Theme:", THEMES,
				composite, "Theme styling of space editor table");
		Group formatGroup = new Group(parent, SWT.NONE);
		formatGroup.setFont(parent.getFont());
		formatGroup.setText("Formats");
		GridDataFactory.defaultsFor(formatGroup).grab(true, false).span(2, 1)
				.applyTo(formatGroup);
		addStringField(
				Preferences.SPACE_EDITOR_INTEGER_FORMAT,
				"&Integer:",
				formatGroup,
				"Integer format e.g. 3456 formatted with \"#,###\" is \"3,456\". Leave empty for no formatting.");
		addStringField(
				Preferences.SPACE_EDITOR_DECIMAL_FORMAT,
				"&Decimal:",
				formatGroup,
				"Decimal format e.g. 12345 formatted with \"##0.##E0\" is \"12.3E3\". Leave empty for no formatting.");
		addStringField(Preferences.SPACE_EDITOR_DATE_FORMAT, "Date/&time:",
				formatGroup, "Date/time format e.g. \"dd/MM/yy HH:mm\".");
		addStringField(
				Preferences.SPACE_EDITOR_TIME_ZONE,
				"Time &zone:",
				formatGroup,
				"The ID for a TimeZone, either an abbreviation such as \"PST\", a full name such as \"America/Los_Angeles\", or a custom ID such as \"GMT-8:00\".");
		addStringField(Preferences.SPACE_EDITOR_BOOLEAN_FORMAT, "&Boolean:",
				formatGroup,
				"Boolean format e.g. \"Yes|No\". Leave empty for checkbox.");
		Group colorGroup = new Group(parent, SWT.NONE);
		colorGroup.setFont(parent.getFont());
		colorGroup.setText("Colors");
		GridDataFactory.defaultsFor(colorGroup).grab(true, false).span(2, 1)
				.applyTo(colorGroup);
		addColorField(Preferences.SPACE_EDITOR_COLOR_BLINK, "Change:",
				colorGroup, "Background color for change in cell value");
		addColorField(Preferences.SPACE_EDITOR_COLOR_BLINK_UP, "Increase:",
				colorGroup,
				"Background color for positive change (new value > old value)");
		addColorField(Preferences.SPACE_EDITOR_COLOR_BLINK_DOWN, "Decrease:",
				colorGroup,
				"Background color for negative change (new value < old value)");
	}
}