package com.tibco.as.spacebar.ui.wizards.transfer;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.tibco.as.io.AbstractExport;

public class ExportOptionsPage extends WizardPage {

	private AbstractExport config;

	public ExportOptionsPage(AbstractExport config) {
		super("ExportOptionsPage");
		setTitle("Browse Options");
		setDescription("Define the browse options for the export.");
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
		ExportEditor editor = new ExportEditor(composite, SWT.NONE, config);
		editor.setFont(composite.getFont());
		GridDataFactory.defaultsFor(editor).grab(true, false).applyTo(editor);
		setControl(composite);
	}

}
