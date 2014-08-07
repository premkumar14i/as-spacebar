package com.tibco.as.spacebar.ui.preferences;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;

import com.tibco.as.spacebar.ui.Messages;

import com.tibco.as.convert.ConverterFactory.Blob;

public class CSVPreferencePage extends TabbedPreferencePage {

	@Override
	protected void addTabFields(TabFolder folder) {
		createImport(createTab(folder, "Import")); //$NON-NLS-1$
		createExport(createTab(folder, "Export")); //$NON-NLS-1$
	}

	private Composite createImport(Composite parent) {
		Composite composite = createTabItemComposite(parent);
		addBooleanField(Preferences.IMPORT_CSV_HEADER,
				Messages.Delimited_Import_Header, composite,
				Messages.Delimited_Import_Header_Tooltip);
		addBooleanField(Preferences.IMPORT_CSV_STRICT_QUOTES,
				Messages.Delimited_Import_StrictQuotes, composite,
				Messages.Delimited_Import_StrictQuotes_Tooltip);
		addBooleanField(Preferences.IMPORT_CSV_IGNORE_LEADING_WHITE_SPACE,
				Messages.Delimited_Import_IgnoreLeadingWhiteSpace, composite,
				Messages.Delimited_Import_IgnoreLeadingWhiteSpace_Tooltip);
		addCharField(Preferences.IMPORT_CSV_SEPARATOR,
				Messages.Delimited_Import_Separator, composite,
				Messages.Delimited_Import_Separator_Tooltip, false);
		addCharField(Preferences.IMPORT_CSV_QUOTE_CHARACTER,
				Messages.Delimited_Import_Quote, composite,
				Messages.Delimited_Import_Quote_Tooltip, false);
		addCharField(Preferences.IMPORT_CSV_ESCAPE_CHARACTER,
				Messages.Delimited_Import_Escape, composite,
				Messages.Delimited_Import_Escape_Tooltip, false);
		addFormatFields(composite, Preferences.IMPORT_CSV_FORMAT_BLOB,
				Preferences.IMPORT_CSV_FORMAT_BOOLEAN,
				Preferences.IMPORT_CSV_FORMAT_DATE,
				Preferences.IMPORT_CSV_FORMAT_INTEGER,
				Preferences.IMPORT_CSV_FORMAT_DECIMAL);
		return composite;
	}

	private Composite createExport(Composite parent) {
		Composite composite = createTabItemComposite(parent);
		addBooleanField(Preferences.EXPORT_CSV_AUTOFLUSH,
				Messages.Delimited_Export_Autoflush, composite,
				Messages.Delimited_Export_Autoflush_Tooltip);
		addBooleanField(Preferences.EXPORT_CSV_HEADER,
				Messages.Delimited_Export_Header, composite,
				Messages.Delimited_Export_Header_Tooltip);
		addCharField(Preferences.EXPORT_CSV_SEPARATOR,
				Messages.Delimited_Export_Separator, composite,
				Messages.Delimited_Export_Separator_Tooltip, false);
		addCharField(Preferences.EXPORT_CSV_QUOTE_CHARACTER,
				Messages.Delimited_Export_Quote, composite,
				Messages.Delimited_Export_Quote_Tooltip, true);
		addCharField(Preferences.EXPORT_CSV_ESCAPE_CHARACTER,
				Messages.Delimited_Export_Escape, composite,
				Messages.Delimited_Export_Escape_Tooltip, true);
		addFormatFields(composite, Preferences.EXPORT_CSV_FORMAT_BLOB,
				Preferences.EXPORT_CSV_FORMAT_BOOLEAN,
				Preferences.EXPORT_CSV_FORMAT_DATE,
				Preferences.EXPORT_CSV_FORMAT_INTEGER,
				Preferences.EXPORT_CSV_FORMAT_DECIMAL);
		return composite;
	}

	private void addFormatFields(Composite parent, String blobName,
			String booleanName, String dateName, String integerName,
			String decimalName) {
		Group group = new Group(parent, SWT.NONE);
		group.setFont(parent.getFont());
		group.setText(Messages.Formats);
		GridDataFactory.defaultsFor(group).grab(true, false).span(2, 1)
				.applyTo(group);
		addComboField(blobName, Messages.Formats_Blob, new String[][] {
				{ Messages.Formats_Blob_Hex, Blob.HEX.name() },
				{ Messages.Formats_Blob_Base64, Blob.BASE64.name() } }, group,
				Messages.Formats_Blob_Tooltip);
		addStringField(booleanName, Messages.Formats_Boolean, group,
				Messages.Formats_Boolean_Tooltip);
		addStringField(dateName, Messages.Formats_DateTime, group,
				Messages.Formats_DateTime_Tooltip);
		addStringField(integerName, Messages.Formats_Integer, group,
				Messages.Formats_Integer_Tooltip);
		addStringField(decimalName, Messages.Formats_Decimal, group,
				Messages.Formats_Decimal_Tooltip);
	}

}
