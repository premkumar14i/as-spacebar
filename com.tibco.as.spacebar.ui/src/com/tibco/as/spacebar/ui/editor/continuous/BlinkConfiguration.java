package com.tibco.as.spacebar.ui.editor.continuous;

import java.util.Arrays;

import org.eclipse.nebula.widgets.nattable.blink.BlinkConfigAttributes;
import org.eclipse.nebula.widgets.nattable.blink.IBlinkingCellResolver;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.swt.graphics.Color;

import com.tibco.as.spacebar.ui.editor.AbstractConfiguration;
import com.tibco.as.spacebar.ui.preferences.Preferences;

public class BlinkConfiguration extends AbstractConfiguration {

	public static final String BLOB_BLINK_RESOLVER = "blobBlinkResolver";
	public static final String BOOLEAN_BLINK_RESOLVER = "booleanBlinkResolver";
	public static final String CHAR_BLINK_RESOLVER = "charBlinkResolver";
	public static final String DATETIME_BLINK_RESOLVER = "dateTimeBlinkResolver";
	public static final String DOUBLE_BLINK_RESOLVER = "doubleBlinkResolver";
	public static final String FLOAT_BLINK_RESOLVER = "floatBlinkResolver";
	public static final String INTEGER_BLINK_RESOLVER = "integerBlinkResolver";
	public static final String LONG_BLINK_RESOLVER = "longBlinkResolver";
	public static final String SHORT_BLINK_RESOLVER = "shortBlinkResolver";
	public static final String STRING_BLINK_RESOLVER = "stringBlinkResolver";

	public static final String BLINK_CONFIG_LABEL = "blinkConfigLabel";
	private static final String BLINK_UP_CONFIG_LABEL = "blinkUpConfigLabel";
	private static final String BLINK_DOWN_CONFIG_LABEL = "blinkDownConfigLabel";

	private Color colorBlink = Preferences.getColor(Preferences.SPACE_EDITOR_COLOR_BLINK);
	private Color colorBlinkUp = Preferences
			.getColor(Preferences.SPACE_EDITOR_COLOR_BLINK_UP);
	private Color colorBlinkDown = Preferences
			.getColor(Preferences.SPACE_EDITOR_COLOR_BLINK_DOWN);

	@Override
	public void configureRegistry(IConfigRegistry configRegistry) {
		super.configureRegistry(configRegistry);
		configRegistry.registerConfigAttribute(
				BlinkConfigAttributes.BLINK_RESOLVER,
				newBlinkResolver(super.getBlobConfigLabels()),
				DisplayMode.NORMAL, BLOB_BLINK_RESOLVER);
		configRegistry.registerConfigAttribute(
				BlinkConfigAttributes.BLINK_RESOLVER,
				getBooleanBlinkResolver(), DisplayMode.NORMAL,
				BOOLEAN_BLINK_RESOLVER);
		configRegistry.registerConfigAttribute(
				BlinkConfigAttributes.BLINK_RESOLVER,
				newBlinkResolver(super.getCharConfigLabels()),
				DisplayMode.NORMAL, CHAR_BLINK_RESOLVER);
		configRegistry.registerConfigAttribute(
				BlinkConfigAttributes.BLINK_RESOLVER,
				newBlinkResolver(super.getDateTimeConfigLabels()),
				DisplayMode.NORMAL, DATETIME_BLINK_RESOLVER);
		configRegistry.registerConfigAttribute(
				BlinkConfigAttributes.BLINK_RESOLVER, getDoubleBlinkResolver(),
				DisplayMode.NORMAL, DOUBLE_BLINK_RESOLVER);
		configRegistry.registerConfigAttribute(
				BlinkConfigAttributes.BLINK_RESOLVER, getFloatBlinkResolver(),
				DisplayMode.NORMAL, FLOAT_BLINK_RESOLVER);
		configRegistry.registerConfigAttribute(
				BlinkConfigAttributes.BLINK_RESOLVER,
				getIntegerBlinkResolver(), DisplayMode.NORMAL,
				INTEGER_BLINK_RESOLVER);
		configRegistry.registerConfigAttribute(
				BlinkConfigAttributes.BLINK_RESOLVER, getLongBlinkResolver(),
				DisplayMode.NORMAL, LONG_BLINK_RESOLVER);
		configRegistry.registerConfigAttribute(
				BlinkConfigAttributes.BLINK_RESOLVER, getShortBlinkResolver(),
				DisplayMode.NORMAL, SHORT_BLINK_RESOLVER);
		configRegistry.registerConfigAttribute(
				BlinkConfigAttributes.BLINK_RESOLVER,
				newBlinkResolver(super.getStringConfigLabels()),
				DisplayMode.NORMAL, STRING_BLINK_RESOLVER);
		// Styles
		registerBackgroundColor(configRegistry, BLINK_CONFIG_LABEL, colorBlink);
		registerBackgroundColor(configRegistry, BLINK_UP_CONFIG_LABEL,
				colorBlinkUp);
		registerBackgroundColor(configRegistry, BLINK_DOWN_CONFIG_LABEL,
				colorBlinkDown);
	}

	private IBlinkingCellResolver newBlinkResolver(String[] configLabels) {
		BlinkResolver resolver = new BlinkResolver(configLabels);
		resolver.setLast(BLINK_CONFIG_LABEL);
		return resolver;
	}

