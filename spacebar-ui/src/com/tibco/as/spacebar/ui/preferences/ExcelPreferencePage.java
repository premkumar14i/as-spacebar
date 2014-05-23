package com.tibco.as.spacebar.ui.preferences;

import org.apache.poi.ss.SpreadsheetVersion;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

import com.tibco.as.convert.ConverterFactory.Blob;

import com.tibco.as.spacebar.ui.Messages;

public class ExcelPreferencePage extends TabbedPreferencePage {

	@Override
	protected void addTabFields(TabFolder folder) {
		createImport(createTab(folder, "Import")); //$NON-NLS-1$
		createExport(createTab(folder, "Export")); //$NON-NLS-1$
	}

	private Composite createImport(Composite parent) {
		Composite composite = createTabItemComposite(parent);
		addBooleanField(Preferences.IMPORT_EXCEL_HEADER,
				Messages.Excel_Import_Header, composite,
				Messages.Excel_Import_Header_Tooltip);
		addComboField(Preferences.IMPORT_EXCEL_FORMAT_BLOB,
				Messages.Formats_Blob, new String[][] {
						{ Messages.Formats_Blob_Base64, Blob.BASE64.name() },
						{ Messages.Formats_Blob_Hex, Blob.HEX.name() } },
				composite, Messages.Formats_Blob_Tooltip);

		return composite;
	}

	private Composite createExport(Composite parent) {
		Composite composite = createTabItemComposite(parent);
		addBooleanField(Preferences.EXPORT_EXCEL_HEADER,
				Messages.Excel_Export_Header, composite,
				Messages.Excel_Export_Header_Tooltip);
		addComboField(Preferences.EXPORT_EXCEL_VERSION,
				Messages.Excel_Export_Version, new String[][] {
						{ Messages.Excel_Export_Version_97,
								SpreadsheetVersion.EXCEL97.name() },
						{ Messages.Excel_Export_Version_2007,
								SpreadsheetVersion.EXCEL2007.name() } },
				composite, Messages.Excel_Export_Version_Tooltip);
		addComboField(Preferences.EXPORT_EXCEL_FORMAT_BLOB,
				Messages.Formats_Blob, new String[][] {
						{ Messages.Formats_Blob_Base64, Blob.BASE64.name() },
						{ Messages.Formats_Blob_Hex, Blob.HEX.name() } },
				composite, Messages.Formats_Blob_Tooltip);
		addStringField(Preferences.EXPORT_EXCEL_FORMAT_DATE,
				Messages.Formats_DateTime, composite,
				Messages.Formats_DateTime_Tooltip);
		return composite;
	}
}
