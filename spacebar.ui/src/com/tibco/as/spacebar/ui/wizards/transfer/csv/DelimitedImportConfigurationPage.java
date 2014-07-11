package com.tibco.as.spacebar.ui.wizards.transfer.csv;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.tibco.as.spacebar.ui.SWTFactory;

import com.tibco.as.io.file.text.delimited.DelimitedImport;

public class DelimitedImportConfigurationPage extends WizardPage {

	private DelimitedImport config;

	public DelimitedImportConfigurationPage(DelimitedImport config) {
		super("DelimitedImportConfigurationPage");
		setTitle("CSV Import Options");
		setDescription("Define the options for the CSV import.");
		this.config = config;
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite = SWTFactory.createComposite(parent, 1, 1,
				GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL);
		DelimitedImportEditor editor = new DelimitedImportEditor(composite,
				SWT.NONE, config);
		editor.setFont(composite.getFont());
		GridDataFactory.defaultsFor(editor).grab(true, false).applyTo(editor);
		Group formatsGroup = new Group(composite, SWT.NONE);
		GridDataFactory.defaultsFor(formatsGroup).grab(true, false)
				.applyTo(formatsGroup);
		formatsGroup.setLayout(new GridLayout());
		formatsGroup.setText("Formats");
		formatsGroup.setFont(composite.getFont());
		FormatsEditor formatsEditor = new FormatsEditor(formatsGroup, SWT.NONE,
				new Formats(config.getAttributes()));
		GridDataFactory.defaultsFor(formatsEditor).grab(true, false)
				.applyTo(formatsEditor);
		formatsEditor.setFont(formatsGroup.getFont());
		setControl(composite);
	}

}
