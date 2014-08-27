package com.tibco.as.spacebar.ui.wizards.transfer.excel;

import org.apache.poi.ss.SpreadsheetVersion;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.tibco.as.spacebar.ui.Messages;
import com.tibco.as.spacebar.ui.SWTFactory;

import com.tibco.as.excel.ExcelExport;

public class ExcelExportConfigurationPage extends WizardPage {

	private SpreadsheetVersion version;

	private ExcelExport config;

	private ComboViewer versionCombo;

	public ExcelExportConfigurationPage(SpreadsheetVersion version,
			ExcelExport config) {
		super("ExcelExportConfigurationPage");
		setTitle("Excel Export Options");
		setDescription("Define the options for the Excel export.");
		this.version = version;
		this.config = config;
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
				| GridData.HORIZONTAL_ALIGN_FILL));
		composite.setFont(parent.getFont());

		Composite versionComposite = SWTFactory.createComposite(composite, 2,
				1, GridData.VERTICAL_ALIGN_FILL
						| GridData.HORIZONTAL_ALIGN_FILL);

		Label versionLabel = new Label(versionComposite, SWT.NONE);
		versionLabel.setText(Messages.Excel_Export_Version);
		versionLabel.setToolTipText(Messages.Excel_Export_Version_Tooltip);

		versionCombo = new ComboViewer(versionComposite, SWT.READ_ONLY);
		versionCombo.getCombo().setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		versionCombo.setContentProvider(ArrayContentProvider.getInstance());
		versionCombo.setInput(SpreadsheetVersion.values());
		versionCombo.getCombo().setToolTipText(
				Messages.Excel_Export_Version_Tooltip);
		versionCombo.setSelection(new StructuredSelection(version));

		ExcelExportEditor editor = new ExcelExportEditor(composite, SWT.NONE,
				config);
		editor.setFont(composite.getFont());
		GridDataFactory.defaultsFor(editor).grab(true, false).applyTo(editor);
		Formats formats = new Formats(config.getAttributes());
		FormatsEditor formatsEditor = new FormatsEditor(composite, SWT.NONE,
				formats);
		GridDataFactory.defaultsFor(formatsEditor).grab(true, false)
				.applyTo(formatsEditor);
		formatsEditor.setFont(composite.getFont());
		setControl(composite);
	}

	public SpreadsheetVersion getSpreadsheetVersion() {
		return (SpreadsheetVersion) ((IStructuredSelection) versionCombo
				.getSelection()).getFirstElement();
	}

}
