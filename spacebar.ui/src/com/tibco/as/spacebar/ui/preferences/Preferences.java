package com.tibco.as.spacebar.ui.preferences;

import java.util.TimeZone;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.tibco.as.convert.Attribute;
import com.tibco.as.convert.Attributes;
import com.tibco.as.convert.ConverterFactory.Blob;
import com.tibco.as.io.Export;
import com.tibco.as.io.Import;
import com.tibco.as.io.Operation;
import com.tibco.as.io.file.excel.ExcelExport;
import com.tibco.as.io.file.excel.ExcelImport;
import com.tibco.as.io.file.text.delimited.DelimitedExport;
import com.tibco.as.io.file.text.delimited.DelimitedImport;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;

/**
 * Constant definitions for plug-in preferences
 */
public class Preferences {

	public static final String SPACE_EDITOR_THEME = "spaceEditorTheme";
	public static final String SPACE_EDITOR_BROWSE_TIME_SCOPE = "spaceEditorBrowseTimeScope";
	public static final String SPACE_EDITOR_BROWSE_PREFETCH = "spaceEditorBrowsePrefetch";
	public static final String SPACE_EDITOR_BROWSE_TIMEOUT = "spaceEditorBrowseTimeout";
	public static final String SPACE_EDITOR_BROWSE_QUERY_LIMIT = "spaceEditorBrowseQueryLimit";
	public static final String SPACE_EDITOR_BROWSE_LIMIT = "spaceEditorBrowseLimit";
	public static final String SPACE_EDITOR_BOOLEAN_FORMAT = "spaceEditorBooleanFormat";
	public static final String SPACE_EDITOR_DATE_FORMAT = "spaceEditorDateFormat";
	public static final String SPACE_EDITOR_DECIMAL_FORMAT = "spaceEditorDecimalFormat";
	public static final String SPACE_EDITOR_INTEGER_FORMAT = "spaceEditorIntegerFormat";
	public static final String SPACE_EDITOR_TIME_ZONE = "spaceEditorTimeZone";
	public static final String SPACE_EDITOR_COLOR_BLINK = "spaceEditorColorBlink";
	public static final String SPACE_EDITOR_COLOR_BLINK_UP = "spaceEditorColorBlinkUp";
	public static final String SPACE_EDITOR_COLOR_BLINK_DOWN = "spaceEditorColorBlinkDown";
	public static final String SPACE_EDITOR_CLIPBOARD_SEPARATOR = "spaceEditorClipboardSeparator";
	public static final String SPACE_EDITOR_CLIPBOARD_HEADER = "spaceEditorClipboardHeader";
	public static final String EXPORT_TIME_SCOPE = "exportTimeScope";
	public static final String EXPORT_BATCH_SIZE = "exportBatchSize";
	public static final String EXPORT_WORKER_COUNT = "exportWorkerCount";
	public static final String EXPORT_TIMEOUT = "exportTimeout";
	public static final String EXPORT_PREFETCH = "exportPrefetch";
	public static final String EXPORT_QUERY_LIMIT = "exportQueryLimit";
	public static final String EXPORT_CSV_AUTOFLUSH = "exportCSVAutoflush";
	public static final String EXPORT_CSV_HEADER = "exportCSVNoHeader";
	public static final String EXPORT_CSV_SEPARATOR = "exportCSVSeparator";
	public static final String EXPORT_CSV_QUOTE_CHARACTER = "exportCSVQuoteChar";
	public static final String EXPORT_CSV_ESCAPE_CHARACTER = "exportCSVEscapeChar";
	public static final String EXPORT_CSV_FORMAT_BLOB = "exportCSVFormatBlob";
	public static final String EXPORT_CSV_FORMAT_BOOLEAN = "exportCSVFormatBoolean";
	public static final String EXPORT_CSV_FORMAT_DATE = "exportCSVFormatDate";
	public static final String EXPORT_CSV_FORMAT_INTEGER = "exportCSVFormatInteger";
	public static final String EXPORT_CSV_FORMAT_DECIMAL = "exportCSVFormatDecimal";
	public static final String EXPORT_EXCEL_HEADER = "exportExcelHeader";
	public static final String EXPORT_EXCEL_VERSION = "exportExcelVersion";
	public static final String EXPORT_EXCEL_FORMAT_BLOB = "exportExcelFormatBlob";
	public static final String EXPORT_EXCEL_FORMAT_DATE = "exportExcelFormatDate";
	public static final String IMPORT_BATCH_SIZE = "importBatchSize";
	public static final String IMPORT_WORKER_COUNT = "importWorkerCount";
	public static final String IMPORT_OPERATION = "importOperation";
	public static final String IMPORT_DISTRIBUTION_ROLE = "distributionRole";
	public static final String IMPORT_WAIT_FOR_READY_TIMEOUT = "importWaitForReadyTimeout";
	public static final String IMPORT_CSV_HEADER = "importCSVHeader";
	public static final String IMPORT_CSV_ESCAPE_CHARACTER = "importCSVEscape";
	public static final String IMPORT_CSV_IGNORE_LEADING_WHITE_SPACE = "importCSVIgnoreLeadingWhiteSpace";
	public static final String IMPORT_CSV_QUOTE_CHARACTER = "importCSVQuoteChar";
	public static final String IMPORT_CSV_SEPARATOR = "importCSVSeparator";
	public static final String IMPORT_CSV_STRICT_QUOTES = "importCSVStrictQuotes";
	public static final String IMPORT_CSV_FORMAT_BLOB = "importCSVFormatBlob";
	public static final String IMPORT_CSV_FORMAT_BOOLEAN = "importCSVFormatBoolean";
	public static final String IMPORT_CSV_FORMAT_DATE = "importCSVFormatDate";
	public static final String IMPORT_CSV_FORMAT_INTEGER = "importCSVFormatInteger";
	public static final String IMPORT_CSV_FORMAT_DECIMAL = "importCSVFormatDecimal";
	public static final String IMPORT_EXCEL_HEADER = "importExcelHeader";
	public static final String IMPORT_EXCEL_FORMAT_BLOB = "importExcelFormatBlob";
	public static final String TIMESCOPE_ALL = "ALL";
	public static final String TIMESCOPE_NEW = "NEW";
	public static final String TIMESCOPE_CURRENT = "CURRENT";
	public static final String TIMESCOPE_SNAPSHOT = "SNAPSHOT";
	public static final String THEME_DEFAULT = "Default";
	public static final String THEME_MODERN = "Modern";
	public static final String THEME_DARK = "Dark";

