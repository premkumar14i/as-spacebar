package com.tibco.as.spacebar.ui.editor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultBooleanDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultCharacterDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDoubleDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultFloatDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultIntegerDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultLongDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultShortDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.IDisplayConverter;
import org.eclipse.nebula.widgets.nattable.painter.cell.CheckBoxPainter;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;

import com.tibco.as.spacebar.ui.editor.display.BlobDisplayConverter;
import com.tibco.as.spacebar.ui.editor.display.DateTimeDisplayConverter;
import com.tibco.as.spacebar.ui.editor.display.FormatDisplayConverter;
import com.tibco.as.spacebar.ui.editor.display.StringDisplayConverter;
import com.tibco.as.spacebar.ui.preferences.Preferences;

import com.tibco.as.convert.format.BooleanFormat;
import com.tibco.as.space.FieldDef;

public abstract class AbstractConfiguration extends
		DefaultNatTableStyleConfiguration {

	public static final String BLOB_CONFIG_LABEL = "blob";
	public static final String BOOLEAN_CONFIG_LABEL = "boolean";
	public static final String CHAR_CONFIG_LABEL = "char";
	public static final String DATETIME_CONFIG_LABEL = "dateTime";
	public static final String DOUBLE_CONFIG_LABEL = "double";
	public static final String FLOAT_CONFIG_LABEL = "float";
	public static final String INTEGER_CONFIG_LABEL = "integer";
	public static final String LONG_CONFIG_LABEL = "long";
	public static final String SHORT_CONFIG_LABEL = "short";
	public static final String STRING_CONFIG_LABEL = "string";
	public static final String CHECKBOX_EDITOR_CONFIG_LABEL = "checkboxEditor";
	public static final String CHECKBOX_PAINTER_CONFIG_LABEL = "checkboxPainter";

	@Override
	public void configureRegistry(IConfigRegistry configRegistry) {
		super.configureRegistry(configRegistry);
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.DISPLAY_CONVERTER,
				getBlobDisplayConverter(), DisplayMode.NORMAL,
				BLOB_CONFIG_LABEL);
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.DISPLAY_CONVERTER,
				getBooleanDisplayConverter(), DisplayMode.NORMAL,
				BOOLEAN_CONFIG_LABEL);
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.DISPLAY_CONVERTER,
				getCharacterDisplayConverter(), DisplayMode.NORMAL,
				CHAR_CONFIG_LABEL);
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.DISPLAY_CONVERTER,
				getDateTimeDisplayConverter(), DisplayMode.NORMAL,
				DATETIME_CONFIG_LABEL);
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.DISPLAY_CONVERTER,
				getDoubleDisplayConverter(), DisplayMode.NORMAL,
				DOUBLE_CONFIG_LABEL);
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.DISPLAY_CONVERTER,
				getFloatDisplayConverter(), DisplayMode.NORMAL,
				FLOAT_CONFIG_LABEL);
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.DISPLAY_CONVERTER,
				getIntegerDisplayConverter(), DisplayMode.NORMAL,
				INTEGER_CONFIG_LABEL);
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.DISPLAY_CONVERTER,
				getLongDisplayConverter(), DisplayMode.NORMAL,
				LONG_CONFIG_LABEL);
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.DISPLAY_CONVERTER,
				getShortDisplayConverter(), DisplayMode.NORMAL,
				SHORT_CONFIG_LABEL);
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.DISPLAY_CONVERTER,
				getStringDisplayConverter(), DisplayMode.NORMAL,
				STRING_CONFIG_LABEL);
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.CELL_PAINTER, new CheckBoxPainter(),
				DisplayMode.NORMAL, CHECKBOX_PAINTER_CONFIG_LABEL);
	}

	protected IDisplayConverter getStringDisplayConverter() {
		return new StringDisplayConverter();
	}

	protected IDisplayConverter getCharacterDisplayConverter() {
		return new DefaultCharacterDisplayConverter();
	}

	protected IDisplayConverter getBooleanDisplayConverter() {
		String format = Preferences
				.getString(Preferences.SPACE_EDITOR_BOOLEAN_FORMAT);
		if (format == null) {
			return new DefaultBooleanDisplayConverter();
		}
		return new FormatDisplayConverter(new BooleanFormat(format));
	}

	protected IDisplayConverter getBlobDisplayConverter() {
		return new BlobDisplayConverter();
	}

	private IDisplayConverter getNumericDisplayConverter(String pattern,
			IDisplayConverter defaultConverter) {
		if (pattern == null) {
			return defaultConverter;
		}
		return new FormatDisplayConverter(new DecimalFormat(pattern));
	}

	protected IDisplayConverter getDoubleDisplayConverter() {
		return getNumericDisplayConverter(
				Preferences.getSpaceEditorDecimalFormat(),
				new DefaultDoubleDisplayConverter());
	}

	protected IDisplayConverter getFloatDisplayConverter() {
		return getNumericDisplayConverter(
				Preferences.getSpaceEditorDecimalFormat(),
				new DefaultFloatDisplayConverter());
	}

	protected IDisplayConverter getIntegerDisplayConverter() {
		return getNumericDisplayConverter(
				Preferences.getSpaceEditorIntegerFormat(),
				new DefaultIntegerDisplayConverter());
	}

	protected IDisplayConverter getLongDisplayConverter() {
		return getNumericDisplayConverter(
				Preferences.getSpaceEditorIntegerFormat(),
				new DefaultLongDisplayConverter());
	}

	protected IDisplayConverter getShortDisplayConverter() {
		return getNumericDisplayConverter(
				Preferences.getSpaceEditorIntegerFormat(),
				new DefaultShortDisplayConverter());
	}

	protected IDisplayConverter getDateTimeDisplayConverter() {
		String pattern = Preferences
				.getString(Preferences.SPACE_EDITOR_DATE_FORMAT);
		TimeZone timeZone = Preferences.getSpaceEditorTimeZone();
		return new DateTimeDisplayConverter(pattern, timeZone);
	}

	protected String[] getStringConfigLabels() {
		return new String[] { STRING_CONFIG_LABEL };
	}

	protected String[] getShortConfigLabels() {
		return new String[] { SHORT_CONFIG_LABEL };
	}

	protected String[] getLongConfigLabels() {
		return new String[] { LONG_CONFIG_LABEL };
	}

	protected String[] getIntegerConfigLabels() {
		return new String[] { INTEGER_CONFIG_LABEL };
	}

	protected String[] getFloatConfigLabels() {
		return new String[] { FLOAT_CONFIG_LABEL };
	}

	protected String[] getDoubleConfigLabels() {
		return new String[] { DOUBLE_CONFIG_LABEL };
	}

	protected String[] getDateTimeConfigLabels() {
		return new String[] { DATETIME_CONFIG_LABEL };
	}

	protected String[] getCharConfigLabels() {
		return new String[] { CHAR_CONFIG_LABEL };
	}

	protected String[] getBooleanConfigLabels() {
		List<String> labels = new ArrayList<String>();
		labels.add(BOOLEAN_CONFIG_LABEL);
		if (Preferences.getString(Preferences.SPACE_EDITOR_BOOLEAN_FORMAT) == null) {
			labels.add(CHECKBOX_EDITOR_CONFIG_LABEL);
			labels.add(CHECKBOX_PAINTER_CONFIG_LABEL);
		}
		return labels.toArray(new String[labels.size()]);
	}

	protected String[] getBlobConfigLabels() {
		return new String[] { BLOB_CONFIG_LABEL };
	}

	public String[] getLabels(FieldDef fieldDef) {
		switch (fieldDef.getType()) {
		case BLOB:
			return getBlobConfigLabels();
		case BOOLEAN:
			return getBooleanConfigLabels();
		case CHAR:
			return getCharConfigLabels();
		case DATETIME:
			return getDateTimeConfigLabels();
		case DOUBLE:
			return getDoubleConfigLabels();
		case FLOAT:
			return getFloatConfigLabels();
		case INTEGER:
			return getIntegerConfigLabels();
		case LONG:
			return getLongConfigLabels();
		case SHORT:
			return getShortConfigLabels();
		default:
			return getStringConfigLabels();
		}
	}
}
