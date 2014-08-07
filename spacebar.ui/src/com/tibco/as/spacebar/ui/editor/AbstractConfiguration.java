package com.tibco.as.spacebar.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.convert.IDisplayConverter;
import org.eclipse.nebula.widgets.nattable.painter.cell.CheckBoxPainter;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;

import com.tibco.as.convert.Attribute;
import com.tibco.as.convert.Attributes;
import com.tibco.as.convert.UnsupportedConversionException;
import com.tibco.as.space.DateTime;
import com.tibco.as.space.FieldDef;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.editor.display.StringDisplayConverter;
import com.tibco.as.spacebar.ui.preferences.Preferences;

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
		try {
			configRegistry.registerConfigAttribute(
					CellConfigAttributes.DISPLAY_CONVERTER,
					getConverter(byte[].class, "BLOB"), DisplayMode.NORMAL,
					BLOB_CONFIG_LABEL);
			configRegistry.registerConfigAttribute(
					CellConfigAttributes.DISPLAY_CONVERTER,
					getConverter(Boolean.class), DisplayMode.NORMAL,
					BOOLEAN_CONFIG_LABEL);
			configRegistry.registerConfigAttribute(
					CellConfigAttributes.DISPLAY_CONVERTER,
					getConverter(Character.class), DisplayMode.NORMAL,
					CHAR_CONFIG_LABEL);
			configRegistry.registerConfigAttribute(
					CellConfigAttributes.DISPLAY_CONVERTER,
					getConverter(DateTime.class, "date"), DisplayMode.NORMAL,
					DATETIME_CONFIG_LABEL);
			configRegistry.registerConfigAttribute(
					CellConfigAttributes.DISPLAY_CONVERTER,
					getConverter(Double.class), DisplayMode.NORMAL,
					DOUBLE_CONFIG_LABEL);
			configRegistry.registerConfigAttribute(
					CellConfigAttributes.DISPLAY_CONVERTER,
					getConverter(Float.class), DisplayMode.NORMAL,
					FLOAT_CONFIG_LABEL);
			configRegistry.registerConfigAttribute(
					CellConfigAttributes.DISPLAY_CONVERTER,
					getConverter(Integer.class), DisplayMode.NORMAL,
					INTEGER_CONFIG_LABEL);
			configRegistry.registerConfigAttribute(
					CellConfigAttributes.DISPLAY_CONVERTER,
					getConverter(Long.class), DisplayMode.NORMAL,
					LONG_CONFIG_LABEL);
			configRegistry.registerConfigAttribute(
					CellConfigAttributes.DISPLAY_CONVERTER,
					getConverter(Short.class), DisplayMode.NORMAL,
					SHORT_CONFIG_LABEL);
		} catch (UnsupportedConversionException e) {
			SpaceBarPlugin.logException(e);
		}
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.DISPLAY_CONVERTER,
				new StringDisplayConverter(), DisplayMode.NORMAL,
				STRING_CONFIG_LABEL);
		configRegistry.registerConfigAttribute(
				CellConfigAttributes.CELL_PAINTER, new CheckBoxPainter(),
				DisplayMode.NORMAL, CHECKBOX_PAINTER_CONFIG_LABEL);
	}

	protected <T> FieldDisplayConverter<T> getConverter(Class<T> clazz,
			String name) throws UnsupportedConversionException {
		return FieldDisplayConverter.create(clazz, name, getAttributes());
	}

	protected <T> IDisplayConverter getConverter(Class<T> clazz)
			throws UnsupportedConversionException {
		return getConverter(clazz, clazz.getSimpleName().toLowerCase());
	}

	protected Attributes getAttributes() {
		Attributes attributes = new Attributes();
		attributes.put(Attribute.BOOLEAN,
				Preferences.getString(Preferences.SPACE_EDITOR_BOOLEAN_FORMAT));
		attributes.put(Attribute.DATE,
				Preferences.getString(Preferences.SPACE_EDITOR_DATE_FORMAT));
		attributes
				.put(Attribute.TIMEZONE, Preferences.getSpaceEditorTimeZone());
		attributes.put(Attribute.INTEGER, Preferences.getString(Preferences.SPACE_EDITOR_INTEGER_FORMAT));
		attributes.put(Attribute.DECIMAL, Preferences.getString(Preferences.SPACE_EDITOR_DECIMAL_FORMAT));
		return attributes;
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
