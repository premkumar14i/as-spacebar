package com.tibco.as.spacebar.ui.editor;

import static org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes.CELL_PAINTER;
import static org.eclipse.nebula.widgets.nattable.grid.GridRegion.FILTER_ROW;
import static org.eclipse.nebula.widgets.nattable.style.DisplayMode.NORMAL;

import java.util.List;

import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDoubleDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultFloatDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultIntegerDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultLongDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultShortDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.IDisplayConverter;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterIconPainter;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowDataLayer;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowPainter;
import org.eclipse.nebula.widgets.nattable.filterrow.TextMatchingMode;
import org.eclipse.nebula.widgets.nattable.filterrow.config.FilterRowConfigAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;

import com.tibco.as.spacebar.ui.editor.display.DateTimeFilterDisplayConverter;
import com.tibco.as.spacebar.ui.preferences.Preferences;

import com.tibco.as.space.FieldDef;
import com.tibco.as.space.FieldDef.FieldType;

public class FilterRowConfiguration extends AbstractRegistryConfiguration {

	private List<FieldDef> fieldDefs;

	public FilterRowConfiguration(List<FieldDef> fieldDefs) {
		this.fieldDefs = fieldDefs;
	}

	public void configureRegistry(IConfigRegistry configRegistry) {
		configRegistry.registerConfigAttribute(
				CELL_PAINTER,
				new FilterRowPainter(new FilterIconPainter(GUIHelper
						.getImage("filter"))), NORMAL, FILTER_ROW);
		for (int index = 0; index < fieldDefs.size(); index++) {
			FieldDef field = fieldDefs.get(index);
			if (isExpression(field)) {
				configRegistry.registerConfigAttribute(
						FilterRowConfigAttributes.FILTER_DISPLAY_CONVERTER,
						getConverter(field), DisplayMode.NORMAL,
						FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX
								+ index);
				configRegistry.registerConfigAttribute(
						FilterRowConfigAttributes.TEXT_MATCHING_MODE,
						TextMatchingMode.REGULAR_EXPRESSION,
						DisplayMode.NORMAL,
						FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX
								+ index);
				if (field.getType() == FieldType.DATETIME) {
					configRegistry.registerConfigAttribute(
							FilterRowConfigAttributes.TEXT_DELIMITER, "\"",
							DisplayMode.NORMAL,
							FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX
									+ index);
				}
			}
		}
	}

	private IDisplayConverter getConverter(FieldDef fieldDef) {
		switch (fieldDef.getType()) {
		case DATETIME:
			return new DateTimeFilterDisplayConverter(
					Preferences.getSpaceEditorTimeZone());
		case DOUBLE:
			return new DefaultDoubleDisplayConverter();
		case FLOAT:
			return new DefaultFloatDisplayConverter();
		case INTEGER:
			return new DefaultIntegerDisplayConverter();
		case LONG:
			return new DefaultLongDisplayConverter();
		case SHORT:
			return new DefaultShortDisplayConverter();
		default:
			return new DefaultDisplayConverter();
		}
	}

	private boolean isExpression(FieldDef fieldDef) {
		switch (fieldDef.getType()) {
		case DATETIME:
		case DOUBLE:
		case FLOAT:
		case INTEGER:
		case LONG:
		case SHORT:
			return true;
		default:
			return false;
		}
	}

}
