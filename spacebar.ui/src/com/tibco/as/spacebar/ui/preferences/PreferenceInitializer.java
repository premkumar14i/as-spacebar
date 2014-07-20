package com.tibco.as.spacebar.ui.preferences;

import java.util.TimeZone;

import org.apache.poi.ss.SpreadsheetVersion;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVWriter;

import com.tibco.as.convert.ConverterFactory;
import com.tibco.as.convert.ConverterFactory.Blob;
import com.tibco.as.io.file.excel.ExcelExporter;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = SpaceBarPlugin.getDefault()
				.getPreferenceStore();
		store.setDefault(Preferences.EXPORT_BATCH_SIZE, 1000);
		store.setDefault(Preferences.EXPORT_WORKER_COUNT, 1);
		store.setDefault(Preferences.EXPORT_TIME_SCOPE, "CURRENT");
		store.setDefault(Preferences.EXPORT_PREFETCH, 1000L);
		store.setDefault(Preferences.EXPORT_QUERY_LIMIT, 100000L);
		store.setDefault(Preferences.EXPORT_TIMEOUT, -1L);
		store.setDefault(Preferences.EXPORT_CLIPBOARD_SEPARATOR, "\\t");
		store.setDefault(Preferences.EXPORT_CSV_HEADER, true);
		store.setDefault(Preferences.EXPORT_CSV_SEPARATOR,
				String.valueOf(CSVWriter.DEFAULT_SEPARATOR));
		// not a bug, import and export have to use the same default escape
		// character
		store.setDefault(Preferences.EXPORT_CSV_ESCAPE_CHARACTER,
				String.valueOf(CSVParser.DEFAULT_ESCAPE_CHARACTER));
		store.setDefault(Preferences.EXPORT_CSV_QUOTE_CHARACTER,
				String.valueOf(CSVWriter.DEFAULT_QUOTE_CHARACTER));
		store.setDefault(Preferences.EXPORT_CSV_FORMAT_BLOB, Blob.HEX.name());
		store.setDefault(Preferences.EXPORT_CSV_FORMAT_BOOLEAN,
				ConverterFactory.DEFAULT_PATTERN_BOOLEAN);
		store.setDefault(Preferences.EXPORT_CSV_FORMAT_NUMBER,
				IPreferenceStore.STRING_DEFAULT_DEFAULT);
		// store.setDefault(Preferences.EXPORT_CSV_FORMAT_DATE,
		// ConverterFactory.DEFAULT_PATTERN_DATE);
		store.setDefault(Preferences.EXPORT_EXCEL_HEADER, true);
		store.setDefault(Preferences.EXPORT_EXCEL_VERSION,
				SpreadsheetVersion.EXCEL2007.name());
		store.setDefault(Preferences.EXPORT_EXCEL_FORMAT_BLOB, Blob.HEX.name());
		store.setDefault(Preferences.EXPORT_EXCEL_FORMAT_DATE,
				ExcelExporter.DEFAULT_DATETIME_PATTERN);
		store.setDefault(Preferences.IMPORT_BATCH_SIZE, 1000);
		store.setDefault(Preferences.IMPORT_WORKER_COUNT, 1);
		store.setDefault(Preferences.IMPORT_OPERATION, "PUT");
		store.setDefault(Preferences.IMPORT_DISTRIBUTION_ROLE, "LEECH");
		store.setDefault(Preferences.IMPORT_WAIT_FOR_READY_TIMEOUT, 30000L);
		store.setDefault(Preferences.IMPORT_CSV_HEADER, true);
		store.setDefault(Preferences.IMPORT_CSV_ESCAPE_CHARACTER,
				String.valueOf(CSVParser.DEFAULT_ESCAPE_CHARACTER));
		store.setDefault(Preferences.IMPORT_CSV_IGNORE_LEADING_WHITE_SPACE,
				CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE);
		store.setDefault(Preferences.IMPORT_CSV_QUOTE_CHARACTER,
				String.valueOf(CSVParser.DEFAULT_QUOTE_CHARACTER));
		store.setDefault(Preferences.IMPORT_CSV_SEPARATOR,
				String.valueOf(CSVParser.DEFAULT_SEPARATOR));
		store.setDefault(Preferences.IMPORT_CSV_STRICT_QUOTES,
				CSVParser.DEFAULT_STRICT_QUOTES);
		store.setDefault(Preferences.IMPORT_CSV_FORMAT_BLOB, Blob.HEX.name());
		store.setDefault(Preferences.IMPORT_CSV_FORMAT_BOOLEAN,
				ConverterFactory.DEFAULT_PATTERN_BOOLEAN);
		store.setDefault(Preferences.IMPORT_CSV_FORMAT_NUMBER,
				IPreferenceStore.STRING_DEFAULT_DEFAULT);
		// store.setDefault(Preferences.IMPORT_CSV_FORMAT_DATE,
		// ConverterFactory.DEFAULT_PATTERN_DATE);
		store.setDefault(Preferences.IMPORT_EXCEL_HEADER, true);
		store.setDefault(Preferences.IMPORT_EXCEL_FORMAT_BLOB, Blob.HEX.name());
		store.setDefault(Preferences.SPACE_EDITOR_THEME,
				Preferences.THEME_MODERN);
		PreferenceConverter.setDefault(store,
				Preferences.SPACE_EDITOR_COLOR_BLINK, new RGB(255, 204, 0));
		PreferenceConverter.setDefault(store,
				Preferences.SPACE_EDITOR_COLOR_BLINK_UP, new RGB(0, 204, 0));
		PreferenceConverter.setDefault(store,
				Preferences.SPACE_EDITOR_COLOR_BLINK_DOWN, new RGB(204, 0, 0));
		// store.setDefault(Preferences.SPACE_EDITOR_DATE_FORMAT,
		// ConverterFactory.DEFAULT_PATTERN_DATE);
		store.setDefault(Preferences.SPACE_EDITOR_TIME_ZONE, TimeZone
				.getTimeZone("GMT").getID());
		store.setDefault(Preferences.SPACE_EDITOR_BROWSE_TIME_SCOPE, "CURRENT");
		store.setDefault(Preferences.SPACE_EDITOR_BROWSE_LIMIT, 10000L);
		setBrowseDefaults(store, Preferences.TIMESCOPE_ALL, 1000L, 10000L, -1L);
		setBrowseDefaults(store, Preferences.TIMESCOPE_NEW, null, null, -1L);
		setBrowseDefaults(store, Preferences.TIMESCOPE_SNAPSHOT, 1000L,
				100000L, null);
		setBrowseDefaults(store, Preferences.TIMESCOPE_CURRENT, 1000L, null,
				null);
	}

	private void setBrowseDefaults(IPreferenceStore store, String timeScope,
			Long prefetch, Long queryLimit, Long timeout) {
		if (prefetch != null) {
			store.setDefault(Preferences.getPreferenceName(
					Preferences.SPACE_EDITOR_BROWSE_PREFETCH, timeScope),
					prefetch);
		}
		if (queryLimit != null) {
			store.setDefault(Preferences.getPreferenceName(
					Preferences.SPACE_EDITOR_BROWSE_QUERY_LIMIT, timeScope),
					queryLimit);
		}
		if (timeout != null) {
			store.setDefault(Preferences.getPreferenceName(
					Preferences.SPACE_EDITOR_BROWSE_TIMEOUT, timeScope),
					timeout);
		}

	}

}