	public static String getPreferenceName(String... parts) {
		String name = "";
		for (int index = 0; index < parts.length; index++) {
			if (index > 0) {
				name += ":";
			}
			name += parts[index];
		}
		return name;
	}

	public static void configureExport(Export export) {
		export.setBatchSize(getInteger(EXPORT_BATCH_SIZE));
		export.setPrefetch(getLong(EXPORT_PREFETCH));
		export.setQueryLimit(getLong(EXPORT_QUERY_LIMIT));
		export.setTimeout(getLong(EXPORT_TIMEOUT));
		export.setTimeScope(com.tibco.as.space.browser.BrowserDef.TimeScope
				.valueOf(getString(EXPORT_TIME_SCOPE)));
		export.setWorkerCount(getInteger(EXPORT_WORKER_COUNT));
	}

	public static void configureImport(Import config) {
		config.setBatchSize(getInteger(IMPORT_BATCH_SIZE));
		config.setWorkerCount(getInteger(IMPORT_WORKER_COUNT));
		config.setOperation(Operation.valueOf(getString(IMPORT_OPERATION)));
		config.setDistributionRole(com.tibco.as.space.Member.DistributionRole
				.valueOf(getString(IMPORT_DISTRIBUTION_ROLE)));
		config.setWaitForReadyTimeout(getLong(IMPORT_WAIT_FOR_READY_TIMEOUT));
	}

	public static Long getBrowseTimeout(String timeScope) {
		return getLong(getPreferenceName(SPACE_EDITOR_BROWSE_TIMEOUT, timeScope));
	}

	public static Long getBrowsePrefetch(String timeScope) {
		return getLong(getPreferenceName(SPACE_EDITOR_BROWSE_PREFETCH,
				timeScope));
	}

	private static IPreferenceStore getPreferenceStore() {
		return SpaceBarPlugin.getDefault().getPreferenceStore();
	}

	public static Long getBrowseQueryLimit(String timeScope) {
		return getLong(getPreferenceName(SPACE_EDITOR_BROWSE_QUERY_LIMIT,
				timeScope));
	}

	public static Integer getInteger(String name) {
		int result = getPreferenceStore().getInt(name);
		if (result == IPreferenceStore.INT_DEFAULT_DEFAULT) {
			return null;
		}
		return result;
	}

	public static Long getLong(String name) {
		long result = getPreferenceStore().getLong(name);
		if (result == IPreferenceStore.LONG_DEFAULT_DEFAULT) {
			return null;
		}
		return result;
	}

	public static Boolean getBoolean(String name) {
		boolean result = getPreferenceStore().getBoolean(name);
		if (result == IPreferenceStore.BOOLEAN_DEFAULT_DEFAULT) {
			return null;
		}
		return result;
	}

	public static Character getChar(String name) {
		return getChar(name, null);
	}

