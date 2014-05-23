package com.tibco.as.spacebar.ui.wizards.transfer.csv;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.tibco.as.io.file.text.delimited.DelimitedExport;

public class DelimitedExportConfigurationPage extends WizardPage {

	private DelimitedExport config;

	public DelimitedExportConfigurationPage(DelimitedExport config) {
		super("DelimitedExportConfigurationPage");
		setTitle("CSV Export Options");
		setDescription("Define the options for the CSV export.");
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
		DelimitedExportEditor editor = new DelimitedExportEditor(composite,
				SWT.NONE, config);
		editor.setFont(composite.getFont());
		GridDataFactory.defaultsFor(editor).grab(true, false).applyTo(editor);
		Group formatsGroup = new Group(composite, SWT.NONE);
		GridDataFactory.defaultsFor(formatsGroup).grab(true, false)
				.applyTo(formatsGroup);
		formatsGroup.setLayout(new GridLayout());
		formatsGroup.setText("Formats");
		formatsGroup.setFont(composite.getFont());
		Formats formats = new Formats(config.getAttributes());
		FormatsEditor formatsEditor = new FormatsEditor(formatsGroup, SWT.NONE,
				formats);
		GridDataFactory.defaultsFor(formatsEditor).grab(true, false)
				.applyTo(formatsEditor);
		formatsEditor.setFont(formatsGroup.getFont());
		setControl(composite);
	}

}
