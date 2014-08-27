package com.tibco.as.spacebar.ui.wizards.transfer.csv;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.wizards.AbstractWizardPage;

import com.tibco.as.file.text.delimited.DelimitedImport;

public class DelimitedImportWizardPage extends AbstractWizardPage {

	private DelimitedImport config;
	private Formats formats;

	protected DelimitedImportWizardPage(DelimitedImport config, Formats formats) {
		super("CSV Import Settings", "CSV Import Settings",
				"CSV import settings", Image.WIZBAN_CSV);
		this.config = config;
		this.formats = formats;
	}

	@Override
	protected Control createControl(ScrolledComposite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setFont(parent.getFont());
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
				formats);
		GridDataFactory.defaultsFor(formatsEditor).grab(true, false)
				.applyTo(formatsEditor);
		formatsEditor.setFont(formatsGroup.getFont());
		return composite;
	}

}