	public static Character getChar(String name, Character defaultValue) {
		String value = getPreferenceStore().getString(name);
		if (IPreferenceStore.STRING_DEFAULT_DEFAULT.equals(value)) {
			return defaultValue;
		}
		return value.charAt(0);
	}

	public static String getString(String name) {
		String value = getPreferenceStore().getString(name);
		if (IPreferenceStore.STRING_DEFAULT_DEFAULT.equals(value)) {
			return null;
		}
		return value;
	}

	/**
	 * 
	 * @param name
	 * @return color for that preference name, or null if no color set
	 */
	public static Color getColor(String name) {
		return new Color(Display.getDefault(), PreferenceConverter.getColor(
				getPreferenceStore(), name));
	}

	public static TimeZone getSpaceEditorTimeZone() {
		String id = getString(Preferences.SPACE_EDITOR_TIME_ZONE);
		if (id == null) {
			return null;
		}
		return TimeZone.getTimeZone(id);
	}

	public static ExcelImport getExcelImport() {
		ExcelImport excelImport = new ExcelImport();
		excelImport.setHeader(getBoolean(IMPORT_EXCEL_HEADER));
		return excelImport;
	}

	public static void configureExcelExport(ExcelExport config) {
		configureExport(config);
		config.setHeader(getBoolean(EXPORT_EXCEL_HEADER));
		Attributes conversion = new Attributes();
		conversion.put(Attribute.DATE, getString(EXPORT_EXCEL_FORMAT_DATE));
		conversion.put(Attribute.BLOB,
				Blob.valueOf(getString(EXPORT_EXCEL_FORMAT_BLOB)));
		config.getAttributes().putAll(conversion);
	}

	public static void configureExcelImport(ExcelImport config) {
		configureImport(config);
		config.setHeader(Preferences
				.getBoolean(Preferences.IMPORT_EXCEL_HEADER));
		Attributes conversion = new Attributes();
		conversion.put(Attribute.BLOB,
				Blob.valueOf(getString(IMPORT_EXCEL_FORMAT_BLOB)));
		config.getAttributes().putAll(conversion);
	}

	public static Export getSpaceEditorExport(String timeScope) {
		Export export = new Export();
		export.setLimit(getLong(Preferences.SPACE_EDITOR_BROWSE_LIMIT));
		export.setTimeScope(com.tibco.as.space.browser.BrowserDef.TimeScope
				.valueOf(timeScope));
		export.setPrefetch(getBrowsePrefetch(timeScope));
		export.setTimeout(getBrowseTimeout(timeScope));
		export.setQueryLimit(getBrowseQueryLimit(timeScope));
		return export;
	}

	public static void configureDelimitedImport(DelimitedImport config) {
		configureImport(config);
		config.setEscapeChar(getChar(IMPORT_CSV_ESCAPE_CHARACTER));
		config.setHeader(getBoolean(IMPORT_CSV_HEADER));
		config.setQuoteChar(getChar(IMPORT_CSV_QUOTE_CHARACTER));
		config.setSeparator(getChar(IMPORT_CSV_SEPARATOR));
		Attributes conversion = new Attributes();
		conversion.put(Attribute.BLOB,
				Blob.valueOf(getString(IMPORT_CSV_FORMAT_BLOB)));
		conversion.put(Attribute.BOOLEAN, getString(IMPORT_CSV_FORMAT_BOOLEAN));
		conversion.put(Attribute.DATE, getString(IMPORT_CSV_FORMAT_DATE));
		conversion.put(Attribute.INTEGER, getString(IMPORT_CSV_FORMAT_INTEGER));
		conversion.put(Attribute.DECIMAL, getString(IMPORT_CSV_FORMAT_DECIMAL));
		config.getAttributes().putAll(conversion);
	}

	public static void configureDelimitedExport(DelimitedExport config) {
		configureExport(config);
		config.setEscapeChar(getChar(EXPORT_CSV_ESCAPE_CHARACTER));
		config.setHeader(getBoolean(EXPORT_CSV_HEADER));
		config.setQuoteChar(getChar(EXPORT_CSV_QUOTE_CHARACTER));
		config.setSeparator(getChar(EXPORT_CSV_SEPARATOR));
		Attributes conversion = new Attributes();
		conversion.put(Attribute.BLOB,
				Blob.valueOf(getString(EXPORT_CSV_FORMAT_BLOB)));
		conversion.put(Attribute.BOOLEAN, getString(EXPORT_CSV_FORMAT_BOOLEAN));
		conversion.put(Attribute.DATE, getString(EXPORT_CSV_FORMAT_DATE));
		conversion.put(Attribute.INTEGER, getString(EXPORT_CSV_FORMAT_INTEGER));
		conversion.put(Attribute.DECIMAL, getString(EXPORT_CSV_FORMAT_DECIMAL));
		config.getAttributes().putAll(conversion);

	}

}
