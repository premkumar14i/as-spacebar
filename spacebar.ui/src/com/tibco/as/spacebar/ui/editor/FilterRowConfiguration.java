package com.tibco.as.spacebar.ui.editor;

import static org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes.CELL_PAINTER;
import static org.eclipse.nebula.widgets.nattable.grid.GridRegion.FILTER_ROW;
import static org.eclipse.nebula.widgets.nattable.style.DisplayMode.NORMAL;

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

import com.tibco.as.convert.Attribute;
import com.tibco.as.convert.Attributes;
import com.tibco.as.convert.UnsupportedConversionException;
import com.tibco.as.space.DateTime;
import com.tibco.as.space.FieldDef;
import com.tibco.as.space.FieldDef.FieldType;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.preferences.Preferences;

public class FilterRowConfiguration extends AbstractRegistryConfiguration {

	private FieldDef[] fieldDefs;

	public FilterRowConfiguration(FieldDef[] fieldDefs) {
		this.fieldDefs = fieldDefs;
	}

	@Override
	public void configureRegistry(IConfigRegistry configRegistry) {
		configRegistry.registerConfigAttribute(
				CELL_PAINTER,
				new FilterRowPainter(new FilterIconPainter(GUIHelper
						.getImage("filter"))), NORMAL, FILTER_ROW);
		for (int index = 0; index < fieldDefs.length; index++) {
			FieldDef field = fieldDefs[index];
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
			Attributes attributes = new Attributes();
			attributes.put(Attribute.TIMEZONE,
					Preferences.getSpaceEditorTimeZone());
			try {
				return FieldDisplayConverter.create(DateTime.class, "date",
						attributes);
			} catch (UnsupportedConversionException e) {
				SpaceBarPlugin.logException(e);
			}
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
