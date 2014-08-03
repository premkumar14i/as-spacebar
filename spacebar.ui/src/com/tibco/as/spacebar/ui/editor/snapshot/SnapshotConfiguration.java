package com.tibco.as.spacebar.ui.editor.snapshot;

import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDoubleDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultFloatDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultIntegerDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultLongDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultShortDisplayConverter;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.editor.CheckBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;

import com.tibco.as.spacebar.ui.editor.AbstractConfiguration;
import com.tibco.as.spacebar.ui.editor.display.DateTimeDisplayConverter;
import com.tibco.as.spacebar.ui.preferences.Preferences;

public class SnapshotConfiguration extends AbstractConfiguration {

	@Override
	public void configureRegistry(IConfigRegistry configRegistry) {
		configRegistry.registerConfigAttribute(
				EditConfigAttributes.CELL_EDITABLE_RULE,
				IEditableRule.ALWAYS_EDITABLE);
		super.configureRegistry(configRegistry);
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.DISPLAY_CONVERTER,
				new DateTimeDisplayConverter(null, Preferences
						.getSpaceEditorTimeZone()), DisplayMode.EDIT,
				DATETIME_CONFIG_LABEL);
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.DISPLAY_CONVERTER,
				new DefaultDoubleDisplayConverter(), DisplayMode.EDIT,
				DOUBLE_CONFIG_LABEL);
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.DISPLAY_CONVERTER,
				new DefaultFloatDisplayConverter(), DisplayMode.EDIT,
				FLOAT_CONFIG_LABEL);
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.DISPLAY_CONVERTER,
				new DefaultIntegerDisplayConverter(), DisplayMode.EDIT,
				INTEGER_CONFIG_LABEL);
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.DISPLAY_CONVERTER,
				new DefaultLongDisplayConverter(), DisplayMode.EDIT,
				LONG_CONFIG_LABEL);
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.DISPLAY_CONVERTER,
				new DefaultShortDisplayConverter(), DisplayMode.EDIT,
				SHORT_CONFIG_LABEL);
		configRegistry.registerConfigAttribute(
				EditConfigAttributes.CELL_EDITOR, new CheckBoxCellEditor(),
				DisplayMode.EDIT, CHECKBOX_EDITOR_CONFIG_LABEL);
	}

}
