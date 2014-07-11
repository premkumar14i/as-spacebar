package com.tibco.as.spacebar.ui.wizards.transfer.excel;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.tibco.as.spacebar.ui.SWTFactory;

import com.tibco.as.io.file.excel.ExcelImport;

public class ExcelImportConfigurationPage extends WizardPage {

	private ExcelImport config;

	public ExcelImportConfigurationPage(ExcelImport config) {
		super("ExcelImportConfigurationPage");
		setTitle("Excel Import Options");
		setDescription("Define the options for the Excel import.");
		this.config = config;
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite = SWTFactory.createComposite(parent, 1, 1,
				GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL);
		ExcelImportEditor editor = new ExcelImportEditor(composite, SWT.NONE,
				config);
		editor.setFont(composite.getFont());
		GridDataFactory.defaultsFor(editor).grab(true, false).applyTo(editor);
		setControl(composite);
	}

}