	@Override
	protected String[] getBlobConfigLabels() {
		return toArray(super.getBlobConfigLabels(), BLOB_BLINK_RESOLVER);
	}

	@Override
	protected String[] getBooleanConfigLabels() {
		return toArray(super.getBooleanConfigLabels(), BOOLEAN_BLINK_RESOLVER);
	}

	@Override
	protected String[] getCharConfigLabels() {
		return toArray(super.getCharConfigLabels(), CHAR_BLINK_RESOLVER);
	}

	@Override
	protected String[] getDateTimeConfigLabels() {
		return toArray(super.getDateTimeConfigLabels(), DATETIME_BLINK_RESOLVER);
	}

	@Override
	protected String[] getDoubleConfigLabels() {
		return toArray(super.getDoubleConfigLabels(), DOUBLE_BLINK_RESOLVER);
	}

	@Override
	protected String[] getFloatConfigLabels() {
		return toArray(super.getFloatConfigLabels(), FLOAT_BLINK_RESOLVER);
	}

	@Override
	protected String[] getIntegerConfigLabels() {
		return toArray(super.getIntegerConfigLabels(), INTEGER_BLINK_RESOLVER);
	}

	@Override
	protected String[] getLongConfigLabels() {
		return toArray(super.getLongConfigLabels(), LONG_BLINK_RESOLVER);
	}

	@Override
	protected String[] getShortConfigLabels() {
		return toArray(super.getShortConfigLabels(), SHORT_BLINK_RESOLVER);
	}

	@Override
	protected String[] getStringConfigLabels() {
		return toArray(super.getStringConfigLabels(), STRING_BLINK_RESOLVER);
	}

	private String[] toArray(String[] labels, String label) {
		String[] result = Arrays.copyOf(labels, labels.length + 1);
		result[labels.length] = label;
		return result;
	}

	public void dispose() {
		colorBlink.dispose();
		colorBlinkDown.dispose();
		colorBlinkUp.dispose();
	}

	private void registerBackgroundColor(IConfigRegistry configRegistry,
			String configLabel, Color color) {
		Style cellStyle = new Style();
		cellStyle
				.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, color);
		configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE,
				cellStyle, DisplayMode.NORMAL, configLabel);
	}

	private IBlinkingCellResolver getDoubleBlinkResolver() {
		return new BlinkResolver(super.getDoubleConfigLabels()) {

			public String[] resolve(Object oldValue, Object newValue) {
				Double old = (Double) oldValue;
				Double latest = (Double) newValue;
				if (latest != null && old != null) {
					setLast(latest > old ? BLINK_UP_CONFIG_LABEL
							: BLINK_DOWN_CONFIG_LABEL);
				}
				return getConfigLabels();
			}
		};
	}

	private IBlinkingCellResolver getFloatBlinkResolver() {
		return new BlinkResolver(super.getFloatConfigLabels()) {

			public String[] resolve(Object oldValue, Object newValue) {
				Float old = (Float) oldValue;
				Float latest = (Float) newValue;
				if (latest != null && old != null) {
					setLast((latest > old ? BLINK_UP_CONFIG_LABEL
							: BLINK_DOWN_CONFIG_LABEL));
				}
				return getConfigLabels();
			}
		};
	}

	private IBlinkingCellResolver getIntegerBlinkResolver() {
		return new BlinkResolver(super.getIntegerConfigLabels()) {

			public String[] resolve(Object oldValue, Object newValue) {
				Integer old = (Integer) oldValue;
				Integer latest = (Integer) newValue;
				if (latest != null && old != null) {
					setLast((latest > old ? BLINK_UP_CONFIG_LABEL
							: BLINK_DOWN_CONFIG_LABEL));
				}
				return getConfigLabels();
			}
		};
	}

	private IBlinkingCellResolver getLongBlinkResolver() {
		return new BlinkResolver(super.getLongConfigLabels()) {

			public String[] resolve(Object oldValue, Object newValue) {
				Long old = (Long) oldValue;
				Long latest = (Long) newValue;
				if (latest != null && old != null) {
					setLast((latest > old ? BLINK_UP_CONFIG_LABEL
							: BLINK_DOWN_CONFIG_LABEL));
				}
				return getConfigLabels();
			}
		};
	}

	private IBlinkingCellResolver getShortBlinkResolver() {
		return new BlinkResolver(super.getShortConfigLabels()) {

			public String[] resolve(Object oldValue, Object newValue) {
				Short old = (Short) oldValue;
				Short latest = (Short) newValue;
				if (latest != null && old != null) {
					setLast((latest > old ? BLINK_UP_CONFIG_LABEL
							: BLINK_DOWN_CONFIG_LABEL));
				}
				return getConfigLabels();
			}
		};
	}

	private IBlinkingCellResolver getBooleanBlinkResolver() {
		return new BlinkResolver(super.getBooleanConfigLabels()) {

			public String[] resolve(Object oldValue, Object newValue) {
				Boolean old = (Boolean) oldValue;
				Boolean latest = (Boolean) newValue;
				if (latest != null && old != null && !latest.equals(old)) {
					setLast((latest ? BLINK_UP_CONFIG_LABEL
							: BLINK_DOWN_CONFIG_LABEL));
				}
				return getConfigLabels();
			}
		};
	}

}
