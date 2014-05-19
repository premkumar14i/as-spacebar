package com.tibco.as.spacebar.ui.wizards.transfer.csv;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;

import com.tibco.as.spacebar.ui.wizards.transfer.AbstractExportWizardPage;

public class DelimitedExportMainPage extends AbstractExportWizardPage implements
		Listener {

	private static final String STORE_CREATE_DIRECTORIES = "DelimitedExportWizardPage.STORE_CREATE_DIRECTORIES";

	protected Button createDirectoryStructureButton;

	protected Button flatStructureButton;

	protected DelimitedExportMainPage(String name,
			IStructuredSelection selection) {
		super(name, selection);
	}

	public DelimitedExportMainPage(IStructuredSelection selection) {
		this("DelimitedExportWizardPage", selection);
		setTitle("CSV");
		setDescription("Export spaces to CSV.");
	}

	@Override
	protected void restoreWidgetValues() {
		super.restoreWidgetValues();
		IDialogSettings settings = getDialogSettings();
		if (settings != null) {
			boolean createDirectories = settings
					.getBoolean(STORE_CREATE_DIRECTORIES);
			createDirectoryStructureButton.setSelection(createDirectories);
			flatStructureButton.setSelection(!createDirectories);
		}
	}

	@Override
	protected void saveWidgetValues() {
		super.saveWidgetValues();
		IDialogSettings settings = getDialogSettings();
		if (settings == null) {
			return;
		}
		settings.put(STORE_CREATE_DIRECTORIES, isCreateMetaspaceDirectories());
	}

	@Override
	protected String getDestinationTitle() {
		return "Export to CSV";
	}

	@Override
	protected void createOptionsGroup(Group optionsGroup) {
		super.createOptionsGroup(optionsGroup);
		Font font = optionsGroup.getFont();
		createDirectoryStructureButton = new Button(optionsGroup, SWT.RADIO
				| SWT.LEFT);
		createDirectoryStructureButton
				.setText("&Create a separate sub-directory for each metaspace");
		createDirectoryStructureButton.setSelection(false);
		createDirectoryStructureButton.setFont(font);

		// create directory structure radios
		flatStructureButton = new Button(optionsGroup, SWT.RADIO | SWT.LEFT);
		flatStructureButton.setText("Export all spaces to specified directory");
		flatStructureButton.setSelection(true);
		flatStructureButton.setFont(font);
	}

	public boolean isCreateMetaspaceDirectories() {
		return createDirectoryStructureButton.getSelection();
	}

}
