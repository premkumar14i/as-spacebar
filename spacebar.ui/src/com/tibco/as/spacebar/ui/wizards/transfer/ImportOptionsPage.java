package com.tibco.as.spacebar.ui.wizards.transfer;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.tibco.as.io.AbstractImport;

public class ImportOptionsPage extends WizardPage {

	private AbstractImport config;

	public ImportOptionsPage(AbstractImport config) {
		super("ImportOptionsPage");
		setTitle("Import Options");
		setDescription("Define the import options.");
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
		ImportEditor editor = new ImportEditor(composite, SWT.NONE, config);
		editor.setFont(composite.getFont());
		GridDataFactory.defaultsFor(editor).grab(true, false).applyTo(editor);
		setControl(composite);
	}

	
}
