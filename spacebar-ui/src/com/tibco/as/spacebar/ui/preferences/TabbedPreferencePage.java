package com.tibco.as.spacebar.ui.preferences;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public abstract class TabbedPreferencePage extends AbstractPreferencePage {

	private TabFolder folder;

	/**
	 * Maximum number of columns for field editors.
	 */
	private int maxNumOfColumns;

	/**
	 * Adds the given field editor to this page.
	 * 
	 * @param editor
	 *            the field editor
	 */
	@Override
	protected void addField(FieldEditor editor) {
		// needed for layout, since there is no way to get fields editor from
		// parent
		int numberOfControls = editor.getNumberOfControls();
		maxNumOfColumns = Math.max(maxNumOfColumns, numberOfControls);
		super.addField(editor);
	}

	@Override
	protected void createFieldEditors(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = getNumberOfColumns();
		composite.setLayout(layout);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessHorizontalSpace = true;
		composite.setLayoutData(gridData);
		addFields(composite);
		TabFolder folder = new TabFolder(composite, SWT.NONE);
		GridData folderData = new GridData(GridData.FILL_BOTH);
		folderData.horizontalSpan = getNumberOfColumns();
		folder.setLayoutData(folderData);
		addTabFields(folder);
		Dialog.applyDialogFont(folder);
	}

	protected int getNumberOfColumns() {
		return 2;
	}

	protected void addFields(Composite composite) {
	}

	protected abstract void addTabFields(TabFolder folder);

	protected Composite createTab(TabFolder folder, String text) {
		TabItem item = new TabItem(folder, SWT.NONE);
		item.setText(text);
		Composite composite = new Composite(folder, SWT.NONE);
		composite.setLayout(new TabFolderLayout());
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessHorizontalSpace = true;
		composite.setLayoutData(gridData);
		item.setControl(composite);
		return composite;
	}

	/**
	 * Adjust the layout of the field editors so that they are properly aligned.
	 */
	@Override
	protected void adjustGridLayout() {
		if (folder != null) {
			TabItem[] items = folder.getItems();
			for (int j = 0; j < items.length; j++) {
				Composite composite = (Composite) items[j].getControl();
				GridLayout layout = (GridLayout) composite.getLayout();
				layout.numColumns = this.maxNumOfColumns;
				layout.marginHeight = 5;
				layout.marginWidth = 5;
			}
		}
		// need to call super.adjustGridLayout() since
		// fieldEditor.adjustForNumColumns() is protected
		super.adjustGridLayout();
		// reset the main container to a single column
		((GridLayout) super.getFieldEditorParent().getLayout()).numColumns = 1;
	}

	/**
	 * Returns a parent composite for a field editor.
	 * <p>
	 * This value must not be cached since a new parent may be created each time
	 * this method called. Thus this method must be called each time a field
	 * editor is constructed.
	 * </p>
	 * 
	 * @return a parent
	 */
	@Override
	protected Composite getFieldEditorParent() {
		if (folder == null || folder.getItemCount() == 0) {
			return super.getFieldEditorParent();
		}
		return (Composite) folder.getItem(folder.getItemCount() - 1)
				.getControl();
	}

	/**
	 * Adds a tab to the page.
	 * 
	 * @param text
	 *            the tab label
	 */
	public void addTab(String text) {
		if (folder == null) {
			// initialize tab folder
			folder = new TabFolder(super.getFieldEditorParent(), SWT.NONE);
			folder.setLayoutData(new GridData(GridData.FILL_BOTH));
		}
		TabItem item = new TabItem(folder, SWT.NONE);
		item.setText(text);
		Composite currentTab = new Composite(folder, SWT.NULL);
		GridLayout layout = new GridLayout();
		currentTab.setLayout(layout);
		currentTab.setFont(super.getFieldEditorParent().getFont());
		currentTab.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		item.setControl(currentTab);
	}

	protected Composite createTabItemComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());
		GridLayout basicLayout = new GridLayout();
		basicLayout.numColumns = 2;
		composite.setLayout(basicLayout);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = getNumberOfColumns();
		composite.setLayoutData(gridData);
		return composite;
	}

}